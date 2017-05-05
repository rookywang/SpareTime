package priv.ky2.sparetime.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import priv.ky2.sparetime.R;

/**
 * Created by wangkaiyan on 2017/5/2.
 */

public class AboutFragment extends PreferenceFragmentCompat{

    public AboutFragment(){

    }

    public static AboutFragment getInstance(){
        return new AboutFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_about);
    }

}
