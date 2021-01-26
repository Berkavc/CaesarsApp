package com.example.sezar.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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


public class SMSReceiver extends BroadcastReceiver {
    private static  String message;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private DatabaseReference myKey;
    private String key;
    private ChildEventListener myRefListener;
    private boolean controlDataBaseAddition;
    private Context mContext;
    private Cursor cursorLoginScreen;
    private DBLoginScreenHelper myDbLoginScreenHelper;
    private Long messageSize;
    private List<String> userNames;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Toast.makeText(context, "Intent", Toast.LENGTH_SHORT).show();
        mContext=context;
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "SMS from ";

        if (bundle != null){

            Object[] pdus = (Object[])bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
           // msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (int i=0; i < msgs.length; i++){

                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                if (i == 0){

                    str += msgs[i].getOriginatingAddress();
                    str += ": ";
                }

                str += msgs[i].getMessageBody();
            }

            Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
            //message=str;
            Log.d("SMSReceiver",str);

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms",str);
            context.sendBroadcast(broadcastIntent);
        }
        myDbLoginScreenHelper = new DBLoginScreenHelper(context);
        cursorLoginScreen = myDbLoginScreenHelper.getAllContacts();
        userNames = new ArrayList<>();
        addUserNames();
        checkDatabase();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context,SmsBackgroundTask.class));
        } else {
            context.startService(new Intent(context,SmsBackgroundTask.class));
        }
    }

    public static String takeMessage(){
        return  message;
    }

    protected void checkDatabase() {
        FirebaseApp.initializeApp(mContext);
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
                                        if(controlDataBaseAddition){
                                            sendNotification();
                                        }
                                        controlDataBaseAddition=false;
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
                if(controlDataBaseAddition){
                    checkDatabase();
                    controlDataBaseAddition=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    protected void sendNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("1",
                "channelName",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("channelDescription");
        Intent notificationIntent = new Intent(mContext, LoginScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, notificationIntent, 0);
        mNotificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "1")
                .setSmallIcon(R.drawable.notificationlogo)
                .setContentTitle("CaesarsApp:")
                .setContentText(mContext.getResources().getString(R.string.sms_background_task_message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true).setContentIntent(contentIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(1, builder.build());


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



}
