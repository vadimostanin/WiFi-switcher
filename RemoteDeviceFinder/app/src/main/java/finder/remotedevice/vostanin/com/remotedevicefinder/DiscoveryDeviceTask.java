package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import finder.remotedevice.vostanin.com.remotedevicefinder.devices.BaseDevice;
import finder.remotedevice.vostanin.com.remotedevicefinder.devices.DeviceTypes;

/**
 * Created by vostanin on 10/18/16.
 */

public class DiscoveryDeviceTask extends AsyncTask<String, Void, ArrayList<BaseDevice>>
{
    private Context mContext = null;
    private WifiManager mWifi;
    private final String mChallenge = "myvoice";
    private IDone<ArrayList<BaseDevice>>  mDone;

    public DiscoveryDeviceTask( Context context, IDone<ArrayList<BaseDevice>> done )
    {
        mContext = context;
        mDone = done;
        mWifi = ( WifiManager ) mContext.getSystemService( Context.WIFI_SERVICE );
    }
    //Фоновая операция
    protected ArrayList<BaseDevice> doInBackground(String... arg)
    {
        ArrayList<BaseDevice> clients = new ArrayList<>();

        if( false == mWifi.isWifiEnabled() )
        {
            return clients;
        }
        WifiInfo wifiInfo = mWifi.getConnectionInfo();

        try
        {
            final DatagramSocket socket = new DatagramSocket( Constants.DISCOVERY_PORT );
            socket.setBroadcast( true );
            socket.setSoTimeout(Constants.TIMEOUT_MS);
            sendDiscoveryRequest(socket);
            clients = listenForResponses( socket );
            socket.close();
        }
        catch( Exception e )
        {
//            Toast.makeText( mContext, e.getMessage(), Toast.LENGTH_SHORT ).show();
            int a = 0;
            a++;
        }
        return clients;
    }

    //Событие по окончанию парсинга
    protected void onPostExecute( final ArrayList<BaseDevice> clients )
    {
        mDone.onDone( clients );
    }

    /**
     * Send a broadcast UDP packet containing a request for boxee services to
     * announce themselves.
     *
     * @throws IOException
     */
    private void sendDiscoveryRequest(DatagramSocket socket) throws IOException {
        String data = String.format("discover");
        Log.d( Constants.TAG, "Sending data " + data );

        final InetAddress broadcastAddress = getBroadcastAddress();
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), broadcastAddress
                , Constants.DISCOVERY_PORT);
        socket.send(packet);
    }

    /**
     * Calculate the broadcast IP we need to send the packet along. If we send it
     * to 255.255.255.255, it never gets sent. I guess this has something to do
     * with the mobile network not wanting to do broadcast.
     */
    private InetAddress getBroadcastAddress() throws IOException
    {
        DhcpInfo dhcp = mWifi.getDhcpInfo();
        if (dhcp == null) {
            Log.d( Constants.TAG, "Could not get dhcp info");
            return null;
        }

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    /**
     * Listen on socket for responses, timing out after TIMEOUT_MS
     *
     * @param socket
     *          socket on which the announcement request was sent
     * @return list of discovered servers, never null
     * @throws IOException
     */
    private ArrayList<BaseDevice> listenForResponses( DatagramSocket socket ) throws IOException
    {
        long start = System.currentTimeMillis();
        byte[] buf = new byte[1024];
        ArrayList<BaseDevice> clients = new ArrayList<>();

        // Loop and try to receive responses until the timeout elapses. We'll get
        // back the packet we just sent out, which isn't terribly helpful, but we'll
        // discard it in parseResponse because the cmd is wrong.
        try
        {
            while( true )
            {
                DatagramPacket packet = new DatagramPacket( buf, buf.length );
                Log.d( Constants.TAG,"before receive" );
                socket.receive(packet);
                Log.d(Constants.TAG, "after receive");
                final int length = packet.getLength();
                String response = new String( packet.getData(), 0, length );
                Log.d(Constants.TAG, "Packet received after "
                        + (System.currentTimeMillis() - start) + " length=" + length + ", " + response);
                Log.d(Constants.TAG, "listenForResponses 1");
                BaseDevice device = DeviceDiscoveryCreator.create( response, packet.getAddress().getHostAddress() );
                Log.d(Constants.TAG, "listenForResponses 2");
                if( false == device.getType().equals( DeviceTypes.None ) )
                {
                    clients.add(device);
                }
            }
        }
        catch( Exception e )
        {
            Log.d( Constants.TAG, "Receive timed out: " + e.getMessage() );
        }
        Set<BaseDevice> hs = new HashSet<>();
        hs.addAll( clients );
        clients.clear();
        clients.addAll( hs );
        return clients;
    }
}
