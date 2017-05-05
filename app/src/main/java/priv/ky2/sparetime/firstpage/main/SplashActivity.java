package priv.ky2.sparetime.firstpage.main;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.service.CacheService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, SplashFragment.getInstance())
                .commit();

        startService(new Intent(this, CacheService.class));
    }

    @Override
    protected void onDestroy() {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (CacheService.class.getName().equals(serviceInfo.service.getClassName())) {
                stopService(new Intent(this, CacheService.class));
            }
        }

        super.onDestroy();
    }
}
