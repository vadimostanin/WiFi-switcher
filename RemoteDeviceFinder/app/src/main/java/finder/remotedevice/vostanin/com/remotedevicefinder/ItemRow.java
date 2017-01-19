package finder.remotedevice.vostanin.com.remotedevicefinder;

/**
 * Created by vostanin on 10/19/16.
 */
import android.graphics.drawable.Drawable;
import android.widget.Adapter;

public class ItemRow
{
    private String itemName;
    private Drawable icon;
    private ItemAdapter mAdapter;

    public ItemRow( String itemName, Drawable icon, ItemAdapter adapter )
    {
        super();
        this.itemName = itemName;
        this.icon = icon;
        mAdapter = adapter;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public ItemAdapter getAdapter() { return mAdapter; }

}
