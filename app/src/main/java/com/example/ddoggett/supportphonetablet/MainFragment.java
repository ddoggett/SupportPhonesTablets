package com.example.ddoggett.supportphonetablet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dylan Doggett on 11/15/15.
 */
public class MainFragment extends Fragment implements MainActivity.FragmentOnBackPressedListener {
    public static final String IS_DETAIL_SHOWING = "IS_DETAIL_SHOWING";
    public static final String DETAIL_ITEM_POSITION = "DETAIL_ITEM_POSITION";
    private static final int ANIM_DURATION = 1000;
    View root;
    protected boolean isTablet = false;
    protected boolean isPortrait = false;
    protected boolean isDetailShowing = false;
    private ContentListFragment contentListFragment;
    private Fragment detailView;

    // state
    public int detailItemPosition = -1;
    private String[] contentNames;
    private int[] contentImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTablet = getResources().getBoolean(R.bool.isTablet);
        isPortrait = getResources().getBoolean(R.bool.isPortrait);

        if(savedInstanceState!=null) {
            detailItemPosition = savedInstanceState.getInt(DETAIL_ITEM_POSITION, -1);
            isDetailShowing = savedInstanceState.getBoolean(IS_DETAIL_SHOWING, false);
            contentNames = savedInstanceState.getStringArray(MainActivity.EXTRA_CONTENT_NAMES);
            contentImages = savedInstanceState.getIntArray(MainActivity.EXTRA_CONTENT_IMAGES);
        } else {
            contentNames = getArguments().getStringArray(MainActivity.EXTRA_CONTENT_NAMES);
            contentImages = getArguments().getIntArray(MainActivity.EXTRA_CONTENT_IMAGES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.content_view, null, false);
            if (isTablet && !isPortrait) {
                addTabletViews();
            } else {
                addPhoneViews();
            }

        return root;
    }

    private void addTabletViews() {
        FragmentTransaction lft = getChildFragmentManager().beginTransaction();
        contentListFragment = new ContentListFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(MainActivity.EXTRA_CONTENT_NAMES, contentNames);
        bundle.putIntArray(MainActivity.EXTRA_CONTENT_IMAGES, contentImages);
        contentListFragment.setArguments(bundle);
        lft.replace(R.id.leftFragmentContainer, contentListFragment).commit();
    //    rft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
    //    rft.replace(R.id.rightFragmentContainer, getDefaultFragmentWithArgs()).commit();
    //    ((BaseDrawerActivity) getActivity()).getSecondaryToolbar().setTitle("");
    }

    private void addPhoneViews() {
        contentListFragment = new ContentListFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(MainActivity.EXTRA_CONTENT_NAMES, contentNames);
        bundle.putIntArray(MainActivity.EXTRA_CONTENT_IMAGES, contentImages);
        contentListFragment.setArguments(bundle);
        showPrimaryFragment(contentListFragment, false, null, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isDetailShowing) {
            if (isTablet && !isPortrait) {
                showSecondaryFragment(ContentDetailFragment.newInstance(detailItemPosition), false, null, false);
            } else {
                showPrimaryFragment(ContentDetailFragment.newInstance(detailItemPosition), false, null, false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected void contentListItemClicked(int i) {
        if (isTablet && !isPortrait) {
            showSecondaryFragment(ContentDetailFragment.newInstance(i), false, null, true);
        } else {
            showPrimaryFragment(ContentDetailFragment.newInstance(i), false, null, true);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) getActivity()).setUpNavOn();
            }
        }, 250);
        detailItemPosition = i;
        isDetailShowing = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_DETAIL_SHOWING, isDetailShowing);
        outState.putInt(DETAIL_ITEM_POSITION, detailItemPosition);
        outState.putStringArray(MainActivity.EXTRA_CONTENT_NAMES, contentNames);
        outState.putIntArray(MainActivity.EXTRA_CONTENT_IMAGES, contentImages);
    }

    public void showFragment(Fragment fragment, boolean primary, boolean addToBackStack, String backStackTag, boolean animate) {
        if(fragment == null) {
            return;
        }

        FragmentTransaction lft = getChildFragmentManager().beginTransaction();
        if (isTablet && !isPortrait) {
            if (primary) {
                if (addToBackStack) {
                    lft.replace(R.id.leftFragmentContainer, fragment).addToBackStack(backStackTag).commit();
                } else {
                    lft.replace(R.id.leftFragmentContainer, fragment).commit();
                }
            } else {
                if (addToBackStack) {
                    lft.replace(R.id.rightFragmentContainer, fragment).addToBackStack(backStackTag).commit();
                } else {
                    lft.replace(R.id.rightFragmentContainer, fragment).commit();
                }
            }
        } else {
            if(animate) {
                // fragment animation
                if (fragment instanceof ContentListFragment) {
                    lft.setCustomAnimations(R.anim.slide_right, R.anim.slide_out_left);
                } else {
                    lft.setCustomAnimations(R.anim.slide_left, R.anim.slide_out_right);
                }
                // hamburger icon animationr
                /*((MainActivity)getActivity()).getToolbar().get().animate()
                        .rotationBy(180).setDuration(ANIM_DURATION)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }
                        })
                        .start();*/
            }
            if (addToBackStack) {
                lft.replace(R.id.fragmentContainer, fragment).addToBackStack(backStackTag).commit();
            } else {
                lft.replace(R.id.fragmentContainer, fragment).commit();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   // ((MainActivity) getActivity()).setUpNavOn();
                }
            }, 250);
        }
    }

    public void showFragment(Fragment fragment, boolean primary) {
        showFragment(fragment, primary, true, null, false);
    }

    public void showPrimaryFragment(Fragment fragment) {
        showFragment(fragment, true);
    }

    public void showPrimaryFragment(Fragment fragment, boolean addToBackStack, String backStackTag, boolean animate) {
        showFragment(fragment, true, addToBackStack, backStackTag, animate);
    }

    public void showSecondaryFragment(Fragment fragment) {
        showFragment(fragment, false);
    }

    public void showSecondaryFragment(Fragment fragment, boolean addToBackStack, String backStackTag, boolean animate) {
        showFragment(fragment, false, addToBackStack, backStackTag, animate);
    }


    @Override
    public boolean onBackPressed() {
        if(isTablet && !isPortrait){
            return false;
        }else {
            Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (currentFragment instanceof ContentDetailFragment) {
                contentListFragment = new ContentListFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArray(MainActivity.EXTRA_CONTENT_NAMES, contentNames);
                bundle.putIntArray(MainActivity.EXTRA_CONTENT_IMAGES, contentImages);
                contentListFragment.setArguments(bundle);
                showPrimaryFragment(contentListFragment, false, null, true);
                isDetailShowing = false;
                // going back to main content
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).setUpNavOff();
                    }
                }, 250);
                return true;
            } else {
                return false;
            }
        }
    }

    public ContentDetailFragment getDetailView() {
        ContentDetailFragment fragment;
        int id;
        if(isTablet && !isPortrait) {
            id = R.id.rightFragmentContainer;
        } else {
            id = R.id.fragmentContainer;
        }
        fragment = (ContentDetailFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(id);
        return fragment;
    }

    public static Fragment newInstance(Bundle bundle) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
