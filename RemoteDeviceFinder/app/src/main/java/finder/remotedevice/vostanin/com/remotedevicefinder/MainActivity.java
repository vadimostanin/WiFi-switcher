package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceSwitcher;
import finder.remotedevice.vostanin.com.remotedevicefinder.swipelistview.BaseSwipeListViewListener;
import finder.remotedevice.vostanin.com.remotedevicefinder.swipelistview.SwipeListView;

public class MainActivity extends AppCompatActivity
{
    private Button mBtnSearch;
    private ListView mListClients;
    public ProgressDialog mProgressDialog;

    public static SwipeListView mSwipeListview;
    private ItemAdapter mAdapter;
    private ArrayList<ItemRow> mItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextGetter.getInstance().set(this);
        DeviceStoreManager.getInstance().load();
        DevicesCache.getInstance().updateRemoteState();

        initControls();

        for( int device_i = 0 ; device_i < DevicesCache.getInstance().count() ; device_i++ )
        {
            final BaseDevice device = DevicesCache.getInstance().get( device_i );
            mItemData.add( new ItemRow( device.getName(), getResources().getDrawable( R.drawable.icon_switcher ), mAdapter ) );
        }
//                mListClients.setAdapter(new ArrayAdapter(MainActivity.this, R.layout.article_text_list_item, clients));
        mAdapter.notifyDataSetChanged();

    }

    private void initControls()
    {
//        mBtnSearch = ( Button ) findViewById( R.id.button_search );
//        mListClients = ( ListView ) findViewById( R.id.listView_clients );
////        mBtnSearch.setOnClickListener( new ButtonSearchClickListener( this, new ListViewClientFill() ) );
//        // register onClickListener to handle click events on each item
//        mListClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            // argument position gives the index of item which is clicked
//            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
//                String selectedmovie = mListClients.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), "Movie Selected : " + selectedmovie, Toast.LENGTH_LONG).show();
//            }
//        });

        mSwipeListview = ( SwipeListView ) findViewById( R.id.example_swipe_lv_list );
        mItemData = new ArrayList<ItemRow>();
        mAdapter = new ItemAdapter( this, R.layout.custom_row, mItemData );


        mSwipeListview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {}

            @Override
            public void onClosed(int position, boolean fromRight) {}

            @Override
            public void onListChanged() {}

            @Override
            public void onMove(int position, float x) {}

            @Override
            public void onStartOpen(int position, int action, boolean right) {}

            @Override
            public void onStartClose(int position, boolean right) {}

            @Override
            public void onClickFrontView(int position) {
                Log.d( Constants.TAG, String.format( "onClickFrontView %d", position ) );
//                Toast.makeText( ContextGetter.getInstance().get(), "Item " + position + " clicked", Toast.LENGTH_SHORT ).show();

                final Intent intent = new Intent( ContextGetter.getInstance().get(), DeviceSwictherActivity.class );
                intent.putExtra( Constants.EXTRA_DEVICE_INDEX, position );
                ContextGetter.getInstance().get().startActivity( intent );

//                swipelistview.openAnimate(position); //when you touch front view it will open
            }

            @Override
            public void onClickBackView(int position) {}

            @Override
            public void onDismiss(int[] reverseSortedPositions) {}

        });

        //These are the swipe listview settings. you can change these
        //setting as your requrement
        mSwipeListview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
        mSwipeListview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
        mSwipeListview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_NONE);
        mSwipeListview.setOffsetLeft(Utils.convertDpToPixel(this, 0.0f)); // left side offset
        mSwipeListview.setOffsetRight( Utils.convertDpToPixel( this, 0.0f ) ); // right side offset
        mSwipeListview.setAnimationTime(50); // animarion time
        mSwipeListview.setSwipeOpenOnLongPress(false); // enable or disable SwipeOpenOnLongPress

        mSwipeListview.setAdapter( mAdapter );

//        for(int i=0;i<10;i++)
//        {
//            itemData.add(new ItemRow("item"+i,getResources().getDrawable(R.drawable.icon_switcher) ));
//
//        }
//
//        adapter.notifyDataSetChanged();

        FloatingActionButton fabScan = (FloatingActionButton) findViewById( R.id.fabScan );
        fabScan.setOnClickListener( new ButtonSearchClickListener( this, new ListViewClientFill() ) );
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById( R.id.fabAdd );
        fabAdd.setOnClickListener( new ButtonAddClickListener( this ) );

        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(
                            Thread paramThread,
                            Throwable paramThrowable
                    ) {
                        //Do your own error handling here

                        if (oldHandler != null)
                            oldHandler.uncaughtException(
                                    paramThread,
                                    paramThrowable
                            ); //Delegates to Android's error handling
                        else
                            System.exit(2); //Prevents the service/app from freezing
                    }
                });


    }

    private class ListViewClientFill extends IDone<ArrayList<BaseDevice>>
    {
        public ListViewClientFill()
        {
        }

        public void onDone( ArrayList<BaseDevice> devices )
        {
            try
            {
                for( BaseDevice device : devices )
                {
                    device.setState(device.getRemoteState().getRemote());
                }
//                devices.add( new DeviceSwitcher( "123", "localhost" ).setName( "localhost" ) );

//                DevicesCache.getInstance().clear();
//                mItemData.clear();
                DevicesCache.getInstance().add( devices );

                for( BaseDevice client : devices )
                {
                    mItemData.add( new ItemRow( client.getName(), getResources().getDrawable( R.drawable.icon_switcher ), mAdapter ) );
                }
//                mListClients.setAdapter(new ArrayAdapter(MainActivity.this, R.layout.article_text_list_item, clients));
                mAdapter.notifyDataSetChanged();
                DeviceStoreManager.getInstance().save();
            }
            catch( Exception e )
            {
                int a = 0;
                a++;
            }
            Toast.makeText( MainActivity.this, "Done", Toast.LENGTH_SHORT ).show();
            mProgressDialog.dismiss();
        }
    }
}
