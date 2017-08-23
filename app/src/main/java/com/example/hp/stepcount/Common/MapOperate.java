package com.example.hp.stepcount.Common;

import android.content.Context;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.hp.stepcount.R;


/**
 * Created by lenovo on 2016/6/27.
 */
public class MapOperate {

    BaiduMap mBaiduMap;

    public MapOperate(BaiduMap mymap)
    {
        mBaiduMap = mymap;
    }


    //在信息数组内提取经纬度信息，根据提取的信息定位标记。
    public LatLng LocateByMessage(char[] temp)
    {

        char[] mlon = new char[14];
        char[] mlat = new char[14];

        System.arraycopy(temp, 6, mlon, 0, 14);
        System.arraycopy(temp, 23, mlat, 0, 14);

        double Lot = Double.valueOf(String.valueOf(mlon));
        double Lat = Double.valueOf(String.valueOf(mlat));

        LatLng point = new LatLng(Lot, Lat);

      //  MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 17);//缩放级别3~21
        //设置标记点居中
     //   mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        //设置比例尺
    //    mBaiduMap.setMapStatus(msu);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(point).zoom(18.0f);//缩放级别3~21
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.star);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        return point;
    }


    //直接输入经纬度信息定位
    public void Baidumaplocate(LatLng mlatlng)
    {

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(mlatlng).zoom(18.0f);//缩放级别3~21
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.star);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(mlatlng).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }



    public LatLng LocateByMessage2(char[] temp)
    {

        char[] mlon = new char[14];
        char[] mlat = new char[14];

        System.arraycopy(temp, 6, mlon, 0, 14);
        System.arraycopy(temp, 23, mlat, 0, 14);

        double Lot = Double.valueOf(String.valueOf(mlon));
        double Lat = Double.valueOf(String.valueOf(mlat));
        mBaiduMap.clear();
        LatLng point = new LatLng(Lot, Lat);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 17);//缩放级别3~21
        //设置标记点居中
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        //设置比例尺
        mBaiduMap.setMapStatus(msu);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.star);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        return point;
    }




    public LatLng mapLocate(double Lo,double La)
    {
        mBaiduMap.clear();
        LatLng point = new LatLng(Lo, La);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 19);//缩放级别3~21

        //设置标记点居中
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        //设置比例尺
        mBaiduMap.setMapStatus(msu);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.star);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        return point;
    }
    //使用GPS原始数据定位
    public LatLng gpsmapLocate(double Lo,double La)
    {
     //   mBaiduMap.clear();
        LatLng point = new LatLng(Lo, La);

        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(point);
        point = converter.convert();

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 19);//缩放级别3~21

        //设置标记点居中
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        //设置比例尺
        mBaiduMap.setMapStatus(msu);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.star);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        return point;
    }

    //弹出气泡显示地址信息
    public void popushow(TextView tv,String str,Context context,LatLng lat)
    {
        tv = new TextView(context);
        tv.setBackgroundResource(R.drawable.popup);
        tv.setTextColor(0xFF000000);
        //  popupText.setText(marker.getExtraInfo().getString("des"));
        tv.setText(str);
        mBaiduMap.showInfoWindow(new InfoWindow(tv, lat, -60));
    }

    //延时函数
    public void delay(int ms){
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }





}
