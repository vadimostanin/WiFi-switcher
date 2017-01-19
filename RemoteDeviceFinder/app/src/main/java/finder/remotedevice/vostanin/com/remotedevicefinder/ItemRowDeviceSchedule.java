package finder.remotedevice.vostanin.com.remotedevicefinder;

/**
 * Created by vostanin on 10/19/16.
 */
import android.graphics.drawable.Drawable;

public class ItemRowDeviceSchedule
{
    private String mAction = "";
    private String mPeriod = "";
    private String mDate = "";
    private String mTime = "";
    private Drawable mIcon;

    private ItemAdapterDeviceSchedule mAdapter;

    private ItemRowDeviceSchedule( Drawable icon, String action, String period, String date, String time, ItemAdapterDeviceSchedule adapter)
    {
        super();
        mAction = action;
        mPeriod = period;
        mDate = date;
        mTime = time;
        this.mIcon = icon;
        mAdapter = adapter;
    }

    public ItemRowDeviceSchedule( Drawable icon, String date, String time, ItemAdapterDeviceSchedule adapter)
    {
        this( icon, "", Constants.SchedPeriodTypeOnes, date, time, adapter );
    }

    public ItemRowDeviceSchedule( Drawable icon, String time, ItemAdapterDeviceSchedule adapter)
    {
        this( icon, "", Constants.SchedPeriodTypeEveryday, "", time, adapter );
    }
    public String getAction(){ return mAction; }
    public String getPeriod(){ return mPeriod; }
    public String getDate(){ return mDate; }
    public String getTime(){ return mTime; }
    public Drawable getIcon() {
        return mIcon;
    }
    public void setIcon( Drawable icon ) {
        mIcon = icon;
    }
    public ItemAdapterDeviceSchedule getAdapter() { return mAdapter; }
}
