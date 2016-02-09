package com.example.ddoggett.supportphonetablet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Dylan Doggett on 11/15/15.
 */
public class ContentDetailFragment extends Fragment {
    // extras
    public static final String DETAIL_ITEM_POSITION = "DETAIL_ITEM_POSITION";

    // state
    public int detailItemPosition = -1;

    // views
    public View root;
    public TextView textView;

    public static Fragment newInstance(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(DETAIL_ITEM_POSITION, i);
        Fragment fragment = new ContentDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null) {
            detailItemPosition = getArguments().getInt(MainFragment.DETAIL_ITEM_POSITION);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.content_detail_view, null, false);
        textView = (TextView)root.findViewById(R.id.text_view);
        textView.setText("Item " + detailItemPosition);
        return root;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      //  ((MainActivity)getActivity()).setUpNavOn();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setContentListPosition(int contentListPosition) {
        this.detailItemPosition = contentListPosition;
        textView.setText("Item " + detailItemPosition);
    }
}
