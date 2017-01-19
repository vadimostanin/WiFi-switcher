package finder.remotedevice.vostanin.com.remotedevicefinder.devices;

/**
 * Created by vostanin on 10/19/16.
 */
public class BaseDevice
{
    private String mId = "";
    private String mType = "";
    private String mIp = "";
    private Integer mPort = new Integer( 33248 );
    private String mName = "";
    protected BaseDeviceState mState;
    protected BaseDeviceRemoteState mRemoteState;

    public BaseDevice( String id, String type, String ip )
    {
        mId = id;
        mType = type;
        mIp = ip;
    }

    public BaseDevice( String id, String type, String ip, Integer port )
    {
        this( id, type, ip );
        mPort = port;
    }

    public boolean equals( Object comparable )
    {
        if( false == ( comparable instanceof BaseDevice ) )
        {
            return false;
        }
        if( getId() != ( ( BaseDevice ) comparable ).getId() )
        {
            return false;
        }
        return true;
    }

    public String getId()
    {
        return mId;
    }

    public String getType()
    {
        return mType;
    }

    public String getIp()
    {
        return mIp;
    }

    public String getName()
    {
        return mName;
    }

    public Integer getPort() { return mPort; }

    public BaseDevice setName( String name )
    {
        mName = name;
        return this;
    }

    public BaseDevice setPort( Integer port ) { mPort = port; return this; }

    public void setState( BaseDeviceState state )
    {
        mState.copy( state );
    }

    public BaseDeviceState getState()
    {
        return mState;
    }

    public void setRemoteState( BaseDeviceRemoteState state )
    {
        mRemoteState = state;
    }

    public BaseDeviceRemoteState getRemoteState()
    {
        return mRemoteState;
    }
}
