package com.example.sezar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sezar.R;
import com.example.sezar.model.WriteEncryptedMessageItem;

import java.util.ArrayList;

public class WriteEncryptedMessageAdapter extends RecyclerView.Adapter<WriteEncryptedMessageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WriteEncryptedMessageItem> userInfos;
    private static boolean controlModification = false;
    private static int check_Position;
    private String[] items = {"a","b"};
    private View view;

    CardView list_cardview;

    public WriteEncryptedMessageAdapter(Context context, ArrayList<WriteEncryptedMessageItem> userinfos) {
        this.context = context;
        this.userInfos = userinfos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.write_encrypted_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        final int positioncheck;


        final WriteEncryptedMessageItem userInfo = userInfos.get(position);
        viewHolder.textView_write_encrypted_message_list.setText(userInfo.getPhoneNumber());

        positioncheck = position;
        list_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CustomDialogTheme);
                builder.setTitle(context.getString(R.string.write_encrypted_message_title));
                items[0]=context.getString(R.string.write_encrypted_message_cardview_remove);
                items[1]=context.getString(R.string.write_encrypted_message_cardview_cancel);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals(items[0])) {
                            userInfos.remove(positioncheck);
                            notifyItemRemoved(positioncheck);
                            notifyItemRangeChanged(positioncheck, userInfos.size());
                            check_Position = positioncheck;
                        } else if (items[which].equals(items[1])) {

                            dialog.dismiss();

                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_write_encrypted_message_list;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_write_encrypted_message_list = itemView.findViewById(R.id.textView_write_encrypted_message_list);
            list_cardview = itemView.findViewById(R.id.cardview_write_encrypted_message_list);

        }
    }

    public static boolean isControlModification() {
        return controlModification;
    }

    public static int getCheck_Position() {
        return check_Position;
    }
}
