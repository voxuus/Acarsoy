package ua.in.zeusapps.acarsoy.common;

/**
 * Created by oleg on 17.08.2017.
 */

public interface IAsyncCommand<I extends Object, O extends Object> {

    void onComplete(O data);

    void onError(String error);

    I getParameters();
}
