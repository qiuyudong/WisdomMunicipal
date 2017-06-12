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
 *     time   : 2017/04/19
 *     desc   :图文上传
 *     version: 1.0
 * </pre>
 */
public interface RetrofitUploadUtil {
        @Multipart
        @POST("uploadFind.action")
        Call<String> updateImage(@Part MultipartBody.Part[] file,
                                 @Part("longitude") RequestBody longitude,
                                 @Part("latitude") RequestBody latitude,
                                 @Part("address") RequestBody address,
                                 @Part("site_desc") RequestBody site_desc,
                                 @Part("finder") RequestBody finder,
                                 @Part("find_time") RequestBody find_time,
                                 @Part("type") RequestBody type);
}
