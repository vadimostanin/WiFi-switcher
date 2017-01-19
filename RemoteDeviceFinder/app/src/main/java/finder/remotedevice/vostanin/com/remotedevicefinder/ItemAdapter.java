package finder.remotedevice.vostanin.com.remotedevicefinder;

/**
 * Created by vostanin on 10/19/16.
 */
import java.util.List;
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
import android.widget.Toast;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;

public class ItemAdapter extends ArrayAdapter {

    private List   mData;
    private Context mContext;
    private int mLayoutResId;

    public ItemAdapter( Context context, int layoutResourceId, List data )
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

            holder.itemName = (TextView)row.findViewById(R.id.activity_device_swicther_listview_item_name);
            holder.icon=(ImageView)row.findViewById(R.id.activity_device_swicther_listview_item_icon);
//            holder.button1=(Button)row.findViewById(R.id.swipe_button1);
            holder.button2=(Button)row.findViewById(R.id.activity_device_swicther_listview_item_button_edit);
            holder.button3=(Button)row.findViewById(R.id.activity_device_swicther_listview_item_button_remove);
            row.setTag(holder);
        }
        else
        {
            holder = (NewsHolder)row.getTag();
        }

        final ItemRow itemData = (ItemRow) mData.get( position );
        holder.itemName.setText( itemData.getItemName() );
        holder.icon.setImageDrawable( itemData.getIcon() );

//        holder.button1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "Button 1 Clicked",Toast.LENGTH_SHORT).show();
//            }
//        });

        holder.button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast.makeText(mContext, "Edit Clicked", Toast.LENGTH_SHORT).show();

                final BaseDevice device = DevicesCache.getInstance().get( position );
                final Intent intent = new Intent( mContext, DeviceEditActivity.class );
                intent.putExtra( Constants.EXTRA_DEVICE_ID, device.getId() );
                intent.putExtra( Constants.EXTRA_DEVICE_TYPE, device.getType() );
                intent.putExtra( Constants.EXTRA_DEVICE_IP, device.getIp() );
                intent.putExtra( Constants.EXTRA_DEVICE_NAME, device.getName() );
                intent.putExtra( Constants.EXTRA_DEVICE_INDEX, position );

                mContext.startActivity( intent );
            }
        });

        holder.button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast.makeText( mContext, "Remove Clicked", Toast.LENGTH_SHORT).show();
                DevicesCache.getInstance().remove( DevicesCache.getInstance().get( position ) );
                MainActivity.mSwipeListview.closeOpenedItems();
                ItemAdapter.this.remove(itemData);
                ItemAdapter.this.mData.remove(itemData);
                ItemAdapter.this.notifyDataSetChanged();
            }
        });

        return row;

    }

    static class NewsHolder{

        TextView itemName;
        ImageView icon;
//        Button button1;
        Button button2;
        Button button3;
    }

}
