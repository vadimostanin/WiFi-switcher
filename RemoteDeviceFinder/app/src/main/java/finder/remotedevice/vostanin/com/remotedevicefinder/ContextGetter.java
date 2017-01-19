package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.content.Context;

/**
 * Created by vostanin on 10/19/16.
 */
public class ContextGetter
{
    private static ContextGetter mInstance = new ContextGetter();

    public static ContextGetter getInstance()
    {
        return mInstance;
    }

    private ContextGetter(){}

    private MainActivity mContext;

    public void set( MainActivity context )
    {
        mContext = context;
    }

    public MainActivity get()
    {
        return mContext;
    }
}
