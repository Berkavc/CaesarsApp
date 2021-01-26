package com.example.sezar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sezar.R;
import com.example.sezar.fragment.Fragment_Read_Encrypted_Message;
import com.example.sezar.model.ContactUsItem;
import com.example.sezar.model.ReadEncryptedMessageItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ReadEncryptedMessagePhoneNumberAdapter extends RecyclerView.Adapter<ReadEncryptedMessagePhoneNumberAdapter.ViewHolder> {
    private Context context;
    private List<String> userInfos;
    private static boolean controlModification = false;
    private static List<String> userPhoneNumbers = new ArrayList<>();
    private static int check_Position;
    Random random = new Random();
    private int contactCounter = 0;


    public ReadEncryptedMessagePhoneNumberAdapter(Context context, List<String> userinfos) {
        this.context = context;
        this.userInfos = userinfos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.read_encrypted_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        // int randomInteger = random.nextInt(3);
        contactCounter = 0;
        final int positioncheck;


        String userInfo = userInfos.get(position);
        if (userInfo != null && !userPhoneNumbers.contains(userInfo)) {
            userPhoneNumbers.add(userInfo);
        }
        if (!Fragment_Read_Encrypted_Message.getContactPhoneNumber().isEmpty()) {
            Iterator<String> checkUserName = Fragment_Read_Encrypted_Message.getContactPhoneNumber().iterator();
            while (checkUserName.hasNext()) {
                if (userInfos.get(position).equals(Fragment_Read_Encrypted_Message.getContactPhoneNumber().get(contactCounter))) {
                    viewHolder.textView_phone_number_list.setText(Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter));
                    break;
                } else {
                    viewHolder.textView_phone_number_list.setText(userInfo);
                }
                contactCounter++;
                if (Fragment_Read_Encrypted_Message.getContactPhoneNumber().size() == contactCounter) {
                    break;
                }
            }
        } else {
            viewHolder.textView_phone_number_list.setText(userInfo);

        }



      /* switch (randomInteger){
           case 0:
               list_cardview.setCardBackgroundColor(Color.GREEN);
               break;
           case 1:
               list_cardview.setCardBackgroundColor(Color.RED);
               break;
           case 2:
               list_cardview.setCardBackgroundColor(Color.BLUE);
               break;
       }*/
        positioncheck = position;
        viewHolder.list_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_Position = position;
                Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_EncryptedMessage");
                controlModification = true;
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_phone_number_list;
        CardView list_cardview;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_phone_number_list = itemView.findViewById(R.id.textView_read_encrypted_message_phone_number);
            list_cardview = itemView.findViewById(R.id.cardview_read_encrypted_message_list);

        }
    }

    public static boolean isControlModification() {
        return controlModification;
    }

    public static void setControlModification(boolean controlModification) {
        ReadEncryptedMessagePhoneNumberAdapter.controlModification = controlModification;
    }

    public static int getCheck_Position() {
        return check_Position;
    }

    public static List<String> getUserPhoneNumbers() {
        return userPhoneNumbers;
    }
}
