package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;

/**
 * Created by vostanin on 10/18/16.
 */
public class ButtonAddClickListener implements View.OnClickListener
{
    private MainActivity mContext;

    public ButtonAddClickListener( MainActivity context )
    {
        mContext = context;
    }
    @Override
    public void onClick( View view )
    {
        final Intent intent = new Intent( mContext, DeviceAddActivity.class );
//        final Intent intent = new Intent( mContext, MyPreferenceActivity.class );
//
        mContext.startActivity( intent );
    }



//    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
//        private Preference preferenceCallback;
//
//        @Override
//        public void onCreate(final Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
////            this.addPreferencesFromResource(R.xml.preferences);
////
////            preferenceCallback = this.findPreference("preference_font_size");
////            preferenceCallback.setOnPreferenceChangeListener(this);
////
////            this.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//        }
//
//        @Override
//        public boolean onPreferenceChange(final Preference preference, final Object newValue) {
//            if (preference.equals(preferenceCallback)) {
//                final int value = (int) newValue;
//                Toast.makeText(getActivity(), "New value is " + value, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//
//            return false;
//        }
//
//        @Override
//        public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
//            if (preferenceCallback != null && key.equals(preferenceCallback.getKey())) {
//                final int value = sharedPreferences.getInt(key, 0);
//                preferenceCallback.setSummary("My custom summary text. Value is " + value);
//            }
//        }
//    }
}
