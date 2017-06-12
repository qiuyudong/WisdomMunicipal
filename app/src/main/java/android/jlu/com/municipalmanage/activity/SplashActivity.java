package android.jlu.com.municipalmanage.activity;

import android.app.Activity;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.utils.PreferenceUtils;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 *
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isGuideShowed = PreferenceUtils.getBoolean(this, "is_user_guide_showed", false);
        if (!isGuideShowed) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }


        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //由于登陆的问题，后面确定是进入主界面还是登陆界面
                boolean isUserLogined = PreferenceUtils.getBoolean(SplashActivity.this,
                        "is_user_logined", false);
                if (!isUserLogined) {
                    //进入登录界面
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    //进入主界面
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

            }
        }, 2000);

    }


}
