package com.iflytek.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.iflytek.voicedemo.VoiceMainActivity;
import com.iflytek.dao.DBOpenHelper;
import com.iflytek.dao.bean.User;
import com.iflytek.voicedemo.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class LoginActivity extends Activity implements View.OnClickListener{

    private DBOpenHelper mDBOpenHelper;
    private TextView mTvLoginactivityRegister;
    private RelativeLayout mRlLoginactivityTop;
    private EditText mEtLoginactivityUsername;
    private EditText mEtLoginactivityPassword;
    private LinearLayout mLlLoginactivityTwo;
    private Button mBtLoginactivityLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
        mDBOpenHelper = new DBOpenHelper(this);
    }


    /**
     * 初始化视图
     */
    private void initView() {
        // 初始化控件
        mBtLoginactivityLogin = findViewById(R.id.bt_loginactivity_login);
        mTvLoginactivityRegister = findViewById(R.id.tv_loginactivity_register);
        mRlLoginactivityTop = findViewById(R.id.rl_loginactivity_top);
        mEtLoginactivityUsername = findViewById(R.id.et_loginactivity_username);
        mEtLoginactivityPassword = findViewById(R.id.et_loginactivity_password);
        mLlLoginactivityTwo = findViewById(R.id.ll_loginactivity_two);

        // 设置点击事件监听器
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);
    }

    /**
     * 点击事件监听器
     * @param view view视图
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.tv_loginactivity_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.bt_loginactivity_login:
                String name = mEtLoginactivityUsername.getText().toString().trim();
                String password = mEtLoginactivityPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<User> data = mDBOpenHelper.getAllData();
                    boolean match = false;
                    for (int i = 0; i < data.size(); i++) {
                        User user = data.get(i);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, VoiceMainActivity.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    } else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 验证手机号有效性
     * @param phoneNum 手机号
     */
    private void checkPhoneNum(String phoneNum){
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phoneNum.length() != 11){
            Toast.makeText(this,"请输入有效位数手机号",Toast.LENGTH_SHORT).show();
        }
        boolean matches = Pattern.compile(regex).matcher(phoneNum).matches();
        if(!matches){
            Toast.makeText(this,"请检验手机号有效性",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证密码有效性 6-8位数字、字母组合
     * @param password 密码
     */
    private void checkPassword(String password){
        String regex = "^[a-zA-Z0-9]{6,8}$";
        boolean isMatch = Pattern.compile(regex).matcher(password).matches();
        if(!isMatch){
            Toast.makeText(this,"密码为6-8位数字、字母组合",Toast.LENGTH_SHORT).show();
        }
    }
}
