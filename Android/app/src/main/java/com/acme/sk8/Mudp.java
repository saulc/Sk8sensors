package com.acme.sk8;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Mudp {
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
    public sensorListener mlistener;
    private DatagramSocket ds = null;
    private int port = 8889;
    private InetAddress addr = null;

    interface sensorListener{
        void updateData(String dat);
    }

    public void setListener(sensorListener l){
        mlistener = l;
    }
    public void requestSensorData() {
        new Thread() {
            @Override
            public void run() {
                try{
                    byte[] ipAddr = new byte[]{(byte) 10, (byte) 0, 0, (byte) 72};

                    iniSocket(InetAddress.getByAddress(ipAddr));
                    sendRequest();
                    mlistener.updateData(rmessage);
//                    closeSocket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void sendRequest(){
        new Thread() {
            @Override
            public void run() {
                try{
                    log("Requesting Data update...  ");
                DatagramPacket requestPac = new DatagramPacket(Message.getBytes(), Message.getBytes().length, addr, port);
                if(ds == null){
                    log("Connection not initialized...");

                }else {
                    ds.setBroadcast(true);
                    ds.send(requestPac);
                    ds.receive(requestPac);
                    rmessage = new String(requestPac.getData(), requestPac.getOffset(), requestPac.getLength());
                    log(rmessage);
                }
            }catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        }.start();
    }
    public void iniSocket(InetAddress address) {
        log("setting up the connection...");
        try {
            addr = address;
            ds = new DatagramSocket(port);
//            ds.setReuseAddress(true);
        } catch (Exception e) {
            log("Connection setup error: ");
            e.printStackTrace();
        }
    }
        public void closeSocket(){
            log("Closing Connection  ");
                try {
                    if (ds != null){
                        ds.disconnect();
                        ds.close();
                    }  } catch (Exception e) {
                    log("Connection close error: ");
                    e.printStackTrace();
                }
            }

}