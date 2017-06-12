package android.jlu.com.municipalmanage.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.activity.LoginActivity;
import android.jlu.com.municipalmanage.utils.PreferenceUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by beyond on 17/4/11.
 */

public class UserMainFragment extends BaseFragment1 {
    private Button logout;
    private EditText signature;
    private TextView name,phone,title;
    public static UserMainFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        UserMainFragment fragment = new UserMainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        name = (TextView)view.findViewById(R.id.name);
        phone=(TextView)view.findViewById(R.id.phone);
        title = (TextView)view.findViewById(R.id.title);
        name.setText ("姓名： "+PreferenceUtils.getString(getActivity(),"USER_NAME","nulllllll"));
        phone.setText("电话： "+PreferenceUtils.getString(getActivity(),"USER_PHONE","nulllllll"));
        title.setText("职位： "+PreferenceUtils.getString(getActivity(),"USER_TITLE","nulllllll"));

        logout = (Button)view.findViewById(R.id.logout);
        signature = (EditText)view.findViewById(R.id.signature);
        //个性签名设置
        String personal_signature = PreferenceUtils.getString(getActivity(), "user_personal_signature", "Say Something...");
        signature.setText(personal_signature);

        signature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PreferenceUtils.setString(getActivity(),
                        "user_personal_signature", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("登出");
                dialog.setMessage("是否确认注销！");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        //将登录状态退出
                        PreferenceUtils.setBoolean(getActivity(),
                                "is_user_logined", false);
                        getActivity().finish();
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        return view;
    }
}
