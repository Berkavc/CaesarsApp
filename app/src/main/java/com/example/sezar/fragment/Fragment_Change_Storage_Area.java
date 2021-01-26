package com.example.sezar.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sezar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Change_Storage_Area extends Fragment {
    public static final String MESSAGE_KEY = "message_key";


    public Fragment_Change_Storage_Area() {
        // Required empty public constructor
    }
    public static Fragment_Change_Storage_Area newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(Fragment_Change_Storage_Area.MESSAGE_KEY, message);
        Fragment_Change_Storage_Area fragment= new Fragment_Change_Storage_Area();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__change__storage__area, container, false);
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(MESSAGE_KEY);

        }
        return view;
    }

}
