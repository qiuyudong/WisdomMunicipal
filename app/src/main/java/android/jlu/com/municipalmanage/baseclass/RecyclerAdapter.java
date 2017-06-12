package android.jlu.com.municipalmanage.baseclass;

import android.content.Context;
import android.jlu.com.municipalmanage.R;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 项目名称：RecyclerViewTest
 * 创建人：Double2号
 * 创建时间：2016/4/18 8:12
 * 修改备注：
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<Task.ProBeanBean> mData;//数据
    private int max_count = 10;//最大显示数
    private Boolean isFootView = false;//是否添加了FootView
    private String footViewText = "";//FootView的内容

    //两个final int类型表示ViewType的两种类型
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1111;


    public RecyclerAdapter(Context context, List<Task.ProBeanBean> data) {
        mContext = context;
        mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView find_time,task_address,task_type,task_state,task_id;
        public ImageView task_photo;

        public TextView tvFootView;//footView的TextView属于独自的一个layout

        //初始化viewHolder，此处绑定后在onBindViewHolder中可以直接使用
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == NORMAL_TYPE) {
                task_photo = (ImageView) itemView.findViewById(R.id.photo);
                task_type = (TextView)itemView.findViewById(R.id.task_type);
                task_id = (TextView)itemView.findViewById(R.id.task_id);
                task_address = (TextView)itemView.findViewById(R.id.task_address);
                find_time = (TextView)itemView.findViewById(R.id.task_time);
                task_state = (TextView)itemView.findViewById(R.id.task_state);
            } else if (viewType == FOOT_TYPE) {
                tvFootView = (TextView) itemView.findViewById(R.id.tw);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View normal_views = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.task_item, parent, false);
        View foot_view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.foot_view, parent, false);

        if (viewType == FOOT_TYPE) {
            return new ViewHolder(foot_view, FOOT_TYPE);
        }
            return new ViewHolder(normal_views, NORMAL_TYPE);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == max_count - 1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //建立起ViewHolder中试图与数据的关联
        Log.d("xjj", getItemViewType(position) + "");
        //如果footview存在，并且当前位置ViewType是FOOT_TYPE
        if (isFootView && (getItemViewType(position) == FOOT_TYPE)) {
            holder.tvFootView.setText(footViewText);
                    max_count += 10;



        } else {
            //加载子项内容
            Task.ProBeanBean proBeanBean = mData.get(position);
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
            Picasso.with(mContext)
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
    }

    @Override
    public int getItemCount() {
        if (mData.size() < max_count) {
            return mData.size();
        }
        return max_count;
    }

    //创建一个方法来设置footView中的文字
    public void setFootViewText(String footViewText) {
        isFootView = true;
        this.footViewText = footViewText;
    }

}
