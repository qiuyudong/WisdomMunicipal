package android.jlu.com.municipalmanage.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class InternetVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "InternetVideoActivity";

    private VideoView videoView;
    //网络路径
    private String video_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_video);
        videoView = (VideoView) findViewById(R.id.video_view);
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        Intent intent = getIntent();
        video_uri = intent.getStringExtra("URI");

        video_uri = video_uri.substring(100);
        video_uri = video_uri.replaceAll("\\\\", "/");
        video_uri = video_uri.replaceAll("\\//", "/");
        video_uri = UriSet.SERVER_URI+video_uri;
        Log.d(TAG, "onCreate: "+video_uri);
        initVideoPath(); // 初始化MediaPlayer

    }

    private void initVideoPath() {
        final ProgressDialog dialog2 = new ProgressDialog(InternetVideoActivity.this);
        dialog2.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog2.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog2.setTitle("提示");
        dialog2.setMessage("小视频正在加载...");
        dialog2.show();
        //取消的监听
        dialog2.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();

            }
        });
        Uri uri = Uri.parse(video_uri);
        videoView.setVideoURI(uri);//播放网络视频

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override  //加载网络资源黑屏结束监听
            public void onPrepared(MediaPlayer mediaPlayer) {
                dialog2.dismiss();
            }

        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            //视频无法播放监听
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                Toast.makeText(InternetVideoActivity.this,"加载错误,检查网络设置",Toast.LENGTH_SHORT).show();
                finish();
                return true;
            }
        });
        videoView.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!videoView.isPlaying()) {
                    videoView.start(); // 开始播放
                }
                break;
            case R.id.pause:
                if (videoView.isPlaying()) {
                    videoView.pause(); // 暂停播放
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
    }

}
