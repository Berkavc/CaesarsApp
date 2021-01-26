package com.example.sezar.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sezar.R;
import com.example.sezar.SmartReplyClient;
import com.example.sezar.activity.ForgotPassword;
import com.example.sezar.activity.LoginScreen;
import com.example.sezar.activity.SMTPAuthenticator;
import com.example.sezar.adapter.ContactUsAdapter;
import com.example.sezar.model.ContactUsItem;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult;
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
//import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply;
//import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage;
//import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion;
//import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Contact_Us extends Fragment {
    public static final String MESSAGE_KEY = "message_key";
    ArrayList<ContactUsItem> users;
    RecyclerView recyclerView_user_info;
    ContactUsAdapter adapter;
    String fact;
    private static String progressTitle;
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private EditText editText;
    private FloatingActionButton floatingActionButton;
    private static final String TAG = "SmartReplyDemo";
    private SmartReplyClient client;
    private Handler handler;
    private static String smartMessage;
    private ArrayList<FirebaseTextMessage> conversation;
    private boolean controlReply;
    private Snackbar snackbar;
    private View view;

    public Fragment_Contact_Us() {
        // Required empty public constructor
    }

    public static Fragment_Contact_Us newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(Fragment_Contact_Us.MESSAGE_KEY, message);
        Fragment_Contact_Us fragment = new Fragment_Contact_Us();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__contact__us, container, false);
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(MESSAGE_KEY);
            editText = view.findViewById(R.id.editText_contact_us_message_box);
            floatingActionButton = view.findViewById(R.id.floatingActionButton_contact_us);
            client = new SmartReplyClient(getContext());
            handler = new Handler();
            users = new ArrayList<>();
            conversation = new ArrayList<FirebaseTextMessage>();
            recyclerView_user_info = view.findViewById(R.id.recyclerView_contact_us);
            LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            adapter = new ContactUsAdapter(getContext(), users);
            recyclerView_user_info.setLayoutManager(layoutmanager);
            recyclerView_user_info.setAdapter(adapter);
            addContactUsItem();
            progressTitle = getString(R.string.forgot_password_wait);

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (checkNetworkAccess()) {
                        if (checkEmpty()) {
                            fireBaseSmartReply(editText.getText().toString());
//                        send(editText.getText().toString());
                            new AsyncContactUs(getContext(), editText.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.write_encrypted_message_box_empty), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
//        handler.post(
//                () -> {
//                    client.loadModel();
//                });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
//        handler.post(
//                () -> {
//                    client.unloadModel();
//                });
    }

    public static void sendEmail(String emailContent) throws Exception {
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
        message.setSubject("FeedBack From:" + LoginScreen.userName());
        message.setContent(emailContent, "text/plain");
        message.setFrom(new InternetAddress("appcaesars@gmail.com"));
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress("appcaesars@gmail.com"));

        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }


    private void addContactUsItem() {
        users.add(new ContactUsItem(getString(R.string.contact_us_email_one)));
    }

    public static String getProgressTitle() {
        return progressTitle;
    }

    private boolean checkEmpty() {
        if (editText.getText().toString().length() != 0) {
            return true;
        } else {
            return false;
        }


    }

    private void send(final String message) {
        handler.post(
                () -> {
//                    StringBuilder textToShow = new StringBuilder();
//                    textToShow.append("Input: ").append(message).append("\n\n");

                    // Get suggested replies from the model.
//                    SmartReply[] ans = client.predict(new String[] {message});
//                    for (SmartReply reply : ans) {
//                       smartMessage= reply.getText();
//                        break;
//                    }
                    //  String ans = client.predict();
                    smartMessage = SmartReplyClient.getRepyList()[1];
                    Toast.makeText(getContext(), smartMessage, Toast.LENGTH_SHORT).show();
//                    textToShow.append("------").append("\n");

//                    runOnUiThread(
//                            () -> {
//                                // Show the message and suggested replies on screen.
//                                messageTextView.append(textToShow);
//
//                                // Clear the input box
//                                messageInput.setText(null);
//
//                                // Scroll to the bottom to show latest entry's classification result.
//                                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
//                            });
                });
    }

    private void fireBaseSmartReply(String replyMessage) {
        conversation.clear();
        conversation.add(FirebaseTextMessage.createForRemoteUser(replyMessage, System.currentTimeMillis(), LoginScreen.userName()));
        getHints();

    }

    private void getHints() {
        controlReply = false;
        FirebaseSmartReply smartReply = FirebaseNaturalLanguage.getInstance().getSmartReply();
        smartReply.suggestReplies(conversation).addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
            @Override
            public void onSuccess(SmartReplySuggestionResult smartReplySuggestionResult) {
                if (smartReplySuggestionResult.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                    Toast.makeText(getContext(), "LANGUAGE NOT SUPPORTED!", Toast.LENGTH_SHORT).show();
                } else if (smartReplySuggestionResult.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {

                    for (SmartReplySuggestion suggestion : smartReplySuggestionResult.getSuggestions()) {
                        String replyText = suggestion.getText();
                        //  if (!controlReply) {
//                            Toast.makeText(getContext(), replyText, Toast.LENGTH_SHORT).show();
                        snackbar = Snackbar.make(view, "SmartReply:" + replyText, Snackbar.LENGTH_INDEFINITE);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(getResources().getColor(R.color.button_background));
                        snackbar.setActionTextColor(getResources().getColor(R.color.white));
                        snackbar.show();
                        break;
                        //}
                        //   controlReply = true;
                    }

//                    Toast.makeText(getContext(),smartReplySuggestionResult.getSuggestions().get(0).getText(),Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "SMART REPLY FAILURE!", Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(getContext(), "SMART REPLY FAILURE!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean checkNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(getContext(), getString(R.string.forgot_password_check_network_access_fail), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static String getSmartMessage() {
        return smartMessage;
    }
}

class AsyncContactUs extends AsyncTask<Void, Void, Void> {
    Context mContext;
    private String mEmailContent;
    private ProgressDialog progressdialog;

    AsyncContactUs(Context context, String emailContent) {
        super();
        mContext = context;
        mEmailContent = emailContent;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressdialog = new ProgressDialog(mContext);
        progressdialog.setMessage(Fragment_Contact_Us.getProgressTitle());
        progressdialog.show();
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected Void doInBackground(Void... strings) {
        try {
            Fragment_Contact_Us.sendEmail(mEmailContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (!isCancelled()) {

        }
        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.dismiss();
//            Toast.makeText(mContext,Fragment_Contact_Us.getSmartMessage(),Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, mContext.getString(R.string.contact_us_feedback), Toast.LENGTH_SHORT).show();
        }

    }
}