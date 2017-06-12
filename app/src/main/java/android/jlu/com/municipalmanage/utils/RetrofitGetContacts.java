package android.jlu.com.municipalmanage.utils;

import android.jlu.com.municipalmanage.baseclass.Contacts;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RetrofitGetContacts {
    @GET("allEmp.action")
    Call<Contacts> getContacts();
}
