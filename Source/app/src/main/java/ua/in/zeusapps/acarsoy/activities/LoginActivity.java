package ua.in.zeusapps.acarsoy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import ua.in.zeusapps.acarsoy.R;
import ua.in.zeusapps.acarsoy.common.IAsyncCommand;
import ua.in.zeusapps.acarsoy.services.AcarsoyService;
import ua.in.zeusapps.acarsoy.services.LocalDataService;
import ua.in.zeusapps.acarsoy.services.TokenService;
import ua.in.zeusapps.acarsoy.services.api.TokenRequest;
import ua.in.zeusapps.acarsoy.services.api.TokenResponse;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_username)
    EditText mEdtTextUserName;

    @BindView(R.id.activity_login_password)
    EditText mEdtTextPassword;

    private AcarsoyService mAcarsoy = new AcarsoyService();
    private TokenService mTokenService = TokenService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCredential();
    }


    private void initCredential() {
        mEdtTextUserName.setText(mLocalDbService.getEmail());
        mEdtTextPassword.setText(mLocalDbService.getPassword());

        if (!TextUtils.isEmpty(mEdtTextUserName.getText().toString()) &&
                !TextUtils.isEmpty(mEdtTextPassword.getText().toString())) {
            onBtnLoginClick();
        }
    }

    @OnClick(R.id.activity_login_button)
    public void onBtnLoginClick() {

        mAcarsoy.getTokenAsync(new IAsyncCommand<TokenRequest, TokenResponse>() {

            @Override
            public void onComplete(TokenResponse data) {

                if (data.ErrorCode != 0) {
                    Toast.makeText(LoginActivity.this, data.Message, Toast.LENGTH_SHORT).show();
                    return;
                }

                mLocalDbService.saveEmail(mEdtTextUserName.getText().toString());
                mLocalDbService.savePassword(mEdtTextPassword.getText().toString());

                mTokenService.setToken(data.Token);
                startActivity(PlantsActivity.class);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public TokenRequest getParameters() {
                return new TokenRequest(mEdtTextUserName.getText().toString(), mEdtTextPassword.getText().toString());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

}
