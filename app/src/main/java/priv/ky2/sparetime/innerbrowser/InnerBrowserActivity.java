package priv.ky2.sparetime.innerbrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import priv.ky2.sparetime.R;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public class InnerBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, InnerBrowserFragment.getInstance())
                .commit();

    }
}
