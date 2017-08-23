package com.example.hp.stepcount.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.hp.stepcount.Common.GPS_parse;
import com.example.hp.stepcount.Common.MapOperate;
import com.example.hp.stepcount.R;
import com.example.hp.stepcount.Service.stepcountservice;

public class locate extends Activity {
    private static final String TAG = "com.example.hp.stepcount.Activity";
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MapOperate mMapOperate;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private boolean isFirstLoc = true; // 是否首次定位
    private double latitude;
    private double longitude;
    private double[] smsblock = new double[2];
    private static String gps_raw = "$GPGGA,000705.055,3952.3683,N,11628.8203,E,0,00,,0.0,M,0.0,M,,0000*41";
    //double[] raw_gps = new double[2];
    private GPS_parse mGPS_parse = new GPS_parse();
    BitmapDescriptor mCurrentMarker;
    public static final String ACTION_LOCAT_REQUEST = "com.example.hp.stepcount.Activity.ACTION_LOCAT_REQUEST";
    private Button maker_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_locate);

        BaiduMapInit();
        locateInit();
        registerMReceiver();

        maker_clear = (Button)findViewById(R.id.maker_clear);
        maker_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.clear();
            }
        });

//        char[] ddaa = gps_raw.toCharArray();
//        double[] raw_gps = GPS_parse.Get_GPS_Data(ddaa);
//        //double[] raw_gps1 = GPS_parse.Get_GPS_Data(aadd);
//        Log.w(TAG, "raw_gps" + "=" + raw_gps[0] + "   " + raw_gps[1]);
//        mMapOperate.gpsmapLocate(raw_gps[0], raw_gps[1]);

    }
    private void BaiduMapInit() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        mMapOperate = new MapOperate(mBaiduMap);

    }



    //定位初始化
    private void locateInit()
    {
        //开启定位层
        mBaiduMap.setMyLocationEnabled(true);

//        //定位初始化
//        //注意: 实例化定位服务 LocationClient类必须在主线程中声明 并注册定位监听接口
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);              //打开GPS
//        option.setCoorType("bd09ll");        //设置坐标类型
//        option.setScanSpan(3000);            //设置发起定位请求的间隔时间为5000ms
//        mLocClient.setLocOption(option);     //设置定位参数
//        mLocClient.start();                  //调用此方法开始定位
        mMapOperate.gpsmapLocate(stepcountservice.location_data[0],stepcountservice.location_data[1]);
    }




    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        public void onReceivePoi(BDLocation location) {
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapview 销毁后不在处理新接收的位置
            if (location == null || mBaiduMap == null) {
                return;
            }
            //MyLocationData.Builder定位数据建造器
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            //设置定位数据
            mBaiduMap.setMyLocationData(locData);
            mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
            //获取经纬度
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            //Toast.makeText(getApplicationContext(), String.valueOf(latitude), Toast.LENGTH_SHORT).show();
            //第一次定位的时候，那地图中心点显示为定位到的位置
            if (isFirstLoc) {
                isFirstLoc = false;
                //地理坐标基本数据结构
                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());
                //MapStatusUpdate描述地图将要发生的变化
                //MapStatusUpdateFactory生成地图将要反生的变化
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
                mBaiduMap.animateMapStatus(msu);
                // Toast.makeText(getApplicationContext(), location.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //广播接收，接收来自MapService的消息、接收数据
    private BroadcastReceiver messageReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if (stepcountservice.ACTION_GPS_CHANGE.equals(action)){
                smsblock = intent.getDoubleArrayExtra("temp_gps");
                mMapOperate.gpsmapLocate(smsblock[0], smsblock[1]);
            }
            if (stepcountservice.ACTION_LOCATION_CHANG.equals(action)){
                double[] temp = intent.getDoubleArrayExtra("location_data");
                mMapOperate.gpsmapLocate(temp[0],temp[1]);
            }

        }
    };

    private void registerMReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(stepcountservice.ACTION_GPS_CHANGE);
        filter.addAction(stepcountservice.ACTION_LOCATION_CHANG);
        registerReceiver(messageReciver, filter); // Don't forget to unregister during onDestroy
    }

    private void unregisterMReceiver(){
        unregisterReceiver(messageReciver);
    }



    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        unregisterMReceiver();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_locate, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
