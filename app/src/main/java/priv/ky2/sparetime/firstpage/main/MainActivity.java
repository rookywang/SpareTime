package priv.ky2.sparetime.firstpage.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.about.AboutActivity;
import priv.ky2.sparetime.collection.CollectionFragment;
import priv.ky2.sparetime.collection.CollectionPresenter;
import priv.ky2.sparetime.systemsettings.SystemSettingsActivity;

import static priv.ky2.sparetime.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainFragment mMainFragment;
    private CollectionFragment mCollectionFragment;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    public static final String ACTION_COLLECTION = "pri.ky2.sparetime.collection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        if (savedInstanceState != null) {
            mMainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MainFragment");
            mCollectionFragment = (CollectionFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CollectionFragment");
        } else {
            mMainFragment = MainFragment.newInstance();
            mCollectionFragment = CollectionFragment.newInstance();
        }

        if (!mMainFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_fragment, mMainFragment, "MainFragment")
                    .commit();
        }

        if (!mCollectionFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.layout_fragment, mCollectionFragment, "CollectionFragment")
                    .commit();
        }

        new CollectionPresenter(MainActivity.this, mCollectionFragment);

        String action = getIntent().getAction();

        if (action != null && action.equals(ACTION_COLLECTION)) {
            showCollectionFragment();
            mNavigationView.setCheckedItem(R.id.nav_collection);
        } else {
            showMainFragment();
            mNavigationView.setCheckedItem(R.id.nav_home);
        }

//        startService(new Intent(this, CacheService.class));
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void showMainFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(mMainFragment);
        fragmentTransaction.hide(mCollectionFragment);
        fragmentTransaction.commit();

        mToolbar.setTitle(getResources().getString(R.string.app_name));

    }

    private void showCollectionFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(mCollectionFragment);
        fragmentTransaction.hide(mMainFragment);
        fragmentTransaction.commit();

        mToolbar.setTitle(getResources().getString(R.string.nav_collection));

        if (mCollectionFragment.isAdded()) {
            mCollectionFragment.notifyDataChanged();
        }

    }

//    @Override
//    protected void onDestroy() {
//
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (CacheService.class.getName().equals(serviceInfo.service.getClassName())) {
//                stopService(new Intent(this, CacheService.class));
//            }
//        }
//
//        super.onDestroy();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        mDrawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {

            case R.id.nav_home:
                showMainFragment();
                break;

            case R.id.nav_collection:
                showCollectionFragment();
                break;

            case R.id.nav_change_theme:
                changeTheme();
                break;

            case R.id.nav_settings:
                startActivity(new Intent(this, SystemSettingsActivity.class));
                break;

            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;

            default:

                break;
        }

        return true;
    }

    private void changeTheme() {
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
                if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                        == Configuration.UI_MODE_NIGHT_YES) {
                    sp.edit().putInt("theme", 0).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    sp.edit().putInt("theme", 1).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mMainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "MainFragment", mMainFragment);
        }

        if (mCollectionFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "CollectionFragment", mCollectionFragment);
        }
    }

    private boolean quit = false; //设置退出标识

    @Override
    public void onBackPressed() {
        if (!quit) { //询问退出程序
//            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Snackbar.make(mToolbar, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
            new Timer(true).schedule(new TimerTask() { //启动定时任务
                @Override
                public void run() {
                    quit = false; //重置退出标识
                }
            }, 2000); //2秒后运行run()方法
            quit = true;
        } else { //确认退出程序
            super.onBackPressed();
            finish();
        }
    }
}
