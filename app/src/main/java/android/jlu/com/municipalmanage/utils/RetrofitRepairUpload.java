package android.jlu.com.municipalmanage.utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/04/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RetrofitRepairUpload {
    @Multipart
    @POST("uploadFinish.action")
    Call<String> updateImage(@Part MultipartBody.Part file,
                             @Part("id") RequestBody id,
                             @Part("service_time") RequestBody service_time,
                             @Part("state") RequestBody state,
                             @Part("service_desc") RequestBody service_desc);

}
