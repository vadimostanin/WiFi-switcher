package finder.remotedevice.vostanin.com.remotedevicefinder.devices;

/**
 * Created by vostanin on 10/24/16.
 */
public class DeviceSwitcherState implements BaseDeviceState
{
    private boolean mSwitchedOn = false;
    public boolean getSwitchedOn()
    {
        return mSwitchedOn;
    }
    public void setSwitchedOn( boolean on )
    {
        mSwitchedOn = on;
    }

    public void copy( BaseDeviceState newState )
    {
        final DeviceSwitcherState newSwitcherState = ( DeviceSwitcherState ) newState;
        setSwitchedOn( newSwitcherState.getSwitchedOn() );
    }
}
