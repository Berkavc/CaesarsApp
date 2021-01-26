package com.example.sezar.activity;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sezar.R;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.example.sezar.encryption_algorithms.RandomStringGenerator;
import com.example.sezar.encryption_algorithms.RandomStringGeneratorVerification;
import com.example.sezar.utility.FingerprintHandler;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.security.auth.callback.Callback;


import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;


@RequiresApi(api = Build.VERSION_CODES.M)
public class CreateAccount extends AppCompatActivity {
   private EditText editText_create_account_user_name, editText_create_account_password, editText_create_account_email;
   private Button button_create_account;
   private ImageView imageView_finger_print;
   private boolean controlUserName, controlPassword, controlEmail;
    private  boolean controlUserNameLength, controlPasswordLength, controlEmailLength;
    private  boolean controlDuplicateName, controlDuplicateEmail;
    private boolean controlDuplicatePhoneNumber;
    private  boolean controlPhone, controlPhoneLength, controlPhoneFormat;
    private   boolean controlPermission;
    private  boolean controlEmailFormat;
    private  boolean controlDuplicateDeviceId;
    private static String userName;
    private static String password;
    private static String email;
    private static String accountStatus = "false";
    private String verificationCode;
    private  static DatabaseReference myRef;
    private  DatabaseReference myKey;
    private   static FirebaseDatabase database;
    private String key;
    private  static String keySecret;
    private  List<String> userNames;
    private  List<String> userEmails;
    private  List<String> userPhoneNumber;
    private  List<String> userKey;
    private  List<String> userDeviceId;
    private  static String getUserPhoneNumber;
    private String verificationPhoneNumber;
    private  String getUserPhoneNumberManual;
    private  int arrayListCounter;
    private  int permissionCheck;
    private static final int PERMISSION_SEND_SMS = 123;
    private static final int PERMISSION_READ_CONTACT = 123;
    private String KEY_NAME;
    private EditText editText_create_account_phone_number;
    private static String DeviceId;
    private  String SENT = "SMS_SENT";
    final private int REQUEST_SEND_SMS = 0;
    final private int REQUEST_REC_SMS = 1;
    IntentFilter intentFilter;
    private int permissionCheck_sms;
    private ChildEventListener myRefListener;
    private static boolean controlInternetConnection;
    private AsyncTask asyncCreateAccountInternetConnectionTask;


    String DELIVERED = "SMS_DELIVERED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
      /*  if (AppCompatDelegate.getDefaultNightMode()
                ==AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.FeedActivityThemeDark);
        }*/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        editText_create_account_user_name = findViewById(R.id.editText_create_account_name);
        editText_create_account_password = findViewById(R.id.editText__create_account_password);
        editText_create_account_email = findViewById(R.id.editText_create_account_email);
        editText_create_account_phone_number = findViewById(R.id.editText_create_phone);
        button_create_account = findViewById(R.id.button_create_account);
        imageView_finger_print = findViewById(R.id.imageView_finger_print);


        checkForSmsPermission();

        if (!getUserPhoneNumber.equals("")) {
            editText_create_account_phone_number.setVisibility(View.GONE);
        }


        controlUserName = false;
        controlPassword = false;
        controlEmail = false;
        controlUserNameLength = false;
        controlPasswordLength = false;
        controlEmailLength = false;
        controlDuplicateName = false;
        controlDuplicateEmail = false;
        controlEmailFormat = false;
        controlDuplicatePhoneNumber = false;
        controlPhone = false;
        controlPhoneLength = false;
        controlPhoneFormat = false;
        controlDuplicateDeviceId = false;
        controlPermission = false;
        controlInternetConnection=false;

        arrayListCounter = 0;


        checkDatabase();
        asyncCreateAccountInternetConnectionTask = new AsyncCreateAccountInternetConnection(CreateAccount.this, database.getReference().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        RandomStringGenerator rsg = new RandomStringGenerator();
        KEY_NAME = rsg.randomStringGenerator();

        setHideSoftKeyboard(editText_create_account_user_name);
        setHideSoftKeyboard(editText_create_account_password);
        setHideSoftKeyboard(editText_create_account_email);


        userNames = new ArrayList<>();
        userEmails = new ArrayList<>();
        userPhoneNumber = new ArrayList<>();
        userKey = new ArrayList<>();
        userDeviceId = new ArrayList<>();

        //permissionCheck_sms = ContextCompat.checkSelfPermission(CreateAccount.this, Manifest.permission.SEND_SMS);

        checkVerificationSms();
        button_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlPermission) {
                    if (checkNetworkAccess()&&controlInternetConnection) {
                        arrayListCounter = 0;
                        checkUserName();
                        checkPassword();
                        checkEmail();
                        if (getUserPhoneNumber.equals("")) {
                            checkUserPhoneNumberManually();
                        } else {
                            preventDuplicatePhoneNumber();
                        }
                        preventDuplicateDeviceId();
                        checkEmpty();
                    }
                } else {
                    checkForSmsPermission();
                    Toast.makeText(CreateAccount.this, getString(R.string.create_account_permission), Toast.LENGTH_SHORT).show();

                }
            }

        });


    }


    private void checkUserName() {
        userName = editText_create_account_user_name.getText().toString().trim();
        if (userName.length() == 0) {
            controlUserNameLength = false;
            controlUserName = false;
        } else {
            controlUserNameLength = true;
            if(userNames!=null){
                preventDuplicateName();
            }else{
                controlUserName=false;
                Toast.makeText(CreateAccount.this, getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkPassword() {
        password = editText_create_account_password.getText().toString().trim();
        if (password.length() == 0) {
            controlPassword = false;
            controlPasswordLength = false;
        } else {
            controlPassword = true;
            controlPasswordLength = true;
        }
    }

    private void checkEmail() {
        email = editText_create_account_email.getText().toString().trim();
        if (email.length() == 0) {
            controlEmail = false;
            controlEmailLength = false;
        } else {
            controlEmailLength = true;
            if (isEmailValid(email)) {
                controlEmailFormat = true;
                if(userEmails!=null){
                    preventDuplicateEmail();
                }else{
                    controlEmail=false;
                    Toast.makeText(CreateAccount.this, getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                }

            } else {
                controlEmailFormat = false;
                controlEmail = false;
            }

        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    private void checkUserPhoneNumberManually() {
        getUserPhoneNumberManual = editText_create_account_phone_number.getText().toString().trim();
        if (getUserPhoneNumberManual.length() == 0) {
            controlPhone = false;
            controlPhoneLength = false;
        } else {
            controlPhoneLength = true;
            if (validCellPhone(getUserPhoneNumberManual)) {
                controlPhoneFormat = true;
                if(userPhoneNumber!=null){
                    preventDuplicatePhoneNumber();
                }else{
                    Toast.makeText(CreateAccount.this, getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                }

            } else {
                controlPhoneFormat = false;
                controlPhone = false;
            }
        }
    }

    private void checkEmpty() {
        if (controlUserName && controlPassword && controlEmail && controlPhone && controlDuplicateDeviceId) {

            verificationPhoneNumber = getUserPhoneNumber;
            RandomStringGenerator rsg = new RandomStringGenerator();
            keySecret = rsg.randomStringGenerator();
            userName = AES.encrypt(userName, keySecret);
            password = AES.encrypt(password, keySecret);
            getUserPhoneNumber = AES.encrypt(getUserPhoneNumber, keySecret);
            email = AES.encrypt(email, keySecret);
            DeviceId = AES.encrypt(DeviceId, keySecret);

          /*  while ((userName.contains("/") && userName.contains("n")) || (password.contains("/") && password.contains("n")) || (email.contains("/") && email.contains("n")) || (getUserPhoneNumber.contains("/") && getUserPhoneNumber.contains("n")) || (DeviceId.contains("/") && DeviceId.contains("n"))) {
                if (userName.contains("/") && userName.contains("n")) {
                    userName = AES.encrypt(userName, keySecret);
                }
                if (password.contains("/") && password.contains("n")) {
                    password = AES.encrypt(password, keySecret);
                }
                if (email.contains("/") && email.contains("n")) {
                    email = AES.encrypt(email, keySecret);
                }
                if (getUserPhoneNumber.contains("/") && getUserPhoneNumber.contains("n")) {
                    getUserPhoneNumber = AES.encrypt(getUserPhoneNumber, keySecret);
                }
                if (DeviceId.contains("/") && DeviceId.contains("n")) {
                    DeviceId = AES.encrypt(DeviceId, keySecret);
                }

            }*/

            RandomStringGeneratorVerification rsgMessage = new RandomStringGeneratorVerification();
            verificationCode = rsgMessage.randomStringGenerator();


            SmsManager smsManager = SmsManager.getDefault();

            PendingIntent sentPI = PendingIntent.getBroadcast(CreateAccount.this, 0, new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(CreateAccount.this, 0, new Intent(DELIVERED), 0);

            smsManager.sendTextMessage(verificationPhoneNumber, null, verificationCode, sentPI, deliveredPI);

            Intent intent = new Intent(CreateAccount.this, AccountVerification.class);
            intent.putExtra("Verification Code", verificationCode);
            startActivity(intent);

            //Toast.makeText(CreateAccount.this, getString(R.string.create_account_successfull), Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(CreateAccount.this, AccountVerification.class));

        } else {
            if (!controlUserNameLength) {
                Toast.makeText(CreateAccount.this, getString(R.string.create_account_user_name_empty), Toast.LENGTH_SHORT).show();
            } else if (!controlPasswordLength) {
                Toast.makeText(CreateAccount.this, getString(R.string.create_account_password_empty), Toast.LENGTH_SHORT).show();
            } else if (!controlEmailLength) {
                Toast.makeText(CreateAccount.this, getString(R.string.create_account_email_empty), Toast.LENGTH_SHORT).show();

            } else if (!controlPhoneLength) {
                Toast.makeText(CreateAccount.this, getString(R.string.create_account_phone_empty), Toast.LENGTH_SHORT).show();
            } else if (!controlUserName) {
                if (!controlDuplicateName) {
                    Toast.makeText(CreateAccount.this, getString(R.string.create_account_user_name_duplication), Toast.LENGTH_SHORT).show();
                } else {
                }
            } else if (!controlEmail) {
                if (!controlEmailFormat) {
                    Toast.makeText(CreateAccount.this, getString(R.string.create_account_email_format), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccount.this, getString(R.string.create_account_email_duplication), Toast.LENGTH_SHORT).show();
                }
            } else if (!controlPhone) {
                if (!controlPhoneFormat) {
                    Toast.makeText(CreateAccount.this, getString(R.string.create_account_phone_format), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccount.this, getString(R.string.create_account_phone_duplication), Toast.LENGTH_SHORT).show();
                }

            } else if (!controlDuplicateDeviceId) {
                Toast.makeText(CreateAccount.this, getString(R.string.create_account_device_id_duplication), Toast.LENGTH_SHORT).show();
            } else if (!checkContact()) {
                Toast.makeText(CreateAccount.this, getString(R.string.create_account_phone_contact), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void preventDuplicateName() {
        arrayListCounter = 0;
        Iterator<String> checkUser = userNames.iterator();
        while (checkUser.hasNext()) {
            if ((editText_create_account_user_name.getText().toString().equals(userNames.get(arrayListCounter)))) {
                controlUserName = false;
                controlDuplicateName = false;
                break;
            } else {
                controlDuplicateName = true;
                controlUserName = true;
            }
            arrayListCounter++;
            if (userNames.size() == arrayListCounter) {
                break;
            }
        }
        if (!checkUser.hasNext()) {
            controlDuplicateName = true;
            controlUserName = true;
        }
    }

    private void preventDuplicateEmail() {
        arrayListCounter = 0;
        Iterator<String> checkUser = userEmails.iterator();
        while (checkUser.hasNext()) {
            if ((editText_create_account_email.getText().toString().equals(userEmails.get(arrayListCounter)))) {
                controlDuplicateEmail = false;
                controlEmail = false;
                break;
            } else {
                controlDuplicateEmail = true;
                controlEmail = true;
            }
            arrayListCounter++;
            if (userEmails.size() == arrayListCounter) {
                break;
            }
        }
        if (!checkUser.hasNext()) {
            controlDuplicateEmail = true;
            controlEmail = true;
        }
    }

    private void preventDuplicatePhoneNumber() {
        checkForSmsPermission();
        arrayListCounter = 0;
        if (getUserPhoneNumber.equals("") || getUserPhoneNumber == null) {
            getUserPhoneNumber = getUserPhoneNumberManual;
        }

        Iterator<String> checkUser = userPhoneNumber.iterator();
        if (userPhoneNumber.isEmpty()) {
            controlDuplicatePhoneNumber = true;
            controlPhone = true;
        } else {
            while (checkUser.hasNext()) {

                if (getUserPhoneNumber.equals(userPhoneNumber.get(arrayListCounter))) {
                    controlDuplicatePhoneNumber = false;
                    controlPhone = false;
                    break;
                } else {
                    controlPhone = true;
                    controlDuplicatePhoneNumber = true;
                }
                arrayListCounter++;
                if (userPhoneNumber.size() == arrayListCounter) {
                    break;
                }
            }
        }


    }

    private void preventDuplicateDeviceId() {
        checkForSmsPermission();
        arrayListCounter = 0;
        if (DeviceId == null) {
            DeviceId = "";
        }
        if (DeviceId != null) {
            Iterator<String> checkUser = userDeviceId.iterator();
            if (userDeviceId.isEmpty()) {
                controlDuplicateDeviceId = true;
            } else {
                while (checkUser.hasNext()) {
                    if (DeviceId.equals(userDeviceId.get(arrayListCounter))) {
                        controlDuplicateDeviceId = false;
                        break;
                    } else {
                        controlDuplicateDeviceId = true;
                    }
                    arrayListCounter++;
                    if (userDeviceId.size() == arrayListCounter) {
                        break;
                    }
                }
            }
        }
    }

    private void setHideSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(CreateAccount.this, getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void checkDatabase() {
        database = FirebaseDatabase.getInstance();
        myKey = database.getReference("keys:");
        key = myKey.getKey();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String username = postSnapshot.child("Username").getValue(String.class);
                    String getKey = postSnapshot.child("Key").getValue(String.class);
                    String email = postSnapshot.child("Email").getValue(String.class);
                    String phone = postSnapshot.child("Phone").getValue(String.class);
                    String deviceId = postSnapshot.child("Device Id").getValue(String.class);

                    if (getKey != null) {
                        getKey = CaesarsCiphering.caesarsDecryption(getKey, 13);
                    }
                    if (username != null && getKey != null) {
                        username = AES.decrypt(username, getKey);
                    }

                    if (email != null && getKey != null) {
                        email = AES.decrypt(email, getKey);
                    }

                    if (phone != null && getKey != null) {
                        phone = AES.decrypt(phone, getKey);
                    }

                    if (deviceId != null && getKey != null) {
                        deviceId = AES.decrypt(deviceId, getKey);
                    }

                    String[] stringArrayUserName = {username};
                    String[] stringArrayUserEmail = {email};
                    String[] stringArrayUserPhone = {phone};
                    String[] stringArrayUserKey = {getKey};
                    String[] stringArrayUserDeviceId = {deviceId};

                    userNames.addAll(Arrays.asList(stringArrayUserName));
                    userEmails.addAll(Arrays.asList(stringArrayUserEmail));
                    userPhoneNumber.addAll(Arrays.asList(stringArrayUserPhone));
                    userKey.addAll(Arrays.asList(stringArrayUserKey));
                    userDeviceId.addAll(Arrays.asList(stringArrayUserDeviceId));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        myRefListener=myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*if (!controlArrayListClear) {
                    //checkDatabase();
                    myRef.removeEventListener(this);
                    startActivity(new Intent(LoginScreen.this, LoginScreen.class));
                }*/

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                userNames.clear();
                userEmails.clear();
                userPhoneNumber.clear();
                userKey.clear();
                userDeviceId.clear();
                checkDatabase();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkForSmsPermission() {
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            controlPermission = true;
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (getUserPhoneNumber == null) {
                getUserPhoneNumber = telephonyManager.getLine1Number();

            }
            DeviceId = telephonyManager.getSimSerialNumber();
            /*DeviceId= Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);*/
            controlPhoneLength = true;
            controlPhoneFormat = true;
            return true;
        } else {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_SEND_SMS);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_READ_CONTACT);

            permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);
            //startActivity(new Intent(CreateAccount.this,CreateAccount.class));
            return false;
        }


    }

    private boolean checkContact() {
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            getUserPhoneNumber = telephonyManager.getLine1Number();
            DeviceId = telephonyManager.getSimOperator();
           /* DeviceId= Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);*/
            return true;
        }
        return false;
    }

    protected static void insertDataBase() {
        keySecret = CaesarsCiphering.caesarsEncryption(keySecret, 13);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().push();
        myRef.child("Username").setValue(userName);
        myRef.child("Password").setValue(password);
        myRef.child("Email").setValue(email);
        myRef.child("Phone").setValue(getUserPhoneNumber);
        myRef.child("Key").setValue(keySecret);
        myRef.child("Device Id").setValue(DeviceId);
        myRef.child("Account Status").setValue(accountStatus);
    }

    private void checkVerificationSms() {
        if (ContextCompat.checkSelfPermission(CreateAccount.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
        }

        if (ContextCompat.checkSelfPermission(CreateAccount.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_REC_SMS);
        }
        if (ContextCompat.checkSelfPermission(CreateAccount.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(CreateAccount.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

        } else {
            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");
            controlPermission = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myRef.removeEventListener(myRefListener);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateAccount.this, LoginScreen.class));
    }

    public static boolean isControlInternetConnection() {
        return controlInternetConnection;
    }

    public static void setControlInternetConnection(boolean controlInternetConnection) {
        CreateAccount.controlInternetConnection = controlInternetConnection;
    }
}


class AsyncCreateAccountInternetConnection extends AsyncTask<Void, Void, Boolean> {
    private Activity mActivity;
    private String mUrl;
    private ProgressDialog progressdialog;


    AsyncCreateAccountInternetConnection(Activity activity, String url) {
        super();
        mActivity = activity;
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
                // 1 second time out.
                httpURLConnection.setConnectTimeout(1000);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    isAvailable = true;
                    CreateAccount.setControlInternetConnection(isAvailable);
                    //LoginScreen.setControlInternetConnectionTask(isAvailable);
                } else {
                    new AsyncCreateAccountInternetConnection(mActivity, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    CreateAccount.setControlInternetConnection(false);
                    Toast.makeText(mActivity.getBaseContext(), mActivity.getBaseContext().getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                    //LoginScreen.setControlInternetConnectionTask(true);
                }
            } catch (Exception e) {
                isAvailable = false;
               // LoginScreen.setControlInternetConnectionTask(true);
                CreateAccount.setControlInternetConnection(false);
                Toast.makeText(mActivity.getBaseContext(), mActivity.getBaseContext().getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                new AsyncCreateAccountInternetConnection(mActivity, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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







