package com.hcode.howienote.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.hcode.howienote.R;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindViews();
        initFragment();
    }

    public void bindViews() {
        myToolbar = findViewById(R.id.tb_mytoolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    void initFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
        private ListPreference mFontSize,mSortBy;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_settings, rootKey);
            bindView();
            bindListeners();
        }

        void bindView() {
            mFontSize = findPreference("setting_font_size");
            mSortBy = findPreference("setting_sort_by");
        }

        void bindListeners() {
            mFontSize.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            mSortBy.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return true;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            return false;
        }

        public CharSequence valueFindEntry(ListPreference preference, String value) {
            return preference.getEntries()
                    [preference.findIndexOfValue(value)];

        }
    }
}