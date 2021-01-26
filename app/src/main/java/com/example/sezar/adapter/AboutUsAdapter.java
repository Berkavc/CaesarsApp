package com.example.sezar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sezar.R;
import com.example.sezar.model.AboutUsItem;
import java.util.Random;
import java.util.ArrayList;

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<AboutUsItem> userInfos;
    private static boolean controlModification = false;
    private static int check_Position;
    Random random = new Random();

    CardView list_cardview;

    public AboutUsAdapter(Context context, ArrayList<AboutUsItem> userinfos) {
        this.context = context;
        this.userInfos = userinfos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.about_us_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,  int position) {
       // int randomInteger = random.nextInt(3);

        final int positioncheck;

        final AboutUsItem userInfo = userInfos.get(position);
        viewHolder.textView_about_us_fact_list.setText(userInfo.getName());
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
        positioncheck=position;
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
        TextView textView_about_us_fact_list;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_about_us_fact_list = itemView.findViewById(R.id.textView_about_us_fact_list);
            list_cardview = itemView.findViewById(R.id.cardview_about_us_list);

        }
    }

    public static boolean isControlModification() {
        return controlModification;
    }

    public static int getCheck_Position() {
        return check_Position;
    }
}
