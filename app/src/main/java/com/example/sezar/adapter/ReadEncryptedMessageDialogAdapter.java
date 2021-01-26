package com.example.sezar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sezar.R;
import com.example.sezar.activity.ReadEncryptedMessage;
import com.example.sezar.fragment.Fragment_Read_Encrypted_Message;
import com.example.sezar.model.AboutUsItem;
import com.ortiz.touchview.TouchImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ReadEncryptedMessageDialogAdapter extends RecyclerView.Adapter<ReadEncryptedMessageDialogAdapter.ViewHolder> {
    private Context context;
    private List<Bitmap> userInfos;
    private static boolean controlModification = false;
    private static int check_Position;
    private ScaleGestureDetector mScaleGestureDetector;
    CardView list_cardview;
    private TextToSpeech textToSpeech;

    public ReadEncryptedMessageDialogAdapter(Context context, List<Bitmap> userinfos) {
        this.context = context;
        this.userInfos = userinfos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.read_encrypted_message_dialog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final int positioncheck;
        final Bitmap userInfo = userInfos.get(position);

        viewHolder.imageView_read_encrypted_message_dialog.setImageBitmap(userInfo);
      /*  if(!Fragment_Read_Encrypted_Message.getDecryptedLocationValue().isEmpty()){
            if (position==0&&(!Fragment_Read_Encrypted_Message.getDecryptedLocationValue().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location"))) {
                ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());

            }
        }else if(!Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().isEmpty()){
            if (position==0&&(!Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location"))) {
                ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());

            }else{
                if(position==0){
                    ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                    viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                    viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                }
            }
        }*/
        if (checkNetworkAccess()) {


            if (!Fragment_Read_Encrypted_Message.getFileContainer().isEmpty()) {

                if (userInfos.size() > 2) {
                    if (position == 0 || position == 1) {
                        ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                        viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                        viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                    }
                } else if (!Fragment_Read_Encrypted_Message.getFileContainer().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Image") || position == 0) {
                    ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                    viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                    viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                } else if (!Fragment_Read_Encrypted_Message.getFileContainer().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Image") && position == 1) {
                    ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                    viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                    viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                }

                /*if (Fragment_Read_Encrypted_Message.getFileContainer().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Image") && position == 1) {
                    ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                    viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                    viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                }*/
            }


        } else {
            if (!Fragment_Read_Encrypted_Message.getFileContainerLocal().isEmpty()) {

                if (userInfos.size() > 2) {
                    if (position == 0 || position == 1) {
                        ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                        viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                        viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                    }

                } else if (!Fragment_Read_Encrypted_Message.getFileContainerLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Image") || position == 0) {
                    ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                    viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                    viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                } else if (!Fragment_Read_Encrypted_Message.getFileContainerLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Image") && position == 1) {
                    ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                    viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                    viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                }
               /* if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Image") && position == 1) {
                    ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(layoutParams);
                    viewHolder.imageView_read_encrypted_message_dialog.setRotation(0);
                    viewHolder.imageView_read_encrypted_message_dialog.setMaxZoom(viewHolder.imageView_read_encrypted_message_dialog.getMinZoom());
                }*/
            }


        }


    /*    if(position == 0 &&!Fragment_Read_Encrypted_Message.getDecryptedLocationValue().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location")){
            viewHolder.imageView_read_encrypted_message_dialog_location.setImageBitmap(userInfo);
            viewHolder.imageView_read_encrypted_message_dialog_location.setVisibility(View.VISIBLE);
            viewHolder.imageView_read_encrypted_message_dialog.setVisibility(View.INVISIBLE);

        } else if (position == 0 &&!Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location")) {
            viewHolder.imageView_read_encrypted_message_dialog_location.setImageBitmap(userInfo);
            viewHolder.imageView_read_encrypted_message_dialog_location.setVisibility(View.VISIBLE);
            viewHolder.imageView_read_encrypted_message_dialog.setVisibility(View.INVISIBLE);

        }else{
            viewHolder.imageView_read_encrypted_message_dialog.setImageBitmap(userInfo);
            viewHolder.imageView_read_encrypted_message_dialog_location.setVisibility(View.INVISIBLE);
        }*/

      /*  try {
            viewHolder.imageView_read_encrypted_message_dialog.setAdjustViewBounds(true);

           // LinearLayout.LayoutParams imageParameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            //viewHolder.imageView_read_encrypted_message_dialog.setLayoutParams(imageParameters);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        viewHolder.imageView_read_encrypted_message_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetworkAccess()) {
                    if (userInfos.size() > 2) {
                        if (position == 0) {
                            googleMapsIntent();
                        } else if (position == 1) {
                            textToSpeech();
                        }
                    } else {
                        if (position == 0 && Fragment_Read_Encrypted_Message.getFileContainer().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location")) {
                            googleMapsIntent();
                        } else if (position == 1 && Fragment_Read_Encrypted_Message.getFileContainer().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location")) {
                            textToSpeech();
                        } else {
                            if (position == 0) {
                                textToSpeech();
                            } else if (Fragment_Read_Encrypted_Message.getFileContainer().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Video")) {
                                ReadEncryptedMessageAdapter.startVideo();

                            } else if (Fragment_Read_Encrypted_Message.getFileContainer().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Audio")) {
                                ReadEncryptedMessageAdapter.startAudio();
                            }
                        }
                    }
                } else {

                    if (userInfos.size() > 2) {
                        if (position == 0) {
                            googleMapsIntent();
                        } else if (position == 1) {
                            textToSpeech();
                        }
                    } else {
                        if (position == 0 && Fragment_Read_Encrypted_Message.getFileContainerLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location")) {
                            googleMapsIntent();
                        } else if (position == 1 && Fragment_Read_Encrypted_Message.getFileContainerLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location")) {
                            textToSpeech();
                        } else {
                            if (position == 0) {
                                textToSpeech();
                            } else if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Video")) {
                                ReadEncryptedMessageAdapter.startVideo();

                            } else if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Audio")) {
                                ReadEncryptedMessageAdapter.startAudio();
                            }
                        }
                    }
                }

            }
        });
        /*if ((position!=0&&!Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location"))||(position==0&&Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber().equals("Location"))) {
            viewHolder.imageView_read_encrypted_message_dialog.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return mScaleGestureDetector.onTouchEvent(motionEvent);
                    //   return false;
                }
            });

        }*/


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
       /* list_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.add_user_permission_dialog));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals(items[0])) {
                            check_Position = positioncheck;
                            Fragment_Add_Permission fragment_add_permission = Fragment_Add_Permission.newInstance("Fragment_Add_Role");
                            controlModification = true;
                            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_add_permission).commit();
                        } else if (items[which].equals(items[1])) {
                            myDb = new DBAddPermissionHelper(context);
                            userInfos.remove(positioncheck);
                            notifyItemRemoved(positioncheck);
                            notifyItemRangeChanged(positioncheck, userInfos.size());
                            myDb.deleteEntry(userInfo.getId());


                        } else if (items[which].equals(items[2])) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TouchImageView imageView_read_encrypted_message_dialog;

        //ImageView imageView_read_encrypted_message_dialog_location;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_read_encrypted_message_dialog = itemView.findViewById(R.id.imageView_read_encrypted_message_dialog);

            // imageView_read_encrypted_message_dialog_location=itemView.findViewById(R.id.imageView_read_encrypted_message_dialog_location);

        }
    }

    public static boolean isControlModification() {
        return controlModification;
    }

    public static int getCheck_Position() {
        return check_Position;
    }

    private void googleMapsIntent() {

        try {
            Uri gmmIntentUri;
            if (checkNetworkAccess()) {
                gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValue().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber());
            } else {
                gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber());
            }

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context,R.string.forgot_password_check_network_access_fail,Toast.LENGTH_SHORT).show();
            gpsIntent();
        }
    }

    private void gpsIntent() {
        String uri;
        if (checkNetworkAccess()) {
            uri = "https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValue().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber();
        } else {
            uri = "https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber();
        }

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
        context.startActivity(Intent.createChooser(sharingIntent, "Share in..."));
    }


    private boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            // Toast.makeText(getContext(), getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void textToSpeech() {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                int check = 0;
                Locale locale;
                if (i == TextToSpeech.SUCCESS) {

                    if (Locale.getDefault().getDisplayLanguage().equals("en")) {
                        locale = new Locale("en");
                        check = textToSpeech.setLanguage(locale);

                    } else {
                        locale = new Locale("tr", "TR");
                        check = textToSpeech.setLanguage(locale);
                    }
                    //textToSpeech.setLanguage(Locale.ENGLISH);

                    if (check == TextToSpeech.LANG_MISSING_DATA || check == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, context.getString(R.string.text_to_speech_error), Toast.LENGTH_SHORT).show();
                    }

                    if (checkNetworkAccess()) {

                        check = textToSpeech.speak(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber(), TextToSpeech.QUEUE_FLUSH, null);
                        //textToSpeech.shutdown();

                    } else {
                        check = textToSpeech.speak(Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber(), TextToSpeech.QUEUE_FLUSH, null);
                       // textToSpeech.shutdown();
                    }

                    if (check == TextToSpeech.ERROR) {
                        Toast.makeText(context, context.getString(R.string.text_to_speech_error), Toast.LENGTH_SHORT).show();
                    } else if (check == TextToSpeech.LANG_MISSING_DATA || check == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, context.getString(R.string.text_to_speech_error), Toast.LENGTH_SHORT).show();
                    }
                }


            }


        });

    }


}

