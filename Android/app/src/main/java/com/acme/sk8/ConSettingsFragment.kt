package com.acme.sk8

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class ConSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}