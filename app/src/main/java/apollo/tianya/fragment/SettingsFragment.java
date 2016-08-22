package apollo.tianya.fragment;

import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import apollo.tianya.AppContext;
import apollo.tianya.R;
import apollo.tianya.base.BasePreferenceFragment;
import apollo.tianya.bean.Constants;
import apollo.tianya.util.FileUtil;
import apollo.tianya.util.UIHelper;

/**
 * Created by Texel on 2016/7/18.
 */
public class SettingsFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static String[] mFontSizeEntries;
    private static String[] mFontSizeValues;
    private static final int mFontSizeValueSize = 3;

    private ListPreference mFontSize = null;
    private CheckBoxPreference mShowImgEnable = null;
    private CheckBoxPreference mShowHeadEnable = null;
    private Preference mCleanCache = null;

    @Override
    protected int getLayoutId() {
        return R.xml.settings;
    }

    @Override
    protected void initPreference() {
        mFontSize = (ListPreference) findPreference(Constants.Settings.KEY_FONT_SIZE);
        mShowImgEnable = (CheckBoxPreference) findPreference(Constants.Settings.KEY_SHOW_IMG);
        mShowHeadEnable = (CheckBoxPreference) findPreference(Constants.Settings.KEY_SHOW_HEAD_IMG);
        mCleanCache = findPreference(Constants.Settings.KEY_CLEAN_CACHE);

        mFontSize.setOnPreferenceChangeListener(this);
        mShowImgEnable.setOnPreferenceChangeListener(this);
        mShowHeadEnable.setOnPreferenceChangeListener(this);
        mCleanCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                UIHelper.clearAppCache(getActivity());
                mCleanCache.setSummary("0KB");
                return false;
            }
        });
    }

    @Override
    protected void initEntryValues() {
        Resources res = getResources();
        int fontSize = AppContext.getFontSize();
        boolean showImage = AppContext.isShowImage();
        boolean showHeadImage = AppContext.isShowHeadImage();

        mFontSizeEntries = res.getStringArray(R.array.font_size_entries);
        mFontSizeValues = res.getStringArray(R.array.font_size_values);

        setPreference(mFontSize, fontSize, mFontSizeEntries, mFontSizeValues, mFontSizeValueSize);

        mShowImgEnable.setChecked(showImage);
        mShowHeadEnable.setChecked(showHeadImage);

        caculateCacheSize();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mFontSize) {
            int fontSize = Integer.parseInt((String) newValue);

            AppContext.setFontSize(fontSize);
            setPreference(mFontSize, fontSize, mFontSizeEntries, mFontSizeValues, mFontSizeValueSize);
        } else if (preference == mShowImgEnable) {
            AppContext.setShowImage((Boolean) newValue);
        } else if (preference == mShowHeadEnable) {
            AppContext.setShowHeadImage((Boolean) newValue);
        }

        mConfigChanged = true;
        return true;
    }

    private void caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getActivity().getFilesDir();
        File cacheDir = getActivity().getCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);

        if (fileSize > 0)
            cacheSize = FileUtil.formatFileSize(fileSize);
        mCleanCache.setSummary(cacheSize);
    }
}
