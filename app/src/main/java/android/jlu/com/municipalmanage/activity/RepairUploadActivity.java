package android.jlu.com.municipalmanage.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.jlu.com.municipalmanage.utils.RetrofitRepairUpload;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.jlu.com.municipalmanage.R.id.iv_photo_show;
import static android.jlu.com.municipalmanage.R.id.iv_take_photo;


public class RepairUploadActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Uri imageUri;
    private File outputImage,upload_photo;

    //照片路径
    private String PHOTO_PIC_PATH="";

    private EditText task_id;
    private EditText task_desc;
    private ImageView take_photo,photo_show;
    private Button mButton;
    private RadioGroup stateRg;
    private RadioButton inProgressRb,finishRb;
    private String  id,service_time,service_desc;
    private String state="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_upload);
        task_id = (EditText)findViewById(R.id.task_id);
        task_desc = (EditText)findViewById(R.id.task_desc);
        take_photo = (ImageView)findViewById(iv_take_photo);
        photo_show = (ImageView) findViewById(iv_photo_show);
        mButton = (Button)findViewById(R.id.upload_button);
        stateRg = (RadioGroup)findViewById(R.id.state_rg);
        inProgressRb = (RadioButton)findViewById(R.id.inProgress);
        finishRb = (RadioButton)findViewById(R.id.finish);

       //获取编号
        Intent intent = getIntent();
        id = intent.getStringExtra("task_id");
        task_id.setText(id);

        //状态选择
        stateRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == inProgressRb.getId()){
                    state = "2";
                }else if(checkedId == finishRb.getId()){
                    state = "3";
                }
            }
        });


        //拍摄照片
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PHOTO_PIC_PATH = Environment
                        .getExternalStorageDirectory().getAbsolutePath()+ "/municipal_photo2.jpg";
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

        //照片展示
        photo_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PHOTO_PIC_PATH.equals("")) {
                    Intent intent = new Intent(RepairUploadActivity.this, PhotoShowActivity.class);
                    intent.putExtra("PHOTO_COMPRESS_PATH", PHOTO_PIC_PATH);
                    startActivity(intent);
                } else {
                    //还没拍照
                    Toast.makeText(RepairUploadActivity.this, "没有拍照哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //上传按钮
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                service_time = format.format(new java.util.Date());
                service_desc = task_desc.getText().toString();
                if(id.equals("")||service_desc.equals("")||state.equals("0")){
                    Toast.makeText(RepairUploadActivity.this, "请确认填写了所有信息", Toast.LENGTH_SHORT).show();
                }else if (PHOTO_PIC_PATH.equals("")){
                    Toast.makeText(RepairUploadActivity.this, "请确认拍摄了照片", Toast.LENGTH_SHORT).show();
                }else {
                    upload_photo = new File(PHOTO_PIC_PATH);
                    upload();
                }

            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
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
                        photo_show.setImageBitmap(bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();// TODO: handle exception
                    }
                }
                break;
            default:
                break;
        }


    }

    private void upload() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在上传");
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriSet.SERVER_URI)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RetrofitRepairUpload retrofitRepairUpload = retrofit.create(RetrofitRepairUpload.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), upload_photo);
        MultipartBody.Part  file = MultipartBody.Part.createFormData("upload", upload_photo.getName(), requestFile);

        Call<String> call = retrofitRepairUpload.updateImage(file,
                RequestBody.create(null, id),
                RequestBody.create(null, service_time),
                RequestBody.create(null, state),
                RequestBody.create(null, service_desc)
               );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                Toast.makeText(RepairUploadActivity.this,"提示信息:"+response.body().toString(),Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(RepairUploadActivity.this,"网络错误，请检查网络设置"+t.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }


}


