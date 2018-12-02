package com.example.tomas.tamz2_projekt;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.InputFilter;
import android.widget.EditText;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads the XML preferences file.
        addPreferencesFromResource(R.xml.preference);

        EditText txtSpeed = ((EditTextPreference)findPreference("rychlost")).getEditText();
        txtSpeed.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 20) });

        EditText txtSila = ((EditTextPreference)findPreference("sila")).getEditText();
        txtSila.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 100) });

        EditText txtRadek = ((EditTextPreference)findPreference("radek")).getEditText();
        txtRadek.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 6) });

        EditText txtSloupec = ((EditTextPreference)findPreference("sloupec")).getEditText();
        txtSloupec.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 6) });
    }
}
