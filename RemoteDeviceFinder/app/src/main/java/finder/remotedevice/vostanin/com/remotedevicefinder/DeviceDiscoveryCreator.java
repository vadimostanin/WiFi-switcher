package finder.remotedevice.vostanin.com.remotedevicefinder;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceNone;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceSwitcher;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceTypes;

/**
 * Created by vostanin on 10/19/16.
 */
public class DeviceDiscoveryCreator
{
    public static BaseDevice create( String deviceresponse, String ip )
    {
        BaseDevice device = new DeviceNone();
        do
        {
            String type = "";
            String id = "";
            deviceresponse = deviceresponse.trim();
            String[] params = deviceresponse.split( "," );

            for( String param : params )
            {
                String[] key_value = param.split( "=" );
                if( key_value.length < 2 )
                {
                    continue;
                }
                if( key_value[0].equals( "type" ) )
                {
                    type = key_value[1];
                }
                else if( key_value[0].equals( "id" ) )
                {
                    id = key_value[1];
                }
            }
            if( true == id.isEmpty() || true == type.isEmpty() )
            {
                break;
            }
            if( type.equals( DeviceTypes.Switcher ) )
            {
                device = new DeviceSwitcher( id, ip );
            }
            device.setName( device.getIp() );
        }while( false );

        return device;
    }
}
