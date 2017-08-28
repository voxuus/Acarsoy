package ua.in.zeusapps.acarsoy.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import ua.in.zeusapps.acarsoy.R;

/**
 * Created by oleg on 27.08.2017.
 */

public abstract class BaseNavActivity extends BaseActivity {

    @BindView(R.id.nav_view_main)
    NavigationView mNavViewMain;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNavigation();
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
                    case R.id.main_nav_menu_exit:
                        exitApp();
                        break;
                }

                return false;
            }
        });
        ((TextView) mNavViewMain.getHeaderView(0).findViewById(R.id.nav_header_main_txt_email)).setText(mLocalDbService.getEmail());
        mNavViewMain.setCheckedItem(getCheckedNavId());
    }

    protected void exitApp() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


}
