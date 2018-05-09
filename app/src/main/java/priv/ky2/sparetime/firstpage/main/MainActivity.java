package priv.ky2.sparetime.firstpage.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.about.AboutActivity;
import priv.ky2.sparetime.collection.CollectionFragment;
import priv.ky2.sparetime.collection.CollectionPresenter;
import priv.ky2.sparetime.systemsettings.SystemSettingsActivity;
import priv.ky2.sparetime.utils.ConstantString;
import priv.ky2.sparetime.utils.SharedPreferenceUtil;

import static priv.ky2.sparetime.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainFragment mMainFragment;
    private CollectionFragment mCollectionFragment;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    private ImageView mHeadImageView;
    private TextView mNameTextView;

    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径

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

        mHeadImageView = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.iv_head);
        mNameTextView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_name);
        mNameTextView.setText(SharedPreferenceUtil.getString(ConstantString.USER_NAME, "路人"));
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            mHeadImageView.setImageDrawable(drawable);
        } else {
            //如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
        }

        mHeadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeDialog();
            }
        });

        mNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNameDialog();
            }
        });
    }

    /**
     * 编辑头像弹窗
     */
    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.layout_chang_head, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void showEditNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.layout_change_name, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_name);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 500);
        Button button = (Button) view.findViewById(R.id.btn_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (name != null && !TextUtils.isEmpty(name)){
                    SharedPreferenceUtil.setString(ConstantString.USER_NAME, name);
                    Logger.d(SharedPreferenceUtil.getString(ConstantString.USER_NAME, ""));
                    mNameTextView.setText(SharedPreferenceUtil.getString(ConstantString.USER_NAME, ""));
                }
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        mHeadImageView.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
