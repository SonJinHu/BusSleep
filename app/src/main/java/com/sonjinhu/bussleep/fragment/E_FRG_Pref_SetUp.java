package com.sonjinhu.bussleep.fragment;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.sonjinhu.bussleep.R;
import com.sonjinhu.bussleep.util.Util;

/**
 * Created by sonjh on 2017-04-18.
 */

public class E_FRG_Pref_SetUp extends PreferenceFragment {

    String remainNo;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setup);

        initPref();

        ListPreference pref_list = (ListPreference) findPreference("pref_condition_list");
        pref_list.setValueIndex(0);

        setOnPreferenceChange(findPreference("pref_way_list"));
        setOnPreferenceChange(findPreference("pref_sound_ringtone"));
        setOnPreferenceChange(findPreference("pref_condition_list"));
    }

    void initPref() {
        Util util = new Util();
        remainNo = util.getSharedPre(getActivity(), "remainNo");
    }

    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        onPreferenceChangeListener.onPreferenceChange(
                mPreference,
                PreferenceManager.
                        getDefaultSharedPreferences(mPreference.getContext()).
                        getString(mPreference.getKey(), ""));
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference.getKey().equals("pref_way_list")) {
                ListPreference pref_list = (ListPreference) preference;
                int index = pref_list.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? pref_list.getEntries()[index] : null);
                if (index == 1) {
                    findPreference("pref_sound_ringtone").setEnabled(false);
                } else {
                    findPreference("pref_sound_ringtone").setEnabled(true);
                }
            }

            if (preference.getKey().equals("pref_sound_ringtone")) {
                Ringtone ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue));
                if (ringtone == null) {
                    preference.setSummary(null);
                } else {
                    String name = ringtone.getTitle(preference.getContext());
                    preference.setSummary(name);
                }
            }

            if (preference.getKey().equals("pref_condition_list")) {
                ListPreference pref_list = (ListPreference) preference;
                int index = pref_list.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ? pref_list.getEntries()[index] : null);
                switch (remainNo) {
                    case "2":
                        pref_list.setEntries(R.array.pref_condition1_list_titles);
                        pref_list.setEntryValues(R.array.pref_condition1_list_values);
                        pref_list.setValueIndex(0);
                        break;
                    case "3":
                        pref_list.setEntries(R.array.pref_condition2_list_titles);
                        pref_list.setEntryValues(R.array.pref_condition2_list_values);
                        pref_list.setValueIndex(0);
                        break;
                    default:
                        pref_list.setEntries(R.array.pref_condition3_list_titles);
                        pref_list.setEntryValues(R.array.pref_condition3_list_values);
                        break;
                }
            }

            return true;
        }
    };
}