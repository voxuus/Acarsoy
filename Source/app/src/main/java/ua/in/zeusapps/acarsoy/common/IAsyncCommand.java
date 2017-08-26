package ua.in.zeusapps.acarsoy.common;

/**
 * Created by oleg on 17.08.2017.
 */

public interface IAsyncCommand {

    void onComplete(Object data);

    void onError(String error);
}
