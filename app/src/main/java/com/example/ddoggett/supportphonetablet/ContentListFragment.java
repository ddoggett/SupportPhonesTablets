package com.example.ddoggett.supportphonetablet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Dylan Doggett on 11/15/15.
 */
public class ContentListFragment extends Fragment{
    private View root;
    private String[] contentItemsArray;
    private ArrayAdapter adapter;
    private ListView contentList;
    private int[] contentImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            contentItemsArray = savedInstanceState.getStringArray(MainActivity.EXTRA_CONTENT_NAMES);
            contentImages = savedInstanceState.getIntArray(MainActivity.EXTRA_CONTENT_IMAGES);
        } else {
            contentItemsArray = getArguments().getStringArray(MainActivity.EXTRA_CONTENT_NAMES);
            contentImages = getArguments().getIntArray(MainActivity.EXTRA_CONTENT_IMAGES);
        }

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, contentItemsArray);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.content_list_view, null, false);
        contentList = (ListView)root.findViewById(R.id.content_list);
        contentList.setAdapter(adapter);
        contentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainFragment)getParentFragment()).contentListItemClicked(i + 1);
            }
        });
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(MainActivity.EXTRA_CONTENT_NAMES, contentItemsArray);
        outState.putIntArray(MainActivity.EXTRA_CONTENT_IMAGES, contentImages);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      //  ((MainActivity)getActivity()).setUpNavOff();
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
}
