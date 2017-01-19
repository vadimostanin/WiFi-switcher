package finder.remotedevice.vostanin.com.remotedevicefinder.devices;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import finder.remotedevice.vostanin.com.remotedevicefinder.Constants;
import finder.remotedevice.vostanin.com.remotedevicefinder.ContextGetter;
import finder.remotedevice.vostanin.com.remotedevicefinder.HtmlHelper;
import finder.remotedevice.vostanin.com.remotedevicefinder.Utils;

/**
 * Created by vostanin on 10/19/16.
 */
public class DeviceSwitcherRemoteState implements BaseDeviceRemoteState
{
    private String mIp;
    public DeviceSwitcherRemoteState(String ip)
    {
        mIp = ip;
    }

    @Override
    public void setRemote( BaseDeviceState newState )
    {
        final DeviceSwitcherState newSwitcherState = ( DeviceSwitcherState ) newState;
        String url = "http://" + mIp + ":33248/setstate?switch=" + ( ( true == newSwitcherState.getSwitchedOn() ) ? "1" : "0" );

// Request a string response from the provided URL.

//        ( new SendTask( semaphore ) ).execute( mIp, data );
        final SendTask sendTask = new SendTask();

        try
        {
            Utils.asyncTaskExecute( sendTask, mIp, url ).get();
        }
        catch( Exception e )
        {

        }
    }

    final DeviceSwitcherState mRemoteState = new DeviceSwitcherState();

    @Override
    public BaseDeviceState getRemote()
    {
        Log.d(Constants.TAG, "getRemote enter");

        final GetStatetask getStateTask = ( new GetStatetask() );

        try
        {
            ArrayList<String> response = Utils.asyncTaskExecute( getStateTask, mIp ).get();

            if( true == response.isEmpty() )
            {
                throw new Exception( "Getting info failed: response empty" );
            }

            mRemoteState.setSwitchedOn( false );

            String deviceType = response.get( 0 );
            String deviceState = response.get( 1 );
            boolean switchedOn = ( deviceState.equals( "ON" ) ) ? true : false;
            mRemoteState.setSwitchedOn( switchedOn );
        }
        catch( Exception e )
        {
            Log.d( Constants.TAG, "send to " + mIp + " failed: " + e.getMessage() );
        }

        return mRemoteState;
    }

    private class GetStatetask extends AsyncTask<String, Void, ArrayList<String>>
    {
        public GetStatetask()
        {
            Log.d( Constants.TAG, "GetStatetask created" );
        }
        private ArrayList<String> StateInfo = new ArrayList<>();
        private String mError = "";
        @Override
        protected ArrayList<String> doInBackground( String... strings )
        {
            Log.d(Constants.TAG, "GetStatetask:doInBackground entered");
            final String deviceIP = strings[0];
            Log.d( Constants.TAG, "GetStatetask:doInBackground deviceIP=" + deviceIP );

            try
            {
                String url = "http://" + deviceIP + ":33248/getstate";

// Request a string response from the provided URL.
                HtmlHelper hh = new HtmlHelper( new URL( url ) );
                Elements deviceTypesElements = hh.getDivsByClass("device_type");
                Elements deviceStateElements = hh.getDivsByClass("device_state");
                Iterator<Element> iteratorDeviceType = deviceTypesElements.iterator();
                Iterator<Element> iteratorDeviceState = deviceStateElements.iterator();
                if( false == iteratorDeviceType.hasNext() || false == iteratorDeviceState.hasNext() )
                {
                    throw new Exception( "device type or state empty in response" );
                }

                Element deviceTypeElement = iteratorDeviceType.next();
                String deviceType = deviceTypeElement.text();

                Element deviceStateElement = iteratorDeviceState.next();
                String deviceState = deviceStateElement.text();

                StateInfo.add( deviceType );
                StateInfo.add( deviceState );

                Log.d( Constants.TAG, "url=" + url + ", deviceType=" + deviceType + ", deviceState=" + deviceState );
            }
            catch( Exception e )
            {
                mError = "Get info failed, IP=" + deviceIP + " failed: " + e.getMessage();
                Log.d( Constants.TAG, mError );
            }

            return StateInfo;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s)
        {
            super.onPostExecute(s);

            if( false == mError.isEmpty() )
            {
                Toast.makeText(ContextGetter.getInstance().get(), mError, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class SendTask extends AsyncTask<String, Void, Void>
    {
        private String mError = "";
        public SendTask(){}
        @Override
        protected Void doInBackground( String... strings )
        {
            final String deviceIP = strings[0];
            final String url = strings[1];
            try
            {
                new HtmlHelper( new URL( url ) );
            }
            catch( Exception e )
            {
                mError = "Sending failed, IP=" + deviceIP;
                Log.d(Constants.TAG, "send to " + deviceIP + " failed: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute( Void aVoid )
        {
            super.onPostExecute(aVoid);

            if( false == mError.isEmpty() )
            {
                Toast.makeText(ContextGetter.getInstance().get(), mError, Toast.LENGTH_LONG).show();
            }
        }
    }
}
