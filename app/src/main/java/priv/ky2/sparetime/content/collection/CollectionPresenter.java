package priv.ky2.sparetime.content.collection;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Random;

import priv.ky2.sparetime.adapter.CollectionAdapter;
import priv.ky2.sparetime.bean.BeanType;
import priv.ky2.sparetime.bean.DoubanMomentNews;
import priv.ky2.sparetime.bean.GuokeSelectionNews;
import priv.ky2.sparetime.bean.ZhihuDailyNews;
import priv.ky2.sparetime.database.DatabaseHelper;
import priv.ky2.sparetime.content.details.DetailsActivity;

import static priv.ky2.sparetime.adapter.CollectionAdapter.TYPE_DOUBAN_NORMAL;
import static priv.ky2.sparetime.adapter.CollectionAdapter.TYPE_DOUBAN_WITH_HEADER;
import static priv.ky2.sparetime.adapter.CollectionAdapter.TYPE_GUOKR_NORMAL;
import static priv.ky2.sparetime.adapter.CollectionAdapter.TYPE_GUOKR_WITH_HEADER;
import static priv.ky2.sparetime.adapter.CollectionAdapter.TYPE_ZHIHU_NORMAL;
import static priv.ky2.sparetime.adapter.CollectionAdapter.TYPE_ZHIHU_WITH_HEADER;

/**
 * Created by wangkaiyan on 2017/4/19.
 */

public class CollectionPresenter implements CollectionContract.Presenter {

    private CollectionContract.View view;
    private Context context;
    private Gson gson;

    private ArrayList<DoubanMomentNews.posts> doubanList;
    private ArrayList<GuokeSelectionNews.result> guokrList;
    private ArrayList<ZhihuDailyNews.Question> zhihuList;

    private ArrayList<Integer> types;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public CollectionPresenter(Context context, CollectionContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        gson = new Gson();
        dbHelper = new DatabaseHelper(context, "History.db", null, 5);
        db = dbHelper.getWritableDatabase();

        zhihuList = new ArrayList<>();
        guokrList = new ArrayList<>();
        doubanList = new ArrayList<>();

        types = new ArrayList<>();

    }

    @Override
    public void start() {

    }

    @Override
    public void loadResults(boolean refresh) {

        if (!refresh) {
            view.showLoading();
        } else {
            zhihuList.clear();
            guokrList.clear();
            doubanList.clear();
            types.clear();
        }

        checkForFreshData();

        view.showResults(zhihuList, guokrList, doubanList, types);

        view.stopLoading();

    }

    @Override
    public void startReading(BeanType type, int position) {
        Intent intent = new Intent(context, DetailsActivity.class);
        switch (type) {
            case TYPE_ZHIHU:
                ZhihuDailyNews.Question q = zhihuList.get(position - 1);
                intent.putExtra("type", BeanType.TYPE_ZHIHU);
                intent.putExtra("id",q.getId());
                intent.putExtra("title", q.getTitle());
                intent.putExtra("coverUrl", q.getImages().get(0));
                break;

            case TYPE_GUOKE:
                GuokeSelectionNews.result r = guokrList.get(position - zhihuList.size() - 2);
                intent.putExtra("type", BeanType.TYPE_GUOKE);
                intent.putExtra("id", r.getId());
                intent.putExtra("title", r.getTitle());
                intent.putExtra("coverUrl", r.getHeadline_img());
                break;
            case TYPE_DOUBAN:
                DoubanMomentNews.posts p = doubanList.get(position - zhihuList.size() - guokrList.size() - 3);
                intent.putExtra("type", BeanType.TYPE_DOUBAN);
                intent.putExtra("id", p.getId());
                intent.putExtra("title", p.getTitle());
                if (p.getThumbs().size() == 0){
                    intent.putExtra("coverUrl", "");
                } else {
                    intent.putExtra("image", p.getThumbs().get(0).getMedium().getUrl());
                }
                break;
            default:
                break;
        }
        context.startActivity(intent);
    }

    @Override
    public void checkForFreshData() {

        // every first one of the 3 lists is with header
        // add them in advance

        types.add(TYPE_ZHIHU_WITH_HEADER);
        Cursor cursor = db.rawQuery("select * from Zhihu where bookmark = ?", new String[]{"1"});
        if (cursor.moveToFirst()) {
            do {
                ZhihuDailyNews.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")), ZhihuDailyNews.Question.class);
                zhihuList.add(question);
                types.add(TYPE_ZHIHU_NORMAL);
            } while (cursor.moveToNext());
        }

        types.add(TYPE_GUOKR_WITH_HEADER);
        cursor = db.rawQuery("select * from Guokr where bookmark = ?", new String[]{"1"});
        if (cursor.moveToFirst()) {
            do {
                GuokeSelectionNews.result result = gson.fromJson(cursor.getString(cursor.getColumnIndex("guokr_news")), GuokeSelectionNews.result.class);
                guokrList.add(result);
                types.add(TYPE_GUOKR_NORMAL);
            } while (cursor.moveToNext());
        }

        types.add(TYPE_DOUBAN_WITH_HEADER);
        cursor = db.rawQuery("select * from Douban where bookmark = ?", new String[]{"1"});
        if (cursor.moveToFirst()) {
            do {
                DoubanMomentNews.posts post = gson.fromJson(cursor.getString(cursor.getColumnIndex("douban_news")), DoubanMomentNews.posts.class);
                doubanList.add(post);
                types.add(TYPE_DOUBAN_NORMAL);
            } while (cursor.moveToNext());
        }

        cursor.close();

    }

    @Override
    public void feelLucky() {
        Random random = new Random();
        int p = random.nextInt(types.size());
        while (true) {
            if (types.get(p) == CollectionAdapter.TYPE_ZHIHU_NORMAL) {
                startReading(BeanType.TYPE_ZHIHU, p);
                break;
            } else if (types.get(p) == CollectionAdapter.TYPE_GUOKR_NORMAL) {
                startReading(BeanType.TYPE_GUOKE, p);
                break;
            } else if (types.get(p) == CollectionAdapter.TYPE_DOUBAN_NORMAL) {
                startReading(BeanType.TYPE_DOUBAN, p);
                break;
            } else {
                p = random.nextInt(types.size());
            }
        }
    }
}
