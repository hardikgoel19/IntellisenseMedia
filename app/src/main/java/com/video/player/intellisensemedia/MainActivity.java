package com.video.player.intellisensemedia;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.video.player.intellisensemedia.fragments.DeviceFragment;
import com.video.player.intellisensemedia.fragments.HistoryFragment;
import com.video.player.intellisensemedia.fragments.HomeFragment;
import com.video.player.intellisensemedia.fragments.LibraryFragment;
import com.video.player.intellisensemedia.utils.Databases;

import java.util.Stack;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction transaction;
    private Stack<Integer> fragmentStack = new Stack<>();
    private ActionBar actionBar;

    //DECLARE FRAGMENTS
    private DeviceFragment deviceFragment;
    private HomeFragment homeFragment;
    private LibraryFragment libraryFragment;
    private HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GET SUPPORT ACTION BAR
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            //SET ACTION BAR ICON
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.icon);
            //SET ACTION BAR TITLE
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'> Home </font>"));
        }

        //INITIALIZE
        init();

    }

    private void init() {

        //FIND VIEWS
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //LISTENERS
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //OPEN HOME FRAGMENT AS DEFAULT
        openFragment(R.id.home);
    }

    private void openFragment(int id) {
        transaction = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.home:
                if (homeFragment == null)
                    homeFragment = new HomeFragment();
                fragmentStack.push(R.id.home);
                transaction.replace(R.id.frame_container, homeFragment);
                transaction.commit();
                if (actionBar != null)
                    actionBar.setTitle(Html.fromHtml("<font color='#ffffff'> Home </font>"));
                break;
            case R.id.device:
                if (deviceFragment == null)
                    deviceFragment = new DeviceFragment();
                fragmentStack.push(R.id.device);
                transaction.replace(R.id.frame_container, deviceFragment);
                transaction.commit();
                if (actionBar != null)
                    actionBar.setTitle(Html.fromHtml("<font color='#ffffff'> Device </font>"));
                break;
            case R.id.library:
                if (libraryFragment == null)
                    libraryFragment = new LibraryFragment();
                fragmentStack.push(R.id.library);
                transaction.replace(R.id.frame_container, libraryFragment);
                transaction.commit();
                if (actionBar != null)
                    actionBar.setTitle(Html.fromHtml("<font color='#ffffff'> Library </font>"));
                break;
            case R.id.history:
                if (historyFragment == null)
                    historyFragment = new HistoryFragment();
                fragmentStack.push(R.id.history);
                transaction.replace(R.id.frame_container, historyFragment);
                transaction.commit();
                if (actionBar != null)
                actionBar.setTitle(Html.fromHtml("<font color='#ffffff'> History </font>"));
                if (actionBar != null)
                    break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            switch (fragmentStack.peek()) {
                case R.id.home:
                    homeFragment.doReload();
                    break;
                case R.id.device:
                    deviceFragment.doReload();
                    break;
                case R.id.library:
                    libraryFragment.doReload();
                    break;
                case R.id.history:
                    historyFragment.doReload();
                    break;
            }
        }
        else if(item.getItemId() == R.id.database){
            showDatabases();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatabases() {
        Databases databases = new Databases(this);
        databases.start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        openFragment(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!fragmentStack.empty())
            fragmentStack.pop();
        if (!fragmentStack.empty())
            bottomNavigationView.setSelectedItemId(fragmentStack.pop());
        else
            super.onBackPressed();
    }
}
//TODO : ADD PERMISSION MANAGEMENT
//TODO : ADD MOTION DETECTION IN PLAYER
//TODO : GET OFFLINE MEDIA STAMP
//TODO : SETTINGS ACTIVITY