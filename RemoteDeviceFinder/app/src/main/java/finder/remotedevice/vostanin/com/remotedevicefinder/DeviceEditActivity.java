package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceTypes;

public class DeviceEditActivity extends AppCompatActivity
{
    private String mDeviceId = "";
    private String mDeviceType = DeviceTypes.None;
    private String mDeviceIP = "";
    private String mDeviceName = "";
    private int mDeviceIndex = 0;

    private Button mBtnApply;
    private EditText mTextDeviceName;
    private TextView mLabelDeviceID;
    private TextView mlabelDeviceIP;
    private TextView mLabelDeviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDeviceId = ( String ) getIntent().getExtras().get( Constants.EXTRA_DEVICE_ID );
        mDeviceIP = ( String ) getIntent().getExtras().get( Constants.EXTRA_DEVICE_IP );
        mDeviceType = ( String ) getIntent().getExtras().get( Constants.EXTRA_DEVICE_TYPE );
        mDeviceName = ( String ) getIntent().getExtras().get( Constants.EXTRA_DEVICE_NAME );
        mDeviceIndex = ( int ) getIntent().getExtras().get( Constants.EXTRA_DEVICE_INDEX );

        mLabelDeviceID = ( TextView ) findViewById(R.id.labelDeviceID);
        mlabelDeviceIP = ( TextView ) findViewById( R.id.labelDeviceIP );
        mLabelDeviceType = ( TextView ) findViewById( R.id.labelDeviceType );
        mTextDeviceName = ( EditText ) findViewById(R.id.textDeviceName);
        mBtnApply = ( Button ) findViewById( R.id.buttonApply );
        mTextDeviceName.setText( mDeviceName, TextView.BufferType.NORMAL );
        mLabelDeviceID.setText( mDeviceId );
        mlabelDeviceIP.setText( mDeviceIP );
        mLabelDeviceType.setText( mDeviceType );
        mBtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String deviceName = mTextDeviceName.getText().toString();
                final BaseDevice device = DevicesCache.getInstance().get( mDeviceIndex );
                if( false == deviceName.isEmpty() )
                {
                    device.setName( deviceName );
                }
                else
                {
                    device.setName( device.getIp() );
                }

                DeviceStoreManager.getInstance().save();

                DeviceEditActivity.this.finish();
            }
        });
    }

}
