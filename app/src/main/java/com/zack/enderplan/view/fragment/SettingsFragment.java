package com.zack.enderplan.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatDelegate;

import com.zack.enderplan.R;
import com.zack.enderplan.model.DataManager;
import com.zack.enderplan.model.preference.PreferenceHelper;
import com.zack.enderplan.view.activity.HomeActivity;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    PreferenceHelper mPreferenceHelper;
    DataManager mDataManager;
    SwitchPreference mNightModePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mPreferenceHelper = PreferenceHelper.getInstance();
        mDataManager = DataManager.getInstance();

        mNightModePreference = (SwitchPreference) findPreference(PreferenceHelper.KEY_PREF_NIGHT_MODE);

        mNightModePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_dialog_switch_night_mode)
                        .setMessage(R.string.msg_dialog_switch_night_mode)
                        .setPositiveButton(R.string.button_restart, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNightModePreference.setChecked(!mNightModePreference.isChecked());
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
                //返回false表示不改变preference的值
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PreferenceHelper.KEY_PREF_NIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(mPreferenceHelper.getNightModeValue() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                //返回并重新创建HomeActivity
                HomeActivity.start(getActivity());
                break;
        }
    }
}
