package com.example.sezar.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sezar.R;
import com.example.sezar.activity.LoginScreen;
import com.example.sezar.database.DBLoginScreenHelper;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SmsBackgroundTask extends Service {
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private DatabaseReference myKey;
    private String key;
    private ChildEventListener myRefListener;
    private boolean controlDataBaseAddition;
    private Cursor cursorLoginScreen;
    private DBLoginScreenHelper myDbLoginScreenHelper;
    private Long messageSize;
    private List<String> userNames;
    private Intent intent;
    private Notification notification;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean stopService = intent.getBooleanExtra("request_stop", false);
        if (stopService) {
            stopSelf();
        }
        myDbLoginScreenHelper = new DBLoginScreenHelper(this);
        cursorLoginScreen = myDbLoginScreenHelper.getAllContacts();
        userNames = new ArrayList<>();
        addUserNames();
        checkDatabase();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("1",
                    "channelName",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channelDescription");
            Intent notificationIntent = new Intent(this.getApplicationContext(), LoginScreen.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, notificationIntent, 0);
            mNotificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.sezar_icon_main)
                    .setContentTitle("CaesarsApp:")
                    .setContentText("CaesarsApp Working At Background")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true).setContentIntent(contentIntent);
             notification = builder.build();
           startForeground(2, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.sezar_icon_main)
                    .setContentTitle("CaesarsApp:")
                    .setContentText("CaesarsApp Working At Background")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true);
            notification = builder.build();
            startForeground(2, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, SMSReceiver.class);
        this.sendBroadcast(broadcastIntent);
    }


    protected void checkDatabase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myKey = database.getReference("keys:");
        key = myKey.getKey();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                controlDataBaseAddition = false;

                for (DataSnapshot firstSnapshot : dataSnapshot.getChildren()) {

                    String userName = firstSnapshot.child("Username").getValue(String.class);
                    String getKey = firstSnapshot.child("Key").getValue(String.class);

                    if (getKey != null) {
                        getKey = CaesarsCiphering.caesarsDecryption(getKey, 13);
                    }

                    if (userName != null && getKey != null) {
                        userName = AES.decrypt(userName, getKey);
                    }
                    for (int i = 0; i < userNames.size(); i++) {
                        if (userName.equals(userNames.get(i))) {
                            myKey = database.getReference().child(firstSnapshot.getKey());
                            if (!controlDataBaseAddition) {
                                messageSize = firstSnapshot.getChildrenCount();
                            }
                            myKey.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getChildrenCount() > messageSize) {
                                        controlDataBaseAddition = true;
                                        if (controlDataBaseAddition) {
                                            sendNotification();
                                        }
                                        controlDataBaseAddition = false;
                                    }
//                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//
//                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            break;
                        }
                    }
                }

//                myRef.removeEventListener(this);
                controlDataBaseAddition = true;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRefListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (controlDataBaseAddition) {
                    checkDatabase();
                    controlDataBaseAddition = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    protected void sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("1",
                    "channelName",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channelDescription");
            Intent notificationIntent = new Intent(this.getApplicationContext(), LoginScreen.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, notificationIntent, 0);
            mNotificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.sezar_icon_main)
                    .setContentTitle("CaesarsApp:")
                    .setContentText(getResources().getString(R.string.sms_background_task_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true).setContentIntent(contentIntent);
            Notification notification = builder.build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.sezar_icon_main)
                    .setContentTitle("CaesarsApp:")
                    .setContentText(getResources().getString(R.string.sms_background_task_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true);
            Notification notification = builder.build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, notification); }


    }

    private void addUserNames() {
        if (cursorLoginScreen != null) {
            if (cursorLoginScreen.moveToFirst()) {
                do {
                    userNames.add(AES.decrypt(cursorLoginScreen.getString(1), CaesarsCiphering.caesarsDecryption(cursorLoginScreen.getString(4), 13)));
                }
                while (cursorLoginScreen.moveToNext());

            }
        }

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(this, SmsBackgroundTask.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
    }


}