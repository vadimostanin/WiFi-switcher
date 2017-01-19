package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.swipelistview.BaseSwipeListViewListener;
import finder.remotedevice.vostanin.com.remotedevicefinder.swipelistview.SwipeListView;

public class DeviceScheduleActivity extends AppCompatActivity
{
    private SwipeListView mSwipeListview;
    private ItemAdapterDeviceSchedule mAdapter;
    private ArrayList<ItemRowDeviceSchedule> mItemData;
    private BaseDevice  mDevice;
    private Button mBtnAddSchedule;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_schedule);

        final int deviceIndex = ( int ) getIntent().getExtras().get( Constants.EXTRA_DEVICE_INDEX );
        mDevice = DevicesCache.getInstance().get( deviceIndex );

        mSwipeListview = (SwipeListView) findViewById(R.id.activity_device_schedule_example_swipe_lv_list);
        mItemData = new ArrayList<>();
        mAdapter = new ItemAdapterDeviceSchedule( ContextGetter.getInstance().get(), R.layout.device_schedule_activity_listview_row, mItemData );

        mSwipeListview.setSwipeMode(SwipeListView.SWIPE_MODE_NONE); // there are five swiping modes
        mSwipeListview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
        mSwipeListview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_NONE);
        mSwipeListview.setOffsetLeft(Utils.convertDpToPixel(ContextGetter.getInstance().get(), 0.0f)); // left side offset
        mSwipeListview.setOffsetRight(Utils.convertDpToPixel(ContextGetter.getInstance().get(), 0.0f)); // right side offset
        mSwipeListview.setAnimationTime(50); // animarion time
        mSwipeListview.setSwipeOpenOnLongPress(false); // enable or disable SwipeOpenOnLongPress

        mSwipeListview.setAdapter(mAdapter);

        mSwipeListview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
            }

            @Override
            public void onStartClose(int position, boolean right) {
            }

            @Override
            public void onClickFrontView(int position) {
            }

            @Override
            public void onClickBackView(int position) {
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
            }

        });

        mBtnAddSchedule = ( Button ) findViewById( R.id.activity_device_schedule_add_schedule_button );
        mBtnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContextGetter.getInstance().get(), DeviceScheduleAddActivity.class);
                intent.putExtra(Constants.EXTRA_DEVICE_INDEX, deviceIndex);
                ContextGetter.getInstance().get().startActivity(intent);
            }
        });
    }

    private void updateScheduleList()
    {
        Utils.asyncTaskExecute(new DeviceGetScheduleListTask(new IDone<ArrayList<DeviceGetScheduleListTask.SchedItem>>() {
            @Override
            public void onDone(ArrayList<DeviceGetScheduleListTask.SchedItem> schedItems) {
                for (DeviceGetScheduleListTask.SchedItem schedItem : schedItems) {
                    String schedAction = schedItem.getAction();
                    String schedPeriod = schedItem.getPeriod();
                    String schedDate = schedItem.getDate();
                    String schedTime = schedItem.getTime();
                    if (true == schedAction.equals("0")) {
                        if (true == schedPeriod.equals(Constants.SchedPeriodTypeOnes)) {
                            mItemData.add(new ItemRowDeviceSchedule(ContextGetter.getInstance().get().getResources().getDrawable(R.drawable.turn_off_64), schedDate, schedTime, mAdapter));
                        } else if (true == schedPeriod.equals(Constants.SchedPeriodTypeOnes)) {
                            mItemData.add(new ItemRowDeviceSchedule(ContextGetter.getInstance().get().getResources().getDrawable(R.drawable.turn_off_64), schedTime, mAdapter));
                        }
                    } else if (true == schedAction.equals("1")) {
                        if (true == schedPeriod.equals(Constants.SchedPeriodTypeOnes)) {
                            mItemData.add(new ItemRowDeviceSchedule(ContextGetter.getInstance().get().getResources().getDrawable(R.drawable.turn_on_64), schedDate, schedTime, mAdapter));
                        } else if (true == schedPeriod.equals(Constants.SchedPeriodTypeOnes)) {
                            mItemData.add(new ItemRowDeviceSchedule(ContextGetter.getInstance().get().getResources().getDrawable(R.drawable.turn_on_64), schedTime, mAdapter));
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }), mDevice.getIp(), (new Integer(mDevice.getPort())).toString());
    }

    @Override
    protected void onResume()
    {
        mAdapter.clear();
        mItemData.clear();
        mAdapter.notifyDataSetChanged();
        updateScheduleList();
        super.onResume();
    }
}
