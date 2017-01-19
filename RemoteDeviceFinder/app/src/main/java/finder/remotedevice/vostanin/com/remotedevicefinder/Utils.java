package finder.remotedevice.vostanin.com.remotedevicefinder;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by vostanin on 10/18/16.
 */
public class Utils
{
    public static String readFile( String filePath )
    {
        String result = "";
        try
        {
            FileReader fileReader = new FileReader( new File( filePath ) );
            char[] fileBuff = new char[1024];

            while( true )
            {
                int readed = fileReader.read( fileBuff );
                if( -1 == readed )
                {
                    break;
                }
                result += String.valueOf( fileBuff, 0, readed );
            }
            fileReader.close();
        }
        catch( Exception e )
        {
            ;
        }
        return result;
    }

    public static String sendReceiveTCP_2( String deviceIP, int devicePort, int timeout, String deviceData, boolean responseExpected ) throws Exception
    {
        String response = "";
        final Socket socket = new Socket( deviceIP, devicePort );
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        dataOutputStream.writeUTF(deviceData);
        dataOutputStream.flush();

        if( true == responseExpected )
        {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while( true )
            {
                try
                {
                    response += dataInputStream.readUTF();
                }
                catch( EOFException e )
                {
                    break;
                }
            }
            dataInputStream.close();
        }


        dataOutputStream.close();
        socket.close();

        return response;
    }

    public static String sendReceiveTCP( String deviceIP, int devicePort, int timeout, String deviceData, boolean responseExpected ) throws Exception
    {
        String response = "";
        final Socket socket = new Socket( deviceIP, devicePort );
        try
        {
            // Send
            {
                socket.setSoTimeout( timeout );
                final OutputStream out = socket.getOutputStream();
                final PrintWriter output = new PrintWriter(out);
                output.print( deviceData );
                output.flush();
                out.flush();
//                out.close();
            }

            if( true == responseExpected )
            {
                // Receive
                {
                    byte[] buf = new byte[1024];
                    final BufferedReader input = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
                    //read line(s)
                    final String st = input.readLine();
                    response = new String( st );
                }
            }
            socket.close();
        }
        catch( Exception e )
        {
            Log.d( Constants.TAG, "sendReceive: " + deviceIP + ", devicePort=" + devicePort + ", timeout=" + timeout + "deviceData=" + deviceData + ", failed: " + e.getMessage() );
            throw e;
        }
        finally
        {
            socket.close();
        }
        return response;
    }

    public static String sendReceiveUDP( String deviceIP, int devicePort, int timeout, String deviceData, boolean responseExpected )
    {
        String response = "";
        try
        {
            DatagramSocket socket = new DatagramSocket( devicePort );
            // Send
            {
                socket.setSoTimeout(timeout);
                final InetAddress deviceAddress = InetAddress.getByName( deviceIP );
                DatagramPacket packet = new DatagramPacket( deviceData.getBytes(), deviceData.length(), deviceAddress, devicePort );
                socket.send( packet );
            }

            if( true == responseExpected )
            {
                // Receive
                {
                    byte[] buf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    final int length = packet.getLength();
                    response = new String(packet.getData(), 0, length);
                }
            }
            socket.close();
        }
        catch( Exception e )
        {
            Log.d( Constants.TAG, "sendReceive: " + deviceIP + ", devicePort=" + devicePort + ", timeout=" + timeout + "deviceData=" + deviceData + ", failed: " + e.getMessage() );
        }
        return response;
    }

    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> asyncTaskExecute( AsyncTask<Params, Progress, Result> task, Params ... params )
    {
        try
        {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
            {
                task.executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR, params );//.get( Constants.TIMEOUT_MS, TimeUnit.MILLISECONDS );
            }
            else
            {
                task.execute( params );//.get( Constants.TIMEOUT_MS, TimeUnit.MILLISECONDS );
            }
        }
        catch ( Exception e )
        {
            Log.d( Constants.TAG, "Utils.asyncTaskExecute:", e );
        }


        return task;
    }

    public static int convertDpToPixel( Context context, float dp )
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi);
        return (int) px;
    }
}
