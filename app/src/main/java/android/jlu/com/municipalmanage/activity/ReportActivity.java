package android.jlu.com.municipalmanage.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.base.BasePopWindow;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.jlu.com.municipalmanage.utils.MyApplication;
import android.jlu.com.municipalmanage.utils.PreferenceUtils;
import android.jlu.com.municipalmanage.utils.RetrofitUploadUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import mabeijianxi.camera.MediaRecorderActivity;
import mabeijianxi.camera.model.AutoVBRMode;
import mabeijianxi.camera.model.MediaRecorderConfig;
import mabeijianxi.camera.util.FileUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by beyond on 17/4/20.
 */

public class ReportActivity extends Activity {
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Uri imageUri;
    private File outputImage;

    //照片路径
    private String PHOTO_PIC_PATH="";

    //视频
    private String videoUri="";
    //视频第一帧
    private String videoScreenshot;

   // private TextView tv_select_type;//类型选择
    private List<String> currentTypeLists;

    private View parentView;
    private PopupWindow popupWindow;

    private TextView tv_pop_title;
    private ImageView iv_pop_back;
    private TextView tv_question_type;
    private ListView popListOne;
    private ListView popListTwo;
    private ImageView iv_take_photo;
    private ImageView iv_take_video;
    private ImageView iv_photo_show;
    private EditText tv_address;
    private ImageView iv_video_screenshot;
    private EditText et_description;
    private AlertDialog dialog;

    //上传按钮
    private Button uploadButton;
    //分别为经度,纬度,地址,问题描述，发现者，时间，类型
    private String longitude,latitude,address,site_desc,finder,type;
    //图片，视频
    private File upload_photo,upload_video;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String find_time = format.format(new java.util.Date());
    private static final String TAG = "ReportActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tv_address = (EditText) findViewById(R.id.tv_address);
        et_description = (EditText) findViewById(R.id.et_description);
        //tv_select_type = (TextView) findViewById(R.id.tv_select_type);
        iv_take_photo = (ImageView) findViewById(R.id.iv_take_photo);
        iv_take_video = (ImageView) findViewById(R.id.iv_take_video);
        iv_photo_show = (ImageView) findViewById(R.id.iv_photo_show);
        tv_question_type =(TextView) findViewById(R.id.tv_question_type);
        iv_video_screenshot = (ImageView) findViewById(R.id.iv_video_screenshot);
        uploadButton = (Button)findViewById(R.id.upload_button);

        //获取传来的数据
        Intent intent =getIntent();
        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");
        address = intent.getStringExtra("address");
        tv_address.setText(address);
        Toast.makeText(this,address, Toast.LENGTH_SHORT).show();
        finder = PreferenceUtils.getString(this,"USER_NAME","wowowo");


        //上传数据
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                site_desc = et_description.getText().toString();
                type = tv_question_type.getText().toString();
                address = tv_address.getText().toString();

                if(site_desc.equals("")||type .equals("")||address.equals("")){
                    Toast.makeText(ReportActivity.this,
                            "请确定所有信息均填入",Toast.LENGTH_SHORT).show();
                }else if(PHOTO_PIC_PATH.equals("")||videoUri.equals("")){
                    Toast.makeText(ReportActivity.this,
                            "请确定照片和视频均拍摄",Toast.LENGTH_SHORT).show();
                }else {
                    upload_photo = new File(PHOTO_PIC_PATH);
                    upload_video = new File(videoUri);
                    //上传
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(UriSet.UPLOAD_URI)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .build();
                        RetrofitUploadUtil retrofitUploadUtil = retrofit.create(RetrofitUploadUtil.class);
                        RequestBody requestFile1 = RequestBody.create(MediaType.parse("image/jpeg"), upload_photo);
                        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), upload_video);
                        MultipartBody.Part[]  file = new MultipartBody.Part[2];
                        file[0] = MultipartBody.Part.createFormData("upload", upload_photo.getName(), requestFile1);
                        file[1] = MultipartBody.Part.createFormData("upload", upload_video.getName(), requestFile2);
                        //  file[1] = MultipartBody.Part.createFormData("upload", "test.mp4", requestFile2);
                        final ProgressDialog dialog = new ProgressDialog(ReportActivity.this);
                        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
                        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
                        dialog.setTitle("提示");
                        dialog.setMessage("正在上传");
                        dialog.show();
                        Call<String> call = retrofitUploadUtil.updateImage(file,
                                RequestBody.create(null, longitude),
                                RequestBody.create(null, latitude),
                                RequestBody.create(null, address),
                                RequestBody.create(null, site_desc),
                                RequestBody.create(null, finder),
                                RequestBody.create(null, find_time),
                                RequestBody.create(null, type));

                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                dialog.dismiss();
                                Toast.makeText(ReportActivity.this,"提示信息："+response.body().toString(),Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(ReportActivity.this,"网络错误，请检查网络设置",Toast.LENGTH_SHORT).show();
                            }
                        });


                }

            }
        });


        tv_question_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取控件的位置
                int[] location = new int[2];
                v.getLocationOnScreen(location);
               // popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                popupWindow.showAsDropDown(v,80,20);
                popListOne.setAdapter(new PopAdapter(MyApplication.typeLists));
            }
        });

        iv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PHOTO_PIC_PATH = Environment
                        .getExternalStorageDirectory().getAbsolutePath()+ "/municipal_photo.jpg";
                outputImage = new File(PHOTO_PIC_PATH);
                // 创建File对象，储存拍照后的照片，存在根目录下
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();// TODO: handle exception
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);// 启动相机程序


            }
        });
        iv_photo_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PHOTO_PIC_PATH.equals("")) {
                    Intent intent = new Intent(ReportActivity.this, PhotoShowActivity.class);
                    intent.putExtra("PHOTO_COMPRESS_PATH", PHOTO_PIC_PATH);
                    startActivity(intent);
                } else {
                    //还没拍照
                    Toast.makeText(ReportActivity.this, "没有拍照哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 录制
                MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                        .doH264Compress(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                        )
                        .setMediaBitrateConfig(new AutoVBRMode()
//                        .setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST)
                        )
                        .smallVideoWidth(480)
                        .smallVideoHeight(580)
                        .recordTimeMax(5 * 1000)
                        .maxFrameRate(20)
                        .captureThumbnailsTime(1)
                        .recordTimeMin((int) (1.5 * 1000))
                        .build();


//                MediaRecorderActivity.goSmallVideoRecorder(
//                        ReportActivity.this, ReportActivity.class.getName(), config);

                startActivityForResult(
                        new Intent(ReportActivity.this, MediaRecorderActivity.class)
                                .putExtra("over_activity_name", ReportActivity.class.getName())
                                .putExtra("media_recorder_config_key", config), 5);


            }
        });




        iv_video_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(ReportActivity.this, VideoPlayerActivity.class)
                                .putExtra("path", videoUri));
            }
        });

//        initData();
        initPopView();



    }


    @Override
    public void onBackPressed() {
        hesitate();
    }

    private void hesitate() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setMessage("是否放弃保存的信息")
                    .setNegativeButton(
                            R.string.record_camera_cancel_dialog_no,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(ReportActivity.this,
                                            "暂不放弃", Toast.LENGTH_SHORT).show();
                                }

                            })
                    .setPositiveButton(R.string.record_camera_cancel_dialog_yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(ReportActivity.this,
                                            "确定放弃", Toast.LENGTH_SHORT).show();
                                    FileUtils.deleteDir(getIntent().getStringExtra(MediaRecorderActivity.OUTPUT_DIRECTORY));
                                    FileUtils.deleteFile(videoUri);
                                    finish();
                                }
                            })
                    .setCancelable(false)
                    .show();
        } else {
            dialog.show();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
        videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
        Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);
        iv_video_screenshot.setImageBitmap(bitmap);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 5:
                if(resultCode == 10){
                    Intent intent = data;
                    videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
                    videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
                    Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);

//            iv_video_screenshot.setImageBitmap(bitmap);
                    iv_video_screenshot.setBackground(new BitmapDrawable(bitmap));
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        iv_photo_show.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();// TODO: handle exception
                    }
                }
                break;
            default:
                break;


        }


    }


    private void initPopView() {

        popupWindow = new BasePopWindow();
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_report, null);

        int width = getResources().getDisplayMetrics().widthPixels *  2/3;
        int height = getResources().getDisplayMetrics().heightPixels * 1/2;
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);

        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_type, null);
        popupWindow.setContentView(view);

        tv_pop_title = (TextView) view.findViewById(R.id.tv_pop_title);
        iv_pop_back = (ImageView) view.findViewById(R.id.iv_pop_back);
        popListOne = (ListView) view.findViewById(R.id.lv_one);
        popListTwo = (ListView) view.findViewById(R.id.lv_two);

        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);//点击外侧是否关闭

        iv_pop_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popListTwo.setVisibility(View.GONE);
                popListOne.setVisibility(View.VISIBLE);
                iv_pop_back.setVisibility(View.GONE);
                tv_pop_title.setText("分类选择");
            }
        });

        popListOne.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_pop_title.setText("项目选择");
                iv_pop_back.setVisibility(View.VISIBLE);
                popListOne.setVisibility(View.GONE);
                popListTwo.setVisibility(View.VISIBLE);
                if (position == 0) {
                    currentTypeLists = MyApplication.roadLists1;
                } else if (position == 1) {
                    currentTypeLists = MyApplication.walkLists2;
                } else if (position == 2) {
                    currentTypeLists = MyApplication.stoneLists3;
                } else if (position == 3) {
                    currentTypeLists = MyApplication.waterLists4;
                } else if (position == 4) {
                    currentTypeLists = MyApplication.lightLists5;
                } else if (position == 5) {
                    currentTypeLists = MyApplication.otherLists6;
                }
                popListTwo.setAdapter(new PopAdapter(currentTypeLists));
            }
        });

        popListTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_question_type.setText(currentTypeLists.get(position));
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_pop_title.setText("分类选择");
                popListTwo.setVisibility(View.GONE);
                popListOne.setVisibility(View.VISIBLE);
                iv_pop_back.setVisibility(View.GONE);
            }
        });

    }


    class PopAdapter extends BaseAdapter {

        private List<String> lists;

        public PopAdapter(List<String> lists) {
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ReportActivity.this)
                        .inflate(R.layout.pop_list_item, null);
                holder.nameView = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (getItem(position) != null) {
                holder.nameView.setText(getItem(position).toString());
            }
            return convertView;

        }
    }

    private static class ViewHolder {
        TextView nameView;
    }
}