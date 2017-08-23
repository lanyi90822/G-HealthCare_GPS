package com.example.hp.stepcount.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.util.Xml;

import com.example.hp.stepcount.Common.Data_parse;
import com.example.hp.stepcount.Common.GPS_parse;
import com.example.hp.stepcount.Interface.BluetoothCB;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by lenovo on 2016/4/13.
 */
public class blueConnectedThread extends Thread {

    private static final String TAG = "com.example.lenovo.caseapp.Common.blue.blueConnectedThread";
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private static FileOutputStream mmFileOutStream;
    public static boolean recive_state = true;
    private BluetoothCB mBluetoothCB;
    private static String data_ = "";

    public blueConnectedThread(BluetoothCB mcb,BluetoothSocket socket) {
        mBluetoothCB = mcb;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();

        } catch (IOException e) { }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run()
    {
        byte[] buffer = new byte[200];
        int bytes;

        while (recive_state)
        {
            try {
                bytes = mmInStream.read(buffer);
                if (bytes > 0 && bytes == 18){
                    byte[] ecgBlock = new byte[bytes];
                    System.arraycopy(buffer, 0, ecgBlock, 0, bytes);
                    int data_data[] = Data_parse.Gesonser_parse(ecgBlock);
                    if (data_data != null && data_data.length>1) {
                        mBluetoothCB.ReceiveBlueData(data_data);
                        Log.w(TAG, "run_data" + " = " + data_data[3] + " = " + data_data[4] + " = " + data_data[5]);
                    }
                }else if (bytes > 0 && bytes != 18){

//                    byte[] array = new byte[bytes];
//                    System.arraycopy(buffer, 0, array, 0, bytes);
//                    char[] data_char = bytearrayToChararray(array);
//                    double[] data_gps = GPS_parse.Get_GPS_Data(data_char);
//
//                    if (!(data_gps[0] == 0 || data_gps[1] == 0)){
//                        mBluetoothCB.ReceiveGPSData(data_gps);
//                    }
//                    data_ = String.valueOf(data_char);
//                    data_ = "";
                }

                Log.w(TAG,"asdfjkl" + " = " + bytes);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!recive_state){

            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    public void updateFilePath(FileOutputStream FileOutStream) {
        mmFileOutStream = FileOutStream;
    }



    private char[] getChars (byte[] bytes) {
        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate (bytes.length);
        bb.put (bytes);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);

        return cb.array();
    }

    private char[] bytearrayToChararray (byte[] bytes) {
        int len = bytes.length;
        char[] data_array = new char[len];

        for (int i = 0;i<len;i++){

          data_array[i] = (char)bytes[i];

        }

        return data_array;
    }
















}
