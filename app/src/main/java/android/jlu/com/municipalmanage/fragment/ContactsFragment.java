package android.jlu.com.municipalmanage.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.baseclass.Cn2Spell;
import android.jlu.com.municipalmanage.baseclass.Contacts;
import android.jlu.com.municipalmanage.baseclass.ContactsName;
import android.jlu.com.municipalmanage.baseclass.SideBar;
import android.jlu.com.municipalmanage.baseclass.SortAdapter;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.jlu.com.municipalmanage.utils.RetrofitGetContacts;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beyond on 17/4/11.
 */

public class ContactsFragment extends BaseFragment1{

    private ListView listView;
    private SortAdapter adapter;
    private List<Contacts.EmpBeanBean> mEmpBeanBeen = new ArrayList<>();
    private List<ContactsName> mContactsNames = new ArrayList<>() ;
    private EditText mEditText;
    private Button mButton;
    private SideBar sideBar;

    public static ContactsFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        mEditText = (EditText)view.findViewById(R.id.search);
        mButton = (Button)view.findViewById(R.id.clear);
        sideBar = (SideBar) view.findViewById(R.id.side_bar);

        //按钮清除
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });


       //侧边栏
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < mContactsNames.size(); i++) {
                    if (selectStr.equalsIgnoreCase(mContactsNames.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });

        //列表项点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactsName contactsName = mContactsNames.get(position);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contactsName.getPhone()));
                startActivity(intent);
            }
        });


        //输入框监听
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 获得文本框中的用户
                String search = mEditText.getText().toString().trim();
                if ("".equals(search)) {
                    // 输入为空,设置按钮不可见
                    mButton.setVisibility(View.INVISIBLE);
                } else {
                    // 输入不为空，设置按钮可见
                    mButton.setVisibility(View.VISIBLE);
                }


                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateUI();
        return view;
    }


    private void updateUI() {
        //进度条
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        dialog.setTitle("提示");
        dialog.setMessage("正在加载...");
        dialog.show();


        //开启网络请求
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriSet.SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitGetContacts retrofitGetContacts = retrofit.create(RetrofitGetContacts.class);

        Call<Contacts> call = retrofitGetContacts.getContacts();
       call.enqueue(new Callback<Contacts>() {
           @Override
           public void onResponse(Call<Contacts> call, Response<Contacts> response) {
               dialog.dismiss();
               mEmpBeanBeen = response.body().getEmpBean();
               //转换所需的list
              for(int i=0; i<mEmpBeanBeen.size();i++){
                  ContactsName contactsName = new ContactsName(mEmpBeanBeen.get(i).getName(),
                          mEmpBeanBeen.get(i).getPhone());
                  mContactsNames.add(contactsName);
              }
               Collections.sort(mContactsNames); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
               adapter = new SortAdapter(getActivity(), mContactsNames);
               listView.setAdapter(adapter);

           }

           @Override
           public void onFailure(Call<Contacts> call, Throwable t) {
               dialog.dismiss();
               Toast.makeText(getActivity(),"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
           }
       });
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactsName> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = mContactsNames;
        } else {
            mSortList.clear();
            for (ContactsName sortModel : mContactsNames) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || Cn2Spell.getPinYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList);
        adapter.updateListView(mSortList);
    }


}
