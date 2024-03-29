package com.example.eliferbil.quickquiz;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eliferbil.quickquiz.memogame.MatchingBaseFragment;
import com.example.eliferbil.quickquiz.memogame.TabletMemoTransitionManager;
import com.example.eliferbil.quickquiz.quickquiz.GameFragment;
import com.example.eliferbil.quickquiz.quickquiz.QuestionFragment;
import com.example.eliferbil.quickquiz.quickquiz.QuickQuizTabletTransitionManager;


public class MainActivity extends AppCompatActivity implements TabletActivity, TransitionManager.Provider {

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;

    BackPressedListener bpl;
    TransitionManager transitionManager;
    public static String[] gamesList = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gamesList[0] = "Quick Quiz";
        gamesList[1] = "Flag Match";
        gamesList[2] = "Profile";
        gamesList[3] = "Friend List";
        gamesList[4] = "High Scores";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Populate the ListView
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, gamesList));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer) {
            //Called when a drawer has settled in a completely closed state
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            //Called when a drawer has settled in a completely open state.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        pushDetailFragment(new ScoreboardFragment(), false);
    }


    public int gameId = 0;

    private void selectItem(int position) {
        // update the main content by replacing fragments

        //int currentPosition = position;
        Fragment fragment;
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                bundle.putInt("gameId", 0);
                fragment = GameSelectionFragment.newInstance(bundle);
                break;
            case 1:
                bundle.putInt("gameId", 1);
                fragment = GameSelectionFragment.newInstance(bundle);
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
            case 3:
                fragment = new FriendListFragment();
                break;
            case 4:
                fragment = new ScoreboardFragment();
                break;
            default:
                //fragment = new Fragment();
                throw new IllegalArgumentException("Unknown position");
        }

        pushDetailFragment(fragment);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.content_frame, fragment, "visible_fragment");
//        ft.addToBackStack(null);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();

        drawerLayout.closeDrawer(drawerList);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        if (menu.findItem(R.id.action_search_friend) != null) {
            menu.findItem(R.id.action_search_friend).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*@Override
    public void itemClicked(long id) {

        Game game = Game.getInstance();
        View fragmentContainer = findViewById(R.id.gameFragment);
        if (fragmentContainer != null) {
            User user;
            Fragment nextFragment;
            switch ((int) id) {
                case 0:
                    QuickQuizTabletTransitionManager tm = QuickQuizTabletTransitionManager.get(this);
                    bpl = tm;
                    transitionManager = tm;
                    getSupportFragmentManager().addOnBackStackChangedListener(tm);
                    String username = game.getUser().getUsername();
                    user = new User(username);
                    nextFragment = new GameFragment();
                    break;
                case 1:
                    bpl = null;
                    transitionManager = TabletMemoTransitionManager.get(this);
                    user = new MortalUser(game.getUser());
                    nextFragment = new GameSelectionFragment();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Id");
            }
            game.setUser(user);
            clearBackstack();
            pushDetailFragment(nextFragment);
        } else {
            User user;
            Class<?> nextActivity;
            switch ((int) id) {
                case 0:
                    String username = game.getUser().getUsername();
                    user = new User(username);
                    nextActivity = PhoneGameActivity.class;
                    break;
                case 1:
                    user = new MortalUser(game.getUser());
                    nextActivity = PhoneMemoActivity.class;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Id");
            }
            game.setUser(user);
            Intent intent = new Intent(this, nextActivity);
            startActivity(intent);
        }
    }*/

    private void clearBackstack() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        while (supportFragmentManager.getBackStackEntryCount() > 0) {
            supportFragmentManager.popBackStackImmediate();
        }
    }

    public void pushDetailFragment(Fragment fragment) {
        pushDetailFragment(fragment, true);
    }

    @Override
    public void pushDetailFragment(Fragment fragment, boolean addToBackStack) {

        FragmentManager sfm = getSupportFragmentManager();
        FragmentTransaction ft = sfm.beginTransaction();
        Fragment current = sfm.findFragmentById(R.id.content_frame);
        if (current != null) {
            ft.hide(current);
        }
        ft.add(R.id.content_frame, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void popDetailFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public Fragment getCurrentDetailFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    @Override
    public void startOver() {
        finish();

        Intent nextActivity = new Intent(this, MainActivity.class);
        startActivity(nextActivity);
    }

    @Override
    public void onBackPressed() {

        if (bpl == null || bpl.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                super.onBackPressed();
            } else {
                finish();
            }

        }
    }

    @Override
    public void endGame() {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setTransitionManager(TransitionManager tm) {
        bpl = null;
        transitionManager = tm;
    }

    @Override
    public void setBackPressedListener(BackPressedListener bpl) {
        this.bpl = bpl;
    }

    @Override
    public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener listener) {
        getSupportFragmentManager().addOnBackStackChangedListener(listener);
    }

    @Override
    public <T extends TransitionManager> T provide(Object tmUser) {
        return (T) selectTransitionManager(tmUser);
    }

    private TransitionManager selectTransitionManager(Object t) {
        if (t instanceof GameFragment || t instanceof QuestionFragment) {
            return QuickQuizTabletTransitionManager.get(this);
        } else if (t instanceof MatchingBaseFragment) {
            return TabletMemoTransitionManager.get(this);
        } else {
            throw new IllegalArgumentException(t.getClass().getName() + " is not known!");
        }
    }
}
