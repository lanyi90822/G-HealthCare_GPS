package com.example.hp.stepcount.Interface;

/**
 * Created by lenovo on 2016/6/23.
 */
public interface BluetoothCB {

    public void ReceiveBlueData(int[] block);
    public void ReceiveGPSData(double[] block);

}
