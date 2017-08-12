package ua.in.zeusapps.acarsoy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.in.zeusapps.acarsoy.R;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.activity_login_username)
    EditText _userNameEditText;
    @BindView(R.id.activity_login_password)
    EditText _passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_login_button)
    public void login(){
        String message = "Login. Username: " +
                _userNameEditText.getText().toString() +
                " password: " +
                _passwordEditText.getText().toString();

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
