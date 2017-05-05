package priv.ky2.sparetime.chromecustomtabs;

import android.support.customtabs.CustomTabsClient;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public interface ServiceConnectionCallback {

    /**
     * Called when the service is connected.
     * @param client a CustomTabsClient
     */
    void onServiceConnected(CustomTabsClient client);

    /**
     * Called when the service is disconnected.
     */
    void onServiceDisconnected();
}
