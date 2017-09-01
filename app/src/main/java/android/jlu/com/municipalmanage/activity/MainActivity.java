package android.jlu.com.municipalmanage.activity;

import android.content.pm.ActivityInfo;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.fragment.ContactsFragment;
import android.jlu.com.municipalmanage.fragment.HomeFragment;
import android.jlu.com.municipalmanage.fragment.TasksFragment;
import android.jlu.com.municipalmanage.fragment.UserMainFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private ArrayList<Fragment> fragmentLists;
    private BadgeItem mTaskNumberBadgeItem;
    private BottomNavigationItem mHomeBtnItem, mUserMainBtnItem,
            mTaskBtnItem, mContactsBtnItem;
    private BottomNavigationBar mBottomNavigationBar;
    private  Toolbar toolbar;

    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            fragmentLists = setFragments();
        }else {
            fragmentLists = getFragments();
        }
        super.onCreate(savedInstanceState);
        //保持竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        initView();
        initData();



    }

    private void initView() {


        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        //设置导航栏模式
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        //设置导航栏背景模式
        mBottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );

        mHomeBtnItem = new BottomNavigationItem(
                R.drawable.home, "地图").setActiveColorResource(R.color.blue);
        mTaskBtnItem = new BottomNavigationItem(
                R.drawable.tasks, "任务").setActiveColorResource(R.color.blue);
        mContactsBtnItem = new BottomNavigationItem(
                android.R.drawable.ic_menu_call, "联系人").setActiveColorResource(R.color.blue);
        mUserMainBtnItem = new BottomNavigationItem(
                R.drawable.usermain, "用户信息").setActiveColorResource(R.color.blue);

       // mTaskNumberBadgeItem = new BadgeItem()
        //    .setBorderWidth(4)
        //      .setBackgroundColorResource(R.color.red)
        //   .setText("5")
        // .setHideOnSelect(true);

        mTaskBtnItem.setBadgeItem(mTaskNumberBadgeItem);

        mBottomNavigationBar.addItem(mHomeBtnItem)
                .addItem(mTaskBtnItem)
                .addItem(mContactsBtnItem)
                .addItem(mUserMainBtnItem)
                .setFirstSelectedPosition(0)//默认
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);

    }

    private void initData() {
        setDefaultFragment();
        toolbar.setTitle(" 地图");
        toolbar.setLogo(R.drawable.top_map);
        toolbar.setTitleTextAppearance(this,R.style.Toolbar_TitleText);
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = fManager.beginTransaction();
        transaction.add(R.id.layFrame, fragmentLists.get(0));//默认
        transaction.commit();
    }

    private ArrayList<Fragment> setFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(fManager.findFragmentByTag("Home"));
        fragments.add(fManager.findFragmentByTag("Records"));
        fragments.add(fManager.findFragmentByTag("Contacts"));
        fragments.add(fManager.findFragmentByTag("UserMain"));
        return fragments;
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(HomeFragment.newInstance("Home"));
        fragments.add(TasksFragment.newInstance("Records"));
        fragments.add(ContactsFragment.newInstance("Contacts"));
        fragments.add(UserMainFragment.newInstance("UserMain"));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if (fragmentLists != null) {
            if (position < fragmentLists.size()) {
                FragmentTransaction ft = fManager.beginTransaction();
                Fragment fragment = fragmentLists.get(position);
                if (fragment.isAdded()) {
                    //   ft.replace(R.id.layFrame, fragment);
                    ft.show(fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();
                switch (position){
                    case 0 :
                        toolbar.setTitle(" 地图");
                        toolbar.setLogo(R.drawable.top_map);
                        break;
                    case 1:
                        toolbar.setTitle(" 问题列表");
                        toolbar.setLogo(R.drawable.top_task);
                      //  BadgeItem badgeItem = mTaskNumberBadgeItem.hide().setText("0");
                       // mTaskBtnItem.setBadgeItem(badgeItem);
                        break;
                    case 2 :
                        toolbar.setTitle(" 联系人");
                        toolbar.setLogo(R.drawable.top_contacts);
                        break;
                    case 3:
                        toolbar.setTitle(" 个人信息");
                        toolbar.setLogo(R.drawable.top_person);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragmentLists != null) {
            if (position < fragmentLists.size()) {
                FragmentTransaction ft = fManager.beginTransaction();
                Fragment fragment = fragmentLists.get(position);
                ft.hide(fragment);
                ft.commitAllowingStateLoss();

//                Toast.makeText(MainActivity.this, "onTabUnselected: " + position, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {
//        Toast.makeText(MainActivity.this, "onTabReselected: " + position, Toast.LENGTH_SHORT).show();
    }


}
