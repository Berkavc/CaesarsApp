package com.example.sezar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.sezar.R;
import com.example.sezar.fragment.Fragment_Change_Background;

public class ChangeBackgroundAdapter extends PagerAdapter
{
    private Context mContext;
    public ChangeBackgroundAdapter(Context context) {
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment__change__background,collection);
        //collection.addView(layout);
        return layout;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title =null;
        if(position==0){
            title=mContext.getResources().getString(R.string.change_theme_default_theme);
        }else if(position==1){
            title=mContext.getResources().getString(R.string.change_theme_extra_theme);
        }
        return title;
    }
}
