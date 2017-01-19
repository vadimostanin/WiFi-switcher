package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceSwitcher;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceTypes;

public class DeviceAddActivity extends AppCompatActivity
{
    private Button mBtnConnect;
    private EditText mTextDeviceName;
    private EditText mLabelDevicePort;
    private EditText mlabelDeviceIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activityAdd_toolbar);
        setSupportActionBar(toolbar);

        mLabelDevicePort = ( EditText ) findViewById(R.id.activityAdd_labelDeviceIPPort);
        mlabelDeviceIP = ( EditText ) findViewById( R.id.activityAdd_labelDeviceIP );
        mTextDeviceName = ( EditText ) findViewById(R.id.activityAdd_textDeviceName);
        mBtnConnect = ( Button ) findViewById( R.id.activityAdd_buttonConnect );
        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String deviceName = mTextDeviceName.getText().toString();
                String deviceIP = mlabelDeviceIP.getText().toString();
                String devicePort = mLabelDevicePort.getText().toString();

                final DeviceGetCredencialsTask getCredsTask = new DeviceGetCredencialsTask();
                try
                {
                    ArrayList<String> result = Utils.asyncTaskExecute( getCredsTask, deviceIP, devicePort ).get();
//                    final String deviceType = result.get( 0 );
                    final String deviceId = result.get( 1 );


                    BaseDevice device = new DeviceSwitcher( deviceId, deviceIP );
                    device.setName( deviceName );
                    device.setPort(Integer.parseInt(devicePort));

                    DevicesCache.getInstance().add(device);
                    DeviceStoreManager.getInstance().save();
                }
                catch( Exception e )
                {
                    Toast.makeText(ContextGetter.getInstance().get(), "Failed: " + e.getMessage(), Toast.LENGTH_LONG ).show();
                }

                DeviceAddActivity.this.finish();
            }
        });

//        class Done extends IDone<Integer>
//        {
//            public void onDone( Integer value )
//            {
////                mLabelDevicePort.setText( value.toString() );
//            }
//        }

//        new NumberPickerDialog( ContextGetter.getInstance().get(), new Done() ).show();

//        ContextGetter.getInstance().get().getFragmentManager().beginTransaction().replace(R.id.activity_deviсу_add, new ButtonAddClickListener.SettingsFragment()).commit();
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
//            PreferenceManager.setDefaultValues(getActivity(), R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragment_preference);
        }
    }

}
