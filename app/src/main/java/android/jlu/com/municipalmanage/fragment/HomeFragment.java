package android.jlu.com.municipalmanage.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.activity.RepairUploadActivity;
import android.jlu.com.municipalmanage.activity.ReportActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beyond on 17/4/11.
 */

public class HomeFragment extends BaseFragment1 {

    public LocationClient mLocationClient;

    private Button find_button, repair_button;
   //分别为经度纬度地址
    private String longitude,latitude,address;

    //显示地图
    private MapView mapView;

    //地图的控制器
    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;


    public static HomeFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("aaa", "onCreate()");

    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        Log.d("aaa", "onCreateview");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //获取按钮
        find_button = (Button) view.findViewById(R.id.find_button);
        repair_button = (Button) view.findViewById(R.id.repair_button);

        find_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向下一个活动传递
                Intent intent = new Intent(getActivity(),ReportActivity.class);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                intent.putExtra("address",address);
                startActivity(intent);

            }
        });


        repair_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), RepairUploadActivity.class));
            }
        });


        //设置为百度地图的坐标系
        SDKInitializer.setCoordType(CoordType.GCJ02);
        mapView = (MapView) view.findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();//获取BaiduMap类
        //允许定位
        baiduMap.setMyLocationEnabled(true);
        //用一个LIST集合存放权限
        List<String> permissionList = new ArrayList<>();
        //对权限进行处理
        if (ContextCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }
        return view;
    }

    //定位到自己的位置
    private void navigateTo(BDLocation location) {
        longitude=location.getLongitude()+"";
        latitude = location.getLatitude()+"";
        address = location.getLocationDescribe();
        //当前位置显示在地图上
        MyLocationData.Builder locationBuilder = new MyLocationData.
                Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
//        Toast.makeText(getActivity(), "你现在" + location.getLocationDescribe(), Toast.LENGTH_SHORT).show();
        if (location.getLocType() != 161) {
//            Toast.makeText(getActivity(), "错误代码" + location.getLocType(), Toast.LENGTH_SHORT).show();
        }
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
        baiduMap.setMyLocationConfiguration(config);
        if (isFirstLocate) {
            //获取地址信息   getAddStr  位置语义化信息  location.getLocationDescribe()
            int error = location.getLocType();
            Log.d("error", "" + error);
            Log.d("error", location.getLocType() + "");
            Log.d("error", location.getAddrStr());
            Log.d("error", location.getLocationDescribe());
            Log.d("error", SDKInitializer.getCoordType() + "");
            //获取经纬度定位到地图上
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            //设置缩放级别（3-19）越大越精细
            update = MapStatusUpdateFactory.zoomTo(18f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;

        }


    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setScanSpan(3000);//位置更新时间间隔
        option.setIsNeedAddress(true);  //将经纬度反编译出地址
        option.setIsNeedLocationDescribe(true);

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onResume() {

        isFirstLocate = true;
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    //权限的处理结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                   Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    //mlocationClient.start() 回调到监听器中
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            navigateTo(location);

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }
}
