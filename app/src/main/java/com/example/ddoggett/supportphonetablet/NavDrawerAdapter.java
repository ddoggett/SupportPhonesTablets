package com.example.ddoggett.supportphonetablet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dylan Doggett on 11/28/15.
 */
public class NavDrawerAdapter extends BaseAdapter {

    private static List<DrawerRowModel> drawerList = new ArrayList<>();
    private final Context context;
    private int selectedPosition = -1;

    public NavDrawerAdapter(Context context) {
        this.context = context;
        if (drawerList.size() == 0) {
            createDrawerItems();
        }

    }

    @Override
    public int getCount() {
        return drawerList.size();
    }

    @Override
    public DrawerRowModel getItem(int position) {
        return drawerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.drawer_list_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder();
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.nav_option_icon);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.nav_option_text);
            convertView.setTag(viewHolder);
        }

        DrawerRowModel item = getItem(position);
        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();


        viewHolder.iconImageView.setImageResource(item.imageLocation);
        viewHolder.titleTextView.setText(context.getString(item.title));


        return convertView;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public static int getTitlePosition(int titleId) {
        for (int x = 0; x < drawerList.size(); x++) {
            if (drawerList.get(x).title == titleId) {
                return x;
            }
        }
        return -1;
    }

    private static class ItemViewHolder {
        public View layout;
        public View rowContent;
        public TextView drawerSubHeader;
        public ImageView iconImageView;
        public TextView titleTextView;
        public TextView subTitleTextView;
        public TextView drawer_counter;
    }

    /**
     * Method that will create the items for the drawer
     */
    private void createDrawerItems() {
        //add all the items to the drawer..
        DrawerRowModel drawerRowModel;

        drawerRowModel = new DrawerRowModel();
        drawerRowModel.title = R.string.film_text;
        drawerRowModel.imageLocation = R.drawable.ic_action_video;
        drawerList.add(drawerRowModel);

        drawerRowModel = new DrawerRowModel();
        drawerRowModel.title = R.string.music_text;
        drawerRowModel.imageLocation = R.drawable.ic_action_av_my_library_music;
        drawerList.add(drawerRowModel);

        drawerRowModel = new DrawerRowModel();
        drawerRowModel.title = R.string.television_text;
        drawerRowModel.imageLocation = R.drawable.ic_action_hardware_tv;
        drawerList.add(drawerRowModel);

        drawerRowModel = new DrawerRowModel();
        drawerRowModel.title = R.string.book_text;
        drawerRowModel.imageLocation = R.drawable.ic_action_action_book;
        drawerList.add(drawerRowModel);
    }

    public class DrawerRowModel {
        public int title;
        public int subTitle =0;
        public int imageLocation;
    }
}