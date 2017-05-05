package priv.ky2.sparetime.chromecustomtabs;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public class KeepAliveService extends Service {

    private static final Binder sBinder = new Binder();

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }
}
