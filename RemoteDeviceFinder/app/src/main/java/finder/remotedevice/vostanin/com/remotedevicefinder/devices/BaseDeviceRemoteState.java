package finder.remotedevice.vostanin.com.remotedevicefinder.devices;

/**
 * Created by vostanin on 10/19/16.
 */
public interface BaseDeviceRemoteState
{
    void setRemote( BaseDeviceState newState );
    BaseDeviceState getRemote();
}
