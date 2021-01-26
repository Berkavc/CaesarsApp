package com.example.sezar.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sezar.R;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;

import java.util.Properties;
import java.util.Timer;


public class ForgotPassword extends AppCompatActivity implements Animation.AnimationListener {
    EditText editText_forgot_password_email;
    Button button_forgot_password_remainder;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String key;
    static String checkEmail;
    static String progressTitle;
    DatabaseReference myKey;
    List<String> userEmail;
    static List<String> userPassword;
    static List<String> userName;
    boolean controlEmailLength, controlEmail, controlEmailFormat;
    static int arrayListCounter;
    Timer timer;
    ProgressDialog progress;
    static String userNameEmail;
    static String passwordEmail;
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static boolean controlInternetConnection;
    private AsyncTask asyncForgotPasswordInternetConnectionTask;
    private RelativeLayout snackBarLayout;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* if (AppCompatDelegate.getDefaultNightMode()
                ==AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.FeedActivityThemeDark);
        }*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editText_forgot_password_email = findViewById(R.id.editText_forgot_email);
        button_forgot_password_remainder = findViewById(R.id.button_forgot_my_password);
        progress = new ProgressDialog(ForgotPassword.this);
        progressTitle = getString(R.string.forgot_password_wait);
        userNameEmail = getString(R.string.forgot_password_username);
        passwordEmail = getString(R.string.forgot_password_password);
        timer = new Timer();

        userEmail = new ArrayList<>();
        userPassword = new ArrayList<>();
        userName = new ArrayList<>();

        controlEmail = false;
        controlEmailLength = false;
        controlEmailFormat = false;

        controlInternetConnection = false;

        arrayListCounter = 0;
        checkDatabase();


        asyncForgotPasswordInternetConnectionTask = new AsyncForgotPasswordInternetConnection(ForgotPassword.this, database.getReference().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        button_forgot_password_remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAccess();
                if (checkNetworkAccess() && controlInternetConnection) {
                    arrayListCounter = 0;
                    checkUserEmail();
                    checkEmpty();
                }
            }
        });

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

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
                    String key = CaesarsCiphering.caesarsDecryption(postSnapshot.child("Key").getValue(String.class), 13);
                    String email = AES.decrypt(postSnapshot.child("Email").getValue(String.class), key);
                    String password = AES.decrypt(postSnapshot.child("Password").getValue(String.class), key);
                    String username = AES.decrypt(postSnapshot.child("Username").getValue(String.class), key);


                    String[] stringArrayUserEmail = {email};
                    String[] stringArrayUserPassword = {password};
                    String[] stringArrayUserName = {username};

                    userEmail.addAll(Arrays.asList(stringArrayUserEmail));
                    userName.addAll(Arrays.asList(stringArrayUserName));
                    userPassword.addAll(Arrays.asList(stringArrayUserPassword));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkUserEmail() {
        editText_forgot_password_email.setText(editText_forgot_password_email.getText().toString().trim());
        checkEmail = editText_forgot_password_email.getText().toString();
        if (checkEmail.length() == 0) {
            controlEmail = false;
            controlEmailLength = false;
        } else {
            controlEmailLength = true;
            if (isEmailValid(checkEmail)) {
                controlEmailFormat = true;
                Iterator<String> checkUser = userEmail.iterator();
                while (checkUser.hasNext()) {
                    if (checkEmail.equals(userEmail.get(arrayListCounter))) {
                        controlEmail = true;
                        controlEmailLength = true;
                        controlEmailFormat = true;
                        break;
                    }
                    arrayListCounter++;
                    if (userEmail.size() == arrayListCounter) {
                        break;
                    }
                }

            } else {
                controlEmailFormat = false;
            }
        }
    }

    private void checkEmpty() {

        if (controlEmail && controlEmailLength && controlEmailFormat) {
            try {
                new AsyncForgotPassword(ForgotPassword.this).execute();
                if (getCurrentFocus() != null) {
                    snackbar = Snackbar.make(getCurrentFocus(), getString(R.string.forgot_password_successfull), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Toast.makeText(ForgotPassword.this, getString(R.string.forgot_password_successfull), Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(ForgotPassword.this, LoginScreen.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!controlEmailLength) {
                Toast.makeText(ForgotPassword.this, getString(R.string.forgot_password_empty), Toast.LENGTH_SHORT).show();
            } else if (!controlEmailFormat) {
                Toast.makeText(ForgotPassword.this, getString(R.string.forgot_password_invalid_format), Toast.LENGTH_SHORT).show();
            } else {
                if (getCurrentFocus() != null) {
                    snackbar = Snackbar.make(getCurrentFocus(), getString(R.string.forgot_password_invalid_email), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Toast.makeText(ForgotPassword.this, getString(R.string.forgot_password_invalid_email), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
        message.setSubject("caesarsApp Password");
        message.setContent(userNameEmail + userName.get(arrayListCounter) + "\n" + passwordEmail + userPassword.get(arrayListCounter), "text/plain");
        message.setFrom(new InternetAddress("appcaesars@gmail.com"));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(email));

        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }


    public boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(ForgotPassword.this, getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static String getProgressTitle() {
        return progressTitle;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPassword.this, LoginScreen.class));
    }

    public static boolean isControlInternetConnection() {
        return controlInternetConnection;
    }

    public static void setControlInternetConnection(boolean controlInternetConnection) {
        ForgotPassword.controlInternetConnection = controlInternetConnection;
    }
}

class AsyncForgotPassword extends AsyncTask<Void, Void, Void> {
    Activity mActivity;
    private ProgressDialog progressdialog;

    AsyncForgotPassword(Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressdialog = new ProgressDialog(mActivity);
        progressdialog.setMessage(ForgotPassword.getProgressTitle());
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected Void doInBackground(Void... strings) {
        Looper.prepare();
        try {
            ForgotPassword.sendEmail(ForgotPassword.checkEmail);

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
            progressdialog.dismiss();
        }

    }
}

class AsyncForgotPasswordInternetConnection extends AsyncTask<Void, Void, Boolean> {
    private Activity mActivity;
    private String mUrl;
    private ProgressDialog progressdialog;


    AsyncForgotPasswordInternetConnection(Activity activity, String url) {
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
                    ForgotPassword.setControlInternetConnection(isAvailable);
                    //LoginScreen.setControlInternetConnectionTask(isAvailable);
                } else {
                    new AsyncForgotPasswordInternetConnection(mActivity, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    ForgotPassword.setControlInternetConnection(false);
                    Toast.makeText(mActivity.getBaseContext(), mActivity.getBaseContext().getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                    //LoginScreen.setControlInternetConnectionTask(true);
                }
            } catch (Exception e) {
                isAvailable = false;
                // LoginScreen.setControlInternetConnectionTask(true);
                ForgotPassword.setControlInternetConnection(false);
                Toast.makeText(mActivity.getBaseContext(), mActivity.getBaseContext().getString(R.string.login_screen_internet_connection_failure), Toast.LENGTH_SHORT).show();
                new AsyncForgotPasswordInternetConnection(mActivity, mUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
