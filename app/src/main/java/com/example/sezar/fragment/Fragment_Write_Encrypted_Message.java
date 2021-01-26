package com.example.sezar.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
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
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Trace;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sezar.BuildConfig;
import com.example.sezar.R;
import com.example.sezar.activity.LoginScreen;
import com.example.sezar.activity.MainActivity;
import com.example.sezar.adapter.WriteEncryptedMessageAdapter;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.example.sezar.encryption_algorithms.RandomStringGenerator;
import com.example.sezar.imageProcess.Classifier;
import com.example.sezar.model.WriteEncryptedMessageItem;
import com.example.sezar.utility.HelperMaps;
import com.example.sezar.utility.ImageUtils;
import com.example.sezar.utility.PhotoHelper;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.hardware.Camera.PreviewCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Write_Encrypted_Message extends Fragment {
    private static final String MESSAGE_KEY = "message_key";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private EditText editText_phone_number;
    private EditText editText_message;
    private Button button_send;
    private ImageView imageView_contacts, imageView_contacts_list, imageView_attach_file, imageView_speaker;
    TextView textView_reciever;
    private ArrayList<WriteEncryptedMessageItem> users;
    private RecyclerView recyclerView_user_info;
    private WriteEncryptedMessageAdapter adapter;
    private String phoneNumber, message;
    static String getUserPhoneNumber;
    static String getUserMessage;
    PendingIntent sentIntent = null, deliveryIntent = null;
    String scAddress = null;
    private boolean controlPermission;
    private boolean controlList;
    private int permissionCheck;
    private String userPhoneNumber;
    private boolean controlPhone, controlMessage;
    private boolean controlPhoneEmpty;
    private boolean controlPhoneExist;
    private DatabaseReference myRef;
    private DatabaseReference myKey;
    private FirebaseDatabase database;
    private StorageReference databaseStorage;
    private AlertDialog.Builder builderVideoStart;
    private AlertDialog.Builder builderLocationStart;
    private AlertDialog.Builder builderLocationAdd;
    private String key;
    private boolean controlCaesarEncryption;
    String deviceId;
    private int phoneCounter;
    private List<String> userNames;
    private List<String> phoneNumbers;
    private List<String> userPasswords;
    private List<String> keysList;
    private String encryptedMessage;
    private List<String> duplicatelist;
    private int arrayListCounter, arrayListUserNameCounter;
    private int recyclerViewCounter, recyclerViewPhoneCounter;
    private int arrayListPhoneCounter;
    private String userName;
    final private int REQUEST_SEND_SMS = 0;
    final private int REQUEST_REC_SMS = 1;
    private IntentFilter intentFilter;
    private String keySecret;
    private String readStateFalse = "false";
    private String userPhoneNumberKey;
    private int listCounterPhones = 0;
    private String SENT = "SMS_SENT";
    private String DELIVERED = "SMS_DELIVERED";
    private int RESULT_PICK_CONTACT;
    private Intent myFileIntent;
    private boolean controlSendFile;
    private static Boolean controlContactState;
    // private boolean photoPermission;
    private boolean audioPermission;
    private Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private int REQUEST_CAMERA = 123, SELECT_FILE = 124, SELECT_AUDIO_FILE = 125, SELECT_VIDEO_FILE = 126, REQUEST_VIDEO_CAMERA = 127, REQUEST_GPS_LOCATION = 128, REQUEST_GOOGLE_MAPS_LOCATION = 129, REQUEST_FILE = 130;
    private Bitmap bitmap;
    private Bitmap thumbnail;
    private byte[] imageValue;
    private String imageByteValue;
    private String uploadId;
    private Uri mImageUri;
    private Uri mSoundUri;
    private Uri mVideoUri;
    private Uri mFileUri;
    private String mLocation;
    private String getDownloadUrl;
    private ProgressDialog progressdialog;
    private boolean controlImage;
    private String progressTitle;
    private ImageView imageView_attach_file_place_holder;
    private static String mFileNameAudio = null;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private boolean imagePermission;
    private ImageView imageView_record_stop;
    private ImageView imageView_record_stop_place_holder;
    private ImageView imageView_video_place_holder;
    private ImageView imageView_file_place_holder;
    private boolean controlAudio;
    private boolean controlVideo;
    private boolean controlFile;
    private VideoView videoView;
    private AlertDialog.Builder builder_recorder_stop;
    private AlertDialog builder_recorder_stop_dialog;
    private ImageView imageView_location;
    private static boolean controlLocationMap;
    private ChildEventListener myRefListener;
    private TextToSpeech textToSpeech;
    private View builderDialogViewPdf;
    private AlertDialog.Builder builder_pdf;
    private PDFView pdfView;
    private View view;
    private String mFileString;
    private AsyncTask asyncWriteMessageInternetConnectionTask;
    private static boolean controlInternetConnection;
    private Intent fileData;
    private View dialogViewImage;
    private ImageView dialogImageView;
    private Classifier classifier;
    private Handler handler;
    private HandlerThread handlerThread;
    private Bitmap imageClassificationBitmap;
    private List<Classifier.Recognition> resultsClassification;
    private FrameLayout snackBarLayout;
    private Snackbar snackbar;


//    Fragment_Write_Encrypted_Message fragment_write_encrypted_message = Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message");


    public Fragment_Write_Encrypted_Message() {
        // Required empty public constructor
    }

    public static Fragment_Write_Encrypted_Message newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(Fragment_Write_Encrypted_Message.MESSAGE_KEY, message);
        Fragment_Write_Encrypted_Message fragment = new Fragment_Write_Encrypted_Message();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__write__encrypted__message, container, false);
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {


            String message = arguments.getString(MESSAGE_KEY);
            editText_phone_number = view.findViewById(R.id.editText_write_encrypted_message_phone_number);
            editText_message = view.findViewById(R.id.editText_write_encrypted_message_box);
            button_send = view.findViewById(R.id.button_write_encrypted_message_send);
            imageView_contacts = view.findViewById(R.id.imageView_contacts);
            imageView_contacts_list = view.findViewById(R.id.imageView_contacts_list);
            imageView_attach_file = view.findViewById(R.id.imageView_attach_file);
            imageView_attach_file_place_holder = view.findViewById(R.id.imageView_attach_file_place_holder);
            imageView_record_stop = view.findViewById(R.id.imageView_recorder);
            imageView_record_stop_place_holder = view.findViewById(R.id.imageView_recorder_placeholder);
            imageView_video_place_holder = view.findViewById(R.id.imageView_video_placeholder);
            imageView_file_place_holder = view.findViewById(R.id.imageView_file_placeholder);
            imageView_location = view.findViewById(R.id.imageView_location);
            imageView_speaker = view.findViewById(R.id.imageView_speaker);
            videoView = view.findViewById(R.id.videoView);


            progressdialog = new ProgressDialog(getContext());
            progressdialog.setTitle(R.string.forgot_password_wait);

            controlPermission = false;
            controlPhone = false;
            controlMessage = false;
            controlList = false;
            controlPhoneEmpty = false;
            controlPhoneExist = false;
            controlContactState = false;
            controlImage = false;
            controlSendFile = false;
            audioPermission = false;
            imagePermission = false;
            controlAudio = false;
            controlVideo = false;
            controlFile = false;
            controlLocationMap = false;
            controlCaesarEncryption = false;
            controlInternetConnection = false;


            arrayListCounter = 0;
            arrayListUserNameCounter = 0;
            phoneCounter = 0;
            recyclerViewCounter = 0;
            recyclerViewPhoneCounter = 0;
            arrayListPhoneCounter = 0;


            userName = LoginScreen.userName();

            userNames = new ArrayList<>();
            phoneNumbers = new ArrayList<>();
            userPasswords = new ArrayList<>();
            keysList = new ArrayList<>();
            duplicatelist = new ArrayList<>();


            users = new ArrayList<>();

            recyclerView_user_info = view.findViewById(R.id.recyclerView_fragment_write_encrypted_message);
            LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            adapter = new WriteEncryptedMessageAdapter(getContext(), users);
            recyclerView_user_info.setLayoutManager(layoutmanager);
            recyclerView_user_info.setAdapter(adapter);


            //checkForSmsPermission();
            checkVerificationSms();
            checkDatabase();


            asyncWriteMessageInternetConnectionTask = new AsyncWriteMessageInternetConnection(getContext(), database.getReference().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);
            button_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checkForSmsPermission();
                    if (checkNetworkAccess() && controlInternetConnection) {
                        smsPermissionVerification();
                        checkSMS();
                    } else {
//                        Fragment_Write_Encrypted_Message fragment_write_encrypted_message = Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message");
//                        getFragmentManager().beginTransaction().replace(R.id.fragment, fragment_write_encrypted_message).commit();
                        Toast.makeText(getContext(), getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            imageView_contacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    controlContactState = true;
                    //Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    //contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                }
            });

            imageView_contacts_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewCounter = 0;
                    recyclerViewPhoneCounter = 0;
                    addWriteEncryptedMessageItem();
                }
            });

            imageView_attach_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }

                    controlContactState = true;
                    if (!PhotoHelper.checkPermission(getContext())) {
                        PhotoHelper.checkPermission(getContext());
                        getFragmentManager().beginTransaction().detach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).attach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).commit();
                    } else if (!getAudioPermission()) {
                        getAudioPermission();
                        getFragmentManager().beginTransaction().detach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).attach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).commit();
                        // getFragmentManager().beginTransaction().replace(R.id.fragment, fragment_write_encrypted_message).commit();
                    } else if (!getCameraPermssion()) {
                        getCameraPermssion();
                        getFragmentManager().beginTransaction().detach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).attach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).commit();
                        // getFragmentManager().beginTransaction().replace(R.id.fragment, fragment_write_encrypted_message).commit();

                    } else {
                        selectImage();
                    }

                }


            });

            imageView_speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textToSpeech();
                }
            });

        }
        imageView_record_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRecorder != null) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }

                builder_recorder_stop_dialog.dismiss();
                Toast.makeText(getContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
                //imageView_record_stop.setVisibility(View.INVISIBLE);


            }
        });

        imageView_record_stop_place_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controlAudio) {
                    startAudio();
                }
            }
        });

        imageView_video_place_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controlVideo) {
                    startVideo();
                }
            }
        });

        imageView_file_place_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controlFile) {
                    if (mFileUri != null) {
                        //geliştirme yapılacak şimdilik sadece pdf çalısıyor.
                        //openFilePdf();
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                openFile(getContext(), new File(mFileUri.toString()));
                            } else {
                                openFile(getContext(), new File(mFileUri.toString() + "." + getFileExtension(mFileUri)));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                       /* if(getFileExtension(mFileUri).equals("application/pdf")){
                            openFilePdf();
                        }else{
                            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_file_format), Toast.LENGTH_LONG).show();
                        }*/
                    }
                }
            }
        });

        imageView_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getGpsPermission()) {
                    getGpsPermission();
                    getFragmentManager().beginTransaction().detach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).attach(Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message")).commit();
                } else {
                    if (checkLocationService()) {
                        if (mLocation == null) {
                            addLocation();
                        } else {
                            selectLocation();
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.write_encrypted_message_location_check), Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        return view;
    }

    private void checkPhoneNumber() {
        phoneNumber = editText_phone_number.getText().toString().trim();
        if (phoneNumber.length() != 0) {
            if (validCellPhone(phoneNumber)) {
                controlPhoneEmpty = true;
                controlPhone = true;
            } else {
                controlPhoneEmpty = true;
                controlPhone = false;
            }

        } else {
            controlPhoneEmpty = false;

        }


    }

    private void checkMessage() {
        message = editText_message.getText().toString().trim();
        if (message.length() != 0) {
            controlMessage = true;
        } else {
            controlMessage = false;

        }

    }

    private void sendSms() {
        RandomStringGenerator rsg = new RandomStringGenerator();
        keySecret = rsg.randomStringGenerator();
        arrayListCounter = 0;
        arrayListPhoneCounter = 0;
        recyclerViewCounter = 0;
        recyclerViewPhoneCounter = 0;
        controlPhoneExist = false;
        listCounterPhones = 0;
        SmsManager smsManager = SmsManager.getDefault();


        encryptedMessage = AES.encrypt(message, keySecret).trim();
        if (getDownloadUrl != null && (mImageUri != null || mSoundUri != null || mVideoUri != null || mFileUri != null)) {
            getDownloadUrl = AES.encrypt((getDownloadUrl), keySecret);
        }

        if (mLocation != null) {
            mLocation = AES.encrypt(mLocation, keySecret);
        }


        //imageValue=AES.encrypt((imageValue),keySecret);


        ArrayList<String> textList = smsManager.divideMessage(encryptedMessage.substring(0, message.length() / 2));

        // String textSMS = encryptedMessage.substring(0, message.length() / 2);
        char text = encryptedMessage.charAt(0);
        char text1 = encryptedMessage.charAt(1);
        char text2 = encryptedMessage.charAt(2);

        String textSMS = String.valueOf(text).concat(String.valueOf(text1).concat(String.valueOf(text2)).concat("(" + getString(R.string.write_encrypted_message_sms_title) + ")"));

        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(DELIVERED), 0);

        keySecret = CaesarsCiphering.caesarsEncryption(keySecret, 13);
        userPhoneNumberKey = CaesarsCiphering.caesarsEncryption(userPhoneNumberKey, 13);

        if (!users.isEmpty()) {

            controlList = true;
            Iterator<WriteEncryptedMessageItem> checkPhoneNumbers = users.iterator();
            while (checkPhoneNumbers.hasNext()) {

                controlPhoneExist = true;
                //smsManager.sendMultipartTextMessage(users.get(recyclerViewCounter).getPhoneNumber(), null, textList, null, null);
                smsManager.sendTextMessage(users.get(recyclerViewCounter).getPhoneNumber(), null, textSMS, sentPI, deliveredPI);

                recyclerViewCounter++;
                recyclerViewPhoneCounter++;

                if (users.size() == recyclerViewPhoneCounter) {
                    break;
                }
            }


        } else {
            int listcounter = 0;
            controlList = false;
            Iterator<String> checkPhoneNumber = phoneNumbers.iterator();
            while (checkPhoneNumber.hasNext()) {
                if (phoneNumber.equals(phoneNumbers.get(listcounter))) {
                    controlPhoneExist = true;
                    //smsManager.sendMultipartTextMessage(phoneNumbers.get(listcounter).getPhoneNumber(), null, textList, null, null);
                    smsManager.sendTextMessage(phoneNumbers.get(listcounter), null, textSMS, sentPI, deliveredPI);

                    //broadCast();
                    break;
                }
                listcounter++;

                if (phoneNumbers.size() == listcounter) {
                    break;
                }
            }
        }


        database = FirebaseDatabase.getInstance();
        myKey = database.getReference("keys:");
        Iterator<String> checkPhoneNumber = phoneNumbers.iterator();
        while (checkPhoneNumber.hasNext()) {


            if (controlList) {
                if ((users.get(arrayListCounter).getPhoneNumber()).equals(phoneNumbers.get(arrayListPhoneCounter))) {
                    myRef = database.getReference(keysList.get(arrayListPhoneCounter));
                    key = myRef.push().getKey();
                    Map<String, Object> postValues = toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(key, postValues);
                    myRef.updateChildren(childUpdates);
                }
            } else {
                if (phoneNumber.equals(phoneNumbers.get(arrayListCounter))) {
                    myRef = database.getReference(keysList.get(arrayListCounter));

                    key = myRef.push().getKey();
                    Map<String, Object> postValues = toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(key, postValues);
                    myRef.updateChildren(childUpdates);
                    break;
                }

            }
            arrayListPhoneCounter++;

            if (users.size() == arrayListCounter && !users.isEmpty()) {
                break;
            }
            if (phoneNumbers.size() == arrayListPhoneCounter) {
                arrayListPhoneCounter = 0;
                arrayListCounter++;
                if (users.size() == arrayListCounter || phoneNumbers.size() == arrayListCounter) {
                    break;
                }
            }
        }

        if (controlPhoneExist) {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_succes), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_doesnt_exist), Toast.LENGTH_SHORT).show();
        }

    }


    private void checkForSmsPermission() {


        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            checkPhoneNumber();
            checkMessage();
            controlPermission = true;
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    0);
        }


    }

    private void smsPermissionVerification() {
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            checkPhoneNumber();
            checkMessage();
            controlPermission = true;
            //sendSms();
        } else {
            controlPermission = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 0:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), getString(R.string.write_encrypted_message_permission_success), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                }

                break;

        }
    }

    private boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    private void checkSMS() {
        if (controlPermission && controlMessage && controlPhone && controlPhoneEmpty) {


            if (mImageUri != null) {
                uploadFile();
            } else if (mSoundUri != null) {
                uploadFile();
            } else if (mVideoUri != null) {
                uploadFile();
            } else if (mFileUri != null) {
                uploadFile();
            } else {
                sendSms();
            }


            //TelephonyManager telephonyManager=(TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
        } else if (!controlPermission) {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_permission), Toast.LENGTH_SHORT).show();
        } else if (!controlPhoneEmpty) {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_phone_empty), Toast.LENGTH_SHORT).show();
        } else if (!controlPhone) {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_phone_format_fail), Toast.LENGTH_SHORT).show();

        } else if (!controlMessage) {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_box_empty), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDatabase() {
        database = FirebaseDatabase.getInstance();
        databaseStorage = FirebaseStorage.getInstance().getReference("images");
        myKey = database.getReference("keys:");
        key = myKey.getKey();


        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String getKey = postSnapshot.child("Key").getValue(String.class);
                    String username = postSnapshot.child("Username").getValue(String.class);
                    String phoneNumber = postSnapshot.child("Phone").getValue(String.class);
                    String keys = postSnapshot.getKey();

                    if (getKey != null) {
                        getKey = CaesarsCiphering.caesarsDecryption(getKey, 13);
                    }

                    if (username != null && getKey != null) {
                        username = AES.decrypt(username, getKey);
                    }
                    if (username != null) {
                        if (username.equals(LoginScreen.userName())) {
                            userPhoneNumber = phoneNumber;
                            userPhoneNumberKey = getKey;
                        }
                    }


                    if (phoneNumber != null && getKey != null) {
                        phoneNumber = AES.decrypt(phoneNumber, getKey);
                    }


                    //String[] stringArrayUserName = {username};
                    //String[] stringArrayPhoneNumber = {phoneNumber};
                    //String[] stringArrayKey = {keys};
                    if (!userNames.contains(username)) {
                        userNames.add(username);
                    }

                    if (!phoneNumbers.contains(phoneNumber)) {
                        phoneNumbers.add(phoneNumber);
                    }

                    if (!keysList.contains(keys)) {
                        keysList.add(keys);
                    }
                    //userNames.addAll(Arrays.asList(stringArrayUserName));
                    //phoneNumbers.addAll(Arrays.asList(stringArrayPhoneNumber));
                    //keysList.addAll(Arrays.asList(stringArrayKey));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*myRefListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userNames.clear();
                phoneNumbers.clear();
                keysList.clear();
                checkDatabase();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                userNames.clear();
                phoneNumbers.clear();
                keysList.clear();
                checkDatabase();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }


    @Exclude
    public Map<String, Object> toMap() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        recyclerViewCounter = 0;
        recyclerViewPhoneCounter = 0;

        HashMap<String, Object> result = new HashMap<>();
        if (controlList) {

            result.put("Encrypted Message", encryptedMessage + " (" + dateFormat.format(cal.getTime()) + ")");
            //result.put("Message", userPhoneNumber + ":" + message + " (" + dateFormat.format(cal.getTime()) + ")");
            result.put("Phone Number Key", userPhoneNumberKey);
            // result.put("Message",encryptedMessage);
            result.put("Message", encryptedMessage);
            result.put("Decryption Key", keySecret);
            result.put("Phone Number", userPhoneNumber);
            result.put("Read State", readStateFalse);
            if (imageValue != null && getDownloadUrl != null) {
                //result.put("Image Value", Base64.encodeToString(imageValue,Base64.NO_WRAP));
                result.put("Image Value", getDownloadUrl);

            } else if (mSoundUri != null && getDownloadUrl != null) {
                result.put("Audio Value", getDownloadUrl);
            } else if (mVideoUri != null && getDownloadUrl != null) {
                result.put("Video Value", getDownloadUrl);
            } else if (mFileUri != null && getDownloadUrl != null) {
                result.put("File Value", getDownloadUrl);
            }
            if (mLocation != null) {
                result.put("Location Value", mLocation);
            }
            return result;

        } else {
            result.put("Encrypted Message", encryptedMessage + " (" + dateFormat.format(cal.getTime()) + ")");
            //result.put("Message", userPhoneNumber + ":" + message + " (" + dateFormat.format(cal.getTime()) + ")");
            //result.put("Message",encryptedMessage);
            result.put("Phone Number Key", userPhoneNumberKey);
            result.put("Message", encryptedMessage);
            result.put("Decryption Key", keySecret);
            result.put("Phone Number", userPhoneNumber);
            result.put("Read State", readStateFalse);
            if (imageValue != null && getDownloadUrl != null) {
                //result.put("Image Value", Base64.encodeToString(imageValue,Base64.NO_WRAP));
                result.put("Image Value", getDownloadUrl);

            } else if (mSoundUri != null && getDownloadUrl != null) {
                result.put("Audio Value", getDownloadUrl);
            } else if (mVideoUri != null && getDownloadUrl != null) {
                result.put("Video Value", getDownloadUrl);
            } else if (mFileUri != null && getDownloadUrl != null) {
                result.put("File Value", getDownloadUrl);
            }
            if (mLocation != null) {
                result.put("Location Value", mLocation);
            }

            return result;

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivity.setControlDeviceAccess(false);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                getContacts(data);
            } else if (requestCode == 10) {
                String path = data.getData().getPath();
            } else if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == SELECT_AUDIO_FILE) {
                onSelectFromGalleryAudioResult(data);
            } else if (requestCode == SELECT_VIDEO_FILE) {
                onSelectFromGalleryVideoResult(data);
            } else if (requestCode == REQUEST_VIDEO_CAMERA) {
                onCaptureVideoResult(data);
            } else if (requestCode == REQUEST_FILE) {
                onSelectFileResult(data);
            }
        }
        if (requestCode == REQUEST_GOOGLE_MAPS_LOCATION) {
            mLocation = HelperMaps.getLatitude() + "," + HelperMaps.getLongitude();
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_location_success), Toast.LENGTH_SHORT).show();
        }
        //return nameList;
    }

    private void getContacts(Intent data) {
        Uri contectDataVar = data.getData();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(contectDataVar,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                //nameList.add(name);
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    String phoneNo = cur.getString(cur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    editText_phone_number.setText(phoneNo.replaceAll("[ -]", ""));
                       /* Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        if (pCur.moveToFirst()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            editText_phone_number.setText(phoneNo.replaceAll("[ -]", ""));
                            break;
                        }
                        pCur.close();*/
                } /*else {
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                editText_phone_number.setText(phoneNo.replaceAll("[ -]", ""));
                            }
                            pCur.close();
                        }
                    }*/

            }
        }
        if (cur != null) {
            cur.close();
        }
        //}

    }

    private void addWriteEncryptedMessageItem() {
        int listcounter = 0;


        checkPhoneNumber();
        Iterator<String> checkPhoneNumber = phoneNumbers.iterator();
        if (controlPhone && controlPhoneEmpty) {
            while (checkPhoneNumber.hasNext()) {
                if (phoneNumber.equals(phoneNumbers.get(listcounter))) {
                    if (!users.isEmpty()) {
                        checkList();
                        break;

                    } else {
                        users.add(new WriteEncryptedMessageItem(phoneNumber));
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }


                listcounter++;
                if (phoneNumbers.size() == listcounter) {
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_doesnt_exist), Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        } else {
            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_phone_format_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkVerificationSms() {
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

    private void checkList() {
        int listCounterString = 0;
        for (int i = 0; i < users.size(); i++) {
            duplicatelist.add(users.get(i).getPhoneNumber());
            if (users.size() == i) {
                break;
            }
        }

        Iterator<String> checkList = duplicatelist.iterator();
        while (checkList.hasNext()) {
            if (!duplicatelist.contains(phoneNumber)) {
                users.add(new WriteEncryptedMessageItem(phoneNumber));
                adapter.notifyDataSetChanged();
                break;
            }
            listCounterString++;
            if (duplicatelist.size() == listCounterString) {
                Toast.makeText(getContext(), getString(R.string.write_encrypted_message_list_element_exists), Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }

    private void broadCast() {
        getContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent ıntent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "message sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                }

            }
        }, new IntentFilter(SENT));

        getContext().registerReceiver(
                new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode()) {
                            case Activity.RESULT_OK:
                                Toast.makeText(arg0, "message delivered", Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));
    }

    public static Boolean getControlContactState() {
        return controlContactState;
    }


    @Override
    public synchronized void onResume() {
        super.onResume();
        controlContactState = false;
        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }


    private void selectImage() {
        controlContactState = true;
        if (mImageUri != null) {
            if (bitmap != null) {
                imageView_attach_file_place_holder.setImageBitmap(bitmap);
            } else if (thumbnail != null) {
                imageView_attach_file_place_holder.setImageBitmap(thumbnail);
            }


            final String[] fileItems = {getString(R.string.write_encrypted_message_change_photo), getString(R.string.write_encrypted_message_remove_photo), getString(R.string.write_encrypted_message_cardview_cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
            dialogViewImage = LayoutInflater.from(getContext()).inflate(R.layout.write_encrypted_message_image_dialog_item, null);
            dialogImageView = dialogViewImage.findViewById(R.id.imageView_write_encrypted_message_dialog_image);
            if (bitmap != null) {
                dialogImageView.setImageBitmap(bitmap);
            } else if (thumbnail != null) {
                dialogImageView.setImageBitmap(thumbnail);
            }
//            if(imageView_file_place_holder.getParent()!=null){
//                ((ViewGroup) imageView_attach_file_place_holder.getParent()).removeView(imageView_attach_file_place_holder);
//            }

            //imageView_attach_file_place_holder.setVisibility(View.VISIBLE);
            builder.setTitle(getString(R.string.write_encrypted_message_file_title));
            builder.setView(dialogViewImage);
            builder.setItems(fileItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (fileItems[which].equals(fileItems[0])) {
                        mImageUri = null;
                        bitmap = null;
                        thumbnail = null;
                        selectImage();
                    } else if (fileItems[which].equals(fileItems[1])) {
                        mImageUri = null;
                        bitmap = null;
                        thumbnail = null;
                        imageView_attach_file_place_holder.setVisibility(View.INVISIBLE);
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[2])) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();
            snackbar = Snackbar.make(view, "Image Classified As:" + resultsClassification.get(0).getTitle() +" "+ "with:" + resultsClassification.get(0).getConfidence(), Snackbar.LENGTH_INDEFINITE);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.button_background));
            snackbar.setActionTextColor(getResources().getColor(R.color.white));
            snackbar.show();


        } else if (mSoundUri != null) {
            controlAudio = true;
            final String[] fileItems = {getString(R.string.write_encrypted_message_change_record), getString(R.string.write_encrypted_message_remove_record), getString(R.string.write_encrypted_message_cardview_cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
            ((ViewGroup) imageView_record_stop_place_holder.getParent()).removeView(imageView_record_stop_place_holder);
            imageView_record_stop_place_holder.setVisibility(View.VISIBLE);
            builder.setTitle(getString(R.string.write_encrypted_message_file_title));
            builder.setView(imageView_record_stop_place_holder);
            builder.setItems(fileItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if (fileItems[which].equals(fileItems[0])) {
                        mSoundUri = null;
                        controlAudio = false;
                        selectImage();
                    } else if (fileItems[which].equals(fileItems[1])) {
                        mSoundUri = null;
                        controlAudio = false;
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[2])) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    //controlAudio = false;
                }
            });

        } else if (mVideoUri != null) {
            controlVideo = true;
            final String[] fileItems = {getString(R.string.write_encrypted_message_change_video), getString(R.string.write_encrypted_message_remove_video), getString(R.string.write_encrypted_message_cardview_cancel)};
            AlertDialog.Builder builderVideo = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
            ((ViewGroup) imageView_video_place_holder.getParent()).removeView(imageView_video_place_holder);
            imageView_video_place_holder.setVisibility(View.VISIBLE);
            builderVideo.setTitle(getString(R.string.write_encrypted_message_file_title));
            builderVideo.setView(imageView_video_place_holder);
            builderVideo.setItems(fileItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if (fileItems[which].equals(fileItems[0])) {
                        mVideoUri = null;
                        controlVideo = false;
                        selectImage();
                    } else if (fileItems[which].equals(fileItems[1])) {
                        mVideoUri = null;
                        controlVideo = false;
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[2])) {
                        dialog.dismiss();

                    }
                }
            });
            builderVideo.show();

            builderVideo.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    //controlAudio = false;
                }
            });

        } else if (mFileUri != null) {
            controlFile = true;
            final String[] fileItems = {getString(R.string.write_encrypted_message_change_file), getString(R.string.write_encrypted_message_remove_file), getString(R.string.write_encrypted_message_cardview_cancel)};
            AlertDialog.Builder builderVideo = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
            ((ViewGroup) imageView_file_place_holder.getParent()).removeView(imageView_file_place_holder);
            imageView_file_place_holder.setVisibility(View.VISIBLE);
            builderVideo.setTitle(getString(R.string.write_encrypted_message_file_title));
            builderVideo.setView(imageView_file_place_holder);
            builderVideo.setItems(fileItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if (fileItems[which].equals(fileItems[0])) {
                        mFileUri = null;
                        controlFile = false;
                        fileIntent();
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[1])) {
                        mFileUri = null;
                        controlFile = false;
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[2])) {
                        dialog.dismiss();

                    }
                }
            });
            builderVideo.show();

            builderVideo.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    //controlAudio = false;
                }
            });
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
            final CharSequence[] fileItems = {getString(R.string.write_encrypted_message_image_gallery), getString(R.string.write_encrypted_message_image_photo), getString(R.string.write_encrypted_message_audio_gallery), getString(R.string.write_encrypted_message_audio_record), getString(R.string.write_encrypted_message_video_gallery), getString(R.string.write_encrypted_message_video_record), getString(R.string.write_encrypted_message_file), getString(R.string.write_encrypted_message_cardview_cancel)};
            builder.setTitle(getString(R.string.write_encrypted_message_file_title));
            builder.setItems(fileItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.setControlDeviceAccess(true);
                    if (fileItems[which].equals(fileItems[0])) {
                        libraryIntent();
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[1])) {
                        cameraIntent();
                        dialog.dismiss();


                    } else if (fileItems[which].equals(fileItems[2])) {
                        audioIntent();
                        dialog.dismiss();


                    } else if (fileItems[which].equals(fileItems[3])) {
                        takeAudioRecord();
                        dialog.dismiss();


                    } else if (fileItems[which].equals(fileItems[4])) {
                        videoLibraryIntent();
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[5])) {
                        videoIntent();
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[6])) {
                        fileIntent();
                        dialog.dismiss();

                    } else if (fileItems[which].equals(fileItems[7])) {
                        dialog.dismiss();

                    }
                }
            });
            builder.show();

        }

    }

    private void cameraIntent() {
        startActivityForResult(intentPhoto, REQUEST_CAMERA);
    }


    private void videoIntent() {
        Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(captureVideoIntent, REQUEST_VIDEO_CAMERA);
    }

    private void libraryIntent() {
        Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGallery.setType("image/*");
        startActivityForResult(Intent.createChooser(intentGallery, "select file"), SELECT_FILE);
    }

    private void audioIntent() {
        Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(audioIntent, SELECT_AUDIO_FILE);
    }


    private void openFilePdf() {
        builderDialogViewPdf = LayoutInflater.from(getContext()).inflate(R.layout.pdf_item, null);
        pdfView = builderDialogViewPdf.findViewById(R.id.pdfView);
        builder_pdf = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        builder_pdf.setView(builderDialogViewPdf);
        pdfView.fromUri(mFileUri)
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                // add dynamic spacing to fit each page on its own on the screen // mode to fit pages in the view
                // fit each page to the view, else smaller pages are scaled relative to largest page.
                // snap pages to screen boundaries
                // toggle night mode
                .load();
        final AlertDialog builderVideoDialog = builder_pdf.create();
        builderVideoDialog.show();
    }

    private void openFile() {
        String[] mimeTypes =

                {"image/*", "application/pdf", "application/msword", "application/vnd.ms-powerpoint", "application/vnd.ms-excel", "text/plain"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                if (mimeTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
                intent.setData(FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(mFileUri.toString())));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                String mimeTypesStr = "";
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                    intent.setData(Uri.fromFile(new File(mFileUri.toString())));
                    intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }


                //intent.setAction("com.example.sezar.activity.MainActivity");

                startActivityForResult(Intent.createChooser(intent, "Choose an Application:"), 35);
                //startActivity(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(Intent.createChooser(intent, "Choose an Application:"));
        }
    }

    public void openFile(Context context, File url) throws IOException {
        // Create URI
        Uri uri;
        File file = url;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(mFileUri.toString()));
        } else {
            uri = Uri.fromFile(file);
        }
        //getRealPathFromUri(context, uri);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setData(mFileUri);
        //intent.putExtra(Intent.ACTION_VIEW,mFileUri);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //  intent.putExtra(Intent.ACTION_VIEW,uri);
        //  intent.putExtra(Intent.ACTION_OPEN_DOCUMENT,uri);
        //intent.addCategory(Intent.ACTION_PICK);
        //intent.addCategory(Intent.ACTION_VIEW);
        //intent.putExtra(Intent.ACTION_VIEW, Uri.parse(getRealPathFromUri(context, uri)));
        // intent.putExtra(Intent.ACTION_VIEW,mFileUri);
        //intent.setData(mFileUri);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        //intent.putExtra(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri);
        //intent.putExtra(Intent.ACTION_GET_CONTENT,uri);
        //intent.setAction(Intent.ACTION_OPEN_DOCUMENT, mFileUri);
        //  intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
//        if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".doc") || uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".docx")) {
//            // Word document
//            intent.setDataAndType(mFileUri, "application/msword");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".pdf")) {
//            // PDF file
//            intent.setDataAndType(mFileUri, "application/pdf");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".ppt") || uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".pptx")) {
//            // Powerpoint file
//            intent.setDataAndType(mFileUri, "application/vnd.ms-powerpoint");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".xls") || uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".xlsx")) {
//            // Excel file
//            intent.setDataAndType(mFileUri, "application/vnd.ms-excel");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".zip") || uri.toString().contains(".rar")) {
//            // WAV audio file
//            intent.setDataAndType(mFileUri, "application/x-wav");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".rtf")) {
//            // RTF file
//            intent.setDataAndType(mFileUri, "application/rtf");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".wav") || uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".mp3")) {
//            // WAV audio file
//            intent.setDataAndType(mFileUri, "audio/x-wav");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".gif")) {
//            // GIF file
//            intent.setDataAndType(mFileUri, "image/gif");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".jpg") || uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".jpeg") || url.toString().contains(".png")) {
//            // JPG file
//            //intent.setData(uri);
//            intent.setDataAndType(mFileUri, "image/jpeg");
//            //  intent.setType("image/*");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".txt")) {
//            // Text file
//            intent.setDataAndType(mFileUri, "text/plain");
//        } else if (uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".3gp") || uri.toString().substring(uri.toString().lastIndexOf(".")).contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
//            // Video files
//            intent.setDataAndType(mFileUri, "video/*");
//        } else {
//            //if you want you can also define the intent type for any other file
//
//            //additionally use else clause below, to manage other unknown extensions
//            //in this case, Android will show all applications installed on the device
//            //so you can choose which application to use
//            intent.setDataAndType(mFileUri, "*/*");
//        }
//        intent.putExtra(Intent.ACTION_OPEN_DOCUMENT, uri);
//        intent.setDataAndType(uri, "*/*");
//        intent.putExtra(Intent.ACTION_GET_CONTENT, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            //intent.setData(mFileUri);
            startActivity(intent);
        }

    }


    private void videoLibraryIntent() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "select file"), SELECT_VIDEO_FILE);
    }

    private void fileIntent() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "select file"), REQUEST_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                mImageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream imageValueByte = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, imageValueByte);
                imageValue = imageValueByte.toByteArray();
                imageClassificationBitmap = bitmap;
                classifier = Classifier.create(getActivity(), Classifier.Model.QUANTIZED, Classifier.Device.GPU, 1);
                processImage();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSelectFileResult(Intent data) {
        if (data != null) {
            fileData = data;
            mFileUri = data.getData();
            mFileString = data.getData().getPath();
          /*  try {
                getPath(getContext(), mFileUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }*/
        }
    }

    private void onCaptureImageResult(Intent data) {

        mImageUri = data.getData();

        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        imageValue = bytes.toByteArray();
        imageClassificationBitmap = thumbnail;

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            if (mImageUri == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mImageUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", destination);
                } else {
                    mImageUri = Uri.fromFile(destination);
                }
            }

            //uploadFile();
            fo.close();
            classifier = Classifier.create(getActivity(), Classifier.Model.QUANTIZED, Classifier.Device.GPU, 1);
            processImage();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //bitmap= BitmapFactory.decodeByteArray(imageValue,0,imageValue.length);
        //imageView_attach_file.setImageBitmap(bitmap);
    }

    private void onSelectFromGalleryAudioResult(Intent data) {
        if (data != null) {
            // mSoundUri = Uri.parse(getRealPathFromUri(getContext(),data.getData()));
            mSoundUri = data.getData();
            if (mSoundUri == null) {
                mSoundUri = Uri.parse(data.getData().getPath());
            }


        }
    }

    private void onSelectFromGalleryVideoResult(Intent data) {
        if (data != null) {
            mVideoUri = data.getData();
            if (mVideoUri == null) {
                mVideoUri = Uri.parse(data.getData().getPath());
            }
        }
    }

    private void onCaptureVideoResult(Intent data) {
        if (data != null) {
            mVideoUri = data.getData();
        }


    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Files.FileColumns.DATA};
            context.getContentResolver();
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {

                return contentUri.getPath();
            }
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {

        if (mImageUri != null) {
            final StorageReference fileReference = databaseStorage.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            //getDownloadUrl= fileReference.child("images/").getDownloadUrl().toString();
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_success), Toast.LENGTH_SHORT).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getDownloadUrl = uri.toString();
                            sendSms();
                        }
                    });


                    //getDownloadUrl=taskSnapshot.getUploadSessionUri().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressdialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_failure), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.show();
                    progressdialog.setCancelable(false);
                    progressdialog.setCanceledOnTouchOutside(false);
                    //Toast.makeText(getContext(),"upload file  on progress", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (mSoundUri != null) {
            final StorageReference fileReference = databaseStorage.child(System.currentTimeMillis() + "." + getFileExtension(mSoundUri));
            //getDownloadUrl= fileReference.child("images/").getDownloadUrl().toString();
            fileReference.putFile(mSoundUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_success), Toast.LENGTH_SHORT).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getDownloadUrl = uri.toString();
                            sendSms();
                        }
                    });


                    //getDownloadUrl=taskSnapshot.getUploadSessionUri().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressdialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_failure), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.show();
                    progressdialog.setCancelable(false);
                    progressdialog.setCanceledOnTouchOutside(false);
                    //Toast.makeText(getContext(),"upload file  on progress", Toast.LENGTH_SHORT).show();

                }
            });
        } else if (mVideoUri != null) {

            final StorageReference fileReference = databaseStorage.child(System.currentTimeMillis() + "." + getFileExtension(mVideoUri));
            //getDownloadUrl= fileReference.child("images/").getDownloadUrl().toString();
            fileReference.putFile(mVideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_success), Toast.LENGTH_SHORT).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getDownloadUrl = uri.toString();
                            sendSms();
                        }
                    });


                    //getDownloadUrl=taskSnapshot.getUploadSessionUri().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressdialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_failure), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.show();
                    progressdialog.setCancelable(false);
                    progressdialog.setCanceledOnTouchOutside(false);
                    //Toast.makeText(getContext(),"upload file  on progress", Toast.LENGTH_SHORT).show();

                }
            });
        } else if (mFileUri != null) {
            long systemTime = System.currentTimeMillis();
            final StorageReference fileReference = databaseStorage.child(systemTime + "." + getFileExtension(mFileUri));
            //getDownloadUrl= fileReference.child("images/").getDownloadUrl().toString();
            fileReference.putFile(mFileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_success), Toast.LENGTH_SHORT).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            getDownloadUrl = systemTime + "." + getFileExtension(mFileUri);
                            sendSms();
                        }
                    });


                    //getDownloadUrl=taskSnapshot.getUploadSessionUri().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressdialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.write_encrypted_message_upload_file_failure), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressdialog.show();
                    progressdialog.setCancelable(false);
                    progressdialog.setCanceledOnTouchOutside(false);
                    //Toast.makeText(getContext(),"upload file  on progress", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private boolean getCameraPermssion() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SEND_SMS);
            return false;
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_SEND_SMS);
            return false;
        } else {
            return true;
        }


    }

    private boolean getAudioPermission() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_SEND_SMS);
            return false;
        } else {
            return true;
        }

    }

    private void takeAudioRecord() {
        mFileNameAudio = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileNameAudio += "/AudioRecording.3gp";
        mSoundUri = Uri.fromFile(new File(mFileNameAudio));
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFileNameAudio);
        try {
            mRecorder.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }


        mRecorder.start();
        Toast.makeText(getContext(), "Recording Started", Toast.LENGTH_LONG).show();
        //imageView_record_stop.setVisibility(View.VISIBLE);
        builder_recorder_stop = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        ((ViewGroup) imageView_record_stop.getParent()).removeView(imageView_record_stop);
        builder_recorder_stop.setTitle(getString(R.string.write_encrypted_message_stop_record));
        imageView_record_stop.setVisibility(View.VISIBLE);
        builder_recorder_stop.setView(imageView_record_stop);

        builder_recorder_stop_dialog = builder_recorder_stop.create();
        builder_recorder_stop_dialog.show();
        // builder_recorder_stop.create().show();


    }


    private void startAudio() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.release();
            }
        }
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mSoundUri.getPath());
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.start();
        Toast.makeText(getContext(), "Recording Playing",
                Toast.LENGTH_LONG).show();
    }

    private void startVideo() {

        if (mVideoUri != null) {
            //final String[] fileItems = {};
            builderVideoStart = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
            ((ViewGroup) videoView.getParent()).removeView(videoView);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(mVideoUri);
            videoView.start();
            //builderVideo.setTitle(getString(R.string.write_encrypted_message_file_title));
            builderVideoStart.setView(videoView);
            final AlertDialog builderVideoDialog = builderVideoStart.create();
            builderVideoDialog.show();

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    builderVideoDialog.dismiss();

                    //videoView.setVisibility(View.INVISIBLE);

                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    builderVideoDialog.dismiss();
                    return false;
                }
            });

        }
        // videoView.setVisibility(View.VISIBLE);

    }


    private boolean getGpsPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS_LOCATION);
            return false;
        }
    }

    private void addLocation() {
        final String[] fileItems = {getString(R.string.write_encrypted_message_location_add), getString(R.string.write_encrypted_message_cardview_cancel)};
        builderLocationAdd = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        builderLocationAdd.setTitle(getString(R.string.write_encrypted_message_location_title));

        builderLocationAdd.setItems(fileItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (fileItems[which].equals(fileItems[0])) {
                    startActivityForResult(new Intent(getContext(), HelperMaps.class), REQUEST_GOOGLE_MAPS_LOCATION);


                } else if (fileItems[which].equals(fileItems[1])) {
                    dialog.dismiss();
                }
            }
        });
        final AlertDialog builderLocationDialog = builderLocationAdd.create();
        builderLocationDialog.show();
    }

    private void selectLocation() {
        final String[] fileItems = {getString(R.string.write_encrypted_message_location_map), getString(R.string.write_encrypted_message_location_change), getString(R.string.write_encrypted_message_location_remove), getString(R.string.write_encrypted_message_cardview_cancel)};
        builderLocationStart = new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        builderLocationStart.setTitle(getString(R.string.write_encrypted_message_location_title));
        //((ViewGroup) videoView.getParent()).removeView(videoView);
        //videoView.setVisibility(View.VISIBLE);
        // builderVideoStart.setView(videoView);

        builderLocationStart.setItems(fileItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (fileItems[which].equals(fileItems[0])) {
                    controlLocationMap = true;
                    startActivity(new Intent(getContext(), HelperMaps.class));


                } else if (fileItems[which].equals(fileItems[1])) {
                    mLocation = null;
                    controlLocationMap = false;
                    addLocation();
                    dialog.dismiss();


                } else if (fileItems[which].equals(fileItems[2])) {
                    mLocation = null;
                    controlLocationMap = false;
                    dialog.dismiss();

                } else if (fileItems[which].equals(fileItems[3])) {
                    dialog.dismiss();

                }
            }
        });
        final AlertDialog builderLocationDialog = builderLocationStart.create();
        builderLocationDialog.show();

    }

    private boolean checkLocationService() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //All location services are disabled
            return false;
        } else {
            return true;
        }
    }

    public static boolean isControlLocationMap() {
        return controlLocationMap;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myRef != null && myRefListener != null) {
            myRef.removeEventListener(myRefListener);
        }

    }

    private boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(getContext(), getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void textToSpeech() {
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                int check = 0;
                Locale locale;
                if (i == TextToSpeech.SUCCESS) {

                    if (Locale.getDefault().getDisplayLanguage().equals("en")) {
                        locale = new Locale("en");
                        check = textToSpeech.setLanguage(locale);

                    } else {
                        locale = new Locale("tr", "TR");
                        check = textToSpeech.setLanguage(locale);
                    }
                    //textToSpeech.setLanguage(Locale.ENGLISH);

                    if (check == TextToSpeech.LANG_MISSING_DATA || check == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getContext(), getString(R.string.text_to_speech_error), Toast.LENGTH_SHORT).show();
                    }


                    if (check == TextToSpeech.ERROR) {
                        Toast.makeText(getContext(), getString(R.string.text_to_speech_error), Toast.LENGTH_SHORT).show();
                    } else if (check == TextToSpeech.LANG_MISSING_DATA || check == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getContext(), getString(R.string.text_to_speech_error), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!editText_message.getText().toString().equals("")) {
                            check = textToSpeech.speak(editText_message.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.text_to_speech_empty), Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            }


        });
    }


    protected synchronized void runInBackground(final Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }

    protected void processImage() {
        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        if (classifier != null) {
//                            final long startTime = SystemClock.uptimeMillis();
                            resultsClassification =
                                    classifier.recognizeImage(imageClassificationBitmap, 0);

//                            Toast.makeText(getContext(), "Image Classified As:" + resultsClassification.get(0).getTitle() + "with:" + resultsClassification.get(0).getConfidence(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), results.get(1).getTitle() + ":" + results.get(1).getConfidence(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), results.get(2).getTitle() + ":" + results.get(2).getConfidence(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    public static boolean isControlInternetConnection() {
        return controlInternetConnection;
    }

    public static void setControlInternetConnection(boolean controlInternetConnection) {
        Fragment_Write_Encrypted_Message.controlInternetConnection = controlInternetConnection;
    }


}


class AsyncWriteMessageInternetConnection extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private String mUrl;
    private ProgressDialog progressdialog;


    AsyncWriteMessageInternetConnection(Context context, String url) {
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
        Looper.prepare();
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
                    isAvailable = true;
                    Fragment_Write_Encrypted_Message.setControlInternetConnection(isAvailable);
                } else {
                    new AsyncWriteMessageInternetConnection(mContext, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    Toast.makeText(mContext, mContext.getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                isAvailable = false;
                Fragment_Write_Encrypted_Message.setControlInternetConnection(false);
                Toast.makeText(mContext, mContext.getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                new AsyncWriteMessageInternetConnection(mContext, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                e.printStackTrace();
            }
            Looper.loop();
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
}





