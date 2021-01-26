package com.example.sezar.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.sezar.R;
import com.example.sezar.activity.LoginScreen;
import com.example.sezar.activity.MainActivity;
import com.example.sezar.adapter.ChangeBackgroundAdapter;
import com.example.sezar.database.DBCreateAccountHelper;
import com.example.sezar.model.CreateAccountItem;
import com.example.sezar.model.LoginScreenCheckBoxItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Change_Background extends Fragment {
    public static final String MESSAGE_KEY = "message_key";
    private Button button_night_theme;
    private Button button_default_theme;
    private Button button_winter_theme;
    private Button button_fall_theme;
    private Button button_summer_theme;
    private Button button_autmn_theme;
    private Cursor cursor;
    private TextView textView_application_theme;
    private TextView textView_application_video;
    private ArrayList<CreateAccountItem> videos;
    private DBCreateAccountHelper myDb;
    private CreateAccountItem dataBaseItem;
    public static boolean controlBackground;
    private boolean controlDefaultTheme;
    private ViewPager viewPager;
    private ChangeBackgroundAdapter changeBackgroundAdapter;
    private TabLayout tabLayout;


    public Fragment_Change_Background() {
        // Required empty public constructor
    }

    public static Fragment_Change_Background newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(Fragment_Change_Background.MESSAGE_KEY, message);
        Fragment_Change_Background fragment = new Fragment_Change_Background();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment__change__background, container, false);
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(MESSAGE_KEY);
            button_night_theme = view.findViewById(R.id.button_night_theme);
            button_default_theme = view.findViewById(R.id.button_default_theme);
            button_winter_theme = view.findViewById(R.id.button_winter_theme);
            button_fall_theme = view.findViewById(R.id.button_spring_theme);
            button_summer_theme = view.findViewById(R.id.button_summer_theme);
            button_autmn_theme = view.findViewById(R.id.button_autmn_theme);
            textView_application_theme = view.findViewById(R.id.textView_change_theme_title);
            textView_application_video = view.findViewById(R.id.textView_change_theme_video_title);
            viewPager = view.findViewById(R.id.viewPager);
            tabLayout = view.findViewById(R.id.tab_layout);


//            videoButtonInvisible();

            controlDefaultTheme = true;
            controlBackground = false;

            videos = new ArrayList<>();

            addVideos();

            myDb = new DBCreateAccountHelper(getContext());
            cursor = myDb.getAllContacts();


            if (!cursor.moveToNext()) {
                dataBaseItem = new CreateAccountItem(videos.get(0).getEmail());
                myDb.insertdata(dataBaseItem);
            }


            changeBackgroundAdapter = new ChangeBackgroundAdapter(getContext());
            viewPager.setAdapter(changeBackgroundAdapter);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        controlDefaultTheme = true;
                        extraThemeTabDefault();
//                        themeButtonVisible();
//                        videoButtonInvisible();
                    } else if (tab.getPosition() == 1) {
                        controlDefaultTheme = false;
                        extraThemeTab();
//                        themeButtonInvisible();
//                        videoButtonVisible();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            button_night_theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    controlBackground = true;
                    startActivity(new Intent(getContext(), MainActivity.class));

                }
            });


            button_default_theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    controlBackground = true;
                    startActivity(new Intent(getContext(), MainActivity.class));

                }
            });


            button_winter_theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(controlDefaultTheme){
                        dataBaseItem = new CreateAccountItem(videos.get(0).getEmail());
                        myDb.deleteEntry(0);
                        myDb.insertdata(dataBaseItem);
                    }else{
                        dataBaseItem = new CreateAccountItem(videos.get(4).getEmail());
                        myDb.deleteEntry(0);
                        myDb.insertdata(dataBaseItem);
                    }

                    controlBackground = true;
                    //dataBaseItem = new CreateAccountItem(videos.get(0));
                    //myDb.updateData(videos.get(0), 0);
                    startActivity(new Intent(getContext(), MainActivity.class));

                }
            });

            button_autmn_theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dataBaseItem = new CreateAccountItem(videos.get(1).getEmail());
                    myDb.deleteEntry(0);
                    myDb.insertdata(dataBaseItem);
                    controlBackground = true;
                    //dataBaseItem = new CreateAccountItem(videos.get(1));
                    // myDb.updateData(videos.get(1), 0);
                    startActivity(new Intent(getContext(), MainActivity.class));

                }
            });

            button_summer_theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataBaseItem = new CreateAccountItem(videos.get(2).getEmail());
                    myDb.deleteEntry(0);
                    myDb.insertdata(dataBaseItem);
                    controlBackground = true;
                    //dataBaseItem = new CreateAccountItem(videos.get(2));
                    //myDb.updateData(videos.get(2), 0);
                    startActivity(new Intent(getContext(), MainActivity.class));

                }

            });

            button_fall_theme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(controlDefaultTheme){
                        dataBaseItem = new CreateAccountItem(videos.get(3).getEmail());
                        myDb.deleteEntry(0);
                        myDb.insertdata(dataBaseItem);
                    }else{
                        dataBaseItem = new CreateAccountItem(videos.get(5).getEmail());
                        myDb.deleteEntry(0);
                        myDb.insertdata(dataBaseItem);
                    }

                    controlBackground = true;
                    //dataBaseItem = new CreateAccountItem(videos.get(3));
                    // myDb.updateData(videos.get(3), 0);
                    startActivity(new Intent(getContext(), MainActivity.class));

                }
            });

        }
        return view;
    }

    private void addVideos() {
        videos.add(new CreateAccountItem("0"));
        videos.add(new CreateAccountItem("1"));
        videos.add(new CreateAccountItem("2"));
        videos.add(new CreateAccountItem("3"));
        videos.add(new CreateAccountItem("4"));
        videos.add(new CreateAccountItem("5"));
    }

//    private void videoButtonInvisible(){
//        button_fall_theme.setVisibility(View.INVISIBLE);
//        button_autmn_theme.setVisibility(View.INVISIBLE);
//        button_winter_theme.setVisibility(View.INVISIBLE);
//        button_summer_theme.setVisibility(View.INVISIBLE);
//        textView_application_video.setVisibility(View.INVISIBLE);
//    }
//
//    private void videoButtonVisible(){
//        button_fall_theme.setVisibility(View.VISIBLE);
//        button_autmn_theme.setVisibility(View.VISIBLE);
//        button_winter_theme.setVisibility(View.VISIBLE);
//        button_summer_theme.setVisibility(View.VISIBLE);
//        textView_application_video.setVisibility(View.VISIBLE);
//    }
//
//    private void themeButtonInvisible(){
//        button_default_theme.setVisibility(View.INVISIBLE);
//        button_night_theme.setVisibility(View.INVISIBLE);
//        textView_application_theme.setVisibility(View.INVISIBLE);
//    }
//
//    private void themeButtonVisible(){
//        button_default_theme.setVisibility(View.VISIBLE);
//        button_night_theme.setVisibility(View.VISIBLE);
//        textView_application_theme.setVisibility(View.VISIBLE);
//    }

    private void extraThemeTab() {
        button_summer_theme.setVisibility(View.INVISIBLE);
        button_autmn_theme.setVisibility(View.INVISIBLE);
        button_winter_theme.setText(getResources().getString(R.string.change_theme_sunlight_theme));
        button_winter_theme.setBackground(getResources().getDrawable(R.drawable.button_background_light));
        button_fall_theme.setText(getResources().getString(R.string.change_theme_moon_theme));
        button_fall_theme.setBackground(getResources().getDrawable(R.drawable.button_background_moon));

    }

    private void extraThemeTabDefault() {
        button_summer_theme.setVisibility(View.VISIBLE);
        button_autmn_theme.setVisibility(View.VISIBLE);
        button_winter_theme.setText(getResources().getString(R.string.change_theme_winter_theme));
        button_winter_theme.setBackground(getResources().getDrawable(R.drawable.button_background_blue));
        button_fall_theme.setText(getResources().getString(R.string.change_theme_spring_theme));
        button_fall_theme.setBackground(getResources().getDrawable(R.drawable.button_background_yellow));

    }

    public static boolean isControlBackground() {
        return controlBackground;
    }
}
