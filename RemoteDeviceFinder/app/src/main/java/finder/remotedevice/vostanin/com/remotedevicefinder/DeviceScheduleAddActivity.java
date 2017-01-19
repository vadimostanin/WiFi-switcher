package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;

public class DeviceScheduleAddActivity extends AppCompatActivity
{
    private BaseDevice mDevice;

    private DeviceScheduleAddActivityOnesFragment mFragmentPeriodOnes;
    private DeviceScheduleAddActivityEverydayFragment mFragmentPeriodEveryday;
    private FragmentManager mFragmentManager;
    private Button mBtnAdd;
    private ProgressDialog mProgressDialog;

    enum PeriodTypes
    {
        ONES, EVERYDAY
    }

    enum ActionTypes
    {
        ON, OFF
    }

    private PeriodTypes mPeriodType = PeriodTypes.ONES;
    private ActionTypes mActionType = ActionTypes.ON;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_device_schedule_add );

        final int deviceIndex = ( int ) getIntent().getExtras().get(Constants.EXTRA_DEVICE_INDEX);
        mDevice = DevicesCache.getInstance().get( deviceIndex );

        mFragmentPeriodOnes = new DeviceScheduleAddActivityOnesFragment();
        mFragmentPeriodEveryday = new DeviceScheduleAddActivityEverydayFragment();
        mFragmentManager = getFragmentManager();

        mBtnAdd = ( Button ) findViewById( R.id.activity_device_schedule_add_button_add );
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view )
            {
                onClickButtonAdd();
            }
        });
        createScheduleOnesContent();
    }

    private void onClickButtonAdd()
    {
        String action = "";
        if( mActionType == ActionTypes.ON )
        {
            action = "1";
        }
        else if( mActionType == ActionTypes.OFF )
        {
            action = "0";
        }

        String period = "";
        String date = "";
        String time = "";
        if( mPeriodType == PeriodTypes.ONES )
        {
            period = "ones";
            final CalendarView calendarView = ( CalendarView ) findViewById( R.id.activity_device_schedule_add_ones_fragment_calendarView );
            final TimePicker timePicker = ( TimePicker ) findViewById( R.id.activity_device_schedule_add_ones_fragment_timePicker );

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format( new Date( calendarView.getDate() ) );

            time = "" + timePicker.getHour() + "%3A" + timePicker.getMinute();

//            mProgressDialog = ProgressDialog.show( ContextGetter.getInstance().get(), "Adding...", "", true, false);

            Utils.asyncTaskExecute( new DeviceScheduleOnesAddTask(), mDevice.getIp(), ( new Integer( mDevice.getPort() ) ).toString(), action, date, time );
        }
        else if( mPeriodType == PeriodTypes.EVERYDAY )
        {
            period = "everyday";
            final TimePicker timePicker = ( TimePicker ) findViewById( R.id.activity_device_schedule_add_everyday_fragment_timePicker );

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            time = "" + timePicker.getHour() + "%3A" + timePicker.getMinute();

//            mProgressDialog = ProgressDialog.show( ContextGetter.getInstance().get(), "Adding...", "", true, false);

            Utils.asyncTaskExecute( new DeviceScheduleEverydayAddTask( new IDone<Void>()
            {
                public void onDone( Void params )
                {
                    finish();
                }
            }), mDevice.getIp(), ( new Integer( mDevice.getPort() ) ).toString(), action, time );
        }
    }

    public void onClickRadioActionOn( View v )
    {
        if( mActionType == ActionTypes.ON )
        {
            return;
        }
//        Toast.makeText( ContextGetter.getInstance().get(), "ON", Toast.LENGTH_SHORT).show();
        mActionType = ActionTypes.ON;
    }

    public void onClickRadioActionOff( View v )
    {
        if( mActionType == ActionTypes.OFF )
        {
            return;
        }
//        Toast.makeText( ContextGetter.getInstance().get(), "OFF", Toast.LENGTH_SHORT).show();
        mActionType = ActionTypes.OFF;
    }

    public void onClickRadioPeriodOnes( View v )
    {
        if( mPeriodType == PeriodTypes.ONES )
        {
            return;
        }
//        Toast.makeText( ContextGetter.getInstance().get(), "Ones", Toast.LENGTH_SHORT).show();
        clearFragmentContainer();
        createScheduleOnesContent();

        mPeriodType = PeriodTypes.ONES;
    }

    public void onClickRadioPeriodEveryday( View v )
    {
        if( mPeriodType == PeriodTypes.EVERYDAY )
        {
            return;
        }
//        Toast.makeText( ContextGetter.getInstance().get(), "Everyday", Toast.LENGTH_SHORT).show();
        clearFragmentContainer();
        createScheduleEverydayContent();

        mPeriodType = PeriodTypes.EVERYDAY;
    }

    private void clearFragmentContainer()
    {
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        fragmentTransaction.remove( mFragmentPeriodOnes );
        fragmentTransaction.remove( mFragmentPeriodEveryday );
        fragmentTransaction.commit();
    }

    private void createScheduleOnesContent()
    {
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        fragmentTransaction.add( R.id.activity_device_schedule_add_fragment_container, mFragmentPeriodOnes, "HELLO" );
        fragmentTransaction.commit();
    }

    private void createScheduleEverydayContent()
    {
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add( R.id.activity_device_schedule_add_fragment_container, mFragmentPeriodEveryday, "HELLO" );
        fragmentTransaction.commit();
    }
}
