package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;

/**
 * Created by vostanin on 11/10/16.
 */
public class ItemAdapterDeviceSchedule extends ArrayAdapter
{
    private List mData;
    private Context mContext;
    private int mLayoutResId;

    public ItemAdapterDeviceSchedule( Context context, int layoutResourceId, List data )
    {
        super( context, layoutResourceId, data );

        mData = data;
        mContext = context;
        mLayoutResId = layoutResourceId;

        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {

        NewsHolder holder;
        View row = convertView;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate( mLayoutResId, parent, false );

            holder = new NewsHolder();

            holder.setIcon( ( ImageView ) row.findViewById( R.id.activity_device_schedule_listview_item_action_icon ) );
//            holder.setPeriod((TextView) row.findViewById(R.id.activity_device_schedule_listview_item_period));
            holder.setDate((TextView) row.findViewById(R.id.activity_device_schedule_listview_item_date));
            holder.setTime((TextView) row.findViewById(R.id.activity_device_schedule_listview_item_time));
//            holder.button1=(Button)row.findViewById(R.id.swipe_button1);
//            holder.setBtnRemove( ( Button ) row.findViewById( R.id.activity_device_schedule_btnRemove ) );
            row.setTag( holder );
        }
        else
        {
            holder = (NewsHolder)row.getTag();
        }

        final ItemRowDeviceSchedule itemData = ( ItemRowDeviceSchedule ) mData.get( position );
//        holder.getPeriod().setText( itemData.getPeriod() );
        holder.getDate().setText( itemData.getDate() );
        holder.getTime().setText( itemData.getTime() );
        holder.getIcon().setImageDrawable( itemData.getIcon() );

//        holder.button1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "Button 1 Clicked",Toast.LENGTH_SHORT).show();
//            }
//        });

//        holder.getBtnRemove().setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
////                Toast.makeText( mContext, "Remove Clicked", Toast.LENGTH_SHORT).show();
//                DevicesCache.getInstance().remove(DevicesCache.getInstance().get(position));
//                ItemAdapterDeviceSchedule.this.remove(itemData);
//                ItemAdapterDeviceSchedule.this.notifyDataSetChanged();
//            }
//        });

        return row;

    }

    static class NewsHolder
    {
        ImageView mIcon;
        TextView mPeriod;
        TextView mDate;
        TextView mTime;
//        Button mBtnRemove;

        public ImageView getIcon(){ return mIcon; }
//        public Button getBtnRemove(){ return mBtnRemove; }
//        public TextView getPeriod(){ return mPeriod; }
        public TextView getDate(){ return mDate; }
        public TextView getTime(){ return mTime; }

        public void setIcon( ImageView icon ){ mIcon = icon; }
//        public void setBtnRemove( Button btn ){ mBtnRemove = btn; }
//        public void setPeriod( TextView period ){ mPeriod = period; }
        public void setDate( TextView date ){ mDate = date; }
        public void setTime( TextView time ){ mTime = time; }
    }
}
