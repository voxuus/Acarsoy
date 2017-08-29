package ua.in.zeusapps.acarsoy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import ua.in.zeusapps.acarsoy.services.LocalDataService;

/**
 * Created by oleg on 26.08.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected LocalDataService mLocalDbService;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        mLocalDbService = new LocalDataService(getSharedPreferences(getPackageName(), MODE_PRIVATE));
    }

    protected void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
