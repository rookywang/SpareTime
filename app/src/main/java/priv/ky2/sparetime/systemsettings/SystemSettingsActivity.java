package priv.ky2.sparetime.systemsettings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import priv.ky2.sparetime.R;

public class SystemSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);

        initViews();

        Fragment fragment = SystemSettingsFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, fragment)
                .commit();

        new SystemSettingsPresenter(SystemSettingsActivity.this, (SystemSettingsContract.View) fragment);

    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
