package apollo.tianya.base;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;

import com.github.machinarius.preferencefragment.PreferenceFragment;

import java.util.HashSet;
import java.util.Set;

import apollo.tianya.bean.Constants;


/**
 * Created by Texel on 2016/7/18.
 */
public abstract class BasePreferenceFragment extends PreferenceFragment {
    protected boolean mConfigChanged;

    protected abstract int getLayoutId();

    protected abstract void initPreference();

    protected abstract void initEntryValues();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(getLayoutId());

        initPreference();
        initEntryValues();
    }

    protected void setPreference(ListPreference preference, int value, String strs[], String values[], int size) {
        if (strs != null && values != null) {
            int val = -1;
            for (int idx=0; idx<size; idx++) {
                val = Integer.parseInt(values[idx]);
                if (val == value) {
                    preference.setSummary(strs[idx]);

                    if (mConfigChanged == false)
                        preference.setValueIndex(idx);
                    break;
                }
            }
        } else {
            preference.setSummary(Constants.Settings.BLANK_SUMMARY);
        }
    }

    protected void setPreference(MultiSelectListPreference preference, int value, String strs[], String values[], int size) {
        if (strs != null && values != null) {
            int val = -1;
            String str = null;
            StringBuffer buf = new StringBuffer(24);
            Set<String> new_values = new HashSet<String>(3);

            for (int idx = 0; idx < values.length; idx++)  {
                val = value & (1 << Integer.parseInt(values[idx]));
                if (val != 0) {
                    new_values.add(values[idx]);
                    buf.append(strs[idx]).append(",");
                }
            }

            int idx = buf.lastIndexOf(",");
            if (idx > 0)
                buf.deleteCharAt(idx);

            str = buf.toString();
            preference.setSummary(str);

            if (mConfigChanged == false)
                preference.setValues(new_values);
        } else {
            preference.setSummary(Constants.Settings.BLANK_SUMMARY);
        }
    }
}
