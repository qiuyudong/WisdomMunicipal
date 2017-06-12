package android.jlu.com.municipalmanage.activity;

import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskItemActivity extends AppCompatActivity  {
     private TextView task_id,task_time,task_address,task_type,task_state,task_desc;
     private String  taskId,taskTime,taskAddress,taskType,taskState,taskDesc;
     private Button show_video,repair_upload;
     private String videoUri;

    private static final String TAG = "TaskItemActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_item);
        task_id = (TextView)findViewById(R.id.task_id);
        task_time = (TextView)findViewById(R.id.task_time);
        task_address = (TextView)findViewById(R.id.task_address);
        task_type = (TextView)findViewById(R.id.task_type);
        task_state = (TextView)findViewById(R.id.task_state);
        task_desc = (TextView)findViewById(R.id.task_desc);
        show_video = (Button)findViewById(R.id.show_video);
        repair_upload = (Button)findViewById(R.id.repair_button);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("task_id");
        taskTime = intent.getStringExtra("task_time");
        taskAddress = intent.getStringExtra("task_address");
        taskType = intent.getStringExtra("task_type");
        taskState = intent.getStringExtra("task_state");
        taskDesc = intent.getStringExtra("task_desc");
        videoUri = intent.getStringExtra("task_video");


        task_id.setText(taskId);
        task_time.setText(taskTime);
        task_address.setText(taskAddress);
        task_type.setText(taskType);
        if(taskState.equals("0")){
            task_state.setText("未维修");
        }else if (taskState.equals("1")){
            task_state.setText("已指派");
        }else if (taskState.equals("2")){
            task_state.setText("维修中");
        }else if (taskState.equals("3")){
            task_state.setText("维修完成");
        }else if (taskState.equals("")){
            task_state.setText("状态出现问题");
        }

        task_desc.setText(taskDesc);



        repair_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskItemActivity.this, RepairUploadActivity.class);
                intent.putExtra("task_id",taskId);
                startActivity(intent);
            }
        });


        show_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            Intent intent_photo = new Intent(TaskItemActivity.this,InternetVideoActivity.class);
                            intent_photo.putExtra("URI",videoUri);
                            startActivity(intent_photo);
            }
        });





    }


}
