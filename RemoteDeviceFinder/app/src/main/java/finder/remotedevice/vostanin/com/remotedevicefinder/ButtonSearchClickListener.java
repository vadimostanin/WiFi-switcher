package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.app.ProgressDialog;
import android.view.View;
import java.util.ArrayList;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;

/**
 * Created by vostanin on 10/18/16.
 */
public class ButtonSearchClickListener implements View.OnClickListener
{
    private MainActivity mContext;
    private IDone< ArrayList<BaseDevice> > mDone;

    public ButtonSearchClickListener( MainActivity context, IDone< ArrayList<BaseDevice> > done )
    {
        mContext = context;
        mDone = done;
    }
    @Override
    public void onClick( View view )
    {
        mContext.mProgressDialog = ProgressDialog.show( mContext, "Working...", "Scanning", true, false );
//        Toast.makeText( mContext, "clicked on Search", Toast.LENGTH_SHORT ).show();
        final DiscoveryDeviceTask getTask = new DiscoveryDeviceTask(mContext, new IDone<ArrayList<BaseDevice>>() {
            @Override
            public void onDone(ArrayList<BaseDevice> baseDevices)
            {
                mDone.onDone(baseDevices );
            }
        });
        Utils.asyncTaskExecute( getTask );
    }
}
