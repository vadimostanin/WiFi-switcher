package finder.remotedevice.vostanin.com.remotedevicefinder;

import java.util.ArrayList;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;

/**
 * Created by vostanin on 10/19/16.
 */
public class DevicesCache
{
    private static DevicesCache mInstance = new DevicesCache();
    private ArrayList<BaseDevice> mDevices = new ArrayList<>();

    public static DevicesCache getInstance()
    {
        return mInstance;
    }

    private DevicesCache(){}

    public void add( BaseDevice device )
    {
        if( true == mDevices.contains( device ) )
        {
            return;
        }
        mDevices.add(device);
    }

    public void add( ArrayList<BaseDevice> newDevices )
    {
        for( final BaseDevice newDevice : newDevices )
        {
            if( false == mDevices.contains( newDevice ) )
            {
                mDevices.add( newDevice );
            }
        }

    }

    public void remove( BaseDevice device )
    {
        mDevices.remove(device);
    }

    public BaseDevice get( int index )
    {
        return mDevices.get( index );
    }

    public void clear()
    {
        mDevices.clear();
    }

    public int count()
    {
        return mDevices.size();
    }

    public void updateRemoteState()
    {
        for( final BaseDevice device : mDevices )
        {
            device.setState( device.getRemoteState().getRemote() );
        }
    }
}
