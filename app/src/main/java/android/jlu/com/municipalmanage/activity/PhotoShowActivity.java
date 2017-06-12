package android.jlu.com.municipalmanage.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.jlu.com.municipalmanage.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by beyond on 17/4/21.
 */

public class PhotoShowActivity extends AppCompatActivity {

    private ImageView iv_photo_show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        iv_photo_show = (ImageView) findViewById(R.id.iv_photo_show);

        Intent intent = getIntent();
        String PHOTO_COMPRESS_PATH = intent.getStringExtra("PHOTO_COMPRESS_PATH");

        if (PHOTO_COMPRESS_PATH != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(PHOTO_COMPRESS_PATH);
//                iv_photo_show.setImageBitmap(bitmap);
            if (bitmap != null) {
                iv_photo_show.setBackground(new BitmapDrawable(bitmap));
            }
        }

    }
}
