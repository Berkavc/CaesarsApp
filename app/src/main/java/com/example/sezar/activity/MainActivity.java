package com.example.sezar.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.sezar.R;
import com.example.sezar.database.DBCreateAccountHelper;
import com.example.sezar.database.DBLoginScreenCheckBoxHelper;
import com.example.sezar.database.DBMainActivitySliderHelper;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.example.sezar.fragment.Fragment_About_Us;
import com.example.sezar.fragment.Fragment_Change_App_Icon;
import com.example.sezar.fragment.Fragment_Change_Background;
import com.example.sezar.fragment.Fragment_Change_Storage_Area;
import com.example.sezar.fragment.Fragment_Contact_Us;
import com.example.sezar.fragment.Fragment_Encrypted_Message_List;
import com.example.sezar.fragment.Fragment_Read_Encrypted_Message;
import com.example.sezar.fragment.Fragment_Write_Encrypted_Message;
import com.example.sezar.model.CreateAccountItem;
import com.example.sezar.model.MainActivitySliderItem;
import com.example.sezar.tokenize.Tokenize;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    VideoView videoview;
    private boolean controlVideoState;
    Toolbar toolbar;
    ImageButton img_button;
    protected static boolean controlDeviceAccess;
    View view;
    Cursor cursor;
    Cursor cursorVideo;
    Cursor cursorSlider;
    DBLoginScreenCheckBoxHelper myDb;
    //private String readAccountState = "false";
    List<String> userNames;
    List<String> userPasswords;
    List<String> keysList;
    List<String> decryptKey;
    List<String> phoneNumberList;
    List<String> deviceList;
    List<String> accountList;
    DatabaseReference myRefdelete;
    FirebaseDatabase database;
    String key;
    DatabaseReference myRef;
    boolean controlDelete;
    DatabaseReference myKey;
    private boolean controlFragmentState;
    private boolean controlVideoAccountState;
    private ProgressDialog progressdialog;
    private boolean accountStatusDestroy;
    private boolean controlAccountStatusPause;
    private boolean controlAccountStatusResume;
    private ImageView imageView_toolbar;
    private AppBarLayout appBarLayout;
    DBCreateAccountHelper myDbVideo;
    DBMainActivitySliderHelper myDbSlider;
    CreateAccountItem dataBaseItem;
    Uri uri;
    private Switch safetySwitch;
    public static boolean controlSafetyFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DBLoginScreenCheckBoxHelper(this);
        cursor = myDb.getAllContacts();
        myDbVideo = new DBCreateAccountHelper(this);
        cursorVideo = myDbVideo.getAllContacts();
        myDbSlider = new DBMainActivitySliderHelper(this);
        cursorSlider = myDbSlider.getAllContacts();


        Toolbar toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(false);
        imageView_toolbar = findViewById(R.id.imageView_collapsing_toolbar_icon);

        toolbar.setTitleTextColor(getResources().getColor(R.color.text_color));
        setSupportActionBar(toolbar);
        setToolbarTitle();


        userNames = new ArrayList<>();
        userPasswords = new ArrayList<>();
        keysList = new ArrayList<>();
        decryptKey = new ArrayList<>();
        phoneNumberList = new ArrayList<>();
        deviceList = new ArrayList<>();
        accountList = new ArrayList<>();

        controlVideoAccountState = false;
        controlDelete = false;
        controlDeviceAccess = false;
        controlFragmentState = false;
        accountStatusDestroy = false;
        controlAccountStatusPause = false;
        controlAccountStatusResume = false;
        controlSafetyFilter = false;


        /*img_button = findViewById(R.id.image_button);
        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlVideoAccountState = true;
                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));

            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.text_color));
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        videoview = (VideoView) findViewById(R.id.videoView_main_menu);
        if (!cursorVideo.moveToNext()) {
            myDbVideo.insertdata(new CreateAccountItem("0"));
        }

        setVideo();
        videoview.setVideoURI(uri);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0f, 0f);
            }
        });
        videoview.start();
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!controlVideoState) {
                    videoview.stopPlayback();
                } else {
                    mp.setLooping(true);
                }
            }
        });
        /*Fragment_Write_Encrypted_Message fragment_write_encrypted_message = Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message");
        videoview.setVisibility(View.INVISIBLE);
        videoview.stopPlayback();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_write_encrypted_message).commit();*/


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    imageView_toolbar.setVisibility(View.VISIBLE);

                } else if ((i + appBarLayout.getTotalScrollRange()) == 0) {
                    imageView_toolbar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        startActivity(new Intent(MainActivity.this, LoginScreen.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        View view;
        MenuItem item;
        item = menu.getItem(0);
        view = item.getActionView();
        safetySwitch = view.findViewById(R.id.switch_item);
        if (!cursorSlider.moveToFirst()) {
            safetySwitch.setChecked(false);
            checkSlider();
        } else {
            cursorSlider.moveToFirst();
            if (cursorSlider.getString(1).equals("true")) {
                safetySwitch.setChecked(true);
                controlSafetyFilter = true;
            } else {
                safetySwitch.setChecked(false);
                controlSafetyFilter = false;
            }
        }
        // safetySwitch.performClick();
        safetySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSlider();
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_account) {
            //checkDatabaseAccountStatus();
            finish();
            startActivity(new Intent(MainActivity.this, LoginScreen.class));
            return true;
        } else if (id == R.id.action_exit_application) {
            checkDatabaseAccountStatus();
            finish();
            moveTaskToBack(true);
            return true;
        } else if (id == R.id.action_delete_account) {
            // controlDelete = true;
            deleteAccountAlertDialog();

            // finish();
            return true;
        } else if (id == R.id.action_restart_video) {
            controlVideoAccountState = true;
            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
        Fragment_Write_Encrypted_Message fragment_write_encrypted_message = Fragment_Write_Encrypted_Message.newInstance("Fragment_Write_Encrypted_Message");
        Fragment_Encrypted_Message_List fragment_encrypted_message_list = Fragment_Encrypted_Message_List.newInstance("Fragment_Encrypted_Message_List");
        Fragment_Change_Storage_Area fragment_change_storage_area = Fragment_Change_Storage_Area.newInstance("Fragment_Change_Storage_Area");
        Fragment_Change_Background fragment_change_background = Fragment_Change_Background.newInstance("Fragment_Change_Background");
        Fragment_Change_App_Icon fragment_change_app_icon = Fragment_Change_App_Icon.newInstance("Fragment_Change_App_Icon");
        Fragment_About_Us fragment_about_us = Fragment_About_Us.newInstance("Fragment_About_Us");
        Fragment_Contact_Us fragment_contact_us = Fragment_Contact_Us.newInstance("Fragment_Contact_Us");
        int id = item.getItemId();

        videoview.setVisibility(View.INVISIBLE);
        videoview.stopPlayback();

        if (id == R.id.nav_read_encrypted_message) {
            //toolbar.setTitle(getString(R.string.main_activity_encrypted_message));
//            videoview.setVisibility(View.INVISIBLE);
//            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();


            // Handle the camera action
        } else if (id == R.id.nav_write_encrypted_message) {
//            videoview.setVisibility(View.INVISIBLE);
//            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_write_encrypted_message).commit();

        }/* else if (id == R.id.nav_encrypted_message_list) {
            videoview.setVisibility(View.INVISIBLE);
            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_encrypted_message_list).commit();

        }*/ /*else if (id == R.id.nav_storage_area) {
            videoview.setVisibility(View.INVISIBLE);
            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_change_storage_area).commit();

        } */ else if (id == R.id.nav_change_background) {
//            videoview.setVisibility(View.INVISIBLE);
//            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_change_background).commit();

        } /*else if (id == R.id.nav_change_app_icon) {
            videoview.setVisibility(View.INVISIBLE);
            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_change_app_icon).commit();

        }*/ else if (id == R.id.nav_about_us) {
//            videoview.setVisibility(View.INVISIBLE);
//            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_about_us).commit();

        } else if (id == R.id.nav_contact_us) {
//            videoview.setVisibility(View.INVISIBLE);
//            videoview.stopPlayback();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_contact_us).commit();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (!getSupportFragmentManager().getFragments().isEmpty()) {
            videoview.setVisibility(View.INVISIBLE);
            controlVideoState = false;
        } else {
            videoview.start();
            controlVideoState = true;
        }
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!controlVideoState) {
                    videoview.stopPlayback();
                } else {
                    videoview.start();
                }
            }
        });
    }

    private void setToolbarTitle() {
        cursor = myDb.getAllContacts();
        while (cursor.moveToNext()) {
            //toolbar.setTitle(getString(R.string.home_screen_user_welcome) + cursor.getString(1));

            setTitle(getString(R.string.home_screen_user_welcome) + AES.decrypt(cursor.getString(1), CaesarsCiphering.caesarsDecryption(cursor.getString(2), 13)));

        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.button_background)));
        cursor.close();
        myDb.close();
        // setTheme(R.style.AppTheme_Customize);
        //appBarLayout.setBackgroundColor(getColor(R.color.red));
    }

    private void setVideo() {
        cursorVideo = myDbVideo.getAllContacts();
        if (cursorVideo != null) {
            //cursorVideo = myDbVideo.getAllContacts();

            while (cursorVideo.moveToNext()) {
                if (cursorVideo.getString(1).equals("0")) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.winter_long);
                } else if (cursorVideo.getString(1).equals("1")) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.autmn_long);
                } else if (cursorVideo.getString(1).equals("2")) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.summer_long);
                } else if (cursorVideo.getString(1).equals("3")) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.spring_long);
                } else if (cursorVideo.getString(1).equals("4")) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sunlight);
                } else if (cursorVideo.getString(1).equals("5")) {
                    uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.moon);
                }
            }

        } else {
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.winter_long);
        }
        cursorVideo.close();
        myDbVideo.close();


    }

    private void deleteAccount() {
        database = FirebaseDatabase.getInstance();
        myKey = database.getReference("keys:");
        key = myKey.getKey();
        myRefdelete = database.getReference();
        myRefdelete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String getKey = postSnapshot.child("Key").getValue(String.class);
                    String keys = postSnapshot.getKey();
                    if (LoginScreen.userKey().equals(keys)) {
                        if (controlDelete) {
                            myRefdelete.child(keys).removeValue();
                            controlDelete = false;
                        }
                    }
                    String[] stringArraygetKey = {getKey};
                    decryptKey.addAll(Arrays.asList(stringArraygetKey));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteAccountAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.main_activity_delete_account);
        builder.setMessage(R.string.main_activity_delete_account_alert_dialog);
        builder.setNegativeButton(R.string.main_activity_delete_account_no, null);
        builder.setPositiveButton(R.string.main_activity_delete_account_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controlDelete = true;
               /* progressdialog = new ProgressDialog(MainActivity.this);
                progressdialog.setMessage(ForgotPassword.getProgressTitle());
                progressdialog.show();
                progressdialog.setCancelable(false);
                progressdialog.setCanceledOnTouchOutside(false);*/
                deleteAccount();
                //new AsyncMainActivity(MainActivity.this).execute();
                startActivity(new Intent(MainActivity.this, LoginScreen.class));
                Toast.makeText(MainActivity.this, R.string.main_activity_delete_account_message, Toast.LENGTH_LONG).show();
                finish();
            }
        });
        builder.create().show();

    }

    @Override
    protected void onPause() {
        super.onPause();
      /* if (Fragment_Write_Encrypted_Message.getControlContactState() != null) {
            if (!controlVideoAccountState && !Fragment_Change_Background.isControlBackground() && !Fragment_Write_Encrypted_Message.getControlContactState()) {
                checkDatabaseAccountStatus();
                //finish();
            }
        } else {
           checkDatabaseAccountStatus();
           //finish();
       }*/
        controlAccountStatusPause = true;
        checkDatabaseAccountStatus();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkNetworkAccess()) {
            controlAccountStatusResume = true;
            checkDatabaseAccountStatus();
        }
//        if (Fragment_Read_Encrypted_Message.isControlInternetState()) {
//            Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
//        }


    }

    private void checkDatabaseAccountStatus() {
        database = FirebaseDatabase.getInstance();
        myKey = database.getReference("keys:");
        key = myKey.getKey();


        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (controlDeviceAccess) {
                        break;
                    }
                    String username = postSnapshot.child("Username").getValue(String.class);
                    String getKey = postSnapshot.child("Key").getValue(String.class);
                    String keys = postSnapshot.getKey();

                    if (getKey != null) {
                        getKey = CaesarsCiphering.caesarsDecryption(getKey, 13);
                    }
                    if (username != null && getKey != null) {
                        username = AES.decrypt(username, getKey);
                    }
                  /*  if ((LoginScreen.userName().equals(username) && !controlDeviceAccess)||accountStatusDestroy) {
                        myRef.child(keys).child("Account Status").setValue("false");
                        controlDeviceAccess = true;
                        myRef.removeEventListener(this);
                        break;
                    }*/
                    if ((LoginScreen.userName().equals(username) && controlAccountStatusPause && !isControlDeviceAccess()) || !checkNetworkAccess()) {
                        myRef.child(keys).child("Account Status").setValue("false");
                        //controlDeviceAccess = true;
                        controlAccountStatusPause = false;
                        myRef.removeEventListener(this);
                        break;
                    } else if (LoginScreen.userName().equals(username) && controlAccountStatusResume) {
                        myRef.child(keys).child("Account Status").setValue("true");
                        //controlDeviceAccess = true;
                        controlAccountStatusResume = false;
                        myRef.removeEventListener(this);
                        break;
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static boolean isControlDeviceAccess() {
        return controlDeviceAccess;
    }

    public static void setControlDeviceAccess(boolean controlDeviceAccess) {
        MainActivity.controlDeviceAccess = controlDeviceAccess;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isPowerSaveMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return powerManager.isPowerSaveMode();
        }

        // For older versions, we just say that device is not in power save mode
        return false;
    }

    public boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void checkSlider() {
        cursorSlider = myDbSlider.getAllContacts();
        if (!cursorSlider.moveToFirst()) {
            myDbSlider.insertdata(new MainActivitySliderItem("false"));
        } else if (safetySwitch.isChecked()) {
            myDbSlider.updateData(new MainActivitySliderItem("true"), 1);
            controlSafetyFilter = true;
            Tokenize.setControlTextToSpeech(true);
            Toast.makeText(MainActivity.this, getString(R.string.main_activity_safe_filter), Toast.LENGTH_SHORT).show();
        } else {
            myDbSlider.updateData(new MainActivitySliderItem("false"), 1);
            controlSafetyFilter = false;
            Tokenize.setControlTextToSpeech(false);
            Toast.makeText(MainActivity.this, getString(R.string.main_activity_safe_filter_deactivate), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isControlSafetyFilter() {
        return controlSafetyFilter;
    }
}
/*class AsyncMainActivity extends AsyncTask<Void, Void, Void> {
    Activity mActivity;
    private ProgressDialog progressdialog;

    AsyncMainActivity(Activity activity) {
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

            try {
                Thread.sleep(5000);
                Toast.makeText(mActivity, R.string.main_activity_delete_account_message, Toast.LENGTH_LONG).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


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
*/