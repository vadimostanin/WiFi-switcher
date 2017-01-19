package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by vostanin on 11/3/16.
 */
public class DeviceGetScheduleListTask extends AsyncTask<String, Void, ArrayList<DeviceGetScheduleListTask.SchedItem>>
{
    public class SchedItem
    {
        private SchedItem( String action, String period, String date, String time )
        {
            mAction = action;
            mPeriod = period;
            mDate = date;
            mTime = time;
        }
        public SchedItem( String action, String date, String time )
        {
            this( action, Constants.SchedPeriodTypeOnes, date, time );
        }
        public SchedItem( String action, String time )
        {
            this( action, Constants.SchedPeriodTypeEveryday, "", time );
        }

        private String mAction = "";
        private String mPeriod = "";
        private String mDate = "";
        private String mTime = "";

        public String getAction(){ return mAction; }
        public String getPeriod(){ return mPeriod; }
        public String getDate(){ return mDate; }
        public String getTime(){ return mTime; }
    }
    private IDone<ArrayList<DeviceGetScheduleListTask.SchedItem>> mDone;
    public DeviceGetScheduleListTask( IDone<ArrayList<DeviceGetScheduleListTask.SchedItem>> done )
    {
        mDone = done;
    }

    private ArrayList<DeviceGetScheduleListTask.SchedItem> StateInfo = new ArrayList<>();

    @Override
    protected ArrayList<DeviceGetScheduleListTask.SchedItem> doInBackground( String... strings )
    {
        final String deviceIP = strings[0];
        final String devicePort = strings[1];
        Log.d(Constants.TAG, "DeviceGetCredencialsTask:doInBackground entered");
        Log.d( Constants.TAG, "DeviceGetCredencialsTask:doInBackground deviceIP=" + deviceIP + ", devicePort=" + devicePort );

        try
        {
            String url = "http://" + deviceIP + ":" + devicePort + "/getschedule";

// Request a string response from the provided URL.
            HtmlHelper hh = new HtmlHelper( new URL( url ) );

//            Log.d( Constants.TAG, hh.mRootDocument.html() );

            Elements schedItemElements = hh.getDivsByClass("schedule_item");
            Iterator<Element> iteratorSchedItem = schedItemElements.iterator();
            do
            {
                Element schedItemElement = iteratorSchedItem.next();

                Elements schedActionElements = hh.getElementsByClass( schedItemElement, "action" );
                Elements schedPeriodElements = hh.getElementsByClass(schedItemElement, "period");

                Iterator<Element> iteratorSchedAction = schedActionElements.iterator();
                Iterator<Element> iteratorSchedPeriod = schedPeriodElements.iterator();
                if( false == iteratorSchedAction.hasNext() || false == iteratorSchedPeriod.hasNext() )
                {
                    throw new Exception( "device type or state empty in response" );
                }

                Element schedActionElement = iteratorSchedAction.next();
                String schedAction = schedActionElement.text();

                Element schedPeriodElement = iteratorSchedPeriod.next();
                String schedPeriod = schedPeriodElement.text();

//                StateInfo.add( schedAction );
//                StateInfo.add( schedPeriod );

                Elements schedDateElements = hh.getElementsByClass( schedItemElement, "date" );
                Elements schedTimeElements = hh.getElementsByClass( schedItemElement, "time" );
                if( true == schedPeriod.equals( "ones" ) )
                {
                    Iterator<Element> iteratorSchedDate = schedDateElements.iterator();
                    Iterator<Element> iteratorSchedTime = schedTimeElements.iterator();
                    if( false == iteratorSchedDate.hasNext() || false == iteratorSchedTime.hasNext() )
                    {
                        throw new Exception( "device type or state empty in response" );
                    }

                    Element schedDateElement = iteratorSchedDate.next();

                    String schedDate = schedDateElement.text();

                    Element schedTimeElement = iteratorSchedTime.next();
                    String schedTime = schedTimeElement.text();

//                    StateInfo.add( schedDate );
//                    StateInfo.add( schedTime );
                    Log.d( Constants.TAG, "schedAction=" + schedAction + ", schedDate=" + schedDate + ", schedTime" + schedTime );
                    final SchedItem schedItem = new SchedItem( schedAction, schedDate, schedTime );
                    StateInfo.add( schedItem );
                }
                else if( true == schedPeriod.equals( "everyday" ) )
                {
                    Iterator<Element> iteratorSchedTime = schedTimeElements.iterator();
                    if( false == iteratorSchedAction.hasNext() || false == iteratorSchedPeriod.hasNext() )
                    {
                        throw new Exception( "device type or state empty in response" );
                    }

                    Element schedTimeElement = iteratorSchedTime.next();
                    String schedTime = schedTimeElement.text();

//                    StateInfo.add( schedTime );

                    Log.d( Constants.TAG, "schedAction=" + schedAction + ", schedTime" + schedTime );
                    final SchedItem schedItem = new SchedItem( schedAction, schedTime );
                    StateInfo.add( schedItem );
                }
            }
            while( true );


        }
        catch( Exception e )
        {

        }
        return StateInfo;
    }

    @Override
    protected void onPostExecute( ArrayList<DeviceGetScheduleListTask.SchedItem> schedItems )
    {
        mDone.onDone( schedItems );
        super.onPostExecute(schedItems);
    }
}
