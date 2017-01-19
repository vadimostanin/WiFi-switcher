package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;

/**
 * Created by vostanin on 11/11/16.
 */
public class DeviceScheduleEverydayAddTask extends AsyncTask<String, Void, Void>
{
    private String mError;
    private IDone<Void> mDone;
    public DeviceScheduleEverydayAddTask( IDone<Void> done )
    {
        mDone = done;
    }
    @Override
    protected Void doInBackground( String... strings )
    {
        final String deviceIP = strings[0];
        final String devicePort = strings[1];
        final String action = strings[2];
        final String time = strings[3];
        Log.d(Constants.TAG, "DeviceGetCredencialsTask:doInBackground entered");
        Log.d( Constants.TAG, "DeviceGetCredencialsTask:doInBackground deviceIP=" + deviceIP + ", devicePort=" + devicePort );

        try
        {
            String url = "http://" + deviceIP + ":" + devicePort + "/schedule?schedule_action=" + action + "&schedule_period=" + Constants.SchedPeriodTypeEveryday + "&schedule_time=" + time;

// Request a string response from the provided URL.
            HtmlHelper hh = new HtmlHelper( new URL( url ) );
        }
        catch( Exception e )
        {
            mError = "Add schedule failed: " + e.getMessage();
            Log.d( Constants.TAG, e.getMessage() );
        }
        return null;
    }

    @Override
    protected void onPostExecute( Void aVoid )
    {
        if( null != mError )
        {
            Toast.makeText( ContextGetter.getInstance().get(), mError, Toast.LENGTH_LONG ).show();
        }
        mDone.onDone( null );
        super.onPostExecute(aVoid);
    }
}
