package ua.in.zeusapps.acarsoy.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.Const;
import ua.in.zeusapps.acarsoy.common.IRefresh;

/**
 * Created by oleg on 27.08.2017.
 */

public abstract class BaseNavActivity extends BaseActivity implements IRefresh {

    @BindView(R.id.nav_view_main)
    NavigationView mNavViewMain;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_img_acarsoy)
    ImageView mImgAcarsoy;

    private Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refresh();
            handler.postDelayed(runnable, Const.INTERVAL_REFRESH);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
        initNavigation();

        initAutoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void initAutoRefresh() {
        handler.postDelayed(runnable, Const.INTERVAL_REFRESH);
    }

    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);

        mImgAcarsoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    protected abstract int getCheckedNavId();

    protected void initNavigation() {
        mNavViewMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                mDrawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.main_nav_menu_plants:
                        startActivity(PlantsActivity.class);
                        break;
                    case R.id.main_nav_menu_turbines:
                        startActivity(TurbinesActivity.class);
                        break;
                    case R.id.main_nav_menu_turbines_list:
                        startActivity(TurbinesListActivity.class);
                        break;
                    case R.id.main_nav_menu_exit:
                        exitApp();
                    case R.id.main_nav_menu_logout:
                        logOut();
                        break;
                }

                return false;
            }
        });
        ((TextView) mNavViewMain.getHeaderView(0).findViewById(R.id.nav_header_main_txt_email)).setText(mLocalDbService.getEmail());
        mNavViewMain.setCheckedItem(getCheckedNavId());
    }

    private void logOut() {
        mLocalDbService.saveEmail(null);
        mLocalDbService.savePassword(null);
        startActivity(LoginActivity.class);
    }

    protected void exitApp() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
