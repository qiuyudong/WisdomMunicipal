package android.jlu.com.municipalmanage.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.activity.InternetPicActivity;
import android.jlu.com.municipalmanage.activity.TaskItemActivity;
import android.jlu.com.municipalmanage.baseclass.Task;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.jlu.com.municipalmanage.utils.PreferenceUtils;
import android.jlu.com.municipalmanage.utils.RetrofitGetTasks;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beyond on 17/4/11.
 */

public class TasksFragment extends BaseFragment1 {

    private static final String TAG = "TasksFragment";
    private RecyclerView mRecyclerView;
    private List<Task.ProBeanBean>  mProBeanBeen = new ArrayList<>();

    private TaskAdapter Adapter = new TaskAdapter(mProBeanBeen);
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    //实现搜索
    private EditText mEditText;
    private Button youritem,allitems;

    ProgressDialog dialog;

    public static TasksFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks,container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.task_recycler_view);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        mEditText = (EditText)view.findViewById(R.id.search);
        youritem = (Button)view.findViewById(R.id.youritem);
        youritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDate();
            }
        });

        allitems = (Button)view.findViewById(R.id.allitems);
        allitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(getActivity());
                dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
                dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
                dialog.setTitle("提示");
                dialog.setMessage("正在加载...");
                dialog.show();
                showAll();
            }
        });

        //设置管理
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initDate();

        //输入框监听
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        return view;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Task.ProBeanBean>  mProBeanBeen1 = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mProBeanBeen1 = mProBeanBeen;
        } else {
            mProBeanBeen1.clear();
            for (Task.ProBeanBean sortModel : mProBeanBeen) {
                String type = sortModel.getType();
                String address = sortModel.getAddress();
                String id = sortModel.getId()+"";
                if (type.indexOf(filterStr) != -1 || address.indexOf(filterStr)!= -1 || id.indexOf(filterStr)!= -1) {
                    mProBeanBeen1.add(sortModel);
                }
            }
        }
        //初始化构造器
        Adapter = new TaskAdapter(mProBeanBeen1);
        mRecyclerView.setAdapter(Adapter);
    }


    private void  initDate(){
        //  进度条

        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在加载...");
        dialog.show();
        showYour();

    }


    private void  refreshDate(){
        //开启网络请求
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriSet.SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitGetTasks retrofitGetTasks = retrofit.create(RetrofitGetTasks.class);
        Call<Task> call = retrofitGetTasks.getTasks();
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                mProBeanBeen = response.body().getProBean();
                //初始化构造器
                Adapter = new TaskAdapter(mProBeanBeen);
                mRecyclerView.setAdapter(Adapter);
                Adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(getActivity(),"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        });
    }



    private void refreshList(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshDate();
            }
        });


    }

    //查看全部
    private void showAll(){
        //开启网络请求
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriSet.SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitGetTasks retrofitGetTasks = retrofit.create(RetrofitGetTasks.class);
        Call<Task> call = retrofitGetTasks.getTasks();
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                dialog.dismiss();
                mProBeanBeen = response.body().getProBean();
                //初始化构造器
                Adapter = new TaskAdapter(mProBeanBeen);
                mRecyclerView.setAdapter(Adapter);
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //查看自己
    private void showYour(){
        //开启网络请求
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriSet.SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitGetTasks retrofitGetTasks = retrofit.create(RetrofitGetTasks.class);
        Call<Task> call = retrofitGetTasks.getTasks();
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                dialog.dismiss();
                mProBeanBeen = response.body().getProBean();
                mProBeanBeen = selectYour(mProBeanBeen);
                //初始化构造器
                Adapter = new TaskAdapter(mProBeanBeen);
                mRecyclerView.setAdapter(Adapter);
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(),"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //筛选自己的
    private List<Task.ProBeanBean> selectYour(List<Task.ProBeanBean>  proBeanBeen){
        String finder = PreferenceUtils.getString(getActivity(),"USER_NAME","wrong");
        Log.d(TAG, "selectYour: "+finder);
        List<Task.ProBeanBean> proBeanList = new ArrayList<>();
        for (Task.ProBeanBean pro:proBeanBeen
             ) {
            Log.d(TAG, "-------"+pro.getPro_finder());
               if(finder.equals(pro.getPro_finder())){
                   proBeanList.add(pro);
               }
        }
        return proBeanList;
    }
    //实现adapter和ViewHolder
    private class TasksHolder extends RecyclerView.ViewHolder{
        public TextView find_time,task_address,task_type,task_state,task_id;
        public ImageView task_photo;
        public  TasksHolder(View itemView) {
            super(itemView);
            task_photo = (ImageView) itemView.findViewById(R.id.photo);
            task_type = (TextView)itemView.findViewById(R.id.task_type);
            task_id = (TextView)itemView.findViewById(R.id.task_id);
            task_address = (TextView)itemView.findViewById(R.id.task_address);
            find_time = (TextView)itemView.findViewById(R.id.task_time);
            task_state = (TextView)itemView.findViewById(R.id.task_state);

        }
    }


    private class TaskAdapter extends RecyclerView.Adapter<TasksHolder>{
        private List<Task.ProBeanBean> probeanlist;
        //构造方法，初始化列表
        public TaskAdapter(List<Task.ProBeanBean> prolist){
            probeanlist = prolist;

        }

        @Override
        public TasksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.task_item,parent,false);
            final TasksHolder holder = new TasksHolder(view);

            holder.task_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Task.ProBeanBean pro = probeanlist.get(position);
                    //获取图片
                    String pic_uri=pro.getFind_pic();
                    pic_uri = pic_uri.replaceAll("\\\\", "/");
                    pic_uri = pic_uri.replaceAll("\\//", "/");
                    pic_uri =UriSet.SERVER_URI+ pic_uri.substring(1);
                    Log.d(TAG, "onClick: "+pic_uri);
                    Intent intent_photo = new Intent(getActivity(),InternetPicActivity.class);
                    intent_photo.putExtra("URI",pic_uri);
                    startActivity(intent_photo);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Task.ProBeanBean pro = probeanlist.get(position);
                    Intent intent = new Intent(getActivity(), TaskItemActivity.class);
                    String time1 = pro.getFind_time().replace("T"," ");//去除时间格式不对，里面的T
                    //转化为string
                    String id = String.valueOf(pro.getId());
                    String state = String.valueOf(pro.getPro_state());
                    intent.putExtra("task_id",id);
                    intent.putExtra("task_time",time1);
                    intent.putExtra("task_address",pro.getAddress());
                    intent.putExtra("task_type",pro.getType());
                    intent.putExtra("task_state",state);
                    intent.putExtra("task_desc",pro.getSite_desc());
                    intent.putExtra("task_video",pro.getFind_video());
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(TasksHolder holder, int position) {
            Task.ProBeanBean proBeanBean = probeanlist.get(position);
            //获取时间
            String time = proBeanBean.getFind_time().replace("T"," ");//去除时间格式不对，里面的T
            //获取图片
            String pic_uri=proBeanBean.getFind_pic();
            pic_uri = pic_uri.replaceAll("\\\\", "/");
            pic_uri = pic_uri.replaceAll("\\//", "/");
            pic_uri =UriSet.SERVER_URI+ pic_uri.substring(1);

            //获取状态
            int taskState = proBeanBean.getPro_state();
            if(taskState==0){
                holder.task_state.setText("未维修");
            }else if (taskState==1){
                holder.task_state.setText("已指派");
            }else if (taskState==2){
                holder.task_state.setText("维修中");
            }else if (taskState==3){
                holder.task_state.setText("已维修");
            }else {
                holder.task_state.setText("？？？");
            }


            //加载图片
            Picasso.with(getActivity())
                    .load(pic_uri)
                    //加载中的图片
                    .placeholder(R.drawable.logo)
                    //设置加载失败的图片显示
                    .error(R.drawable.logo)
                    .into(holder.task_photo);
            holder.task_type.setText(proBeanBean.getType());
            holder.task_id.setText(proBeanBean.getId()+"");
            holder.task_address.setText(proBeanBean.getAddress());
            holder.find_time.setText(time);
        }

        @Override
        public int getItemCount() {
            return probeanlist.size();
        }
    }




}