package ua.in.zeusapps.acarsoy.services.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 26.08.2017.
 */

public class TokenResponse {

    @SerializedName("error")
    @Expose
    public int ErrorCode;

    @SerializedName("token")
    @Expose
    public String Token;

    @SerializedName("message")
    @Expose
    public String Message;

}
