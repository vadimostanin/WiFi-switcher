package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by vostanin on 11/3/16.
 */
public class DeviceGetCredencialsTask extends AsyncTask<String, Void, ArrayList<String>>
{
    private ArrayList<String> StateInfo = new ArrayList<>();

    @Override
    protected ArrayList<String> doInBackground( String... strings )
    {
        final String deviceIP = strings[0];
        final String devicePort = strings[1];
        Log.d(Constants.TAG, "DeviceGetCredencialsTask:doInBackground entered");
        Log.d( Constants.TAG, "DeviceGetCredencialsTask:doInBackground deviceIP=" + deviceIP + ", devicePort=" + devicePort );

        try
        {
            String url = "http://" + deviceIP + ":" + devicePort + "/getstate";

// Request a string response from the provided URL.
            HtmlHelper hh = new HtmlHelper( new URL( url ) );
            Elements deviceTypesElements = hh.getDivsByClass("device_type");
            Elements deviceIdElements = hh.getDivsByClass("device_id");
            Iterator<Element> iteratorDeviceType = deviceTypesElements.iterator();
            Iterator<Element> iteratorDeviceId = deviceIdElements.iterator();
            if( false == iteratorDeviceType.hasNext() || false == iteratorDeviceId.hasNext() )
            {
                throw new Exception( "device type or state empty in response" );
            }

            Element deviceTypeElement = iteratorDeviceType.next();
            String deviceType = deviceTypeElement.text();

            Element deviceIdElement = iteratorDeviceId.next();
            String deviceId = deviceIdElement.text();

            StateInfo.add( deviceType );
            StateInfo.add( deviceId );

            Log.d( Constants.TAG, "url=" + url + ", deviceType=" + deviceType + ", deviceId=" + deviceId );
        }
        catch( Exception e )
        {

        }
        return StateInfo;
    }
}
