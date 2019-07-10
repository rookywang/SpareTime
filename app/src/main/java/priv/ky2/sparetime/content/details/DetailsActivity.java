package priv.ky2.sparetime.content.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import priv.ky2.sparetime.R;
import priv.ky2.sparetime.bean.BeanType;

public class DetailsActivity extends AppCompatActivity {

    private DetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        if (savedInstanceState != null) {
            fragment = (DetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "DetailsFragment");
        } else {
            fragment = new DetailsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

        Intent intent = getIntent();

        DetailsPresenter presenter = new DetailsPresenter(DetailsActivity.this, fragment);

        presenter.setType((BeanType) intent.getSerializableExtra("type"));
        presenter.setId(intent.getIntExtra("id", 0));
        presenter.setTitle(intent.getStringExtra("title"));
        presenter.setCoverUrl(intent.getStringExtra("coverUrl"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "DetailsFragment", fragment);
        }
    }
}
