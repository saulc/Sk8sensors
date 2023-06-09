package com.acme.sk8;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.fragment.app.Fragment;


public class MConnect  {
//https://stackoverflow.com/questions/26016954/udp-client-on-android
//    udb client example

    private final String TAG = getClass().getSimpleName();

    private void log(String s ) {
        Log.i(TAG, s);
    }
    private InetAddress IPAddress = null;
    public String rmessage = "Connection Error.? ";
    private AsyncTask<Void, Void, Void> async_cient;
    public String Message = "Test message data place holder ............";
    public Mudp.sensorListener mlistener;
//
//    interface sensorListener{
//        void updateData(String dat);
//    }
    @SuppressLint("NewApi")
    public void requestSensorData()
    {
        async_cient = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                DatagramSocket ds = null;
                int port = 8889;

                try
                {
                    byte[] ipAddr = new byte[]{ (byte) 10, (byte) 0,0, (byte) 74};
                    InetAddress addr = InetAddress.getByAddress(ipAddr);

                    ds = new DatagramSocket(port);
                    ds.setReuseAddress(true);
//                    if(!ds.isBound() & !ds.isConnected())
//                    ds.bind(new InetSocketAddress(port));
                    DatagramPacket requestPac;
                    requestPac = new DatagramPacket(Message.getBytes(), Message.getBytes().length, addr, port);
//                    DatagramPacket responsePac;
//                    responsePac = new DatagramPacket(Message.getBytes(), Message.getBytes().length, addr, 8888);
                    ds.setBroadcast(true);
                    ds.send(requestPac);

                    ds.receive(requestPac);
                    rmessage = new String(requestPac.getData(), requestPac.getOffset(), requestPac.getLength());
//                    rmessage = requestPac.toString();
                    log(rmessage);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (ds != null)
                    {
                        ds.disconnect();
                        ds.close();
                    }
                }
                return null;
            }

            protected void onPostExecute(Void result)
            {
                super.onPostExecute(result);
                log("post execute ...");
                mlistener.updateData(rmessage);
            }
        };

        if (Build.VERSION.SDK_INT >= 11) async_cient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else async_cient.execute();
    }
}