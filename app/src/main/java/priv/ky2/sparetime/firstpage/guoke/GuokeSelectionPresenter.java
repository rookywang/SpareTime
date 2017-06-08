package priv.ky2.sparetime.firstpage.guoke;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Random;

import priv.ky2.sparetime.bean.BeanType;
import priv.ky2.sparetime.bean.GuokeSelectionNews;
import priv.ky2.sparetime.network.StringModelImplement;
import priv.ky2.sparetime.database.DatabaseHelper;
import priv.ky2.sparetime.details.DetailsActivity;
import priv.ky2.sparetime.network.OnStringListener;
import priv.ky2.sparetime.network.Urls;
import priv.ky2.sparetime.service.CacheService;
import priv.ky2.sparetime.network.NetworkState;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public class GuokeSelectionPresenter implements GuokeSelectionContract.Presenter {

    private GuokeSelectionContract.View view;
    private Context context;
    private StringModelImplement model;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private ArrayList<GuokeSelectionNews.result> list = new ArrayList<GuokeSelectionNews.result>();
    private Gson gson = new Gson();

    public GuokeSelectionPresenter(Context context, GuokeSelectionContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
        model = new StringModelImplement(context);
        dbHelper = new DatabaseHelper(context, "History.db", null, 5);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void startReading(int position) {
        GuokeSelectionNews.result item = list.get(position);
        context.startActivity(new Intent(context, DetailsActivity.class)
                .putExtra("type", BeanType.TYPE_GUOKE)
                .putExtra("id", item.getId())
                .putExtra("coverUrl", item.getHeadline_img())
                .putExtra("title", item.getTitle())
        );
    }

    @Override
    public void feelLucky() {
        if (list.isEmpty()) {
            view.showError();
            return;
        }
        startReading(new Random().nextInt(list.size()));
    }

    @Override
    public void start() {
        loadPosts();
    }

    @Override
    public void loadPosts() {

        view.showLoading();

        if (NetworkState.networkConnected(context)) {
            model.load(Urls.GUOKR_ARTICLES, new OnStringListener() {
                @Override
                public void onSuccess(String result) {

                    // 由于果壳并没有按照日期加载的api
                    // 所以不存在加载指定日期内容的操作，当要请求数据时一定是在进行刷新
                    list.clear();

                    try {

                        Logger.json(result);

                        GuokeSelectionNews question = gson.fromJson(result, GuokeSelectionNews.class);

                        for (GuokeSelectionNews.result re : question.getResult()){

                            list.add(re);

                            if(!queryIfIDExists(re.getId())) {
                                try {
                                    db.beginTransaction();
                                    ContentValues values = new ContentValues();
                                    values.put("guokr_id", re.getId());
                                    values.put("guokr_news", gson.toJson(re));
                                    values.put("guokr_content", "");
                                    values.put("guokr_time", (long)re.getDate_picked());
                                    db.insert("Guokr", null, values);
                                    values.clear();
                                    db.setTransactionSuccessful();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    db.endTransaction();
                                }

                            }

                            Intent intent = new Intent("pri.ky2.sparetime.LOCAL_BROADCAST");
                            intent.putExtra("type", CacheService.TYPE_GUOKR);
                            intent.putExtra("id", re.getId());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        }
                        view.showResults(list);

                    } catch (JsonSyntaxException e) {
                        view.showError();
                    }

                    view.stopLoading();

                }

                @Override
                public void onError(VolleyError error) {
                    view.stopLoading();
                    view.showError();
                }
            });
        } else {

            Logger.d("没有网络");

            Cursor cursor = db.query("Guokr", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    GuokeSelectionNews.result result = gson.fromJson(cursor.getString(cursor.getColumnIndex("guokr_news")), GuokeSelectionNews.result.class);
                    list.add(result);
                } while (cursor.moveToNext());
            }
            cursor.close();
            view.stopLoading();
            view.showResults(list);
            //当第一次安装应用，并且没有打开网络时
            //此时既无法网络加载，也无法本地加载
            if (list.isEmpty()) {
                view.showError();
            }
        }
    }

    @Override
    public void refresh() {
        loadPosts();
    }

    private boolean queryIfIDExists(int id){
        Cursor cursor = db.query("Guokr",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("guokr_id"))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }
}
