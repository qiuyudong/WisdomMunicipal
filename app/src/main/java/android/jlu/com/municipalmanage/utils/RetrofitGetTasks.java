package android.jlu.com.municipalmanage.utils;

import android.jlu.com.municipalmanage.baseclass.Task;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RetrofitGetTasks {
    @GET("allUnProject.action")
    Call<Task> getTasks();
}
