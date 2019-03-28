package com.cheadtech.popularmovieskt.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.*
import com.cheadtech.popularmovieskt.R

class PostersSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.pref_posters)

        PreferenceManager.getDefaultSharedPreferences(requireContext()).also { prefs ->
            (0 until preferenceScreen.preferenceCount).forEach { index ->
                preferenceScreen.getPreference(index).also { preference ->
                    if (preference !is CheckBoxPreference)
                        setPreferenceSummary(preference, prefs.getString(preference.key, "") ?: "")
                }
            }
            prefs.registerOnSharedPreferenceChangeListener(this)
        }
    }

    private fun setPreferenceSummary(pref: Preference, value: String) {
        if (pref is ListPreference) {
            val index = pref.findIndexOfValue(value)
            if (index >= 0)
                pref.summary = pref.entries[index]
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(requireContext()).unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        findPreference(key)?.also {
            if(it !is CheckBoxPreference)
                setPreferenceSummary(it, preferences?.getString(it.key, "") ?: "")
        }
    }
}