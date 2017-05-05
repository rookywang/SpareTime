package priv.ky2.sparetime.chromecustomtabs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import priv.ky2.sparetime.innerbrowser.InnerBrowserActivity;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public class CustomFallback implements ChromeCustomTabActivityHelper.CustomTabFallback {

    @Override
    public void openUri(Activity activity, Uri uri) {
        activity.startActivity(new Intent(activity, InnerBrowserActivity.class).putExtra("url", uri.toString()));
    }

}
