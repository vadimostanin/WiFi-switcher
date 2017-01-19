package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceSwitcherRemoteState;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceSwitcherState;

public class DeviceSwictherActivity extends AppCompatActivity
{
    private ImageButton mBtnSwitcher;
    private Button      mBtnSchedule;
    private ImageButton      mBtnUpdateState;
    private ImageView   mImageStateSwitcher;
    private BaseDevice  mDevice;
    public static final String EXTRA_DEVICE_INDEX = "DeviceIndex";
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_swicther);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final int deviceIndex = ( int ) getIntent().getExtras().get( EXTRA_DEVICE_INDEX );
        mDevice = DevicesCache.getInstance().get( deviceIndex );

        mBtnSwitcher = ( ImageButton ) findViewById( R.id.activity_device_switch_btnSwitcher );
        mImageStateSwitcher = ( ImageView ) findViewById( R.id.activity_device_switch_imageDeviceState );
        mBtnSchedule = ( Button ) findViewById( R.id.activity_device_switch_btnSchedule );
        mBtnUpdateState = ( ImageButton ) findViewById( R.id.activity_device_switch_btnUpdateState );
        final DeviceSwitcherRemoteState deviceRemoteState = (DeviceSwitcherRemoteState) mDevice.getRemoteState();
        mDevice.setState( deviceRemoteState.getRemote() );
        final DeviceSwitcherState deviceState = ( DeviceSwitcherState ) mDevice.getState();
        updateBigImage( deviceState );
        updateSmallImage(deviceState);
        mBtnSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText( ContextGetter.getInstance().get(), "turn on/off clicked", Toast.LENGTH_SHORT ).show();

                final DeviceSwitcherState newState = new DeviceSwitcherState();
                newState.setSwitchedOn(false == ((DeviceSwitcherState) mDevice.getState()).getSwitchedOn());
                deviceRemoteState.setRemote(newState);
                mDevice.setState(deviceRemoteState.getRemote());
                final DeviceSwitcherState deviceState = (DeviceSwitcherState) mDevice.getState();
                updateBigImage(deviceState);
                updateSmallImage(deviceState);
            }
        });

        mBtnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view )
            {
                Intent intent = new Intent( ContextGetter.getInstance().get(), DeviceScheduleActivity.class );
                intent.putExtra(Constants.EXTRA_DEVICE_INDEX, deviceIndex);
                ContextGetter.getInstance().get().startActivity(intent);
            }
        });

        mBtnUpdateState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view )
            {
                mDevice.setState( deviceRemoteState.getRemote() );
                final DeviceSwitcherState deviceState = (DeviceSwitcherState) mDevice.getState();
                updateBigImage( deviceState );
                updateSmallImage( deviceState );
            }
        });
    }

    private void updateBigImage( final DeviceSwitcherState deviceState )
    {
        Bitmap bitmapTurnOnOff;
        if( true == deviceState.getSwitchedOn() )
        {
            bitmapTurnOnOff = BitmapFactory.decodeResource( ContextGetter.getInstance().get().getResources(), R.drawable.turn_off_512 );
        }
        else
        {
            bitmapTurnOnOff = BitmapFactory.decodeResource( ContextGetter.getInstance().get().getResources(), R.drawable.turn_on_512 );
        }
        mBtnSwitcher.setBackground( new BitmapDrawable(ContextGetter.getInstance().get().getResources(), bitmapTurnOnOff));
    }

    private void updateSmallImage( final DeviceSwitcherState deviceState )
    {
        if( true == deviceState.getSwitchedOn() )
        {
            mImageStateSwitcher.setImageResource( R.drawable.turn_on_64 );
        }
        else
        {
            mImageStateSwitcher.setImageResource( R.drawable.turn_off_64 );
        }
    }
}
