package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

/**
 * Created by vostanin on 11/11/16.
 */
public class DeviceScheduleAddActivityEverydayFragment extends Fragment
{
    CalendarView mCalendar;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate( R.layout.activity_device_schedule_add_everyday_fragment, null );


        return v;
    }
}
