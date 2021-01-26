package com.example.sezar.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.sezar.R;
import com.example.sezar.database.DBLoginScreenCheckBoxHelper;
import com.example.sezar.encryption_algorithms.AES;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    Cursor cursor;
    DBLoginScreenCheckBoxHelper myDb;


    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate
                .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (AppCompatDelegate.getDefaultNightMode()
                == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.FeedActivityThemeDark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        myDb = new DBLoginScreenCheckBoxHelper(this);
        cursor = myDb.getAllContacts();
        //toolbar = findViewById(R.id.toolbar_home_screen);



        setToolbarTitle();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_account) {
            finish();
            startActivity(new Intent(HomeScreen.this, LoginScreen.class));
            return true;
        } else if (id == R.id.action_exit_application) {
            finish();
            moveTaskToBack(true);
            return true;
        } else if (id == R.id.action_delete_account) {
            finish();
            //deleteAccount();
            startActivity(new Intent(HomeScreen.this,LoginScreen.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setToolbarTitle() {
        cursor = myDb.getAllContacts();
        while (cursor.moveToNext()) {
            //toolbar.setTitle(getString(R.string.home_screen_user_welcome) + cursor.getString(1));
            setTitle(getString(R.string.home_screen_user_welcome) + cursor.getString(1));

        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
        // setTheme(R.style.AppTheme_Customize);
        //appBarLayout.setBackgroundColor(getColor(R.color.red));
    }



}

