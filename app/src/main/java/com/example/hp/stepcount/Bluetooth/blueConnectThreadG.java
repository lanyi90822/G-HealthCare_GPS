package com.example.hp.stepcount.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.example.hp.stepcount.Service.stepcountservice;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by HP on 2017/2/15.
 */
public class blueConnectThreadG extends Thread{

    private static final String METHOD_CREATE_RFCOMM_SOCKET  = "createRfcommSocket";
    public static BluetoothSocket mmSocket = null;
    private final BluetoothDevice mmDevice;
    private final Handler mmHandler;
    private static final UUID btUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public blueConnectThreadG(BluetoothDevice device, Handler handler) throws IOException {
        mmHandler = handler;
        mmDevice = device;
        mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(btUUID);
    }

    public void run() {
        try {
            mmSocket.connect();
            mmHandler.sendEmptyMessage(stepcountservice.msg_manageconnected);
        } catch (IOException connectException) {
            System.out.println(connectException);
            try {
                mmSocket.close();
            }catch (IOException closeException) {
            }
            return;
        }
    }








    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    public BluetoothSocket getSocket(){return mmSocket; }




}
