package com.example.sezar.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sezar.R;
import com.example.sezar.activity.ForgotPassword;
import com.example.sezar.activity.LoginScreen;
import com.example.sezar.activity.MainActivity;
import com.example.sezar.activity.ReadEncryptedMessage;
import com.example.sezar.adapter.ReadEncryptedMessageAdapter;
import com.example.sezar.adapter.ReadEncryptedMessagePhoneNumberAdapter;
import com.example.sezar.database.DBLoginScreenHelper;
import com.example.sezar.database.DBReadMessageHelper;
import com.example.sezar.database.DBReadMessageHelperLocal;
import com.example.sezar.database.DBReadMessageHelperLocalMessage;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.example.sezar.encryption_algorithms.RandomStringGenerator;
import com.example.sezar.imageProcess.Classifier;
import com.example.sezar.model.ImageUploadFile;
import com.example.sezar.model.LoginScreenItem;
import com.example.sezar.model.ReadEncryptedMessageItem;
import com.example.sezar.model.ReadMessageItem;
import com.example.sezar.model.ReadMessageItemFile;
import com.example.sezar.tokenize.TextClassificationClient;
import com.example.sezar.tokenize.Tokenize;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Read_Encrypted_Message extends Fragment {
    private static final String MESSAGE_KEY = "message_key";
    private static final String TAG = "TextClassificationDemo";
    final private int REQUEST_SEND_SMS = 0;
    final private int REQUEST_REC_SMS = 1;
    BroadcastReceiver smsSentReceiver;
    private IntentFilter intentFilter;
    TextView SMSes;
    private boolean controlPermissionSend;
    private boolean controlPermissionReciever;
    private boolean controlMessageList;
    static boolean deleteList;
    View view;
    private static DatabaseReference myRef;
    private DatabaseReference myRefImage;
    private static FirebaseDatabase database;
    private static DatabaseReference myKey;
    private int messageCounterPhone;
    DatabaseReference key;
    FirebaseDatabase databaseKey;
    String keys;
    private static ArrayList<ReadEncryptedMessageItem> fileContainer;
    private static ArrayList<ReadEncryptedMessageItem> fileContainerLocal;
    private ArrayList<ReadEncryptedMessageItem> usersAdapter;
    private static ArrayList<ReadEncryptedMessageItem> usersAdapterLocal;
    private ArrayList<ReadEncryptedMessageItem> deleteAdapterList;
    private ArrayList<ReadEncryptedMessageItem> deleteAdapterListLocal;
    private List<ImageUploadFile> mUploads;
    private List<String> usersAdapterPhoneNumber;
    private static List<String> contactPhoneNumber;
    private static List<String> contactUserName;
    private static ArrayList<ReadEncryptedMessageItem> decryptedMessages;
    private static ArrayList<ReadEncryptedMessageItem> decryptedMessagesLocal;
    private static ArrayList<ReadEncryptedMessageItem> decryptedImageValue;
    private static ArrayList<ReadEncryptedMessageItem> decryptedImageValueLocal;
    private static ArrayList<ReadEncryptedMessageItem> decryptedAudioValue;
    private static ArrayList<ReadEncryptedMessageItem> decryptedAudioValueLocal;
    private static ArrayList<ReadEncryptedMessageItem> decryptedVideoValue;
    private static ArrayList<ReadEncryptedMessageItem> decryptedVideoValueLocal;
    private static ArrayList<ReadEncryptedMessageItem> decryptedPhoneNumber;
    private static ArrayList<ReadEncryptedMessageItem> decryptedPhoneNumberLocal;
    private static ArrayList<ReadEncryptedMessageItem> decryptedLocationValue;
    private static ArrayList<ReadEncryptedMessageItem> decryptedLocationValueLocal;
    private static ArrayList<ReadEncryptedMessageItem> decryptedDocumentValue;
    private static ArrayList<ReadEncryptedMessageItem> decryptedDocumentValueLocal;
    private static ArrayList<ReadEncryptedMessageItem> readMessageState;
    private static ArrayList<ReadEncryptedMessageItem> readMessageStateLocal;
    private static ArrayList<ReadEncryptedMessageItem> updateDeleteAllList;
    private List<String> users;
    private RecyclerView recyclerView_user_info;
    Bundle bundle;
    private ReadEncryptedMessagePhoneNumberAdapter adapterPhone;
    private ReadEncryptedMessageAdapter adapter;
    private String userName;
    String indexOfMessage;
    private int arrayListCounter, arrayListUserNameCounter;
    private int messageCounter;
    private List<String> keysList;
    String phoneNumberKey;
    private String[] items = {"a", "b"};
    static int deleteCounter;
    private ImageView imageView_delete;
    private boolean controlDelete;
    private boolean controlAdapter;
    private ImageView imageView_back;
    private int contactCounter;
    private String decryptedGetImageValue;
    private String decryptedGetAudioValue;
    private String decryptedGetVideoValue;
    private String decryptedGetLocationValue;
    private String decryptedGetDocumentValue;
    private ChildEventListener myRefListener;
    private Cursor cursorReadMessage;
    private Cursor cursorReadMessageLocal;
    private Cursor cursorReadMessageLocalFile;
    private DBReadMessageHelper myDbReadMessageHelper;
    private DBReadMessageHelperLocal myDbReadMessageHelperLocal;
    private DBReadMessageHelperLocalMessage myDbReadMessageHelperLocalMessage;
    private static Bitmap bitmapLocal;
    private boolean controlLocalDatabaseMessage;
    private boolean controlLocalDatabaseFile;
    private String imageStringLocal;
    private static boolean controlImageAsync;
    private static String progressTitle;
    private static boolean controlInternetConnection;
    private AsyncTask asyncReadMessageInternetConnectionTask;
    private AsyncTask asyncReadMessageTokenization;
    private String keySecret;
    private static boolean controlInternetState;
    private static boolean controlLocalInternet;
    private static boolean controlNetworkInternet;
    private static Handler handler;
    private static TextClassificationClient client;
    private static String state;
    private String getPhoneNumberKeyDummy;

    private static Classifier classifier;
    private static   Handler handlerClassification;
    private HandlerThread handlerThread;
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //SMSes = view.findViewById(R.id.textView_read_encrypted_message);
            //SMSes.setText(intent.getExtras().getString("sms"));
            // SMSes.setText(SMSReceiver.takeMessage());
        }
    };

    public Fragment_Read_Encrypted_Message() {
        // Required empty public constructor
    }

    public static Fragment_Read_Encrypted_Message newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(Fragment_Read_Encrypted_Message.MESSAGE_KEY, message);
        Fragment_Read_Encrypted_Message fragment = new Fragment_Read_Encrypted_Message();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_encrypted_message, container, false);
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            controlPermissionSend = false;
            controlPermissionReciever = false;
            controlMessageList = false;
            controlDelete = false;
            controlLocalDatabaseMessage = false;
            controlLocalDatabaseFile = true;
            controlImageAsync = false;
            controlInternetState = false;
            controlLocalInternet = false;
            controlNetworkInternet = false;
            progressTitle = getString(R.string.forgot_password_wait);
            //checkDatabase();
            database = FirebaseDatabase.getInstance();
//            if (!AsyncReadMessageInternetConnection.isControlAsyncTask()) {
//                asyncReadMessageInternetConnectionTask = new AsyncReadMessageInternetConnection(getContext(), database.getReference().toString()).execute();
//            }
           asyncReadMessageInternetConnectionTask= new AsyncReadMessageInternetConnection(getContext(), database.getReference().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            if (checkNetworkAccess()) {
                controlLocalDatabaseMessage = true;
            }

            userName = LoginScreen.userName();

            arrayListCounter = 0;
            arrayListUserNameCounter = 0;
            messageCounter = 0;

            myDbReadMessageHelper = new DBReadMessageHelper(getContext());
            myDbReadMessageHelperLocal = new DBReadMessageHelperLocal(getContext());
            myDbReadMessageHelperLocalMessage = new DBReadMessageHelperLocalMessage(getContext());

            users = new ArrayList<>();
            usersAdapter = new ArrayList<>();
            usersAdapterLocal = new ArrayList<>();
            usersAdapterPhoneNumber = new ArrayList<>();
            decryptedMessages = new ArrayList<>();
            decryptedMessagesLocal = new ArrayList<>();
            readMessageState = new ArrayList<>();
            readMessageStateLocal = new ArrayList<>();
            decryptedPhoneNumber = new ArrayList<>();
            decryptedPhoneNumberLocal = new ArrayList<>();
            keysList = new ArrayList<>();
            deleteAdapterList = new ArrayList<>();
            deleteAdapterListLocal = new ArrayList<>();
            contactPhoneNumber = new ArrayList<>();
            contactUserName = new ArrayList<>();
            decryptedImageValue = new ArrayList<>();
            decryptedImageValueLocal = new ArrayList<>();
            decryptedAudioValue = new ArrayList<>();
            decryptedAudioValueLocal = new ArrayList<>();
            decryptedVideoValue = new ArrayList<>();
            decryptedVideoValueLocal = new ArrayList<>();
            decryptedLocationValue = new ArrayList<>();
            decryptedLocationValueLocal = new ArrayList<>();
            decryptedDocumentValue = new ArrayList<>();
            decryptedDocumentValueLocal = new ArrayList<>();
            fileContainer = new ArrayList<>();
            fileContainerLocal = new ArrayList<>();
            updateDeleteAllList = new ArrayList<>();
            mUploads = new ArrayList<>();


            if(MainActivity.controlSafetyFilter){
                handler = new Handler();
                client = new TextClassificationClient(getContext());
                asyncReadMessageTokenization = new AsyncReadMessageTokenization(getContext(), handler, client).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            imageView_delete = view.findViewById(R.id.imageView_fragment_read_delete);
            imageView_back = view.findViewById(R.id.imageView_fragment_read_encrypted_message_arrow);

            getContacts();

            if (checkNetworkAccess()) {
                checkDatabase();
            }

            recyclerView_user_info = view.findViewById(R.id.recyclerView_fragment_read_encrypted_message);
            LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            //layoutmanager.setReverseLayout(true);
            //layoutmanager.setStackFromEnd(true);
            recyclerView_user_info.setLayoutManager(layoutmanager);
            //adapter = new ReadEncryptedMessageAdapter(getContext(), usersAdapter);
            if (ReadEncryptedMessagePhoneNumberAdapter.isControlModification()) {

                if (checkNetworkAccess()) {
                    adapter = new ReadEncryptedMessageAdapter(getContext(), usersAdapter);
                } else {
                    insertLocalDatabase();
                    insertLocalDatabaseFile();
                    adapter = new ReadEncryptedMessageAdapter(getContext(), usersAdapterLocal);
                }

                recyclerView_user_info.setAdapter(adapter);
                imageView_back.setVisibility(View.VISIBLE);


            } else {
                if (!checkNetworkAccess() || !controlInternetConnection) {
                    insertLocalDatabasePhone();
                }

                adapterPhone = new ReadEncryptedMessagePhoneNumberAdapter(getContext(), usersAdapterPhoneNumber);
                recyclerView_user_info.setAdapter(adapterPhone);
                imageView_back.setVisibility(View.INVISIBLE);

            }


            // checkImageStorage();

            String message = arguments.getString(MESSAGE_KEY);
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
            }

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_REC_SMS);
            }
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            } else {
                intentFilter = new IntentFilter();
                intentFilter.addAction("SMS_RECEIVED_ACTION");
            }


        }
        imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!deleteAdapterList.isEmpty()) {
                    controlDelete = true;
                    deleteAccountAlertDialog();
                }
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usersAdapter.isEmpty() || !usersAdapterLocal.isEmpty() || !usersAdapterPhoneNumber.isEmpty()) {
                    ReadEncryptedMessagePhoneNumberAdapter.setControlModification(false);
                    Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                    getFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                } else {
                    Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                    getFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                    ReadEncryptedMessagePhoneNumberAdapter.setControlModification(false);
                    startActivity(new Intent(getContext(), LoginScreen.class));
                }
            }
        });
        return view;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//
//
//    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (controlPermissionReciever && controlPermissionSend) {
            getActivity().registerReceiver(intentReceiver, intentFilter);
        } else {
        }
        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handlerClassification = new Handler(handlerThread.getLooper());
    }


    public static Handler getHandlerClassification() {
        return handlerClassification;
    }

    public static Classifier getClassifier() {
        return classifier;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (controlPermissionReciever && controlPermissionSend) {
            getActivity().unregisterReceiver(intentReceiver);
        } else {
        }
        //myRef.removeEventListener(myRefListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        try {
            classifier = Classifier.create(getActivity(), Classifier.Model.QUANTIZED, Classifier.Device.GPU, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        if(MainActivity.controlSafetyFilter){
            if(handler!=null){
                handler.post(
                        () -> {
                            if (client != null) {
                                client.unload();
                            }

                        });
            }

        }

    }
    public static String classify(final String text) {
//        handler.post(
//                () -> {
//
//
//                    // Run text classification with TF Lite.
//                    List<TextClassificationClient.Result> results = client.classify(text);
//                    if(results.get(0).getConfidence()>results.get(1).getConfidence()){
//                        state="positive";
//                    }else{
//                        state="negative";
//                    }
//                    // Show classification result on screen
//                    //showResult(text, results);
//                });
        List<TextClassificationClient.Result> results = client.classify(text);
        float x=results.get(0).getConfidence();
        float y=results.get(1).getConfidence();
        String temp=results.get(0).getTitle();
        if(results.get(0).getTitle().equals("Negative")){
            state="negative";
        }else{
            state="positive";
        }
        return state;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    controlPermissionSend = true;
                    Toast.makeText(getContext(), "Send Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    controlPermissionSend = false;
                    Toast.makeText(getContext(), "Send Permission Denied", Toast.LENGTH_SHORT).show();
                }

                break;
            case REQUEST_REC_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    controlPermissionReciever = true;
                    Toast.makeText(getContext(), "Receive Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    controlPermissionReciever = false;
                    Toast.makeText(getContext(), "Receive Permission Denied", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void checkDatabase() {

        database = FirebaseDatabase.getInstance();

        controlMessageList = false;
        controlAdapter = false;
        messageCounter = 0;
        messageCounterPhone = 0;
        contactCounter = 0;
        //deleteAdapterList.clear();


        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //deleteAdapterList.clear();
                for (DataSnapshot firstSnapshot : dataSnapshot.getChildren()) {

                    String userName = firstSnapshot.child("Username").getValue(String.class);
                    String deletekey = firstSnapshot.child("Key").getValue(String.class);
                    String keys = firstSnapshot.getKey();

                    if (keys.equals(LoginScreen.userKey())) {
                        if (controlAdapter) {
                            usersAdapter.clear();
                            usersAdapterLocal.clear();
                            usersAdapterPhoneNumber.clear();
                            deleteAdapterList.clear();
                            deleteAdapterListLocal.clear();
                            decryptedMessages.clear();
                            decryptedMessagesLocal.clear();
                            readMessageState.clear();
                            readMessageStateLocal.clear();
                            decryptedPhoneNumber.clear();
                            decryptedPhoneNumberLocal.clear();
                            decryptedImageValue.clear();
                            decryptedImageValueLocal.clear();
                            decryptedAudioValue.clear();
                            decryptedAudioValueLocal.clear();
                            decryptedVideoValue.clear();
                            decryptedVideoValueLocal.clear();
                            decryptedLocationValue.clear();
                            decryptedLocationValueLocal.clear();
                            decryptedDocumentValue.clear();
                            decryptedDocumentValueLocal.clear();
                            fileContainer.clear();
                            fileContainerLocal.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            if (usersAdapterPhoneNumber != null) {
                                adapterPhone.notifyDataSetChanged();
                            }

                            break;
                        }
                        myKey = database.getReference().child(firstSnapshot.getKey());
                        myKey.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    if (controlDelete && (!usersAdapter.isEmpty() || !usersAdapterPhoneNumber.isEmpty())) {
                                        String mykeys = postSnapshot.getKey();
                                        myKey.child(mykeys).removeValue();
                                        messageCounter++;
                                        if (deleteAdapterList.size() == messageCounter || (updateDeleteAllList.size() == messageCounter && updateDeleteAllList.size() > 0)) {
                                            controlDelete = false;
                                            controlAdapter = true;
                                            usersAdapter.clear();
                                            usersAdapterPhoneNumber.clear();
                                            break;
                                        }

                                    } else {
                                        messageCounterPhone = 0;
                                        String getKey = postSnapshot.child("Message").getValue(String.class);
                                        String encryptedMessage = postSnapshot.child("Encrypted Message").getValue(String.class);
                                        String getDecryptedKey = postSnapshot.child("Decryption Key").getValue(String.class);
                                        String encryptedPhoneNumber = postSnapshot.child("Phone Number").getValue(String.class);
                                        String getReadState = postSnapshot.child("Read State").getValue(String.class);
                                        String getPhoneNumberKey = postSnapshot.child("Phone Number Key").getValue(String.class);
                                        //ImageUploadFile upload=postSnapshot.getValue(ImageUploadFile.class);
                                        String getImageValue = postSnapshot.child("Image Value").getValue(String.class);
                                        String getAudioValue = postSnapshot.child("Audio Value").getValue(String.class);
                                        String getVideoValue = postSnapshot.child("Video Value").getValue(String.class);
                                        String getLocationValue = postSnapshot.child("Location Value").getValue(String.class);
                                        String getDocumentValue = postSnapshot.child("File Value").getValue(String.class);
                                        if (getKey != null && encryptedPhoneNumber != null) {
                                            if (getDecryptedKey != null) {
                                                getDecryptedKey = CaesarsCiphering.caesarsDecryption(getDecryptedKey, 13);
                                            }
                                            if (getPhoneNumberKey != null) {
                                                getPhoneNumberKeyDummy=getPhoneNumberKey;
                                                getPhoneNumberKey = CaesarsCiphering.caesarsDecryption(getPhoneNumberKey, 13);
                                            }
                                            String decryptedMessage = AES.decrypt(getKey, getDecryptedKey);
                                            String PhoneNumberDecrypted = AES.decrypt(encryptedPhoneNumber, getPhoneNumberKey);
                                            if(PhoneNumberDecrypted==null){
                                                PhoneNumberDecrypted=AES.decrypt(encryptedPhoneNumber,getPhoneNumberKeyDummy);
                                            }
                                            if (getImageValue != null) {
                                                decryptedGetImageValue = AES.decrypt(getImageValue, getDecryptedKey);
                                            }
                                            if (getAudioValue != null) {
                                                decryptedGetAudioValue = AES.decrypt(getAudioValue, getDecryptedKey);
                                            }
                                            if (getVideoValue != null) {
                                                decryptedGetVideoValue = AES.decrypt(getVideoValue, getDecryptedKey);
                                            }

                                            if (getLocationValue != null) {
                                                decryptedGetLocationValue = AES.decrypt(getLocationValue, getDecryptedKey);
                                            }
                                            if (getDocumentValue != null) {
                                                decryptedGetDocumentValue = AES.decrypt(getDocumentValue, getDecryptedKey);
                                            }


                                            if (!usersAdapterPhoneNumber.contains(PhoneNumberDecrypted) && PhoneNumberDecrypted != null && !controlAdapter) {
                                                usersAdapterPhoneNumber.add(PhoneNumberDecrypted);
                                            }
                                            if (ReadEncryptedMessagePhoneNumberAdapter.isControlModification()) {
                                                if (usersAdapterPhoneNumber.size() > ReadEncryptedMessagePhoneNumberAdapter.getCheck_Position()) {
                                                    if (usersAdapterPhoneNumber.get(ReadEncryptedMessagePhoneNumberAdapter.getCheck_Position()).equals(PhoneNumberDecrypted)) {
                                                        usersAdapter.add(new ReadEncryptedMessageItem(encryptedMessage));
                                                        decryptedMessages.add(new ReadEncryptedMessageItem(decryptedMessage));
                                                        readMessageState.add(new ReadEncryptedMessageItem(getReadState));
                                                        decryptedPhoneNumber.add(new ReadEncryptedMessageItem(PhoneNumberDecrypted));

                                                        if (getLocationValue != null) {
                                                            decryptedLocationValue.add(new ReadEncryptedMessageItem(decryptedGetLocationValue));
                                                        } else {
                                                            decryptedLocationValue.add(new ReadEncryptedMessageItem("Location"));
                                                        }
                                                        if (getImageValue != null) {
                                                            decryptedImageValue.add(new ReadEncryptedMessageItem(decryptedGetImageValue));
                                                            decryptedVideoValue.add(new ReadEncryptedMessageItem("Image"));
                                                            decryptedAudioValue.add(new ReadEncryptedMessageItem("Image"));
                                                            decryptedDocumentValue.add(new ReadEncryptedMessageItem("Image"));
                                                            fileContainer.add(new ReadEncryptedMessageItem("Image"));
                                                        } else if (getAudioValue != null) {
                                                            decryptedAudioValue.add(new ReadEncryptedMessageItem(decryptedGetAudioValue));
                                                            decryptedImageValue.add(new ReadEncryptedMessageItem("Audio"));
                                                            decryptedVideoValue.add(new ReadEncryptedMessageItem("Audio"));
                                                            decryptedDocumentValue.add(new ReadEncryptedMessageItem("Audio"));
                                                            fileContainer.add(new ReadEncryptedMessageItem("Audio"));
                                                        } else if (getVideoValue != null) {
                                                            decryptedVideoValue.add(new ReadEncryptedMessageItem(decryptedGetVideoValue));
                                                            decryptedImageValue.add(new ReadEncryptedMessageItem("Video"));
                                                            decryptedAudioValue.add(new ReadEncryptedMessageItem("Video"));
                                                            decryptedDocumentValue.add(new ReadEncryptedMessageItem("Video"));
                                                            fileContainer.add(new ReadEncryptedMessageItem("Video"));
                                                        } else if (getDocumentValue != null) {
                                                            decryptedDocumentValue.add(new ReadEncryptedMessageItem(decryptedGetDocumentValue));
                                                            decryptedImageValue.add(new ReadEncryptedMessageItem("Document"));
                                                            decryptedVideoValue.add(new ReadEncryptedMessageItem("Document"));
                                                            decryptedAudioValue.add(new ReadEncryptedMessageItem("Document"));
                                                            fileContainer.add(new ReadEncryptedMessageItem("Document"));
                                                        } else {
                                                            decryptedVideoValue.add(new ReadEncryptedMessageItem("Message"));
                                                            decryptedImageValue.add(new ReadEncryptedMessageItem("Message"));
                                                            decryptedAudioValue.add(new ReadEncryptedMessageItem("Message"));
                                                            decryptedDocumentValue.add(new ReadEncryptedMessageItem("Message"));
                                                            fileContainer.add(new ReadEncryptedMessageItem("Message"));
                                                        }
                                                        deleteAdapterList.add(new ReadEncryptedMessageItem(decryptedMessage));

                                                    }
                                                }


                                            } else {
                                                usersAdapterLocal.add(new ReadEncryptedMessageItem(encryptedMessage));
                                                decryptedMessagesLocal.add(new ReadEncryptedMessageItem(decryptedMessage));
                                                readMessageStateLocal.add(new ReadEncryptedMessageItem(getReadState));
                                                decryptedPhoneNumberLocal.add(new ReadEncryptedMessageItem(PhoneNumberDecrypted));

                                                if (getLocationValue != null) {
                                                    decryptedLocationValueLocal.add(new ReadEncryptedMessageItem(decryptedGetLocationValue));
                                                } else {
                                                    decryptedLocationValueLocal.add(new ReadEncryptedMessageItem("Location"));
                                                }
                                                if (getImageValue != null) {
                                                    //ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(decryptedGetImageValue);
                                                    //imageLoadAsyncTask.execute();
                                                    decryptedImageValueLocal.add(new ReadEncryptedMessageItem(decryptedGetImageValue));
                                                    decryptedVideoValueLocal.add(new ReadEncryptedMessageItem("Image"));
                                                    decryptedAudioValueLocal.add(new ReadEncryptedMessageItem("Image"));
                                                    decryptedDocumentValueLocal.add(new ReadEncryptedMessageItem("Image"));
                                                    fileContainerLocal.add(new ReadEncryptedMessageItem("Image"));
                                                } else if (getAudioValue != null) {
                                                    decryptedAudioValueLocal.add(new ReadEncryptedMessageItem(decryptedGetAudioValue));
                                                    decryptedImageValueLocal.add(new ReadEncryptedMessageItem("Audio"));
                                                    decryptedVideoValueLocal.add(new ReadEncryptedMessageItem("Audio"));
                                                    decryptedDocumentValueLocal.add(new ReadEncryptedMessageItem("Audio"));
                                                    fileContainerLocal.add(new ReadEncryptedMessageItem("Audio"));
                                                } else if (getVideoValue != null) {
                                                    decryptedVideoValueLocal.add(new ReadEncryptedMessageItem(decryptedGetVideoValue));
                                                    decryptedImageValueLocal.add(new ReadEncryptedMessageItem("Video"));
                                                    decryptedAudioValueLocal.add(new ReadEncryptedMessageItem("Video"));
                                                    decryptedDocumentValueLocal.add(new ReadEncryptedMessageItem("Video"));
                                                    fileContainerLocal.add(new ReadEncryptedMessageItem("Video"));
                                                } else if (getDocumentValue != null) {
                                                    decryptedDocumentValueLocal.add(new ReadEncryptedMessageItem(decryptedGetDocumentValue));
                                                    decryptedVideoValueLocal.add(new ReadEncryptedMessageItem("Document"));
                                                    decryptedImageValueLocal.add(new ReadEncryptedMessageItem("Document"));
                                                    decryptedAudioValueLocal.add(new ReadEncryptedMessageItem("Document"));
                                                    fileContainerLocal.add(new ReadEncryptedMessageItem("Document"));

                                                } else {
                                                    decryptedVideoValueLocal.add(new ReadEncryptedMessageItem("Message"));
                                                    decryptedImageValueLocal.add(new ReadEncryptedMessageItem("Message"));
                                                    decryptedAudioValueLocal.add(new ReadEncryptedMessageItem("Message"));
                                                    decryptedDocumentValueLocal.add(new ReadEncryptedMessageItem("Message"));
                                                    fileContainerLocal.add(new ReadEncryptedMessageItem("Message"));
                                                }
                                                deleteAdapterList.add(new ReadEncryptedMessageItem(decryptedMessage));
                                            }


                                        } else {
                                            controlMessageList = true;
                                            controlDelete = false;
                                            break;
                                        }
                                    }
                                }
                                if (ReadEncryptedMessagePhoneNumberAdapter.isControlModification() && adapter != null) {
                                    adapter.notifyDataSetChanged();
                                    ReadEncryptedMessagePhoneNumberAdapter.setControlModification(false);
                                } else {
                                    if (adapterPhone != null) {
                                        insertLocalDatabasePhone();
                                        insertLocalDatabase();
                                        insertLocalDatabaseFile();
                                        cursorReadMessage.close();
                                        cursorReadMessageLocal.close();
                                        cursorReadMessageLocalFile.close();
                                        myDbReadMessageHelper.close();
                                        myDbReadMessageHelperLocalMessage.close();
                                        myDbReadMessageHelperLocal.close();
                                        adapterPhone.notifyDataSetChanged();
                                    } else if (adapter != null) {
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                myKey.removeEventListener(this);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //adapter.notifyDataSetChanged();
                    }

                    String[] stringArrayUser = {userName};
                    String[] stringArrayKey = {keys};

                    users.addAll((Arrays.asList(stringArrayUser)));
                    keysList.addAll(Arrays.asList(stringArrayKey));


                }

                myRef.removeEventListener(this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
      /*  myRefListener=myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                usersAdapter.clear();
                usersAdapterPhoneNumber.clear();
                deleteAdapterList.clear();
                keysList.clear();
                decryptedMessages.clear();
                readMessageState.clear();
                decryptedPhoneNumber.clear();
                decryptedImageValue.clear();
                decryptedAudioValue.clear();
                decryptedVideoValue.clear();
                decryptedLocationValue.clear();
                fileContainer.clear();
                updateDeleteAllList.clear();
                checkDatabase();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedMessages() {
        return decryptedMessages;
    }
    public static ArrayList<ReadEncryptedMessageItem>getReadMessageState() {
        return readMessageState;
    }
    public static ArrayList<ReadEncryptedMessageItem> getDecryptedPhoneNumber() {
        return decryptedPhoneNumber;
    }
    private void deleteAccountAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.read_encrypted_message_delete_title);
        builder.setMessage(R.string.read_encrypted_message_delete_account_alert_dialog);
        builder.setNegativeButton(R.string.main_activity_delete_account_no, null);
        builder.setPositiveButton(R.string.main_activity_delete_account_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkDatabase();
                // startActivity(new Intent(MainActivity.this, LoginScreen.class));
                Toast.makeText(getContext(), R.string.read_encrypted_message_delete_successfull, Toast.LENGTH_LONG).show();
                //finish();
            }
        });
        builder.create().show();

    }

    private void getContacts() {
        contactUserName.clear();
        contactPhoneNumber.clear();
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        // Uri contectDataVar = data.getData();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                contactUserName.add(name);
                //nameList.add(name);
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    String phoneNo = cur.getString(cur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactPhoneNumber.add(phoneNo.replaceAll("[ -]", ""));

                }
            }
        }
    }

    public static List<String> getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public static List<String> getContactUserName() {
        return contactUserName;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedImageValue() {
        return decryptedImageValue;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedAudioValue() {
        return decryptedAudioValue;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedVideoValue() {
        return decryptedVideoValue;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedLocationValue() {
        return decryptedLocationValue;
    }

    public static ArrayList<ReadEncryptedMessageItem> getFileContainer() {
        return fileContainer;
    }

    public static void setFileContainer(ArrayList<ReadEncryptedMessageItem> fileContainer) {
        Fragment_Read_Encrypted_Message.fileContainer = fileContainer;
    }

    private void checkImageStorage() {
        myRefImage = FirebaseDatabase.getInstance().getReference();
        myRefImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ImageUploadFile upload = postSnapshot.getValue(ImageUploadFile.class);
                    mUploads.add(upload);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static ArrayList<ReadEncryptedMessageItem> getUpdateDeleteAllList() {
        return updateDeleteAllList;
    }

    public static void setUpdateDeleteAllList(ArrayList<ReadEncryptedMessageItem> updateDeleteAllList) {
        Fragment_Read_Encrypted_Message.updateDeleteAllList = updateDeleteAllList;
    }


    private void insertLocalDatabase() {
        int localDatabaseCounter = 0;

        cursorReadMessageLocal = myDbReadMessageHelperLocalMessage.getAllContacts();
        if (cursorReadMessageLocal != null) {
            if (cursorReadMessageLocal.getCount() > 0 && !checkNetworkAccess()) {
                usersAdapterLocal.clear();
                deleteAdapterListLocal.clear();
                decryptedMessagesLocal.clear();
                readMessageStateLocal.clear();
                decryptedPhoneNumberLocal.clear();
            }
        }

        if (checkNetworkAccess() && controlLocalDatabaseMessage) {
            controlLocalDatabaseMessage = false;
            myDbReadMessageHelperLocalMessage.deletedata();
            //myDbReadMessageHelperLocal.close();


        }


        /*if (checkNetworkAccess()&&controlLocalDatabaseMessage) {
            int localMessageCounter=0;
            controlLocalDatabaseMessage=false;
            cursorReadMessageLocal.moveToFirst();
            do{
                if(cursorReadMessageLocal.getCount()==localMessageCounter){
                    break;
                }
                myDbReadMessageHelperLocalMessage.deleteEntry(localMessageCounter);
                localMessageCounter++;

               // myDbReadMessageHelperLocalMessage.close();

            }while (cursorReadMessageLocal.moveToNext());



        }*/

        if (checkNetworkAccess()) {
            RandomStringGenerator rsg = new RandomStringGenerator();
            keySecret = rsg.randomStringGenerator();
            myDbReadMessageHelperLocalMessage = new DBReadMessageHelperLocalMessage(getContext());
            while (usersAdapterLocal.size() > localDatabaseCounter) {
                ReadMessageItem dataBaseItem = new ReadMessageItem(AES.encrypt(decryptedMessagesLocal.get(localDatabaseCounter).getPhoneNumber(), keySecret), AES.encrypt(decryptedPhoneNumberLocal.get(localDatabaseCounter).getPhoneNumber(), keySecret), AES.encrypt(usersAdapterLocal.get(localDatabaseCounter).getPhoneNumber(), keySecret), AES.encrypt(readMessageStateLocal.get(localDatabaseCounter).getPhoneNumber(), keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13));
                myDbReadMessageHelperLocalMessage.insertdata(dataBaseItem);
                localDatabaseCounter++;

            }

        } else {
            try {
                cursorReadMessageLocal.moveToFirst();
                do {
                    // for (int i = 0; i < ReadEncryptedMessagePhoneNumberAdapter.getUserPhoneNumbers().size(); i++) {;
                    if (ReadEncryptedMessagePhoneNumberAdapter.getUserPhoneNumbers().get(ReadEncryptedMessagePhoneNumberAdapter.getCheck_Position()).equals(AES.decrypt(cursorReadMessageLocal.getString(2), CaesarsCiphering.caesarsDecryption(cursorReadMessageLocal.getString(5), 13)))) {
                        //deleteAdapterListLocal.add(new ReadEncryptedMessageItem(cursorReadMessageLocal.getString(2)));
                        decryptedMessagesLocal.add(new ReadEncryptedMessageItem(AES.decrypt(cursorReadMessageLocal.getString(1), CaesarsCiphering.caesarsDecryption(cursorReadMessageLocal.getString(5), 13))));
                        decryptedPhoneNumberLocal.add(new ReadEncryptedMessageItem(AES.decrypt(cursorReadMessageLocal.getString(2), CaesarsCiphering.caesarsDecryption(cursorReadMessageLocal.getString(5), 13))));
                        usersAdapterLocal.add(new ReadEncryptedMessageItem(AES.decrypt(cursorReadMessageLocal.getString(3), CaesarsCiphering.caesarsDecryption(cursorReadMessageLocal.getString(5), 13))));
                        readMessageStateLocal.add(new ReadEncryptedMessageItem(AES.decrypt(cursorReadMessageLocal.getString(4), CaesarsCiphering.caesarsDecryption(cursorReadMessageLocal.getString(5), 13))));
                    }
                    // }
                } while (cursorReadMessageLocal.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
                startActivity(new Intent(getContext(), LoginScreen.class));
                Toast.makeText(getContext(), R.string.read_encrypted_message_cache_empty, Toast.LENGTH_LONG).show();
            }
        }
//        myDbReadMessageHelperLocal.close();

    }


    private void insertLocalDatabaseFile() {
        int localDatabaseCounter = 0;


        cursorReadMessageLocalFile = myDbReadMessageHelperLocal.getAllContactsFiles();
        if (cursorReadMessageLocalFile != null) {
            if (cursorReadMessageLocalFile.getCount() > 0 && !checkNetworkAccess()) {
                decryptedImageValueLocal.clear();
                decryptedAudioValueLocal.clear();
                decryptedVideoValueLocal.clear();
                decryptedLocationValueLocal.clear();
                decryptedDocumentValueLocal.clear();
                fileContainerLocal.clear();
            }
        }


        if (checkNetworkAccess() && controlLocalDatabaseFile) {
            controlLocalDatabaseFile = false;
            myDbReadMessageHelperLocal.deletedatafile();
            //myDbReadMessageHelperLocal.close();


        }

      /*  if (checkNetworkAccess()&&controlLocalDatabaseFile) {
            int localCounter=0;
            controlLocalDatabaseFile=false;
            cursorReadMessageLocalFile.moveToFirst();
            do{
                if(cursorReadMessageLocalFile.getCount()==localCounter){
                    break;
                }
                myDbReadMessageHelperLocal.deleteEntry(localCounter);
                localCounter++;

                // myDbReadMessageHelperLocalMessage.close();

            }while (cursorReadMessageLocalFile.moveToNext());

        }*/

        if (checkNetworkAccess()) {
            new ImageLoadAsyncTask(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            // myDbReadMessageHelperLocal = new DBReadMessageHelperLocal(getContext());
           /* while (usersAdapterLocal.size() > localDatabaseCounter) {
                if ((fileContainerLocal.get(localDatabaseCounter).getPhoneNumber().equals("Image"))) {

                   // imageStringLocal = ImageLoadAsyncTask.getImageValueLocal();
                    //  Picasso.get().load(Fragment_Read_Encrypted_Message.getDecryptedImageValue().get(getAdapterPosition()).getPhoneNumber()).into(image);
                   // decryptedImageValueLocal.remove(localDatabaseCounter);
                    //decryptedImageValueLocal.add(localDatabaseCounter, new ReadEncryptedMessageItem(imageStringLocal));
                    //ReadMessageItemFile dataBaseItem = new ReadMessageItemFile(decryptedImageValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedAudioValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedVideoValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedLocationValueLocal.get(localDatabaseCounter).getPhoneNumber(), fileContainerLocal.get(localDatabaseCounter).getPhoneNumber());
                   // myDbReadMessageHelperLocal.insertdatafiles(dataBaseItem);
                    localDatabaseCounter++;


                }else{
                    ReadMessageItemFile dataBaseItem = new ReadMessageItemFile(decryptedImageValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedAudioValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedVideoValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedLocationValueLocal.get(localDatabaseCounter).getPhoneNumber(), fileContainerLocal.get(localDatabaseCounter).getPhoneNumber());
                    myDbReadMessageHelperLocal.insertdatafiles(dataBaseItem);
                    localDatabaseCounter++;
                }

            }*/
        } else {
            try {
                cursorReadMessageLocalFile.moveToFirst();
                cursorReadMessageLocal.moveToFirst();
                do {
                    //for (int i = 0; i < decryptedPhoneNumberLocal.size(); i++) {
                    if (ReadEncryptedMessagePhoneNumberAdapter.getUserPhoneNumbers().get(ReadEncryptedMessagePhoneNumberAdapter.getCheck_Position()).equals(AES.decrypt(cursorReadMessageLocal.getString(2), CaesarsCiphering.caesarsDecryption(cursorReadMessageLocal.getString(5), 13)))) {
                        decryptedImageValueLocal.add(new ReadEncryptedMessageItem(cursorReadMessageLocalFile.getBlob(1)));
                        decryptedAudioValueLocal.add(new ReadEncryptedMessageItem(cursorReadMessageLocalFile.getString(2)));
                        decryptedVideoValueLocal.add(new ReadEncryptedMessageItem(cursorReadMessageLocalFile.getString(3)));
                        decryptedLocationValueLocal.add(new ReadEncryptedMessageItem(cursorReadMessageLocalFile.getString(4)));
                        decryptedDocumentValueLocal.add(new ReadEncryptedMessageItem(cursorReadMessageLocalFile.getString(5)));
                        fileContainerLocal.add(new ReadEncryptedMessageItem(cursorReadMessageLocalFile.getString(6)));
                        if (decryptedMessagesLocal.size() == fileContainerLocal.size()) {
                            break;
                        }
                        // }
                    }
                } while (cursorReadMessageLocalFile.moveToNext() && cursorReadMessageLocal.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
                startActivity(new Intent(getContext(), LoginScreen.class));
                Toast.makeText(getContext(), R.string.read_encrypted_message_cache_empty, Toast.LENGTH_LONG).show();
            }
        }
        // myDbReadMessageHelper.close();

    }


    private void insertLocalDatabasePhone() {
        int localDatabaseCounter = 0;
        RandomStringGenerator rsg = new RandomStringGenerator();
        keySecret = rsg.randomStringGenerator();
        cursorReadMessage = myDbReadMessageHelper.getAllContactsPhone();
        if (cursorReadMessage.getCount() > 0 && !checkNetworkAccess()) {
            usersAdapterLocal.clear();
            usersAdapterPhoneNumber.clear();
            deleteAdapterListLocal.clear();
            decryptedMessagesLocal.clear();
            readMessageStateLocal.clear();
            decryptedPhoneNumberLocal.clear();
            decryptedImageValueLocal.clear();
            decryptedAudioValueLocal.clear();
            decryptedVideoValueLocal.clear();
            decryptedLocationValueLocal.clear();
            decryptedDocumentValueLocal.clear();
            fileContainerLocal.clear();
        }

        if (checkNetworkAccess() && cursorReadMessage.getCount() > 0) {
            myDbReadMessageHelper.deletedataPhone();
            myDbReadMessageHelper.close();

        }

        if (checkNetworkAccess()) {
            try {
                myDbReadMessageHelper = new DBReadMessageHelper(getContext());
                while (usersAdapterPhoneNumber.size() > localDatabaseCounter) {
                    ReadMessageItem dataBaseItem = new ReadMessageItem(AES.encrypt(usersAdapterPhoneNumber.get(localDatabaseCounter), keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13));
                    myDbReadMessageHelper.insertdataPhone(dataBaseItem);
                    localDatabaseCounter++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                cursorReadMessage.moveToFirst();
                do {
                    usersAdapterPhoneNumber.add(AES.decrypt((cursorReadMessage.getString(1)), CaesarsCiphering.caesarsDecryption(cursorReadMessage.getString(2), 13)));
                }
                while (cursorReadMessage.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            //Toast.makeText(getContext(), getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static ArrayList<ReadEncryptedMessageItem> getFileContainerLocal() {
        return fileContainerLocal;
    }


    public ArrayList<ReadEncryptedMessageItem> getDeleteAdapterListLocal() {
        return deleteAdapterListLocal;
    }


    public static ArrayList<ReadEncryptedMessageItem> getDecryptedImageValueLocal() {
        return decryptedImageValueLocal;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedAudioValueLocal() {
        return decryptedAudioValueLocal;
    }


    public static ArrayList<ReadEncryptedMessageItem> getDecryptedVideoValueLocal() {
        return decryptedVideoValueLocal;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedPhoneNumberLocal() {
        return decryptedPhoneNumberLocal;
    }


    public static ArrayList<ReadEncryptedMessageItem> getDecryptedLocationValueLocal() {
        return decryptedLocationValueLocal;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedDocumentValueLocal() {
        return decryptedDocumentValueLocal;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedMessagesLocal() {
        return decryptedMessagesLocal;
    }

    public static ArrayList<ReadEncryptedMessageItem> getReadMessageStateLocal() {
        return readMessageStateLocal;
    }

    public static ArrayList<ReadEncryptedMessageItem> getDecryptedDocumentValue() {
        return decryptedDocumentValue;
    }

    public static boolean isControlLocalInternet() {
        return controlLocalInternet;
    }

    public static void setControlLocalInternet(boolean controlLocalInternet) {
        Fragment_Read_Encrypted_Message.controlLocalInternet = controlLocalInternet;
    }

    public static boolean isControlNetworkInternet() {
        return controlNetworkInternet;
    }

    public static void setControlNetworkInternet(boolean controlNetworkInternet) {
        Fragment_Read_Encrypted_Message.controlNetworkInternet = controlNetworkInternet;
    }

    public static Bitmap getBitmapLocal() {
        return bitmapLocal;
    }

    public static void setBitmapLocal(Bitmap bitmapLocal) {
        Fragment_Read_Encrypted_Message.bitmapLocal = bitmapLocal;
    }

    public static boolean isControlImageAsync() {
        return controlImageAsync;
    }

    public static void setControlImageAsync(boolean controlImageAsync) {
        Fragment_Read_Encrypted_Message.controlImageAsync = controlImageAsync;
    }

    public static ArrayList<ReadEncryptedMessageItem> getUsersAdapterLocal() {
        return usersAdapterLocal;
    }

    public static void setUsersAdapterLocal(ArrayList<ReadEncryptedMessageItem> usersAdapterLocal) {
        Fragment_Read_Encrypted_Message.usersAdapterLocal = usersAdapterLocal;
    }

    public static String getProgressTitle() {
        return progressTitle;
    }

    public static boolean isControlInternetConnection() {
        return controlInternetConnection;
    }

    public static void setControlInternetConnection(boolean controlInternetConnection) {
        Fragment_Read_Encrypted_Message.controlInternetConnection = controlInternetConnection;
    }

    public static boolean isControlInternetState() {
        return controlInternetState;
    }

    public static void setControlInternetState(boolean controlInternetState) {
        Fragment_Read_Encrypted_Message.controlInternetState = controlInternetState;
    }

}


class ImageLoadAsyncTask extends AsyncTask<Void, Void, String> {

    // private String url;
    private Activity activity;
    private String imageStringLocal;
    private int counter;
    private ProgressDialog progressdialog;
    private static String imageValueLocal;
    private static boolean controlImageAsync = true;
    private DBReadMessageHelperLocal myDbReadMessageHelperLocal;
    // private ImageView imageView;

    ImageLoadAsyncTask(Activity activity) {
        super();
        this.activity = activity;
        //this.url = url;
        //this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressdialog = new ProgressDialog(activity);
        progressdialog.setMessage(Fragment_Read_Encrypted_Message.getProgressTitle());
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected String doInBackground(Void... params) {
        counter = 0;
        myDbReadMessageHelperLocal = new DBReadMessageHelperLocal(activity);
        try {
            try {
                while (Fragment_Read_Encrypted_Message.getUsersAdapterLocal().size() > counter) {
                    if ((Fragment_Read_Encrypted_Message.getFileContainerLocal().get(counter).getPhoneNumber().equals("Image"))) {
                        // new ImageLoadAsyncTask(getActivity(), decryptedImageValueLocal.get(localDatabaseCounter).getPhoneNumber(),localDatabaseCounter).execute();
                        // imageStringLocal = ImageLoadAsyncTask.getImageValueLocal();
                        //  Picasso.get().load(Fragment_Read_Encrypted_Message.getDecryptedImageValue().get(getAdapterPosition()).getPhoneNumber()).into(image);
                        // decryptedImageValueLocal.remove(localDatabaseCounter);
                        //decryptedImageValueLocal.add(localDatabaseCounter, new ReadEncryptedMessageItem(imageStringLocal));
                        //ReadMessageItemFile dataBaseItem = new ReadMessageItemFile(decryptedImageValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedAudioValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedVideoValueLocal.get(localDatabaseCounter).getPhoneNumber(), decryptedLocationValueLocal.get(localDatabaseCounter).getPhoneNumber(), fileContainerLocal.get(localDatabaseCounter).getPhoneNumber());
                        // myDbReadMessageHelperLocal.insertdatafiles(dataBaseItem);


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bitmap = Picasso.get().load(Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().get(counter).getPhoneNumber()).get();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                        byte[] imageBytes = baos.toByteArray();
                        imageStringLocal = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        //imageValueLocal = imageStringLocal;
                        Fragment_Read_Encrypted_Message.setControlImageAsync(true);
                        //imageStringLocal = ImageLoadAsyncTask.getImageValueLocal();
                        //  Picasso.get().load(Fragment_Read_Encrypted_Message.getDecryptedImageValue().get(getAdapterPosition()).getPhoneNumber()).into(image);
                        Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().remove(counter);
                        Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().add(counter, new ReadEncryptedMessageItem(imageStringLocal));
                        //   ReadMessageItemFile dataBaseItem = new ReadMessageItemFile(Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedAudioValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedVideoValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getFileContainerLocal().get(counter).getPhoneNumber());

                        ReadMessageItemFile dataBaseItem = new ReadMessageItemFile(imageBytes, Fragment_Read_Encrypted_Message.getDecryptedAudioValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedVideoValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedDocumentValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getFileContainerLocal().get(counter).getPhoneNumber());
                        myDbReadMessageHelperLocal.insertdatafiles(dataBaseItem);

                        counter++;


                    } else {
                        ReadMessageItemFile dataBaseItem = new ReadMessageItemFile(Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().get(counter).getImageValue(), Fragment_Read_Encrypted_Message.getDecryptedAudioValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedVideoValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedDocumentValueLocal().get(counter).getPhoneNumber(), Fragment_Read_Encrypted_Message.getFileContainerLocal().get(counter).getPhoneNumber());
                        myDbReadMessageHelperLocal.insertdatafiles(dataBaseItem);
                        counter++;
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                progressdialog.dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
            progressdialog.dismiss();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.dismiss();

        }

    }

    public static String getImageValueLocal() {
        return imageValueLocal;
    }
}


class AsyncReadMessageInternetConnection extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private String mUrl;
    private static boolean controlAsyncTask = false;

    private ProgressDialog progressdialog;


    AsyncReadMessageInternetConnection(Context context, String url) {
        super();
        mContext = context;
        mUrl = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressdialog = new ProgressDialog(mActivity);
//        progressdialog.setMessage(LoginScreen.getProgressTitle());
//        progressdialog.show();
//        progressdialog.setCancelable(false);
//        progressdialog.setCanceledOnTouchOutside(false);
//        if (isCancelled()) {
//            progressdialog.dismiss();
//        }

    }

    @Override
    protected Boolean doInBackground(Void... booleans) {
        boolean isAvailable = false;
//        Looper.prepare();
        if (isCancelled()) {
            // progressdialog.dismiss();
            return false;
        } else {
            try {
                URL url = new URL(mUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(1000);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // Fragment_Read_Encrypted_Message fragment_read_encrypted_message=new Fragment_Read_Encrypted_Message();
                    isAvailable = true;
                    controlAsyncTask = true;
                    if (Fragment_Read_Encrypted_Message.isControlLocalInternet()) {
                        Fragment_Read_Encrypted_Message.setControlInternetConnection(true);
                        Fragment_Read_Encrypted_Message.setControlInternetState(true);
                        Toast.makeText(mContext, mContext.getString(R.string.read_encrypted_message_internet_activated), Toast.LENGTH_SHORT).show();
                    }
                    Fragment_Read_Encrypted_Message.setControlLocalInternet(false);
                    Fragment_Read_Encrypted_Message.setControlNetworkInternet(true);
                    Fragment_Read_Encrypted_Message.setControlInternetConnection(isAvailable);

                } else {
                    if (Fragment_Read_Encrypted_Message.isControlNetworkInternet()) {
                        Fragment_Read_Encrypted_Message.setControlInternetConnection(true);
                        Fragment_Read_Encrypted_Message.setControlInternetState(true);
                         Looper.prepare();
                        Toast.makeText(mContext, mContext.getString(R.string.read_encrypted_message_internet_deactivated), Toast.LENGTH_SHORT).show();
                        Looper.myLooper();
                       // Looper.loop();
                    } else {
                        Fragment_Read_Encrypted_Message.setControlLocalInternet(true);
                        Fragment_Read_Encrypted_Message.setControlNetworkInternet(false);
                        Looper.prepare();
                        Toast.makeText(mContext, mContext.getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                        Looper.myLooper();
                      //  Looper.loop();
                        new AsyncReadMessageInternetConnection(mContext, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                }
                new AsyncReadMessageInternetConnection(mContext, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
                if (Fragment_Read_Encrypted_Message.isControlNetworkInternet()) {
                    Fragment_Read_Encrypted_Message.setControlInternetState(true);
                    Looper.prepare();
                    Toast.makeText(mContext, mContext.getString(R.string.read_encrypted_message_internet_deactivated), Toast.LENGTH_SHORT).show();
                    Looper.myLooper();
                   // Looper.loop();
                } else {
                    isAvailable = false;
                    Fragment_Read_Encrypted_Message.setControlLocalInternet(true);
                    Fragment_Read_Encrypted_Message.setControlNetworkInternet(false);
                    Fragment_Read_Encrypted_Message.setControlInternetConnection(false);
                    Looper.prepare();
                    Toast.makeText(mContext, mContext.getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                    Looper.myLooper();
                   // Looper.loop();
                    new AsyncReadMessageInternetConnection(mContext, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            }
//            Looper.loop();
            return isAvailable;
        }


    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
//        if (isCancelled()) {
//            progressdialog.dismiss();
//        }
//        if (progressdialog != null && progressdialog.isShowing()) {
//            //asyncLoginScreen.cancel(true);
//            progressdialog.dismiss();
//        }
    }

    public static boolean isControlAsyncTask() {
        return controlAsyncTask;
    }

    public static void setControlAsyncTask(boolean controlAsyncTask) {
        AsyncReadMessageInternetConnection.controlAsyncTask = controlAsyncTask;
    }
}

class AsyncReadMessageTokenization extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    Handler mHandler;
    TextClassificationClient mClient;
    String mText;
    private ProgressDialog progressdialog;

    AsyncReadMessageTokenization(Context context, Handler handler, TextClassificationClient client) {
        super();
        // mText=text;
        mContext = context;
        mHandler = handler;
        mClient = client;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
//        progressdialog = new ProgressDialog(mContext);
//        progressdialog.setMessage(ForgotPassword.getProgressTitle());
//        progressdialog.show();
//        progressdialog.setCancelable(false);
//        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected Void doInBackground(Void... strings) {

        try {
            if(mHandler!=null){
                mHandler.post(
                        () -> {
                            mClient.load();
                        });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        if (!isCancelled()) {
//
//        }
//        if (progressdialog != null && progressdialog.isShowing()) {
//            progressdialog.dismiss();
//        }

    }
//    public static String classify(final String text,Handler handler,TextClassificationClient client) {
//        final String[] state = {null};
//        handler.post(
//                () -> {
//                    // Run text classification with TF Lite.
//                    List<TextClassificationClient.Result> results = client.classify(text);
//                    if(results.get(0).getConfidence()>results.get(1).getConfidence()){
//                        state[0] ="positive";
//                    }else{
//                        state[0] ="negative";
//                    }
//                    // Show classification result on screen
//                    //showResult(text, results);
//                });
//        return state[0];
//    }

}