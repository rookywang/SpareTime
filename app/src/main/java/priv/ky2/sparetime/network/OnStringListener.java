package priv.ky2.sparetime.network;

import com.android.volley.VolleyError;

/**
 * Created by wangkaiyan on 2017/4/18.
 */

public interface OnStringListener {

    /**
     * 请求成功时回调
     * @param result
     */
    void onSuccess(String result);

    /**
     * 请求失败时回调
     * @param error
     */
    void onError(VolleyError error);
}
