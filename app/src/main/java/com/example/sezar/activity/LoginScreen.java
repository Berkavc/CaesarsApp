package com.example.sezar.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.sezar.R;
import com.example.sezar.database.DBLoginScreenCheckBoxHelper;

import com.example.sezar.database.DBLoginScreenHelper;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.example.sezar.encryption_algorithms.RandomStringGenerator;
import com.example.sezar.model.LoginScreenCheckBoxItem;
import com.example.sezar.model.LoginScreenItem;
import com.example.sezar.utility.SmsBackgroundTask;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.Build;
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

import android.widget.TextView;

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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;


@RequiresApi(api = Build.VERSION_CODES.M)
public class LoginScreen extends AppCompatActivity {
    private EditText editText_login_screen_user_name, editText_login_screen_password;
    private Button button_login_screen_login, button_login_screen_create_an_account, button_login_screen_forgot_password;
    private boolean controlUserName, controlPassword;
    private boolean controlUserNameLength, controlPasswordLength;
    private boolean controlCheckBox;
    private boolean controlPermission;
    private static boolean controlPhone;
    private boolean controlArrayListCounter;
    private String userName, password;
    private static String getUserName;
    private String readAccountState = "true";
    private boolean controlArrayListClear;
    private CheckBox checkBox_login_screen_reminder;
    private Cursor cursor;
    private DBLoginScreenCheckBoxHelper myDb;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private List<String> userNames;
    private List<String> userPasswords;
    private List<String> keysList;
    private List<String> decryptKey;
    private List<String> phoneNumberList;
    private List<String> deviceList;
    private List<String> accountList;
    private List<String> emailList;
    private String key;
    private static String userNameKey;
    private String passwordKey;
    private int arrayListCounter;
    private int accountStatusCounter;
    private int arrayListUserNameCounter;
    private int arrayListPasswordCounter;
    private DatabaseReference myKey;
    private ImageView imageView_finger_print;
    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private static BiometricPrompt.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private boolean controlDatabase;
    private BiometricPrompt biometricPrompt;
    private int permissionCheck;
    private int permissionContact, permissionReadPhoneState;
    private String getUserPhoneNumber;
    private static BiometricPrompt.AuthenticationResult getResult;
    private static final int PERMISSION_SEND_SMS = 123;
    private static final int PERMISSION_READ_PHONE_STATE = 124;
    private static final int PERMISSION_READ_CONTACT = 125;
    private String userPhoneNumber;
    private TelephonyManager telephonyManager;
    private static boolean fingerPrintSuccess = false, fingerPrintFail = false, fingerPrintError = false;
    private boolean controlAccountStatus;
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static String progressTitle;
    private static String differentAccessMessage;
    private boolean refresh;
    private ChildEventListener myRefListener;
    private Cursor cursorLoginScreen;
    private DBLoginScreenHelper myDbLoginScreenHelper;
    private static boolean controlInternetConnection;
    private static boolean controlInternetConnectionTask;
    private AsyncTask asyncLoginScreenInternetConnectionTask;
    private String keySecret;
    private static boolean controlMessageService;
    private Intent backgroundIntent;
    private boolean controlFirebaseDatabase;
//    private ProgressBar progressDialog;
//    private View dialogView;
//    private AlertDialog alertDialogBuilder;


    //Fragment_Read_Encrypted_Message fragment_read_encrypted_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        editText_login_screen_user_name = (EditText) findViewById(R.id.editText_login_screen_username);
        editText_login_screen_password = (EditText) findViewById(R.id.editText_login_screen_password);

        button_login_screen_login = (Button) findViewById(R.id.button_login_screen_login);
        button_login_screen_create_an_account = (Button) findViewById(R.id.button_login_screen_create_an_account);
        button_login_screen_forgot_password = (Button) findViewById(R.id.button_login_screen_forgot_password);

        progressTitle = getString(R.string.forgot_password_wait);
        differentAccessMessage = getString(R.string.login_screen_accessing_from_different_device);

        checkBox_login_screen_reminder = (CheckBox) findViewById(R.id.checkBox_login_screen_reminder);
        //  dialogView= LayoutInflater.from(this).inflate(R.layout.login_screen_dialog_item,null,false);

//        progressDialog=new ProgressBar(this);
//        progressDialog.setTitle(getProgressTitle());
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);

        imageView_finger_print = findViewById(R.id.imageView_finger_print);


        imageView_finger_print.setVisibility(View.INVISIBLE);


        controlUserName = false;
        controlPassword = false;
        controlUserNameLength = false;
        controlPasswordLength = false;
        controlCheckBox = true;
        controlPhone = false;
        controlPermission = false;
        controlAccountStatus = false;
        controlArrayListCounter = false;
        controlArrayListClear = false;
        controlDatabase = false;
        controlInternetConnection = false;
        controlInternetConnectionTask = false;
        refresh = false;
        controlMessageService = false;
        controlFirebaseDatabase=false;


        myDb = new DBLoginScreenCheckBoxHelper(this);
        myDbLoginScreenHelper = new DBLoginScreenHelper(this);

        checkBox_login_screen_reminder.performClick();

        userNames = new ArrayList<>();
        userPasswords = new ArrayList<>();
        keysList = new ArrayList<>();
        decryptKey = new ArrayList<>();
        phoneNumberList = new ArrayList<>();
        deviceList = new ArrayList<>();
        accountList = new ArrayList<>();
        emailList = new ArrayList<>();
        backgroundIntent = new Intent(this, SmsBackgroundTask.class);


        checkDatabase();
        checkBox();
        checkContact();
        asyncLoginScreenInternetConnectionTask = new AsyncLoginScreenInternetConnection(LoginScreen.this, database.getReference().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        arrayListUserNameCounter = 0;
        arrayListPasswordCounter = 0;
        checkBox_login_screen_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox_login_screen_reminder.isChecked()) {
                    controlCheckBox = true;
                } else {
                    controlCheckBox = false;
                }
            }
        });

        showFingerPrint();

        imageView_finger_print.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                checkContact();
                fingerPrintSuccess = false;
                fingerPrintError = false;
                fingerPrintFail = false;
                fingerPrintCheck();

                try {

                    generateKey();
                    initCipher();
                    if (initCipher()) {

                        BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
                        Executor executor = Executors.newSingleThreadExecutor();

                        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {  // 1
                            @Override
                            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {  // 2
                                if (errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                                    biometricPrompt.cancelAuthentication();  // 3
                                fingerPrintError = true;
                                new AsyncFingerPrint(LoginScreen.this, LoginScreen.this).execute();


                            }

                            @Override
                            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) { // 4
                                fingerPrintSuccess = true;
                                checkPhoneNumber();

                                cryptoObject = result.getCryptoObject();

                            }

                            @Override
                            public void onAuthenticationFailed() {
                                fingerPrintFail = true;
                                new AsyncFingerPrint(LoginScreen.this, LoginScreen.this).execute();

                            }
                        };

                        if (biometricPrompt == null) {
                            biometricPrompt = new BiometricPrompt(LoginScreen.this, executor, callback);  // 7
                        }


                        biometricPrompt = getBiometricPrompt();
                        biometricPrompt.authenticate(promptInfo);
                    }

                } catch (FingerprintException e) {
                    e.printStackTrace();
                }
            }
        });

        button_login_screen_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlUserName = false;
                controlPassword = false;
                controlUserNameLength = false;
                controlPasswordLength = false;
                controlAccountStatus = false;
                if (checkContact()) {
                    if (checkNetworkAccess() && controlInternetConnection) {
                        checkUserName();
                        checkPassword();
                        checkEmpty();
                    } else {
                        insertLocalDatabase();
                        checkUserName();
                        checkPassword();
                        checkEmpty();
                    }
                } else {
                    checkContact();
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_permission_deny), Toast.LENGTH_LONG).show();
                }


            }
        });

        button_login_screen_create_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginScreen.this, CreateAccount.class));
            }
        });

        button_login_screen_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this, ForgotPassword.class));

            }
        });

        //startActivity(new Intent(LoginScreen.this,MainActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_permission_access), Toast.LENGTH_SHORT).show();
                    // finish();
                    //startActivity(getIntent());
                } else {
                    //Toast.makeText(LoginScreen.this, "The app was not allowed to send SMS. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_permission_access), Toast.LENGTH_SHORT).show();
                    //finish();
                    //startActivity(getIntent());
                } else {
                    //Toast.makeText(LoginScreen.this, "The app was not allowed to read your phone state. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case PERMISSION_READ_CONTACT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_permission_access), Toast.LENGTH_SHORT).show();
                    //finish();
                    // startActivity(getIntent());
                } else {
                    // Toast.makeText(LoginScreen.this, "The app was not allowed to read your contacts. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }

    }

    private void checkUserName() {
        editText_login_screen_user_name.setText(editText_login_screen_user_name.getText().toString().trim());
        userName = editText_login_screen_user_name.getText().toString();
        arrayListCounter = 0;
        if (userName.length() == 0) {
            controlUserNameLength = false;
            controlUserName = false;
        } else {
            controlUserNameLength = true;
            if (userNames.size() != 0) {
                Iterator<String> checkUser = userNames.iterator();
                while (checkUser.hasNext()) {
                    if (userName.equals(userNames.get(arrayListCounter))) {
                        controlUserName = true;
                        controlUserNameLength = true;
                        if (keysList != null) {
                            if (keysList.size() > arrayListCounter) {
                                userNameKey = keysList.get(arrayListCounter);
                            }

                        }
                        break;
                    }
                    arrayListCounter++;
                    arrayListUserNameCounter++;
                    if (userNames.size() == arrayListUserNameCounter) {
                        break;
                    }
                }
            } else {
                controlUserName = false;
                Toast.makeText(LoginScreen.this, getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void checkPassword() {
        editText_login_screen_password.setText(editText_login_screen_password.getText().toString().trim());
        password = editText_login_screen_password.getText().toString();
        arrayListCounter = 0;
        if (password.length() == 0) {
            controlPassword = false;
            controlPasswordLength = false;
        } else {
            controlPasswordLength = true;
            if (userPasswords.size() != 0) {
                Iterator<String> checkPassword = userPasswords.iterator();
                while (checkPassword.hasNext()) {
                    if (password.equals(userPasswords.get(arrayListCounter))) {
                        if (keysList.size() > arrayListCounter) {
                            passwordKey = keysList.get(arrayListCounter);
                        }
                        if (passwordKey != null) {
                            if (passwordKey.equals(userNameKey)) {
                                controlPassword = true;
                                controlPasswordLength = true;
                                accountStatusCounter = arrayListCounter;
                                break;
                            }
                        } else {
                            controlPassword = true;
                            controlPasswordLength = true;
                            accountStatusCounter = arrayListCounter;
                        }

                    }
                    arrayListCounter++;
                    arrayListPasswordCounter++;
                    if (userPasswords.size() == arrayListPasswordCounter) {
                        break;
                    }
                }
            } else {
                controlPassword = false;
                Toast.makeText(LoginScreen.this, getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
            }


        }
    }


    private void checkAccountStatus() {
        controlAccountStatus = true;
        controlArrayListClear = true;
        checkDatabaseState();
    }

    @SuppressLint("MissingPermission")
    private void checkPhoneNumber() {
        Iterator<String> checkUser;
        controlPhone = false;
        arrayListCounter = 0;
        checkAccountStatus();
        if (telephonyManager.getLine1Number().equals("")) {
            checkUser = deviceList.iterator();
            while (checkUser.hasNext()) {
                if (getUserPhoneNumber.equals(deviceList.get(arrayListCounter))) {
                    controlPhone = true;
                    getUserName = userNames.get(arrayListCounter);
                    userNameKey = keysList.get(arrayListCounter);
                    if (controlCheckBox) {
                        RandomStringGenerator rsg = new RandomStringGenerator();
                        keySecret = rsg.randomStringGenerator();
                        LoginScreenCheckBoxItem dataBaseItem = new LoginScreenCheckBoxItem(AES.encrypt(getUserName, keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13));
                        cursor = myDb.getAllContacts();
                        while (cursor.moveToNext()) {
                            myDb.updateData(dataBaseItem, 0);
                        }
                        if (!cursor.moveToNext()) {
                            myDb.insertdata(dataBaseItem);
                        }
                        new AsyncFingerPrint(LoginScreen.this, LoginScreen.this).execute();
                        cursor.close();
                        myDb.close();
                        // Toast.makeText(LoginScreen.this, getString(R.string.login_screen_successfull), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginScreen.this, MainActivity.class));
                        break;

                    } else {
                        new AsyncFingerPrint(LoginScreen.this, LoginScreen.this).execute();
                        cursor.close();
                        myDb.close();
                        startActivity(new Intent(LoginScreen.this, MainActivity.class));
                        break;

                    }


                }
                arrayListCounter++;
                arrayListUserNameCounter++;
                if (deviceList.size() == arrayListUserNameCounter) {
                    break;
                }
            }


        } else {
            checkUser = phoneNumberList.iterator();
            while (checkUser.hasNext()) {

                if (getUserPhoneNumber.equals(phoneNumberList.get(arrayListCounter))) {
                    controlPhone = true;
                    getUserName = userNames.get(arrayListCounter);
                    userNameKey = keysList.get(arrayListCounter);
                    if (controlCheckBox) {
                        LoginScreenCheckBoxItem dataBaseItem = new LoginScreenCheckBoxItem(AES.encrypt(getUserName, keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13));
                        cursor = myDb.getAllContacts();
                        while (cursor.moveToNext()) {
                            myDb.updateData(dataBaseItem, 0);
                        }
                        if (!cursor.moveToNext()) {
                            myDb.insertdata(dataBaseItem);
                        }
                        new AsyncFingerPrint(LoginScreen.this, LoginScreen.this).execute();
                        // Toast.makeText(LoginScreen.this, getString(R.string.login_screen_successfull), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginScreen.this, MainActivity.class));
                        break;

                    } else {
                        new AsyncFingerPrint(LoginScreen.this, LoginScreen.this).execute();
                        startActivity(new Intent(LoginScreen.this, MainActivity.class));
                        break;

                    }


                }
                arrayListCounter++;
                arrayListUserNameCounter++;
                if (phoneNumberList.size() == arrayListUserNameCounter) {
                    break;
                }
            }
        }

        if (!controlPhone) {
            new AsyncFingerPrint(LoginScreen.this, LoginScreen.this).execute();
        }


    }

    public boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            //asyncLoginScreenInternetConnectionTask = new AsyncLoginScreenInternetConnection(LoginScreen.this, database.getReference().toString()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
//            while (!controlInternetConnection ||!controlInternetConnectionTask ){
//                if(LoginScreen.isControlInternetConnection()){
//                    return true;
//                }
//                if(LoginScreen.isControlInternetConnectionTask()){
//                   return false;
//                }
//
//            }
            return true;
        } else {
            // Toast.makeText(LoginScreen.this, getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

//    public static boolean isServerConnected(String myUrl) {
//        boolean isAvailable = false;
//        try {
//            URL url = new URL(myUrl);
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            // 10 second time out.
//            httpURLConnection.setConnectTimeout(10000);
//            httpURLConnection.connect();
//            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                isAvailable = true;
//            }
//        } catch (Exception e) {
//            isAvailable = false;
//            e.printStackTrace();
//        }
//        return isAvailable;
//    }


    private void checkEmpty() {

        if (arrayListUserNameCounter == arrayListPasswordCounter) {
            controlArrayListCounter = true;
            arrayListUserNameCounter = 0;
            arrayListPasswordCounter = 0;
        } else {
            controlArrayListCounter = false;
            arrayListUserNameCounter = 0;
            arrayListPasswordCounter = 0;
        }
        if (controlUserName && controlPassword) {
            checkAccountStatus();
        }
        if (accountList.size() > accountStatusCounter) {
            if ((controlUserName && controlPassword && accountList.get(accountStatusCounter).equals("false")) || (controlUserName && controlPassword && (((phoneNumberList.get(accountStatusCounter).equals(getUserPhoneNumber) || deviceList.get(accountStatusCounter).equals(getUserPhoneNumber)))))) {
                //MainActivity.setControlDeviceAccess(false);
                getUserName = userName;
                // controlArrayListClear = true;
                if (controlCheckBox) {
                    LoginScreenCheckBoxItem dataBaseItem = new LoginScreenCheckBoxItem(AES.encrypt(userName, keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13));
                    cursor = myDb.getAllContacts();
                    while (cursor.moveToNext()) {
                        myDb.updateData(dataBaseItem, 0);
                        myDb.close();
                    }
                    if (!cursor.moveToNext()) {
                        myDb.insertdata(dataBaseItem);
                        myDb.close();
                    }
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_successfull), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginScreen.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_successfull), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginScreen.this, MainActivity.class));
                }

                checkPhoneNumberAuthentication();
            } else if ((!checkNetworkAccess() || !controlInternetConnection) && controlUserName && controlPassword) {
                getUserName = userName;
                // controlArrayListClear = true;
                if (controlCheckBox) {
                    LoginScreenCheckBoxItem dataBaseItem = new LoginScreenCheckBoxItem(AES.encrypt(userName, keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13));
                    cursor = myDb.getAllContacts();
                    while (cursor.moveToNext()) {
                        myDb.updateData(dataBaseItem, 0);
                    }
                    if (!cursor.moveToNext()) {
                        myDb.insertdata(dataBaseItem);
                    }
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_successfull), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginScreen.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_successfull), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginScreen.this, MainActivity.class));
                }

                //checkPhoneNumberAuthentication();

            } else if (!controlUserName) {
                //  asyncLoginScreenInternetConnectionTask.cancel(true);
                if (!controlUserNameLength) {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_user_name_empty), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_invalid_credentials), Toast.LENGTH_SHORT).show();
                }

            } else if (!controlPassword) {
                //  asyncLoginScreenInternetConnectionTask.cancel(true);
                if (!controlPasswordLength) {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_user_password_empty), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginScreen.this, getString(R.string.login_screen_invalid_credentials), Toast.LENGTH_SHORT).show();
                }
            } else if (!controlInternetConnection) {
                Toast.makeText(this, getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(LoginScreen.this, getString(R.string.login_screen_account_status_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkBox() {
        cursor = myDb.getAllContacts();
        while (cursor.moveToNext()) {
            editText_login_screen_user_name.setText(AES.decrypt(cursor.getString(1), CaesarsCiphering.caesarsDecryption(cursor.getString(2), 13)));
        }
    }

    protected void checkDatabase() {
        database = FirebaseDatabase.getInstance();
        myKey = database.getReference("keys:");
        key = myKey.getKey();
        controlDatabase = false;

        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String username = postSnapshot.child("Username").getValue(String.class);
                    String password = postSnapshot.child("Password").getValue(String.class);
                    String phoneNumber = postSnapshot.child("Phone").getValue(String.class);
                    String email = postSnapshot.child("Email").getValue(String.class);
                    String deviceId = postSnapshot.child("Device Id").getValue(String.class);
                    String getKey = postSnapshot.child("Key").getValue(String.class);
                    String accountStatus = postSnapshot.child("Account Status").getValue(String.class);
                    String keys = postSnapshot.getKey();
                    /*if(MainActivity.isControlDeviceAccess()&&keysList.get(accountStatusCounter).equals(keys) ){

                    }*/

                    if (getKey != null) {
                        getKey = CaesarsCiphering.caesarsDecryption(getKey, 13);
                    }

                    if (username != null && getKey != null) {
                        username = AES.decrypt(username, getKey);
                    }
                    if (password != null && getKey != null) {
                        password = AES.decrypt(password, getKey);
                    }

                    if (phoneNumber != null && getKey != null) {
                        phoneNumber = AES.decrypt(phoneNumber, getKey);
                    }
                    if (deviceId != null && getKey != null) {
                        deviceId = AES.decrypt(deviceId, getKey);
                    }


                    if (email != null && getKey != null) {
                        email = AES.decrypt(email, getKey);
                    }

                    if (userNames.contains(username)) {
                        break;
                    }
                    if (keysList.size() > accountStatusCounter) {
                        if (controlAccountStatus && keysList.get(accountStatusCounter).equals(keys) && !MainActivity.isControlDeviceAccess()) {
                            myRef.child(keys).child("Account Status").setValue(readAccountState);
                            accountList.remove(accountStatusCounter);
                            accountList.add(accountStatusCounter, readAccountState);
                            controlDatabase = true;
                            break;
                        }
                    }


                    String[] stringArrayUserName = {username};
                    String[] stringArrayUserPassword = {password};
                    String[] stringArrayKey = {keys};
                    String[] stringArrayPhoneNumber = {phoneNumber};
                    String[] stringArrayDeviceId = {deviceId};
                    String[] stringArraygetKey = {getKey};
                    String[] stringArrayAccountStatus = {accountStatus};
                    String[] stringArrayEmail = {email};


                    userNames.addAll(Arrays.asList(stringArrayUserName));
                    userPasswords.addAll(Arrays.asList(stringArrayUserPassword));
                    keysList.addAll(Arrays.asList(stringArrayKey));
                    decryptKey.addAll(Arrays.asList(stringArraygetKey));
                    phoneNumberList.addAll(Arrays.asList(stringArrayPhoneNumber));
                    deviceList.addAll(Arrays.asList(stringArrayDeviceId));
                    accountList.addAll(Arrays.asList(stringArrayAccountStatus));
                    emailList.addAll(Arrays.asList(stringArrayEmail));
//
//                    if (controlMessageService) {
//                        if (getUserName != null) {
//                            if (getUserName.equals(username)) {
//                                startService(backgroundIntent);
//                                controlMessageService = false;
//                            }
//                        }
//                    }
                }

           myRef.removeEventListener(this);
                insertLocalDatabase();
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
                /*if (!controlArrayListClear) {
                    //checkDatabase();
                    myRef.removeEventListener(this);
                    startActivity(new Intent(LoginScreen.this, LoginScreen.class));
                }*/
                userNames.clear();
                userPasswords.clear();
                keysList.clear();
                decryptKey.clear();
                phoneNumberList.clear();
                deviceList.clear();
                accountList.clear();
                emailList.clear();
                controlMessageService = true;
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

    }


    protected void checkDatabaseState() {
        database = FirebaseDatabase.getInstance();
        myKey = database.getReference("keys:");
        key = myKey.getKey();
        controlDatabase = false;

        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String username = postSnapshot.child("Username").getValue(String.class);
                    String password = postSnapshot.child("Password").getValue(String.class);
                    String phoneNumber = postSnapshot.child("Phone").getValue(String.class);
                    String deviceId = postSnapshot.child("Device Id").getValue(String.class);
                    String getKey = postSnapshot.child("Key").getValue(String.class);
                    String accountStatus = postSnapshot.child("Account Status").getValue(String.class);
                    String keys = postSnapshot.getKey();
                    /*if(MainActivity.isControlDeviceAccess()&&keysList.get(accountStatusCounter).equals(keys) ){

                    }*/

                    if (getKey != null) {
                        getKey = CaesarsCiphering.caesarsDecryption(getKey, 13);
                    }

                    if (username != null && getKey != null) {
                        username = AES.decrypt(username, getKey);
                    }
                    if (password != null && getKey != null) {
                        password = AES.decrypt(password, getKey);
                    }

                    if (phoneNumber != null && getKey != null) {
                        phoneNumber = AES.decrypt(phoneNumber, getKey);
                    }
                    if (deviceId != null && getKey != null) {
                        deviceId = AES.decrypt(deviceId, getKey);

                    }
                    if (keysList.size() > accountStatusCounter && accountList.size() > accountStatusCounter) {
                        if (controlAccountStatus && keysList.get(accountStatusCounter).equals(keys) && accountList.get(accountStatusCounter).equals("false")) {
                            myRef.child(keys).child("Account Status").setValue(readAccountState);
                            accountList.remove(accountStatusCounter);
                            accountList.add(accountStatusCounter, readAccountState);
                            controlDatabase = true;
                        }
                    }


                }
                myRef.removeEventListener(this);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        controlUserName = false;
        controlPassword = false;
        controlUserNameLength = false;
        controlPasswordLength = false;
        controlCheckBox = true;
        controlArrayListCounter = false;


    }


    @Override
    protected void onPause() {
        super.onPause();
        myRef.removeEventListener(myRefListener);
    }

    public static String userName() {
        return getUserName;
    }

    public static String userKey() {
        return userNameKey;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();


        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    //Create a new method that we’ll use to initialize our cipher//
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);

//            fingerPrintKey = KEY_NAME;

            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public BiometricPrompt getBiometricPrompt() {
        return biometricPrompt;
    }

    /*public static String getFingerPrintKey() {
        return fingerPrintKey;
    }*/
    private BiometricPrompt.PromptInfo buildBiometricPrompt() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("Login into your account")
                .setDescription("Touch your finger on the finger print sensor to authorise your account.")
                .setNegativeButtonText("Cancel")
                .build();


    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void fingerPrintCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get an instance of KeyguardManager and FingerprintManager//


            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//

                Toast.makeText(LoginScreen.this, "Your device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
            }
            //Check whether the user has granted your app the USE_FINGERPRINT permission//
            else if (ActivityCompat.checkSelfPermission(LoginScreen.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginScreen.this, "Please enable the fingerprint permission", Toast.LENGTH_SHORT).show();
                // If your app doesn't have this permission, then display the following text//
            }

            //Check that the user has registered at least one fingerprint//
            else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(LoginScreen.this, "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_SHORT).show();
                // If the user hasn’t configured any fingerprints, then display the following message//
            }

            //Check that the lockscreen is secured//
            else if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(LoginScreen.this, "Please enable lockscreen security in your device's Settings", Toast.LENGTH_SHORT).show();
                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
            } else {
                //Toast.makeText(LoginScreen.this, "FingerPrint Authentication Enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isFingerPrintSuccess() {
        return fingerPrintSuccess;
    }

    public static boolean isFingerPrintFail() {
        return fingerPrintFail;
    }

    public static boolean isFingerPrintError() {
        return fingerPrintError;
    }


    private boolean checkContact() {
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        permissionContact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED && permissionContact == PackageManager.PERMISSION_GRANTED && permissionReadPhoneState == PackageManager.PERMISSION_GRANTED) {
            controlPermission = true;
            telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            getUserPhoneNumber = telephonyManager.getLine1Number();
            if (getUserPhoneNumber.equals("")) {
                getUserPhoneNumber = telephonyManager.getSimSerialNumber();
            }
            return true;
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 0);


            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_PHONE_STATE},

                        0);

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_CONTACTS},
                        0);
            }

            return false;
        }
    }


    public static boolean isControlPhone() {
        return controlPhone;
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void showFingerPrint() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            imageView_finger_print.setVisibility(View.VISIBLE);

        } else {
            imageView_finger_print.setVisibility(View.GONE);
        }
    }

    private void checkPhoneNumberAuthentication() {
        if (!(phoneNumberList.get(accountStatusCounter).equals(getUserPhoneNumber) || deviceList.get(accountStatusCounter).equals(getUserPhoneNumber))) {
            new AsyncLoginScreen(LoginScreen.this, emailList.get(accountStatusCounter)).execute();
        }
    }


    public static void sendEmail(String email) throws Exception {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);
        // uncomment for debugging infos to stdout
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();
        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("caesarsApp Access From Different Device");
        message.setContent(differentAccessMessage, "text/plain");
        message.setFrom(new InternetAddress("appcaesars@gmail.com"));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(email));

        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }


    public static String getProgressTitle() {
        return progressTitle;
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean hasPermissionSendSMS = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionSendSMS) {
            ActivityCompat.requestPermissions(LoginScreen.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        }

        boolean hasPermissionReadPhoneState = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionReadPhoneState) {
            ActivityCompat.requestPermissions(LoginScreen.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_READ_PHONE_STATE);
        }

        boolean hasPermissionReadContact = (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionReadContact) {
            ActivityCompat.requestPermissions(LoginScreen.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_READ_CONTACT);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkNetworkAccess()){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(backgroundIntent);
            } else {
                startService(backgroundIntent);
            }
        }



    }

    private void insertLocalDatabase() {
        int localDatabaseCounter = 0;
        int localDatabaseRefresherCounter = 0;
        RandomStringGenerator rsg = new RandomStringGenerator();
        keySecret = rsg.randomStringGenerator();


        cursorLoginScreen = myDbLoginScreenHelper.getAllContacts();

        if (cursorLoginScreen.getCount() > 0 && !checkNetworkAccess()) {
            userNames.clear();
            userPasswords.clear();
            deviceList.clear();
        }
        if (userNames.size() != 0) {
            if (myDbLoginScreenHelper != null) {

/*            while (cursorLoginScreen.getCount()> localDatabaseRefresherCounter) {
                myDbLoginScreenHelper.deleteEntry(localDatabaseRefresherCounter);
                localDatabaseRefresherCounter++;
            }*/
                myDbLoginScreenHelper.deletedata();
                //  int x=cursorLoginScreen.getCount();
                myDbLoginScreenHelper = new DBLoginScreenHelper(this);

            }
            while (userNames.size() > localDatabaseCounter) {
                LoginScreenItem dataBaseItem = new LoginScreenItem(AES.encrypt(userNames.get(localDatabaseCounter), keySecret), AES.encrypt(userPasswords.get(localDatabaseCounter), keySecret), AES.encrypt(deviceList.get(localDatabaseCounter), keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13));
                myDbLoginScreenHelper.insertdata(dataBaseItem);
                localDatabaseCounter++;
            }

        } else {
            //userNames.add(cursorLoginScreen.getString(1));
            //userPasswords.add(cursorLoginScreen.getString(2));
            //deviceList.add(cursorLoginScreen.getString(3));

            if (!cursorLoginScreen.moveToFirst()) {
                Toast.makeText(LoginScreen.this, getString(R.string.login_screen_caches_empty), Toast.LENGTH_SHORT).show();

            } else {
                do {
                    accountList.add(cursorLoginScreen.getString(0));
                    userNames.add(AES.decrypt(cursorLoginScreen.getString(1), CaesarsCiphering.caesarsDecryption(cursorLoginScreen.getString(4), 13)));
                    userPasswords.add(AES.decrypt(cursorLoginScreen.getString(2), CaesarsCiphering.caesarsDecryption(cursorLoginScreen.getString(4), 13)));
                    deviceList.add(AES.decrypt(cursorLoginScreen.getString(3), CaesarsCiphering.caesarsDecryption(cursorLoginScreen.getString(4), 13)));
                    phoneNumberList.add(AES.decrypt(cursorLoginScreen.getString(3), CaesarsCiphering.caesarsDecryption(cursorLoginScreen.getString(4), 13)));
                }
                while (cursorLoginScreen.moveToNext());
            }

        }
        cursorLoginScreen.close();
        myDbLoginScreenHelper.close();


    }

    public static boolean isControlInternetConnection() {
        return controlInternetConnection;
    }

    public static void setControlInternetConnection(boolean controlInternetConnection) {
        LoginScreen.controlInternetConnection = controlInternetConnection;
    }

    public static boolean isControlInternetConnectionTask() {
        return controlInternetConnectionTask;
    }

    public static boolean isControlMessageService() {
        return controlMessageService;
    }

    public static void setControlInternetConnectionTask(boolean controlInternetConnectionTask) {
        LoginScreen.controlInternetConnectionTask = controlInternetConnectionTask;
    }

    class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }
}

class AsyncLoginScreen extends AsyncTask<Void, Void, Void> {
    private Activity mActivity;
    private String mEmail;
    private ProgressDialog progressdialog;


    AsyncLoginScreen(Activity activity, String email) {
        super();
        mActivity = activity;
        mEmail = email;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressdialog = new ProgressDialog(mActivity);
        progressdialog.setMessage(LoginScreen.getProgressTitle());
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected Void doInBackground(Void... strings) {
        Looper.prepare();
        try {
            LoginScreen.sendEmail(mEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Looper.loop();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (!isCancelled()) {

        }
        if (progressdialog != null && progressdialog.isShowing()) {
            //asyncLoginScreen.cancel(true);
            progressdialog.dismiss();
        }

    }


}


class AsyncLoginScreenInternetConnection extends AsyncTask<Void, Void, Boolean> {
    private Activity mActivity;
    private String mUrl;
    private ProgressDialog progressdialog;


    AsyncLoginScreenInternetConnection(Activity activity, String url) {
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
                    LoginScreen.setControlInternetConnection(isAvailable);
                    LoginScreen.setControlInternetConnectionTask(isAvailable);
                } else {
                    new AsyncLoginScreenInternetConnection(mActivity, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    //  Toast.makeText(mActivity.getBaseContext(), mActivity.getBaseContext().getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                    LoginScreen.setControlInternetConnectionTask(true);
                }
            } catch (Exception e) {
                isAvailable = false;
                LoginScreen.setControlInternetConnectionTask(true);
                LoginScreen.setControlInternetConnection(false);
                //  Toast.makeText(mActivity.getBaseContext(), mActivity.getBaseContext().getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                new AsyncLoginScreenInternetConnection(mActivity, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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





