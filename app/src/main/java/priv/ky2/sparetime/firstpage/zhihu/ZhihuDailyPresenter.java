package priv.ky2.sparetime.firstpage.zhihu;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import priv.ky2.sparetime.bean.BeanType;
import priv.ky2.sparetime.bean.StringModelImplement;
import priv.ky2.sparetime.bean.ZhihuDailyNews;
import priv.ky2.sparetime.database.DatabaseHelper;
import priv.ky2.sparetime.details.DetailsActivity;
import priv.ky2.sparetime.network.NetworkState;
import priv.ky2.sparetime.network.OnStringListener;
import priv.ky2.sparetime.network.Urls;
import priv.ky2.sparetime.service.CacheService;
import priv.ky2.sparetime.utils.DateFormatter;

/**
 * Created by wangkaiyan on 2017/4/18.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {

    private ZhihuDailyContract.View view;
    private Context context;
    private StringModelImplement model;

    private DateFormatter formatter = new DateFormatter();
    private Gson gson = new Gson();

    private ArrayList<ZhihuDailyNews.Question> list = new ArrayList<ZhihuDailyNews.Question>();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ZhihuDailyPresenter(Context context, ZhihuDailyContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImplement(context);
        dbHelper = new DatabaseHelper(context, "History.db", null, 5);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void loadPosts(long date, final boolean clearing) {

        if (clearing) {
            view.showLoading();
        }

        if (NetworkState.networkConnected(context)) {

            model.load(Urls.ZHIHU_HISTORY + formatter.ZhihuDailyDateFormat(date), new OnStringListener() {
                @Override
                public void onSuccess(String result) {

                    try {

                        Logger.json(result);

                        ZhihuDailyNews post = gson.fromJson(result, ZhihuDailyNews.class);
                        ContentValues values = new ContentValues();

                        if (clearing) {
                            list.clear();
                        }

                        for (ZhihuDailyNews.Question item : post.getStories()) {
                            list.add(item);
                            if ( !queryIfIDExists(item.getId())) {
                                db.beginTransaction();
                                try {
                                    DateFormat format = new SimpleDateFormat("yyyyMMdd");
                                    Date date = format.parse(post.getDate());
                                    values.put("zhihu_id", item.getId());
                                    values.put("zhihu_news", gson.toJson(item));
                                    values.put("zhihu_content", "");
                                    values.put("zhihu_time", date.getTime() / 1000);
                                    db.insert("Zhihu", null, values);
                                    values.clear();
                                    db.setTransactionSuccessful();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    db.endTransaction();
                                }

                            }
                            Intent intent = new Intent("com.marktony.zhihudaily.LOCAL_BROADCAST");
                            intent.putExtra("type", CacheService.TYPE_ZHIHU);
                            intent.putExtra("id", item.getId());
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

            if (clearing) {

                list.clear();

                Cursor cursor = db.query("Zhihu", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        ZhihuDailyNews.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")), ZhihuDailyNews.Question.class);
                        list.add(question);
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
    }

    @Override
    public void refresh() {
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    @Override
    public void loadMore(long date) {
        loadPosts(date, false);
    }

    @Override
    public void startReading(int position) {

        context.startActivity(new Intent(context, DetailsActivity.class)
                .putExtra("type", BeanType.TYPE_ZHIHU)
                .putExtra("id", list.get(position).getId())
                .putExtra("title", list.get(position).getTitle())
                .putExtra("coverUrl", list.get(position).getImages().get(0)));

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
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    private boolean queryIfIDExists(int id){

        Cursor cursor = db.query("Zhihu",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("zhihu_id"))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }

}
