package ru.ndevelop.reusersamsung.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.interfaces.OnSettingsChangeListener;

public class SettingsFragment extends PreferenceFragmentCompat {
    OnSettingsChangeListener onSettingsChangeListener;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference aboutButton = findPreference("DEVELOPER_SITE");
        aboutButton.setOnPreferenceClickListener(preference -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nai1ka.github.io"));
            startActivity(browserIntent);
            return true;
        });
    }
}