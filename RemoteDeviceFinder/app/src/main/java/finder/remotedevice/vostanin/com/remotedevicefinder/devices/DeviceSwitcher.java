package finder.remotedevice.vostanin.com.remotedevicefinder.devices;

/**
 * Created by vostanin on 10/19/16.
 */
public class DeviceSwitcher extends BaseDevice
{
    public DeviceSwitcher( String id, String ip )
    {
        super( id, DeviceTypes.Switcher, ip );
        mState = new DeviceSwitcherState();
        setRemoteState( new DeviceSwitcherRemoteState( ip ) );
    }
}
