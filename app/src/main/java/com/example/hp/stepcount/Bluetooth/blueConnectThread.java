package com.example.hp.stepcount.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.example.hp.stepcount.Service.stepcountservice;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by lenovo on 2016/4/13.
 */
public class blueConnectThread extends Thread {

    private static final String METHOD_CREATE_RFCOMM_SOCKET  = "createRfcommSocket";
    public static BluetoothSocket mmSocket = null;
    private final BluetoothDevice mmDevice;
    private final Handler mmHandler;

    public blueConnectThread(BluetoothDevice device, Handler handler) throws IOException {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        mmHandler = handler;
        BluetoothSocket tmp = null;
        mmDevice = device;
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            Method m = mmDevice.getClass().getMethod(METHOD_CREATE_RFCOMM_SOCKET, new Class[] {int.class});
            tmp = (BluetoothSocket) m.invoke(mmDevice, 1);
        }catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            // Log.w(TAG, "TARGET CONNECTED");
            mmHandler.sendEmptyMessage(stepcountservice.msg_manageconnected);
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            System.out.println(connectException);
            try {
                mmSocket.close();
            }catch (IOException closeException) {
            }
            return;
        }
        // Do work to manage the connection (in a separate thread)
        //   manageConnectedSocket(mmSocket);
    }








    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    public BluetoothSocket getSocket(){return mmSocket; }




}
