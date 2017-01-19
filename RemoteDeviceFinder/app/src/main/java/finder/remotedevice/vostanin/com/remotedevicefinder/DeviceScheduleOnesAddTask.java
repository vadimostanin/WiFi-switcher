package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.Iterator;

/**
 * Created by vostanin on 11/11/16.
 */
public class DeviceScheduleOnesAddTask extends AsyncTask<String, Void, Void>
{
    @Override
    protected Void doInBackground( String... strings )
    {
        final String deviceIP = strings[0];
        final String devicePort = strings[1];
        final String action = strings[2];
        final String date = strings[3];
        final String time = strings[4];
        Log.d(Constants.TAG, "DeviceGetCredencialsTask:doInBackground entered");
        Log.d( Constants.TAG, "DeviceGetCredencialsTask:doInBackground deviceIP=" + deviceIP + ", devicePort=" + devicePort );

        try
        {
            String url = "http://" + deviceIP + ":" + devicePort + "/schedule?schedule_action=" + action + "&schedule_period=" + Constants.SchedPeriodTypeOnes + "&schedule_date=" + date + "&schedule_time=" + time;

// Request a string response from the provided URL.
            HtmlHelper hh = new HtmlHelper( new URL( url ) );
        }
        catch( Exception e )
        {
            Log.d( Constants.TAG, e.getMessage() );
        }
        return null;
    }
}
