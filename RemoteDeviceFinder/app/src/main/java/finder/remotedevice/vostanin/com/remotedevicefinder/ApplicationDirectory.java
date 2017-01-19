package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.content.Context;
import android.os.Environment;

/**
 * Created by Asus on 06.03.2016.
 */
public class ApplicationDirectory {
    private static ApplicationDirectory ourInstance = new ApplicationDirectory();

    public static ApplicationDirectory getInstance() {
        return ourInstance;
    }

    private ApplicationDirectory() {
    }

    public String Get( Context context )
    {
        return context.getFilesDir().getPath().toString();
//        return Environment.getExternalStorageDirectory().getPath();
    }

    public String getDataDir( Context context )
    {
        return Get( context ) + "/data";
    }
}
