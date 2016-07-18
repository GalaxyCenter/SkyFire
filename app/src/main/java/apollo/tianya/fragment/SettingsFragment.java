package apollo.tianya.fragment;

import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import apollo.tianya.R;
import apollo.tianya.base.BasePreferenceFragment;
import apollo.tianya.bean.Constants;

/**
 * Created by Texel on 2016/7/18.
 */
public class SettingsFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static String[] mFontSizeEntries;
    private static String[] mFontSizeValues;
    private static final int mFontSizeValueSize = 3;

    private ListPreference mFontSize = null;

    @Override
    protected int getLayoutId() {
        return R.xml.settings;
    }

    @Override
    protected void initPreference() {
        mFontSize = (ListPreference) findPreference(Constants.Settings.KEY_FONT_SIZE);

        mFontSize.setOnPreferenceChangeListener(this);
    }

    @Override
    protected void initEntryValues() {
        Resources res = getResources();

        mFontSizeEntries = res.getStringArray(R.array.font_size_entries);
        mFontSizeValues = res.getStringArray(R.array.font_size_values);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFontSize) {
            mConfig.fontSize = Integer.parseInt((String) newValue);
            setPreference(mFontSize, mConfig.fontSize, mFontSizeEntries, mFontSizeValues, mFontSizeValueSize);
        }
        return true;
    }
}
