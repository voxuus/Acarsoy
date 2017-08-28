package ua.in.zeusapps.acarsoy.services;

/**
 * Created by oleg on 26.08.2017.
 */

public class TokenService {

    private static TokenService sInstance;

    public static TokenService getInstance() {
        if (sInstance == null) {
            sInstance = new TokenService();
        }
        return sInstance;
    }

    private TokenService() {
    }

    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

}
