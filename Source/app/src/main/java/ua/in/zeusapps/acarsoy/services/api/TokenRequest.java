package ua.in.zeusapps.acarsoy.services.api;

/**
 * Created by oleg on 26.08.2017.
 */

public class TokenRequest {

    private String mEmail;

    private String mPassword;

    public TokenRequest(String mEmail, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }
}
