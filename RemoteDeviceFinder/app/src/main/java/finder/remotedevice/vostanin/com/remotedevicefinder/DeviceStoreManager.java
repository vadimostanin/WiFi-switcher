package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceSwitcher;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceTypes;

/**
 * Created by vostanin on 10/19/16.
 */
public class DeviceStoreManager
{
    private static DeviceStoreManager mInstance = new DeviceStoreManager();

    public static DeviceStoreManager getInstance()
    {
        return mInstance;
    }

    private DeviceStoreManager(){}

    public DeviceStoreManager save()
    {
        String filePath = ApplicationDirectory.getInstance().Get(ContextGetter.getInstance().get()) + "/devices.txt";
        try
        {
            File newFile = new File( filePath );
            newFile.delete();
            newFile.createNewFile();
            FileWriter fileWriter = new FileWriter( newFile );

            int count = DevicesCache.getInstance().count();
            for( int device_i = 0 ; device_i < count ; device_i++ )
            {
                final BaseDevice device = DevicesCache.getInstance().get( device_i );
                String deviceLine = String.format( "id=%s,type=%s,ip=%s,name=%s\n", device.getId(), device.getType(), device.getIp(), device.getName() );
                fileWriter.write( deviceLine );
            }
            fileWriter.flush();
            fileWriter.close();
        }
        catch( Exception e )
        {
            Toast.makeText( ContextGetter.getInstance().get(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return this;
    }

    public DeviceStoreManager load()
    {
        String filePath = ApplicationDirectory.getInstance().Get( ContextGetter.getInstance().get() ) + "/devices.txt";
        try
        {
            try( BufferedReader br = new BufferedReader( new FileReader( filePath ) ) )
            {
                String line;
                while( ( line = br.readLine() ) != null )
                {
                    String deviceId = "";
                    String deviceType = DeviceTypes.None;
                    String deviceIP = "";
                    String deviceName = "";
                    line = line.trim();
                    String[] params = line.split( "," );
                    for( String param : params )
                    {
                        String[] key_value = param.split( "=" );
                        if( key_value.length < 2 )
                        {
                            continue;
                        }
                        if( key_value[0].equals( "type" ) )
                        {
                            deviceType = key_value[1];
                        }
                        else if( key_value[0].equals( "id" ) )
                        {
                            deviceId = key_value[1];
                        }
                        else if( key_value[0].equals( "ip" ) )
                        {
                            deviceIP = key_value[1];
                        }
                        else if( key_value[0].equals( "name" ) )
                        {
                            deviceName = key_value[1];
                        }
                    }
                    if( true == deviceId.isEmpty() || DeviceTypes.None == deviceType || true == deviceIP.isEmpty() || true == deviceName.isEmpty() )
                    {
                        continue;
                    }
                    if( deviceType.equals( DeviceTypes.Switcher ) )
                    {
                        final BaseDevice device = new DeviceSwitcher( deviceId, deviceIP );
                        device.setName( deviceName );
                        DevicesCache.getInstance().add( device );
                    }
                }
            }
        }
        catch( Exception e )
        {

        }
        return this;
    }
}
