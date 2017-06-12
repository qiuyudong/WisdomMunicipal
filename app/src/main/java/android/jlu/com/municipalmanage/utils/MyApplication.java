package android.jlu.com.municipalmanage.utils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import mabeijianxi.camera.VCamera;

public class MyApplication extends Application {

    public static final List<String> typeLists = new ArrayList<>(
            Arrays.asList("道路", "人行步道", "边石", "排水", "路灯", "其他"));

    public static final List<String> roadLists1 = new ArrayList<>(
            Arrays.asList("坑槽", "铣刨罩面", "翻浆", "灌缝", "冷补料补垫", "临时补垫"));

    public static final List<String> walkLists2 = new ArrayList<>(
            Arrays.asList("方砖调整", "方砖更换", "火烧板更换", "火烧板调整", "界石调整",
                    "界石更换", "树池调整", "树池更换"));

    public static final List<String> stoneLists3 = new ArrayList<>(
            Arrays.asList("边石调整", "边石更换", "M型边石更换", "边石勾缝"));

    public static final List<String> waterLists4 = new ArrayList<>(
            Arrays.asList("更换管线", "管线疏通", "检查井清掏", "收水井清掏",
                    "井筒抬高", "更换检查井盖板", "更换收水井盖板", "检查井井盖更换",
                    "检查井井盖调整", "收水井井筒子更换", "收水井井筒子调整"));

    public static final List<String> lightLists5 = new ArrayList<>(
            Arrays.asList("灯具", "空开", "电缆故障", "控制箱", "灯杆"));

    public static final List<String> otherLists6 = new ArrayList<>(
            Arrays.asList("刨冰", "清理超高土", "清淤", "污水泵抽水", "余料外运"));

    //存储路径(缓存)
    public static String MY_CACHE_PATH = Environment
            .getExternalStorageDirectory() + "/Municipal/";

    //照片
   // public static  String PHOTO_PATH = Environment
     //       .getExternalStorageDirectory().getAbsolutePath()+ "/photo/";

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());//百度地图
        JPushInterface.setDebugMode(true);//极光推送
        JPushInterface.init(this);
        initSmallVideo(this);//小视频
    }

    public void initSmallVideo(Context context) {
        // 设置拍摄视频缓存路径
//        File dcim = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        if (DeviceUtils.isZte()) {
//            if (dcim.exists()) {
//                VIDEO_CACHE_PATH = dcim + "/mabeijianxi/";
////                VCamera.setVideoCachePath(VIDEO_CACHE_PATH);
//            } else {
//                VIDEO_CACHE_PATH = dcim.getPath().replace("/sdcard/",
//                        "/sdcard-ext/")
//                        + "/mabeijianxi/";
////                VCamera.setVideoCachePath(VIDEO_CACHE_PATH);
//            }
//        } else {
//            VIDEO_CACHE_PATH = dcim + "/mabeijianxi/";
//        }


        VCamera.setVideoCachePath(MY_CACHE_PATH);

        VCamera.setDebugMode(true);
        VCamera.initialize(context);
    }

}
