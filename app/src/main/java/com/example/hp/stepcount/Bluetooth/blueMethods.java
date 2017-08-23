package com.example.hp.stepcount.Bluetooth;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.Method;

/**
 * Created by lenovo on 2016/4/13.
 */
public class blueMethods {



    static public boolean autoBond(BluetoothDevice device,String strPin) throws Exception {
        Method autoBondMethod = device.getClass().getMethod("setPin", new Class[]{byte[].class});
        Boolean result = (Boolean)autoBondMethod.invoke(device,new Object[]{strPin.getBytes()});
        return result;
    }

    //开始配对
    static public boolean createBond(BluetoothDevice device) throws Exception {
        Method createBondMethod = device.getClass().getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }











}
