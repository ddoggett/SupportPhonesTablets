package com.example.ddoggett.supportphonetablet;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by Dylan Doggett on 11/14/15.
 */
public class MainActivity extends AppCompatActivity {
    private static final String EXTRA_IS_DRAWER_OPEN = "EXTRA_IS_DRAWER_OPEN";
    private static final String EXTRA_IS_SECONDARY_SHOWN = "EXTRA_IS_SECONDARY_SHOWN";
    private static final String EXTRA_NAVIGATION_SELECTION = "EXTRA_NAVIGATION_SELECTION";
    private static final String EXTRA_SHOULD_USE_UP = "EXTRA_SHOULD_USE_UP";
    private static final int MOVIES = 0;
    private static final int MUSIC = 1;
    private static final int TELEVISION = 2;
    private static final int BOOKS = 3;
    public static final String EXTRA_CONTENT_NAMES = "EXTRA_CONTENT_NAMES";
    public static final String EXTRA_CONTENT_IMAGES = "EXTRA_CONTENT_IMAGES";
    private String[] navDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private NavDrawerAdapter adapter;
    private Toolbar toolbar, secondaryToolbar;
    protected boolean isTablet = false;
    protected boolean isSecondaryShown = false;
    protected boolean lockDrawer = false;
    private boolean isDrawerOpen = false;
    private FrameLayout toolbarDivider;
    private FrameLayout mainContent;
    private ActionBarDrawerToggle drawerToggle = null;
    private float lastTranslate = 0.0f;
    private boolean shouldUseUp = false;
    private boolean isPortrait;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isPortrait = getResources().getBoolean(R.bool.isPortrait);
        isTablet = getResources().getBoolean(R.bool.isTablet);
        navDrawerItems = getResources().getStringArray(R.array.drawer_options);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setDivider(null);
        mDrawerList.setDividerHeight(0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        secondaryToolbar = (Toolbar) findViewById(R.id.secondaryToolbar);
        toolbarDivider = (FrameLayout) findViewById(R.id.toolbarDivider);
        mainContent = (FrameLayout) findViewById(R.id.content_container);

        if (savedInstanceState == null) {
            replaceFragment(0);
        } else {
            // we're being restored from a previous state
            int selectedPosition = savedInstanceState.getInt(EXTRA_NAVIGATION_SELECTION);
            mDrawerList.setSelection(selectedPosition);
            // on ICS orientation change, onPrepareOptionsMenu(Menu) is called before
            // onPostCreate(Bundle), so we'll save drawer state ourselves since
            // DrawerLayout will be null in onPrepareOptionsMenu(Menu)
            isDrawerOpen = savedInstanceState.getBoolean(EXTRA_IS_DRAWER_OPEN);
            isSecondaryShown = savedInstanceState.getBoolean(EXTRA_IS_SECONDARY_SHOWN);
            shouldUseUp = savedInstanceState.getBoolean(EXTRA_SHOULD_USE_UP);
        }

        // show secondary toolbar if needed
        if (isSecondaryShown) {
            setSecondaryToolbarVisibility(true);
        }
        // set toolbar
        setSupportActionBar(toolbar);
        if (isTablet) {
            // On tablets, the nav drawer has the same elevation as the primary content
            // so we don't want nav drawer fading the primary content when opening
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        }

        // set up nav drawer
        adapter = new NavDrawerAdapter(this);
        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onDrawerItemClicked(i);
            }
        });

        /*
           The navigation drawer will be different between phone and tablet.
           Tablets will have a nav drawer that is clipped under the toolbar and shares same elevation as the primary content.
           Phones will have a temporary navigation drawer that sits above all content when in open state.
         */
        if(isTablet) {
            drawerToggle = new ActionBarDrawerToggle(this, getDrawerLayout(), getToolbar(), R.string.drawer_open,
                    R.string.drawer_close) {

                @Override
                public boolean onOptionsItemSelected(MenuItem item) {
                    return super.onOptionsItemSelected(item);
                }

                @Override
                public void onDrawerOpened(View drawerView) {

                }

                @SuppressLint("NewApi")
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    float moveFactor = (mDrawerList.getWidth() * slideOffset);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        mainContent.setTranslationX(moveFactor);
                    } else {
                        TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                        anim.setDuration(0);
                        anim.setFillAfter(true);
                        mainContent.startAnimation(anim);
                        lastTranslate = moveFactor;
                    }
                }
            };



       //     getSupportActionBar().setDisplayHomeAsUpEnabled(false);
       //     getSupportActionBar().setHomeButtonEnabled(false);
       //     drawerToggle.setDrawerIndicatorEnabled(true);


        } else {
            drawerToggle = new ActionBarDrawerToggle(this, getDrawerLayout(), getToolbar(), R.string.drawer_open,
                    R.string.drawer_close) {

                @Override
                public boolean onOptionsItemSelected(MenuItem item) {
                    return super.onOptionsItemSelected(item);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
        }
        mDrawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void onDrawerOpened() {
        // clear menu and toggle title
        isDrawerOpen = true;
        toolbar.getMenu().clear();

        // give our drawer focus so it handles onBackPress events
        getDrawerLayout().requestFocus();
    }

    private void onDrawerClosed() {
        // restore menu and toggle title
        isDrawerOpen = false;
        toolbar.showOverflowMenu();

        supportInvalidateOptionsMenu();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerLayout.closeDrawer(GravityCompat.START);
        drawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTablet) {
            setSecondaryToolbarVisibility(true);
        }

        if(shouldUseUp) {
            setUpNavOn();
        } else {
            setUpNavOff();
        }


        //   replaceFragment(0);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // don't display action bar menu items when drawer is open
/*        if (isDrawerOpen()) {
            menu.clear();
        }*/
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
//        if (shouldUseUp) {
//            onBackPressed();
//            return true;
//        } else {
//            if (!lockDrawer) {
//                if (drawerToggle.onOptionsItemSelected(item)) {
//                    return true;
//                }
//            }
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            drawerToggle.onConfigurationChanged(newConfig);

    }

    /**
     * Call to determine if the drawer is currently open
     *
     * @return true if the drawer is open
     */
    public boolean isDrawerOpen() {
        return isDrawerOpen;
    }

    /**
     * Call this method to lock the navigation drawer closed, preventing the
     * user from swiping the edge to pull out the drawer.
     *
     * @param lockDrawer - true to lock the drawer closed, false to unlock
     */
    public void lockDrawer(boolean lockDrawer) {
        this.lockDrawer = lockDrawer;
        if (mDrawerLayout != null) {
            if (lockDrawer) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }

    }

    /**
     * returns the tool bar view
     *
     * @return
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    public Toolbar getSecondaryToolbar() {
        return secondaryToolbar;
    }

    public void setSecondaryToolbarVisibility(boolean visible) {
        if (visible) {
            secondaryToolbar.setVisibility(View.VISIBLE);
        } else {
            if (secondaryToolbar != null && toolbarDivider != null) {
                secondaryToolbar.setVisibility(View.GONE);
                toolbarDivider.setVisibility(View.GONE);
            }
        }
    }

    private void onDrawerItemClicked(final int position) {
        if (position == mDrawerList.getSelectedItemPosition()) {
            // don't do anything if this is already the selected position
            getDrawerLayout().closeDrawers();
            return;
        }
        // give the fragment time to be replaced before closing the drawer
        // this creates the effect that the drawer closes to reveal the new
        // content and prevents animation lag
        getDrawerLayout().closeDrawers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceFragment(position);
            }
        }, 300);
    }

    /**
     *
     * @param position
     */
    private void replaceFragment(int position) {
        Bundle bundle = getContentBundle(position);
        Fragment homeFragment = MainFragment.newInstance(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_container, homeFragment);
            ft.commit();
    }

    private Bundle getContentBundle(int position) {
        String[] contentNames = new String[0];
        int[] imageResources = new int[0];
        switch (position) {
            case 0:
                contentNames = getResources().getStringArray(R.array.film_array);
                imageResources = getResources().getIntArray(R.array.film_images);
                break;
            case 1:
                contentNames = getResources().getStringArray(R.array.music_array);
                imageResources = getResources().getIntArray(R.array.music_images);
                break;
            case 2:
                contentNames = getResources().getStringArray(R.array.television_array);
                imageResources = getResources().getIntArray(R.array.television_images);
                break;
            case 3:
                contentNames = getResources().getStringArray(R.array.books_array);
                imageResources = getResources().getIntArray(R.array.books_images);
                break;
            default:
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_CONTENT_NAMES, contentNames);
        bundle.putIntArray(EXTRA_CONTENT_IMAGES, imageResources);

        return bundle;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // only save cache state if the app is being put in the background, not
        // if the device is changing orientation
        int newOrientation = getResources().getConfiguration().orientation;
//        if (orientation == newOrientation && !calledFromActivity) {
            //getApplication().onSaveInstanceState(outState);
//        }
        outState.putInt(EXTRA_NAVIGATION_SELECTION,
                mDrawerList.getSelectedItemPosition());
        outState.putBoolean(EXTRA_IS_DRAWER_OPEN, isDrawerOpen);
        outState.putBoolean(EXTRA_IS_SECONDARY_SHOWN, isSecondaryShown);
        outState.putBoolean(EXTRA_SHOULD_USE_UP, shouldUseUp);
    }

    public NavDrawerAdapter getAdapter() {
        return adapter;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_container);
        if (currentFragment == null) {
            return;
        }
        // fragment to handle on back pressed,
        if (currentFragment instanceof FragmentOnBackPressedListener) {
            if (((FragmentOnBackPressedListener) currentFragment).onBackPressed()) {
                return;
            }
        }
    }

    public interface FragmentOnBackPressedListener {
        /**
         * Method callback to handle an activity's onBackPressed event
         *
         * @return true if the fragment consumed the back press
         */
        boolean onBackPressed();
    }


    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    public void setDrawerToggle(ActionBarDrawerToggle drawerToggle) {
        this.drawerToggle = drawerToggle;
    }


    public void setUpNavOn() {
        if (!isTablet || isPortrait) {
            shouldUseUp = true;
            drawerToggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
            getDrawerToggle().setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void setUpNavOff() {
        if (!isTablet || isPortrait) {
            shouldUseUp = false;
            drawerToggle.setDrawerIndicatorEnabled(true);
        }

    }
}
