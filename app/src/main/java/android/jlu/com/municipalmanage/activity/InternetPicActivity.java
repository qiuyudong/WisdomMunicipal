package android.jlu.com.municipalmanage.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class InternetPicActivity extends AppCompatActivity {
    private String pic_uri;
    private ImageView photo;
    private static final String TAG = "InternetPicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(InternetPicActivity.this);
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在加载...");
        dialog.show();
        //取消的监听
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
               finish();

            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_pic);
        photo = (ImageView)findViewById(R.id.photo_show);
        Intent intent = getIntent();
        pic_uri = intent.getStringExtra("URI");
        Picasso.with(getBaseContext())
                .load(pic_uri)
                //设置加载失败的图片显示
                .error(R.drawable.report_name)
                .into(photo,
                        new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onError() {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "加载错误或者照片不存在", Toast.LENGTH_SHORT).show();
                            }
                        });

    }

}
