package com.example.sezar.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.sezar.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Change_App_Icon extends Fragment {
    public static final String MESSAGE_KEY = "message_key";
    View view;
    ImageView image_button_number_one;
    ImageView image_button_number_two;
    Bitmap bitmap;
    //ImageView imageView_default_icon,imageView_number_one_icon;


    public Fragment_Change_App_Icon() {
        // Required empty public constructor
    }

    public static Fragment_Change_App_Icon newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(Fragment_Change_App_Icon.MESSAGE_KEY, message);
        Fragment_Change_App_Icon fragment = new Fragment_Change_App_Icon();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__change__app__icon, container, false);
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(MESSAGE_KEY);
            image_button_number_one=view.findViewById(R.id.imageButton_app_icon_number_one);
            image_button_number_two=view.findViewById(R.id.imageButton_app_icon_number_two);


            image_button_number_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shortcutDel();
                    //shortcutAddDefaultIcon(50);
                }
            });


            image_button_number_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //shortcutDel();
                    //shortcutAddNumberTwoIcon(50);
                }
            });


        }
        return view;
    }


   /* private void shortcutAddDefaultIcon(int number) {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(getContext(),Fragment_Change_App_Icon.class);
       // shortcutIntent.setAction(Constants.ACTION_PLAY);

        // Create bitmap with number in it -> very default. You probably want to give it a more stylish look
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(0xFF808080); // gray
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        new Canvas(bitmap).drawText(""+number, 50, 50, paint);
        //imageView_default_icon=view.findViewById(R.id.imageButton_app_icon_number_one);
      //  image_button_number_one.setImageBitmap(bitmap);
        //((ImageView) view.findViewById(R.id.imageButton_app_icon_number_one)).setImageBitmap(bitmap);

        // Decorate the shortcut
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        //addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

        // Inform launcher to create shortcut
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getContext().sendBroadcast(addIntent);
    }

    private void shortcutDel() {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(getContext(), Fragment_Change_App_Icon.class);
      //  shortcutIntent.setAction(Intent.);

        // Decorate the shortcut
        Intent delIntent = new Intent();
        delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
       // delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // Inform launcher to remove shortcut
        delIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        getContext().sendBroadcast(delIntent);
    }

    private void shortcutAddNumberTwoIcon(int number) {
        // Intent to be send, when shortcut is pressed by user ("launched")
        Intent shortcutIntent = new Intent(getContext(),Fragment_Change_App_Icon.class);
        // shortcutIntent.setAction(Constants.ACTION_PLAY);

        // Create bitmap with number in it -> very default. You probably want to give it a more stylish look
        //Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(0xFF808080); // gray
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);;
        //bitmap=BitmapFactory.decodeResource(image_button_number_two.getDrawable()).getBig;
        bitmap = ((BitmapDrawable)image_button_number_two.getDrawable()).getBitmap();
        //InputStream is = getContext().getResources().openRawResource(R.drawable.gladiator_icon);
        //bitmap = BitmapFactory.decodeStream(is);
       // new Canvas(bitmap).drawText(""+number, 50, 50, paint);
        //imageView_number_one_icon=view.findViewById(R.id.imageButton_app_icon_number_two);
       // image_button_number_two.setImageBitmap(bitmap);
        //((ImageView) view.findViewById(R.id.imageButton_app_icon_number_two)).setImageBitmap(bitmap);

        // Decorate the shortcut
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        //addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);

        // Inform launcher to create shortcut
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getContext().sendBroadcast(addIntent);
    }*/

}
