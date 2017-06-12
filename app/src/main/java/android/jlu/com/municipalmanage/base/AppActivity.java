package android.jlu.com.municipalmanage.base;

import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.os.Bundle;

/**
 * Created by beyond on 17/4/13.
 */

public abstract class AppActivity extends BaseActivity {

    protected abstract BaseFragment getFirstFragment();

    protected void handleIntent(Intent intent) {
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        if (null != getIntent()) {
            handleIntent(getIntent());
        }

        if (getSupportFragmentManager().getFragments() == null) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }
    }
}
