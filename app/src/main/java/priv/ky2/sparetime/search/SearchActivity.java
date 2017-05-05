package priv.ky2.sparetime.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import priv.ky2.sparetime.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        SearchFragment fragment = SearchFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        new SearchPresenter(this, fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
