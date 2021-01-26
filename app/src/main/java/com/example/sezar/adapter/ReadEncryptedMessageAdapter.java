package com.example.sezar.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sezar.BuildConfig;
import com.example.sezar.R;
import com.example.sezar.activity.ForgotPassword;
import com.example.sezar.activity.LoginScreen;
import com.example.sezar.activity.MainActivity;
import com.example.sezar.activity.ReadEncryptedMessage;
import com.example.sezar.database.DBLoginScreenCheckBoxHelper;
import com.example.sezar.database.DBReadMessageFilePathContainerHelper;
import com.example.sezar.encryption_algorithms.AES;
import com.example.sezar.encryption_algorithms.CaesarsCiphering;
import com.example.sezar.encryption_algorithms.RandomStringGenerator;
import com.example.sezar.fragment.Fragment_Read_Encrypted_Message;
import com.example.sezar.fragment.Fragment_Write_Encrypted_Message;
import com.example.sezar.imageProcess.Classifier;
import com.example.sezar.model.ReadEncryptedMessageItem;
import com.example.sezar.model.ReadMessageItemFilePath;
import com.example.sezar.tokenize.Tokenize;
import com.example.sezar.utility.HelperMaps;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import edu.stanford.nlp.ling.tokensregex.Env;

public class ReadEncryptedMessageAdapter extends RecyclerView.Adapter<ReadEncryptedMessageAdapter.ViewHolder> {
    private Context context;
    private static Context staticContext;
    private ArrayList<ReadEncryptedMessageItem> userInfos;
    private ArrayList<ReadEncryptedMessageItem> restartuserInfos;
    private ArrayList<ReadEncryptedMessageItem> restartuserInfosDelete;
    private ArrayList<ReadEncryptedMessageItem> clearUserInfos;
    private static List<Bitmap> dialogImages;
    private List<String> userdata;
    private List<String> readData;
    private DatabaseReference myRef;
    private static StorageReference myStorageRef;
    private FirebaseDatabase database;
    DatabaseReference myKey2;
    private DatabaseReference myKey;
    ArrayList<ReadEncryptedMessageItem> readMessageState;
    private int listCounter;
    private int readListCounter;
    private String[] items = {"a", "b", "c"};
    private static boolean controlModification = false;
    private static boolean controlDeleteList = false;
    private int check_Position;
    private static int deleteCounter;
    private CardView list_cardview;
    private boolean deleteList = false;
    private boolean updateReadList = false;
    private boolean deleteListControl = false;
    private ReadEncryptedMessageItem userInfo;
    DBLoginScreenCheckBoxHelper myDb;
    private String readStateTrue = "true";
    private int databaseCounter = 0;
    private int readCounter = 0;
    private int contactCounter = 0;
    private ImageView image;
    private ImageView audio;
    private ImageView video;
    private ImageView location;
    private ImageView speech;
    private static MediaPlayer mPlayer;
    private static boolean checkAudio;
    private static boolean checkVideo;
    private static boolean checkDocument;
    private boolean checkLocation;
    private static androidx.appcompat.app.AlertDialog.Builder builderVideo;
    private static VideoView videoView;
    private RecyclerView recyclerView_dialog;
    private AlertDialog builderDialogCreate;
    private static AlertDialog builderDialogCreateFile;
    private AlertDialog.Builder builderDialog;
    private static int checkPosition;
    private ReadEncryptedMessageDialogAdapter dialogAdapter;
    private static String progressTitle;
    private AsyncTask mTask;
    private TextToSpeech textToSpeech;
    private View builderDialogView;
    private ImageView imageDialogSpeech;
    private ImageView imageDialogImage;
    private ImageView imageDialogPlaceHolder;
    private ImageView imageDialogTranslate;
    private static Integer statusDownloadedFile;
    private static Long statusDownloadFileLong;
    private AsyncTask asyncTaskFile;
    private AsyncTask asyncTaskTextToSpeech;
    private static DBReadMessageFilePathContainerHelper mydbReadMessageFilePathContainerHelper;
    private static Cursor cursorFilePath;
    private static View builderDialogViewFileLocal;
    private static AlertDialog.Builder builderDialogFileLocal;
    private static TextView textViewFileLocal;
    private static String keySecret;
    private boolean controlException;
    private Bitmap imageClassificationBitmap;
    private List<Classifier.Recognition> resultsClassification;
    private Bitmap bitmapLocal;
    private Snackbar snackbar;
    private View view;
    private Snackbar asyncImageTaskSnackbar;
    private FirebaseTranslatorOptions options;
    private FirebaseTranslator englishTurkishTranslator = null;
    private View builderDialogViewTranslator;
    private AlertDialog.Builder builderDialogTranslator;
    private TextView textViewTranslator;
    private ProgressBar progressBarTranslator;


    public ReadEncryptedMessageAdapter(Context context, ArrayList<ReadEncryptedMessageItem> userinfos) {
        this.context = context;
        this.userInfos = userinfos;
        staticContext = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.read_encrypted_message_item, parent, false);
        progressTitle = view.getResources().getString(R.string.forgot_password_wait);
        //builderDialogView=LayoutInflater.from(parent.getContext()).inflate(R.layout.read_encrypted_message_alert_dialog_item,parent,false);
        /*if(Fragment_Read_Encrypted_Message.isControlDelete()){
            Fragment_Read_Encrypted_Message.setControlDelete(false);
            userInfos.clear();
            notifyDataSetChanged();
        }*/
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        contactCounter = 0;
        userInfo = userInfos.get(position);

        viewHolder.textView_phone_number_list.setText(userInfo.getPhoneNumber());
        if (checkNetworkAccess()) {
            if (Fragment_Read_Encrypted_Message.getReadMessageState().size() > position) {
                if (Fragment_Read_Encrypted_Message.getReadMessageState().get(position).getPhoneNumber().equals("true") && !updateReadList) {

                    viewHolder.textView_phone_number_list.setText(userInfo.getPhoneNumber().concat("(").concat(context.getString(R.string.read_encrypted_message_read)).concat(")"));
                    // list_cardview.setCardBackgroundColor(Color.BLACK);
                }
            }

            if (updateReadList) {
                if (restartuserInfos.size() > position) {
                    if (restartuserInfos.get(position).getPhoneNumber().equals("true")) {
                        viewHolder.textView_phone_number_list.setText(userInfo.getPhoneNumber().concat("(").concat(context.getString(R.string.read_encrypted_message_read)).concat(")"));
                        // list_cardview.setCardBackgroundColor(Color.BLACK);
                        // userInfos.addAll(restartuserInfos);
                    }
                }

            }
        } else {
            if (Fragment_Read_Encrypted_Message.getReadMessageStateLocal().size() > position) {
                if (Fragment_Read_Encrypted_Message.getReadMessageStateLocal().get(position).getPhoneNumber().equals("true") && !updateReadList) {
                    viewHolder.textView_phone_number_list.setText(userInfo.getPhoneNumber().concat("(").concat(context.getString(R.string.read_encrypted_message_read)).concat(")"));
                    // list_cardview.setCardBackgroundColor(Color.BLACK);
                }
            }

            if (updateReadList) {
                if (restartuserInfos.size() > position) {
                    if (restartuserInfos.get(position).getPhoneNumber().equals("true")) {
                        viewHolder.textView_phone_number_list.setText(userInfo.getPhoneNumber().concat("(").concat(context.getString(R.string.read_encrypted_message_read)).concat(")"));
                        // list_cardview.setCardBackgroundColor(Color.BLACK);
                        // userInfos.addAll(restartuserInfos);
                    }
                }

            }
        }


    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_phone_number_list;
        Bitmap bitmap;


        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            textView_phone_number_list = itemView.findViewById(R.id.textView_read_encrypted_message_phone_number);
            list_cardview = itemView.findViewById(R.id.cardview_read_encrypted_message_list);
            image = itemView.findViewById(R.id.imageView_image_value_place_holder);
            audio = itemView.findViewById(R.id.imageView_record_value_place_holder);
            video = itemView.findViewById(R.id.imageView_video_value_place_holder);
            location = itemView.findViewById(R.id.imageView_location_value_place_holder);
            videoView = itemView.findViewById(R.id.videoView_video_value_place_holder);
            speech = itemView.findViewById(R.id.imageView_speech_value_place_holder);
            recyclerView_dialog = itemView.findViewById(R.id.recyclerView_dialog);

            list_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    builderDialog = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                    builderDialogView = LayoutInflater.from(context).inflate(R.layout.read_encrypted_message_alert_dialog_item, null);

                    imageDialogImage = builderDialogView.findViewById(R.id.imageView_read_encrypted_message_dialog);
                    imageDialogPlaceHolder = builderDialogView.findViewById(R.id.imageView_read_encrypted_message_dialog_placeholder);
                    imageDialogSpeech = builderDialogView.findViewById(R.id.imageView_read_encrypted_message_dialog_speech);
                    imageDialogTranslate = builderDialogView.findViewById(R.id.imageView_read_Encrypted_message_dialog_translate);


                    imageDialogSpeech.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Handler handler = new Handler();
                            handler.post(
                                    () -> {
                                        textToSpeech();

                                    });

                        }
                    });

                    imageDialogTranslate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Handler handler = new Handler();
                            handler.post(
                                    () -> {
                                        IdentifyLanguage();
                                    });

                        }
                    });

                    imageDialogImage.setVisibility(View.INVISIBLE);
                    imageDialogPlaceHolder.setVisibility(View.INVISIBLE);

                    if (mTask != null) {
                        mTask.cancel(true);
                    }
                    check_Position = getAdapterPosition();
                    checkPosition = getAdapterPosition();
                    checkAudio = false;
                    checkVideo = false;
                    checkDocument = false;
                    checkLocation = false;
                    //recyclerView_dialog.setVisibility(View.INVISIBLE);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                    builder.setTitle(context.getString(R.string.read_encrypted_message_title_encrypted));
                    items[0] = context.getString(R.string.read_encrypted_message_title_cipher);
                    items[1] = context.getString(R.string.read_encrypted_message_title_delete);
                    items[2] = context.getString(R.string.write_encrypted_message_cardview_cancel);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            controlException = false;
                            if (items[which].equals(items[0])) {
                                String filterMessage;
                                mTask = null;
                                readCounter = 0;
                                updateReadList = false;
                                readListCounter = 0;
                                contactCounter = 0;


                                dialogImages = new ArrayList<>();

                                if (!dialogImages.isEmpty()) {
                                    dialogImages.clear();
                                }

                                if (checkNetworkAccess()) {
                                    try {
                                        if (Fragment_Read_Encrypted_Message.getDecryptedLocationValue().size() > 0 && !Fragment_Read_Encrypted_Message.getDecryptedLocationValue().get(getAdapterPosition()).getPhoneNumber().equals("Location")) {
                                            checkLocation = true;
                                        }

                                        if (Fragment_Read_Encrypted_Message.getDecryptedImageValue().size() > 0 & Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Image")) {
                                            Picasso.get().load(Fragment_Read_Encrypted_Message.getDecryptedImageValue().get(getAdapterPosition()).getPhoneNumber()).into(image, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    Bitmap bm = null;
                                                    builderDialogCreate.dismiss();
                                                    asyncImageTaskSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
                                                    View asyncImageTaskSnackBarView = asyncImageTaskSnackbar.getView();
                                                    asyncImageTaskSnackBarView.setBackgroundColor(context.getColor(R.color.button_background));
                                                    asyncImageTaskSnackbar.setActionTextColor(context.getColor(R.color.white));
                                                    mTask = new AsyncReadEncryptedMessagePhoneNumberAdapter(context, dialogAdapter, imageDialogImage, items[0], asyncImageTaskSnackbar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    e.printStackTrace();
                                                }
                                            });


                                        } else if (Fragment_Read_Encrypted_Message.getDecryptedAudioValue().size() > 0 && Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Audio")) {
                                            checkAudio = true;
                                        } else if (Fragment_Read_Encrypted_Message.getDecryptedVideoValue().size() > 0 && Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Video")) {
                                            checkVideo = true;
                                        } else if (Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().size() > 0 && Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Document")) {
                                            checkDocument = true;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                        controlException = true;
                                        Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();

                                    }
                                } else {
                                    try {
                                        if (Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().size() > 0 && !Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(getAdapterPosition()).getPhoneNumber().equals("Location")) {
                                            checkLocation = true;
                                        }

                                        if (Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().size() > 0 & Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Image")) {
                                            bitmapLocal = BitmapFactory.decodeByteArray(Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().get(getAdapterPosition()).getImageValue(), 0, Fragment_Read_Encrypted_Message.getDecryptedImageValueLocal().get(getAdapterPosition()).getImageValue().length);
                                            imageDialogImage.setImageBitmap(bitmapLocal);
                                            imageDialogImage.setVisibility(View.VISIBLE);
                                            processImage();

                                        } else if (Fragment_Read_Encrypted_Message.getDecryptedAudioValueLocal().size() > 0 && Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Audio")) {
                                            checkAudio = true;

                                        } else if (Fragment_Read_Encrypted_Message.getDecryptedVideoValueLocal().size() > 0 && Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Video")) {
                                            checkVideo = true;

                                        } else if (Fragment_Read_Encrypted_Message.getDecryptedDocumentValueLocal().size() > 0 && Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Document")) {
                                            checkDocument = true;

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                        controlException = true;
                                        Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                    }
                                }


                                readData = new ArrayList<>();
                                restartuserInfos = new ArrayList<>();
                                clearUserInfos = new ArrayList<>();

                                clearUserInfos.addAll(userInfos);

                                restartuserInfos.addAll((Fragment_Read_Encrypted_Message.getReadMessageState()));


                                //Collections.reverse(restartuserInfos);

                                Iterator<ReadEncryptedMessageItem> checkUser = userInfos.iterator();
                                while (checkUser.hasNext()) {
                                    readData.add(userInfos.get(readListCounter).getPhoneNumber());
                                    readListCounter++;
                                    if (userInfos.size() == readListCounter) {
                                        //userInfos.clear();
                                        break;
                                    }
                                }
                                dialog.dismiss();

                                builderDialog.setTitle(context.getString(R.string.read_encrypted_message_title_decrypted_message));


                                if (checkNetworkAccess()) {
                                    try {
                                        if (!Fragment_Read_Encrypted_Message.getContactPhoneNumber().isEmpty()) {
                                            Iterator<String> checkUserName = Fragment_Read_Encrypted_Message.getContactPhoneNumber().iterator();
                                            while (checkUserName.hasNext()) {
                                                if (Fragment_Read_Encrypted_Message.getDecryptedPhoneNumber().get(getAdapterPosition()).getPhoneNumber().equals(Fragment_Read_Encrypted_Message.getContactPhoneNumber().get(contactCounter))) {
                                                    if (MainActivity.controlSafetyFilter) {
                                                        Tokenize tokenize = new Tokenize();
                                                        filterMessage = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                        items[0] = Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter) + ":" + filterMessage;
                                                    } else {
                                                        items[0] = Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter) + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber();
                                                    }
                                                    //viewHolder.textView_phone_number_list.setText(Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter));

                                                    break;
                                                } else {
                                                    if (MainActivity.controlSafetyFilter) {
                                                        Tokenize tokenize = new Tokenize();
                                                        filterMessage = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                        items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumber().get(getAdapterPosition()).getPhoneNumber() + ":" + filterMessage;
                                                    } else {
                                                        items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumber().get(getAdapterPosition()).getPhoneNumber() + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber();
                                                    }

                                                }
                                                contactCounter++;
                                                if (Fragment_Read_Encrypted_Message.getContactPhoneNumber().size() == contactCounter) {
                                                    break;
                                                }
                                            }

                                        } else {
                                            try {
                                                if (!Fragment_Read_Encrypted_Message.getContactUserName().isEmpty()) {
                                                    if (MainActivity.controlSafetyFilter) {
                                                        Tokenize tokenize = new Tokenize();
                                                        filterMessage = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                        items[0] = Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter) + ":" + filterMessage;
                                                    } else {
                                                        items[0] = Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter) + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber();
                                                    }
                                                } else {
                                                    if (MainActivity.controlSafetyFilter) {
                                                        Tokenize tokenize = new Tokenize();
                                                        filterMessage = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                        items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumber().get(getAdapterPosition()).getPhoneNumber() + ":" + filterMessage;
                                                    } else {
                                                        items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumber().get(getAdapterPosition()).getPhoneNumber() + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                dialog.dismiss();
                                                controlException = true;
                                                Tokenize tokenize = new Tokenize();
                                                items[0] = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                builderDialog.setMessage(items[0]);
                                                builderDialogCreate = builderDialog.create();
                                                builderDialogCreate.dismiss();
                                                Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                            }

                                            //viewHolder.textView_phone_number_list.setText(Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter));

                                        }


                                        contactCounter++;
                                        if (Fragment_Read_Encrypted_Message.getContactPhoneNumber().size() == contactCounter) {
                                        }
                                        if (checkLocation) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_add_location_black_24dp));
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    googleMapsIntent();
                                                }
                                            });

                                        }

                                        if (Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Image")) {
                                            //imageDialogImage.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                        } else if (Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Audio")) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_adjust_black_24dp));
                                            imageDialogImage.setVisibility(View.GONE);
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startAudio();
                                                }
                                            });
                                        } else if (Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Video")) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_ondemand_video_black_24dp));
                                            imageDialogImage.setVisibility(View.GONE);
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startVideo();
                                                }
                                            });
                                        } else if (Fragment_Read_Encrypted_Message.getFileContainer().get(getAdapterPosition()).getPhoneNumber().equals("Document")) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_file_download_black_24dp));
                                            imageDialogImage.setVisibility(View.GONE);
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    try {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                            openFile(staticContext, new File(Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(check_Position).getPhoneNumber()));
                                                        } else {
                                                            openFile(staticContext, new File(Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(check_Position).getPhoneNumber()));
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        } else {
                                            imageDialogImage.setVisibility(View.GONE);
                                            if (!checkLocation) {
                                                imageDialogPlaceHolder.setVisibility(View.GONE);
                                            }
                                            builderDialog.setView(builderDialogView);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                        controlException = true;
                                        Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                    }
                                } else {
                                    if (!Fragment_Read_Encrypted_Message.getContactPhoneNumber().isEmpty()) {
                                        try {
                                            Iterator<String> checkUserName = Fragment_Read_Encrypted_Message.getContactPhoneNumber().iterator();
                                            while (checkUserName.hasNext()) {
                                                if (Fragment_Read_Encrypted_Message.getDecryptedPhoneNumberLocal().get(getAdapterPosition()).getPhoneNumber().equals(Fragment_Read_Encrypted_Message.getContactPhoneNumber().get(contactCounter))) {

                                                    if (MainActivity.controlSafetyFilter) {
                                                        Tokenize tokenize = new Tokenize();
                                                        filterMessage = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                        items[0] = Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter) + ":" + filterMessage;
                                                    } else {
                                                        items[0] = Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter) + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber();
                                                    }
                                                    //viewHolder.textView_phone_number_list.setText(Fragment_Read_Encrypted_Message.getContactUserName().get(contactCounter));

                                                    break;
                                                } else {
                                                    if (MainActivity.controlSafetyFilter) {
                                                        Tokenize tokenize = new Tokenize();
                                                        filterMessage = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                        items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumberLocal().get(getAdapterPosition()).getPhoneNumber() + ":" + filterMessage;
                                                    } else {
                                                        items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumberLocal().get(getAdapterPosition()).getPhoneNumber() + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber();
                                                    }

                                                }
                                                contactCounter++;
                                                if (Fragment_Read_Encrypted_Message.getContactPhoneNumber().size() == contactCounter) {
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            dialog.dismiss();
                                            controlException = true;
                                            Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                        }
                                    } else {
                                        try {
                                            if (MainActivity.controlSafetyFilter) {
                                                Tokenize tokenize = new Tokenize();
                                                filterMessage = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumberLocal().get(getAdapterPosition()).getPhoneNumber() + ":" + filterMessage;
                                            } else {
                                                items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumberLocal().get(getAdapterPosition()).getPhoneNumber() + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
//                                            dialog.dismiss();
                                            controlException = true;
                                            Tokenize tokenize = new Tokenize();
                                            if (MainActivity.controlSafetyFilter) {
                                                if (Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().size() > 0) {
                                                    items[0] = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                } else if (Fragment_Read_Encrypted_Message.getDecryptedMessages().size() > 0) {
                                                    items[0] = tokenize.safeMessage(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(getAdapterPosition()).getPhoneNumber(), controlException);
                                                }
                                            } else {

                                                items[0] = Fragment_Read_Encrypted_Message.getDecryptedPhoneNumberLocal().get(getAdapterPosition()).getPhoneNumber() + ":" + Fragment_Read_Encrypted_Message.getDecryptedMessagesLocal().get(getAdapterPosition()).getPhoneNumber();
                                            }


                                            builderDialog.setMessage(items[0]);
                                            builderDialogCreate = builderDialog.create();
//                                            builderDialogCreate.dismiss();
//                                            Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
//                                            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                        }


                                    }

                                    try {
                                        if (checkLocation) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_add_location_black_24dp));
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    googleMapsIntent();
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                        Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                    }

                                    try {
                                        if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Image")) {
                                            //   imageDialogImage.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                        } else if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Audio")) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_adjust_black_24dp));
                                            imageDialogImage.setVisibility(View.GONE);
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startAudio();
                                                }
                                            });
                                        } else if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Video")) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_ondemand_video_black_24dp));
                                            imageDialogImage.setVisibility(View.GONE);
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    startVideo();
                                                }
                                            });
                                        } else if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(getAdapterPosition()).getPhoneNumber().equals("Document")) {
                                            imageDialogPlaceHolder.setImageDrawable(context.getDrawable(R.drawable.ic_file_download_black_24dp));
                                            imageDialogImage.setVisibility(View.GONE);
                                            imageDialogPlaceHolder.setVisibility(View.VISIBLE);
                                            builderDialog.setView(builderDialogView);
                                            imageDialogPlaceHolder.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    try {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                            dialog.dismiss();
                                                            builderDialogCreate.dismiss();
                                                            openFile(context, new File(Fragment_Read_Encrypted_Message.getDecryptedDocumentValueLocal().get(check_Position).getPhoneNumber()));
                                                        } else {
                                                            dialog.dismiss();
                                                            builderDialogCreate.dismiss();
                                                            openFile(context, new File(Fragment_Read_Encrypted_Message.getDecryptedDocumentValueLocal().get(check_Position).getPhoneNumber()));
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        dialog.dismiss();
                                                        Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();

                                                    }
                                                }
                                            });
                                        } else {
                                            imageDialogImage.setVisibility(View.GONE);
                                            if (!checkLocation) {
                                                imageDialogPlaceHolder.setVisibility(View.GONE);
                                            }
                                            builderDialog.setView(builderDialogView);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                        Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                    }

                                }


                                builderDialog.setMessage(items[0]);
                                builderDialogCreate = builderDialog.create();

                                if (builderDialogCreateFile != null) {
                                    if (builderDialogCreateFile.isShowing()) {
                                        builderDialogCreate.dismiss();
                                    }
                                } else {
                                    builderDialogCreate.show();
                                }


                                if (controlException && Fragment_Read_Encrypted_Message.isControlInternetState()) {
                                    builderDialogCreate.dismiss();
                                    Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                }
                                if (mTask != null) {
                                    builderDialogCreate.dismiss();
                                }
                                if (checkNetworkAccess()) {
                                    updateReadState();
                                }
                            }
                            if (items[which].equals(items[1])) {
                                listCounter = 0;
                                deleteList = false;
                                deleteListControl = false;
                                userdata = new ArrayList<>();
                                Iterator<ReadEncryptedMessageItem> checkUser = userInfos.iterator();
                                while (checkUser.hasNext()) {
                                    userdata.add(userInfos.get(listCounter).getPhoneNumber());
                                    listCounter++;
                                    if (userInfos.size() == listCounter) {
                                        break;
                                    }
                                }
                                check_Position = getAdapterPosition();
                                deleteDatabase();
                            } else if (items[which].equals(items[2])) {
                                dialog.dismiss();
                            }
                        }
                    });
                    if (builderDialogCreateFile != null) {
                        if (builderDialogCreateFile.isShowing()) {
                            builderDialogCreate.dismiss();

                        }
                    } else {
                        builder.show();
                    }


                }
            });

        }

    }

    private void setRecyclerView() {
        boolean checkLocationDuplicate = false;

        Drawable drawable = null;


        if (checkLocation) {
            checkLocationDuplicate = true;
           /*drawable=(Drawable) location.getDrawable();
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            dialogImages.add(bitmap);*/

        }
        if (checkNetworkAccess()) {
            if (!Fragment_Read_Encrypted_Message.getFileContainer().isEmpty()) {
                if (Fragment_Read_Encrypted_Message.getFileContainer().get(check_Position).getPhoneNumber().equals("Video")) {
                    drawable = (Drawable) video.getDrawable();
                } else if (Fragment_Read_Encrypted_Message.getFileContainer().get(check_Position).getPhoneNumber().equals("Audio")) {
                    drawable = (Drawable) audio.getDrawable();
                }
            }

        } else {
            if (!Fragment_Read_Encrypted_Message.getFileContainerLocal().isEmpty()) {
                if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(check_Position).getPhoneNumber().equals("Video")) {
                    drawable = (Drawable) video.getDrawable();
                } else if (Fragment_Read_Encrypted_Message.getFileContainerLocal().get(check_Position).getPhoneNumber().equals("Audio")) {
                    drawable = (Drawable) audio.getDrawable();
                }
            }

        }

        if (drawable != null && !checkLocationDuplicate) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            dialogImages.add(bitmap);
        }

        dialogAdapter = new ReadEncryptedMessageDialogAdapter(context, ReadEncryptedMessageAdapter.getDialogImages());
        LinearLayoutManager layoutmanager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ;

        layoutmanager.setReverseLayout(true);
        layoutmanager.setStackFromEnd(true);
        recyclerView_dialog.setLayoutManager(layoutmanager);
        recyclerView_dialog.setAdapter(dialogAdapter);
        if (recyclerView_dialog.getParent() != null) {
            ((ViewGroup) recyclerView_dialog.getParent()).removeView(recyclerView_dialog);
        }
        // builder.setView(recyclerView_dialog);
        recyclerView_dialog.setVisibility(View.VISIBLE);
    }


    public static boolean isControlModification() {
        return controlModification;
    }


    private void deleteDatabase() {
        controlDeleteList = false;
        deleteList = false;
        deleteCounter = 0;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot firstSnapshot : dataSnapshot.getChildren()) {

                    String userName = firstSnapshot.child("Username").getValue(String.class);
                    String keys = firstSnapshot.getKey();

                    if (keys.equals(LoginScreen.userKey())) {
                        if (deleteList) {
                            deleteList = false;
                            //Fragment_Read_Encrypted_Message.getDecryptedMessages().remove(check_Position);
                            break;
                        }
                        myKey = database.getReference().child(firstSnapshot.getKey());
                        myKey.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (deleteList) {
                                            break;
                                        }
                                        String getKey = postSnapshot.child("Encrypted Message").getValue(String.class);
                                        if (getKey != null && getKey.equals((userdata.get(check_Position)))) {
                                            deleteList = true;
                                            controlDeleteList = true;
                                            myKey.child(postSnapshot.getKey()).removeValue();
                                            userInfos.remove(check_Position);
                                            Fragment_Read_Encrypted_Message.setUpdateDeleteAllList(userInfos);
                                            notifyDataSetChanged();
                                            break;
                                        }
                                        /*deleteCounter++;
                                        if (userdata.size() == deleteCounter) {
                                            break;
                                        }*/

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                }
                                myKey.removeEventListener(this);
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
                myRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateReadState() {
        databaseCounter = 0;
        updateReadList = false;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot firstSnapshot : dataSnapshot.getChildren()) {
                    String userName = firstSnapshot.child("Username").getValue(String.class);
                    String keys = firstSnapshot.getKey();

                    if (updateReadList) {
                        break;
                    }

                    if (keys.equals(LoginScreen.userKey())) {

                        myKey = database.getReference().child(firstSnapshot.getKey());
                        myKey.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (updateReadList) {

                                            //readData.remove(check_Position);
                                            //updateReadList=false;
                                            break;
                                        }

                                        //String getKey = postSnapshot.child("Read State").getValue(String.class);
                                        String getKey = postSnapshot.child("Encrypted Message").getValue(String.class);
                                        String getReadState = postSnapshot.child("Read State").getValue(String.class);

                                        //restartuserInfos.add(new ReadEncryptedMessageItem(getReadState));


                                        if (!updateReadList) {

                                            if (getKey != null && (getKey.equals(readData.get(check_Position)))) {
                                                updateReadList = true;
                                                myKey.child(postSnapshot.getKey()).child("Read State").setValue(readStateTrue);
                                                restartuserInfos.remove(check_Position);
                                                restartuserInfos.add(check_Position, new ReadEncryptedMessageItem(readStateTrue));
                                                if (Fragment_Read_Encrypted_Message.getReadMessageState().size() > check_Position) {
                                                    Fragment_Read_Encrypted_Message.getReadMessageState().remove(check_Position);
                                                    Fragment_Read_Encrypted_Message.getReadMessageState().add(check_Position, new ReadEncryptedMessageItem(readStateTrue));
                                                }

                                                notifyDataSetChanged();
                                                break;


                                                // viewHolder.textView_phone_number_list.setText(userInfo.getPhoneNumber().concat("(").concat(context.getString(R.string.read_encrypted_message_read)).concat(")"));
                                            }
                                        }


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Fragment_Read_Encrypted_Message fragment_read_encrypted_message = Fragment_Read_Encrypted_Message.newInstance("Fragment_Read_Encrypted_Message");
                                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment_read_encrypted_message).commit();
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        myKey.removeEventListener(this);

                    }

                }
                myRef.removeEventListener(this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void openFile(Context context, File url) throws IOException {
        // Create URI
        Uri uri;
        File file = url;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(url.toString()));
        } else {
            uri = Uri.fromFile(file);
        }
        if (checkNetworkAccess()) {
            asyncTaskFile = new AsyncFileTask(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            downloadFileFromUrlLocal();
        }


        //downloadFileFromUrl();

        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
//        if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".doc") || url.toString().substring(url.toString().lastIndexOf(".")).contains(".docx")) {
//            // Word document
//            intent.setDataAndType(uri, "application/msword");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".pdf")) {
//            // PDF file
//            intent.setDataAndType(uri, "application/pdf");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".ppt") || url.toString().substring(url.toString().lastIndexOf(".")).contains(".pptx")) {
//            // Powerpoint file
//            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".xls") || url.toString().substring(url.toString().lastIndexOf(".")).contains(".xlsx")) {
//            // Excel file
//            intent.setDataAndType(uri, "application/vnd.ms-excel");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".zip") || url.toString().contains(".rar")) {
//            // WAV audio file
//            intent.setDataAndType(uri, "application/x-wav");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".rtf")) {
//            // RTF file
//            intent.setDataAndType(uri, "application/rtf");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".wav") || url.toString().substring(url.toString().lastIndexOf(".")).contains(".mp3")) {
//            // WAV audio file
//            intent.setDataAndType(uri, "audio/x-wav");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".gif")) {
//            // GIF file
//            intent.setDataAndType(uri, "image/gif");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".jpg") || url.toString().substring(url.toString().lastIndexOf(".")).contains(".jpeg") || url.toString().contains(".png")) {
//            // JPG file
//            intent.setDataAndType(uri, "image/jpeg");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".txt")) {
//            // Text file
//            intent.setDataAndType(uri, "text/plain");
//        } else if (url.toString().substring(url.toString().lastIndexOf(".")).contains(".3gp") || url.toString().substring(url.toString().lastIndexOf(".")).contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
//            // Video files
//            intent.setDataAndType(uri, "video/*");
//        } else {
//            //if you want you can also define the intent type for any other file
//
//            //additionally use else clause below, to manage other unknown extensions
//            //in this case, Android will show all applications installed on the device
//            //so you can choose which application to use
//            intent.setDataAndType(uri, "*/*");
//        }


    }

    protected static void downloadFileFromUrl(ProgressDialog progressDialog) {
        if (checkNetworkAccess()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            myStorageRef = storageReference.child("images/").child(Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(checkPosition).getPhoneNumber());
            //myStorageRef = storageReference.child(Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(check_Position).getPhoneNumber());
            // myStorageRef=Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(check_Position).getPhoneNumber();
            myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    downloadFile(staticContext, Environment.DIRECTORY_DOWNLOADS, uri.toString(), progressDialog);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(staticContext, staticContext.getString(R.string.write_encrypted_message_upload_file_failure), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            downloadFileFromUrlLocal();
        }
        //   intent.setDataAndType(downloadManager.getUriForDownloadedFile(statusDownloadedFile), downloadManager.getMimeTypeForDownloadedFile(statusDownloadedFile));

    }

    protected static void downloadFileFromUrlLocal() {

        RandomStringGenerator rsg = new RandomStringGenerator();
        keySecret = rsg.randomStringGenerator();
        builderDialogFileLocal = new AlertDialog.Builder(staticContext, R.style.CustomDialogTheme);
        builderDialogViewFileLocal = LayoutInflater.from(staticContext).inflate(R.layout.read_encrypted_message_file_local_dialog_item, null);
        textViewFileLocal = builderDialogViewFileLocal.findViewById(R.id.textView_read_encrypted_message_adapter_file_local);

        boolean filePathSavedLocal = false;
        mydbReadMessageFilePathContainerHelper = new DBReadMessageFilePathContainerHelper(staticContext);
        cursorFilePath = mydbReadMessageFilePathContainerHelper.getAllContacts();
        if (cursorFilePath.moveToFirst()) {
            cursorFilePath.moveToFirst();
            do {
                if (AES.decrypt(cursorFilePath.getString(2), CaesarsCiphering.caesarsDecryption(cursorFilePath.getString(3), 13)).equals(Fragment_Read_Encrypted_Message.getDecryptedDocumentValueLocal().get(checkPosition).getPhoneNumber())) {
                    filePathSavedLocal = true;
                    break;
                }
            } while (cursorFilePath.moveToNext());
        } else {
            //Toast.makeText(staticContext, staticContext.getString(R.string.read_encrypted_message_cache_empty), Toast.LENGTH_SHORT).show();
            textViewFileLocal.setText(staticContext.getString(R.string.read_encrypted_message_cache_empty));
            if (builderDialogViewFileLocal.getParent() != null) {
                ((ViewGroup) builderDialogViewFileLocal.getParent()).removeView(builderDialogViewFileLocal);
            }

            builderDialogFileLocal.setView(builderDialogViewFileLocal);
            builderDialogFileLocal.show();
        }

        if (!filePathSavedLocal) {
            //  Toast.makeText(staticContext, staticContext.getString(R.string.read_encrypted_message_file_path_empty), Toast.LENGTH_SHORT).show();
            textViewFileLocal.setText(staticContext.getString(R.string.read_encrypted_message_file_path_empty));
            if (builderDialogViewFileLocal.getParent() != null) {
                ((ViewGroup) builderDialogViewFileLocal.getParent()).removeView(builderDialogViewFileLocal);
            }
            builderDialogFileLocal.setView(builderDialogViewFileLocal);
            builderDialogCreateFile = builderDialogFileLocal.create();
            builderDialogCreateFile.show();
        } else {
            Intent intentLocal = new Intent();
            intentLocal.setAction(Intent.ACTION_VIEW);
            intentLocal.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentLocal.setData(Uri.parse(AES.decrypt(cursorFilePath.getString(1), CaesarsCiphering.caesarsDecryption(cursorFilePath.getString(3), 13))));
            if (intentLocal.resolveActivity(staticContext.getPackageManager()) != null) {
                staticContext.startActivity(intentLocal);
                //progressDialog.dismiss();
            }
        }
    }


    protected static void downloadFile(Context context, String destinationDirectory, String url, ProgressDialog progressDialog) {
        boolean downloading = true;
        boolean filePathSaved = false;
        RandomStringGenerator rsg = new RandomStringGenerator();
        keySecret = rsg.randomStringGenerator();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(checkPosition).getPhoneNumber());
//        String path = String.valueOf(context.getExternalFilesDir(destinationDirectory + "/" + Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(checkPosition).getPhoneNumber()));
//        File file = new File(path);
        mydbReadMessageFilePathContainerHelper = new DBReadMessageFilePathContainerHelper(context);
        cursorFilePath = mydbReadMessageFilePathContainerHelper.getAllContacts();
        if (cursorFilePath.moveToFirst()) {
            cursorFilePath.moveToFirst();
            do {
                if (AES.decrypt(cursorFilePath.getString(2), CaesarsCiphering.caesarsDecryption(cursorFilePath.getString(3), 13)).equals(Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(checkPosition).getPhoneNumber())) {
                    filePathSaved = true;
                    break;
                }
            } while (cursorFilePath.moveToNext());

        }
        if (!filePathSaved) {
            statusDownloadFileLong = downloadManager.enqueue(request);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
            Cursor c;
            while (downloading) {
                c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    statusDownloadedFile = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (statusDownloadedFile == DownloadManager.STATUS_SUCCESSFUL || statusDownloadFileLong == DownloadManager.STATUS_SUCCESSFUL) {
                        if (statusDownloadFileLong != null) {
                            mydbReadMessageFilePathContainerHelper.insertdata(new ReadMessageItemFilePath(AES.encrypt(downloadManager.getUriForDownloadedFile(statusDownloadFileLong).toString(), keySecret), AES.encrypt(Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(checkPosition).getPhoneNumber(), keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13)));
                        } else if (statusDownloadedFile != null) {
                            mydbReadMessageFilePathContainerHelper.insertdata(new ReadMessageItemFilePath(AES.encrypt(downloadManager.getUriForDownloadedFile(statusDownloadedFile).toString(), keySecret), AES.encrypt(Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(checkPosition).getPhoneNumber(), keySecret), CaesarsCiphering.caesarsEncryption(keySecret, 13)));
                        }

                        downloading = false;
                        break;
                    }
                    if (statusDownloadedFile == DownloadManager.STATUS_FAILED || statusDownloadFileLong == DownloadManager.STATUS_FAILED) {
                        downloading = false;
                        Toast.makeText(context, context.getString(R.string.write_encrypted_message_upload_file_failure), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (!filePathSaved) {
            if (statusDownloadFileLong != null) {
                intent.setDataAndType(downloadManager.getUriForDownloadedFile(statusDownloadFileLong), downloadManager.getMimeTypeForDownloadedFile(statusDownloadFileLong));
            } else if (statusDownloadedFile != null) {
                intent.setDataAndType(downloadManager.getUriForDownloadedFile(statusDownloadedFile), downloadManager.getMimeTypeForDownloadedFile(statusDownloadedFile));
            }

        } else {
            intent.setData(Uri.parse(AES.decrypt(cursorFilePath.getString(1), CaesarsCiphering.caesarsDecryption(cursorFilePath.getString(3), 13))));
//            if (!cursorFilePath.moveToFirst()) {
//                mydbReadMessageFilePathContainerHelper.insertdata(new ReadMessageItemFilePath(Integer.toString(statusDownloadedFile), url));
//                intent.setDataAndType(Uri.parse(path), getFileExtension(Uri.parse(path)));
//            } else {
//                cursorFilePath.moveToFirst();
//                do {
//                    if (Fragment_Read_Encrypted_Message.getDecryptedDocumentValue().get(checkPosition).getPhoneNumber().equals(cursorFilePath.getString(2))) {
//                        intent.setDataAndType(Uri.parse(path), getFileExtension(Uri.parse(path)));
//                    }
//                } while (cursorFilePath.moveToNext());
//            }
        }
        //   intent.setDataAndType(downloadManager.getUriForDownloadedFile(statusDownloadedFile), downloadManager.getMimeTypeForDownloadedFile(statusDownloadedFile));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            progressDialog.dismiss();
        }
    }

    private static String getFileExtension(Uri uri) {
        ContentResolver cr = staticContext.getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Read State", readStateTrue);
        return result;

    }

    protected synchronized void runInBackground(final Runnable r) {
        if (Fragment_Read_Encrypted_Message.getHandlerClassification() != null) {
            Fragment_Read_Encrypted_Message.getHandlerClassification().post(r);
        }
    }

    protected void processImage() {
        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        if (Fragment_Read_Encrypted_Message.getClassifier() != null) {
//                            final long startTime = SystemClock.uptimeMillis();

                            if (bitmapLocal != null) {
                                resultsClassification =
                                        Fragment_Read_Encrypted_Message.getClassifier().recognizeImage(bitmapLocal, 0);
                            }
                            snackbar = Snackbar.make(view, "Image Classified As:" + resultsClassification.get(0).getTitle() + " " + "with:" + resultsClassification.get(0).getConfidence(), Snackbar.LENGTH_INDEFINITE);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(context.getColor(R.color.button_background));
                            snackbar.setActionTextColor(context.getColor(R.color.white));
                            snackbar.show();

                            //                           Toast.makeText(context, "Image Classified As:" + resultsClassification.get(0).getTitle() + "with:" + resultsClassification.get(0).getConfidence(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), results.get(1).getTitle() + ":" + results.get(1).getConfidence(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), results.get(2).getTitle() + ":" + results.get(2).getConfidence(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void gpsIntent() {
        String uri;
        if (checkNetworkAccess()) {
            uri = "https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValue().get(check_Position).getPhoneNumber();
        } else {
            uri = "https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(check_Position).getPhoneNumber();
        }
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
        context.startActivity(Intent.createChooser(sharingIntent, "Share in..."));
    }

    private void googleMapsIntent() {

        try {
            Uri gmmIntentUri;
            //Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + HelperMaps.getLatitude() + "," + HelperMaps.getLongitude());
            if (checkNetworkAccess()) {
                gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValue().get(check_Position).getPhoneNumber());
            } else {
                gmmIntentUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Fragment_Read_Encrypted_Message.getDecryptedLocationValueLocal().get(check_Position).getPhoneNumber());

            }
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);

        } catch (Exception e) {
            e.printStackTrace();
            gpsIntent();
        }
    }

    public static boolean isControlDeleteList() {
        return controlDeleteList;
    }

    public ArrayList<ReadEncryptedMessageItem> getRestartuserInfosDelete() {
        return restartuserInfosDelete;
    }

    private static boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) staticContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            // Toast.makeText(getContext(), getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static List<Bitmap> getDialogImages() {
        return dialogImages;
    }


    public static int getCheckPosition() {
        return checkPosition;
    }

    public static String getProgressTitle() {
        return progressTitle;
    }

    public void textToSpeech() {
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

                    if (Tokenize.isControlTextToSpeech()) {
                        Toast.makeText(context, context.getString(R.string.text_to_speech_control_safety), Toast.LENGTH_SHORT).show();
                    } else if (checkNetworkAccess()) {
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

    protected static void startVideo() {
        if (checkVideo) {

            AsyncVideoTask asyncVideoTask = (AsyncVideoTask) new AsyncVideoTask(staticContext, Fragment_Read_Encrypted_Message.getDecryptedVideoValue().get(checkPosition).getPhoneNumber(), Fragment_Read_Encrypted_Message.getDecryptedVideoValue().get(checkPosition).getPhoneNumber());
            //  asyncVideoTask.execute(Fragment_Read_Encrypted_Message)
            asyncVideoTask.execute(Fragment_Read_Encrypted_Message.getDecryptedVideoValue().get(checkPosition).getPhoneNumber());
            //final String[] fileItems = {};
            builderVideo = new androidx.appcompat.app.AlertDialog.Builder(staticContext, R.style.CustomDialogTheme);
            ((ViewGroup) videoView.getParent()).removeView(videoView);
            videoView.setVisibility(View.VISIBLE);
            if (!Fragment_Read_Encrypted_Message.getDecryptedVideoValue().isEmpty()) {
                videoView.setVideoURI(Uri.parse(Fragment_Read_Encrypted_Message.getDecryptedVideoValue().get(checkPosition).getPhoneNumber()));
            } else {
                videoView.setVideoURI(Uri.parse(Fragment_Read_Encrypted_Message.getDecryptedVideoValueLocal().get(checkPosition).getPhoneNumber()));
            }

            videoView.start();
            //builderVideo.setTitle(getString(R.string.write_encrypted_message_file_title));
            builderVideo.setView(videoView);
            builderVideo.show();

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    builderVideo.create().dismiss();
                    //videoView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    protected static void startAudio() {
        if (checkAudio) {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.release();
                }
            }
            mPlayer = new MediaPlayer();
            try {
                if (!Fragment_Read_Encrypted_Message.getDecryptedAudioValue().isEmpty()) {
                    mPlayer.setDataSource(Fragment_Read_Encrypted_Message.getDecryptedAudioValue().get(checkPosition).getPhoneNumber());
                } else {
                    mPlayer.setDataSource(Fragment_Read_Encrypted_Message.getDecryptedAudioValueLocal().get(checkPosition).getPhoneNumber());
                }
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mPlayer.start();
        }
    }

    private void IdentifyLanguage() {
        AlertDialog alertDialogTranslator;
        builderDialogTranslator = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
        builderDialogViewTranslator = LayoutInflater.from(context).inflate(R.layout.progress_bar, null);
        builderDialogTranslator.setCancelable(false);
        textViewTranslator = builderDialogViewTranslator.findViewById(R.id.textView_translator);
        progressBarTranslator = builderDialogViewTranslator.findViewById(R.id.progressBar_translator);
        progressBarTranslator.setVisibility(View.VISIBLE);
        builderDialogTranslator.setView(builderDialogViewTranslator);
        alertDialogTranslator = builderDialogTranslator.create();
        if (Tokenize.isControlTextToSpeech()) {
            Toast.makeText(context, "Cant Use Translation While Safety Filter Is On", Toast.LENGTH_SHORT).show();
        } else if (checkNetworkAccess()) {
            alertDialogTranslator.show();
            FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
            languageIdentifier.identifyLanguage(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber())
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@Nullable String languageCode) {
                                    if (!languageCode.equals("und")) {
                                        // Toast.makeText(context, "Language Code:" + languageCode, Toast.LENGTH_SHORT).show();
                                        if (languageCode.equals("en") || languageCode.equals("tr")) {
                                            if (languageCode.equals("en")) {
                                                options =
                                                        new FirebaseTranslatorOptions.Builder()
                                                                .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                                                .setTargetLanguage(FirebaseTranslateLanguage.TR)
                                                                .build();
                                            } else {
                                                options =
                                                        new FirebaseTranslatorOptions.Builder()
                                                                .setSourceLanguage(FirebaseTranslateLanguage.TR)
                                                                .setTargetLanguage(FirebaseTranslateLanguage.EN)
                                                                .build();
                                            }
                                            englishTurkishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                                            if (englishTurkishTranslator.downloadModelIfNeeded().isComplete()) {
                                                englishTurkishTranslator.translate(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(String s) {
                                                        Toast.makeText(context, "Translation:" + s, Toast.LENGTH_SHORT).show();
//                                                        snackbar = Snackbar.make(view, "Translation:" + s, Snackbar.LENGTH_INDEFINITE);
//                                                        View snackBarView = snackbar.getView();
//                                                        snackBarView.setBackgroundColor(context.getColor(R.color.button_background));
//                                                        snackbar.setActionTextColor(context.getColor(R.color.white));
//                                                        snackbar.show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        alertDialogTranslator.setCancelable(true);
                                                        alertDialogTranslator.dismiss();
                                                        e.printStackTrace();
                                                        Toast.makeText(context, "Translation Failure!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                                                        .requireWifi()
                                                        .build();
                                                englishTurkishTranslator.downloadModelIfNeeded(conditions)
                                                        .addOnSuccessListener(
                                                                new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void v) {
                                                                        //   Toast.makeText(context, "Translation Model Download Successfully!", Toast.LENGTH_SHORT).show();
                                                                        englishTurkishTranslator.translate(Fragment_Read_Encrypted_Message.getDecryptedMessages().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                                            @Override
                                                                            public void onSuccess(String s) {
                                                                                alertDialogTranslator.setCancelable(true);
                                                                                progressBarTranslator.setVisibility(View.INVISIBLE);
                                                                                // Toast.makeText(context, "Translation:" + s, Toast.LENGTH_SHORT).show();
                                                                                textViewTranslator.setText("Translation:" + s);
                                                                                //alertDialogTranslator.dismiss();
//                                                                                snackbar = Snackbar.make(view, "Translation:" + s, Snackbar.LENGTH_INDEFINITE);
//                                                                                View snackBarView = snackbar.getView();
//                                                                                snackBarView.setBackgroundColor(context.getColor(R.color.button_background));
//                                                                                snackbar.setActionTextColor(context.getColor(R.color.white));
//                                                                                snackbar.show();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                e.printStackTrace();
                                                                                alertDialogTranslator.setCancelable(true);
                                                                                Toast.makeText(context, "Translation Failure!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                        // Model downloaded successfully. Okay to start translating.
                                                                        // (Set a flag, unhide the translation UI, etc.)
                                                                    }
                                                                })
                                                        .addOnFailureListener(
                                                                new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        alertDialogTranslator.setCancelable(true);
                                                                        Toast.makeText(context, "Translation Model Download Failure!", Toast.LENGTH_SHORT).show();
                                                                        // Model couldn’t be downloaded or other internal error.
                                                                        // ...
                                                                    }
                                                                });
                                            }
                                        }


//                                    Log.i(TAG, "Language: " + languageCode);
                                    } else {
                                        alertDialogTranslator.setCancelable(true);
                                        alertDialogTranslator.dismiss();
                                        Toast.makeText(context, "Language Translation Not Supported", Toast.LENGTH_SHORT).show();
//                                    Log.i(TAG, "Can't identify language.");
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
        } else {
            alertDialogTranslator.setCancelable(true);
            alertDialogTranslator.dismiss();
            Toast.makeText(context, "Cannot Use Translation Without Any Network Connection!", Toast.LENGTH_SHORT).show();
        }

    }

}


class AsyncReadEncryptedMessagePhoneNumberAdapter extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    // Activity mActivity;
    private Bitmap mBitmap;
    private ProgressDialog progressdialog;
    private ReadEncryptedMessageDialogAdapter mDialogAdapter;
    //private ImageView mImageView_dialog;
    private String mMessage;
    private View dialogView;
    private ImageView mImageView_dialog;
    private List<Classifier.Recognition> resultsClassification;
    private Snackbar mSnackbar;

    AsyncReadEncryptedMessagePhoneNumberAdapter(Context context, ReadEncryptedMessageDialogAdapter dialogAdapter, ImageView imageView_dialog, String message, Snackbar snackbar) {
        super();
        mContext = context;
        //mActivity=activity;
        //  mBitmap=bitmap;
        mDialogAdapter = dialogAdapter;
        mImageView_dialog = imageView_dialog;
        mMessage = message;
        mSnackbar = snackbar;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressdialog = new ProgressDialog(mContext);
        progressdialog.setMessage(ReadEncryptedMessageAdapter.getProgressTitle());
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected Void doInBackground(Void... strings) {
        Looper.prepare();
        try {
            dialogView = LayoutInflater.from(mContext).inflate(R.layout.read_encrypted_message_alert_dialog_item, null);
            mImageView_dialog = dialogView.findViewById(R.id.imageView_read_encrypted_message_dialog);
            mBitmap = Picasso.get().load(Fragment_Read_Encrypted_Message.getDecryptedImageValue().get(ReadEncryptedMessageAdapter.getCheckPosition()).getPhoneNumber()).get();
            //ReadEncryptedMessageAdapter.getDialogImages().add(mBitmap);
            mImageView_dialog.setVisibility(View.VISIBLE);
            mImageView_dialog.setImageBitmap(mBitmap);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.CustomDialogTheme);
            builder.setTitle(mContext.getString(R.string.read_encrypted_message_title_decrypted_message));
            builder.setMessage(mMessage);
            builder.setView(dialogView);
            // mDialogAdapter.notifyDataSetChanged();
            /* dialogAdapter = new ReadEncryptedMessageDialogAdapter(mContext, ReadEncryptedMessageAdapter.getDialogImages());
                LinearLayoutManager layoutmanager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                mRecyclerView_dialog.setLayoutManager(layoutmanager);
                mRecyclerView_dialog.setAdapter(dialogAdapter);
                ((ViewGroup) mRecyclerView_dialog.getParent()).removeView(mRecyclerView_dialog);
                builder.setView(mRecyclerView_dialog);
                mRecyclerView_dialog.setVisibility(View.VISIBLE);*/
            builder.show();
            processImage();
            progressdialog.dismiss();


        } catch (Exception e) {
            e.printStackTrace();

        }
        Looper.loop();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressdialog.dismiss();

    }

    protected synchronized void runInBackground(final Runnable r) {
        if (Fragment_Read_Encrypted_Message.getHandlerClassification() != null) {
            Fragment_Read_Encrypted_Message.getHandlerClassification().post(r);
        }
    }

    protected void processImage() {
        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        if (Fragment_Read_Encrypted_Message.getClassifier() != null) {
//                            final long startTime = SystemClock.uptimeMillis();

                            if (mBitmap != null) {
                                resultsClassification =
                                        Fragment_Read_Encrypted_Message.getClassifier().recognizeImage(mBitmap, 0);
                            }

                            mSnackbar.setText("Image Classified As:" + resultsClassification.get(0).getTitle() + "with:" + resultsClassification.get(0).getConfidence());
                            mSnackbar.show();
//                            Toast.makeText(mContext, "Image Classified As:" + resultsClassification.get(0).getTitle() + "with:" + resultsClassification.get(0).getConfidence(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), results.get(1).getTitle() + ":" + results.get(1).getConfidence(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getContext(), results.get(2).getTitle() + ":" + results.get(2).getConfidence(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}


class AsyncVideoTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog progressdialog;
    private Context mContext;
    private String mFileUrl;
    private String mFileName;

    AsyncVideoTask(Context context, String fileUrl, String fileName) {
        super();
        mContext = context;
        mFileUrl = fileUrl;
        mFileName = fileName;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressdialog = new ProgressDialog(mContext);
        progressdialog.setMessage(ForgotPassword.getProgressTitle());
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected String doInBackground(String... sUrl) {
        //  Looper.prepare();


        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            //output = new FileOutputStream("/sdcard/file_name.mp4")
            // ;
            // addVideo(output);

            byte data[] = new byte[4096];
            Log.d("Orignalvalu1", String.valueOf(new byte[4096]));
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                Log.d("Orignalvalu1", String.valueOf(input.read(data)));
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                Log.d("Orignalvalu2", String.valueOf(total));
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(aVoid);
        progressdialog.dismiss();

    }

    private void addVideo(File videoFile) {
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Video.Media.TITLE, mFileUrl);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
        mContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        //return mContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }
}


class AsyncFileTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    // private String mEmail;
    private ProgressDialog progressdialog;


    AsyncFileTask(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressdialog = new ProgressDialog(mContext);
        progressdialog.setMessage(LoginScreen.getProgressTitle());
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected Void doInBackground(Void... strings) {
//        Looper.prepare();
        try {
            ReadEncryptedMessageAdapter.downloadFileFromUrl(progressdialog);
            //progressdialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            progressdialog.dismiss();
        }
//        Looper.loop();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (!isCancelled()) {

        }
        if (progressdialog != null && progressdialog.isShowing()) {
            // progressdialog.dismiss();
        }
    }
}

