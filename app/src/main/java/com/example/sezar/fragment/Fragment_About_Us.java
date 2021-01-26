package com.example.sezar.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sezar.R;
import com.example.sezar.adapter.AboutUsAdapter;
import com.example.sezar.model.AboutUsItem;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_About_Us extends Fragment {
    public static final String MESSAGE_KEY = "message_key";
    ArrayList<AboutUsItem> users;
    RecyclerView recyclerView_user_info;
    AboutUsAdapter adapter;
    String fact;


    public Fragment_About_Us() {
        // Required empty public constructor
    }

    public static Fragment_About_Us newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(Fragment_About_Us.MESSAGE_KEY, message);
        Fragment_About_Us fragment = new Fragment_About_Us();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__about__us, container, false);
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(MESSAGE_KEY);

            users = new ArrayList<>();

            recyclerView_user_info = view.findViewById(R.id.recyclerView_about_us_facts);
            LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            adapter = new AboutUsAdapter(getContext(), users);
            recyclerView_user_info.setLayoutManager(layoutmanager);
            recyclerView_user_info.setAdapter(adapter);
            addAboutUsItem();


        }
        return view;
    }

    private void addAboutUsItem() {
        users.add(new AboutUsItem(getString(R.string.about_us_fact_number_one)));
        users.add(new AboutUsItem(getString(R.string.about_us_fact_number_two)));
        users.add(new AboutUsItem(getString(R.string.about_us_fact_number_three)));
    }

}
