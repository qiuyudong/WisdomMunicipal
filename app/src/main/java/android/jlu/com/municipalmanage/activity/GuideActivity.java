package android.jlu.com.municipalmanage.activity;

import android.app.Activity;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.utils.DensityUtil;
import android.jlu.com.municipalmanage.utils.PreferenceUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 *
 */

public class GuideActivity extends Activity {

    //引导页图片
//    private static final int[] mImageIds = new int[]{
//            R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3
//    };
    private static final int[] mImageIds = new int[]{
            R.drawable.guide_1,
            R.drawable.guide_2,
            R.drawable.guide_3
    };


    private ViewPager vpGuide;
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llPointGroup;// 引导圆点的父控件
    private int mPointWidth;// 圆点间的距离
    private View viewRedPoint;// 小红点
    private Button btnStart;// 开始体验

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        viewRedPoint = findViewById(R.id.view_red_point);
        btnStart = (Button) findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save
                PreferenceUtils.setBoolean(GuideActivity.this,
                        "is_user_guide_showed", true);
                boolean isUserLogined = PreferenceUtils.getBoolean(GuideActivity.this,
                        "is_user_logined", false);
                if (!isUserLogined) {
                    //进入登录界面
                    startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                } else {
                    //进入主界面
                    startActivity(new Intent(GuideActivity.this, MainActivity.class));
                }
                finish();
            }
        });

        initViews();
        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.addOnPageChangeListener(new GuidePageListener());
    }

    private void initViews() {

        mImageViewList = new ArrayList<>();

        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);
        }

        int pxParams = DensityUtil.dip2px(GuideActivity.this, 10);
        //初始化引导页小圆点
        for (int i = 0; i < mImageIds.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    pxParams, pxParams);

            if (i > 0) {
                params.leftMargin = pxParams;//设置原点间隔
            }
            //设置原点大小
            point.setLayoutParams(params);
            llPointGroup.addView(point);
        }

        mPointWidth = pxParams * 2;

        //获取视图树, 对layout结束事件进行监听
//        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    // 当layout执行结束后回调此方法
//                    @Override
//                    public void onGlobalLayout() {
//                        llPointGroup.getViewTreeObserver()
//                                .removeGlobalOnLayoutListener(this);
//                        mPointWidth = llPointGroup.getChildAt(1).getLeft()
//                                - llPointGroup.getChildAt(0).getLeft();
//                    }
//                });


    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class GuidePageListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            int len = (int) (mPointWidth * positionOffset) + position
                    * mPointWidth;
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();
            params.leftMargin = len;
            //设置小红点布局参数
            viewRedPoint.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            if (position == mImageIds.length - 1) {// 最后一个页面
                btnStart.setVisibility(View.VISIBLE);// 显示开始体验的按钮
            } else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
