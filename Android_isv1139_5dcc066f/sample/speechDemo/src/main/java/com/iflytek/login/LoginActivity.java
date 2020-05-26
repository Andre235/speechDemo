package com.iflytek.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.iflytek.voicedemo.vocalverify.VocalVerifyDemo;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class LoginActivity extends Activity implements View.OnClickListener{

    private DBOpenHelper mDBOpenHelper;
    private TextView mTvLoginactivityRegister;
    private RelativeLayout mRlLoginactivityTop;
    private EditText usernameEdidText;
    private EditText passwordEditText;
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

    private class UsernameFocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String username = getString(usernameEdidText);
                checkPhoneNum(username);
            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        // 初始化控件
        mBtLoginactivityLogin = findViewById(R.id.bt_loginactivity_login);
        mTvLoginactivityRegister = findViewById(R.id.tv_loginactivity_register);
        mRlLoginactivityTop = findViewById(R.id.rl_loginactivity_top);
        usernameEdidText = findViewById(R.id.et_loginactivity_username);
        passwordEditText = findViewById(R.id.et_loginactivity_password);
        mLlLoginactivityTwo = findViewById(R.id.ll_loginactivity_two);

        // 设置点击事件监听器
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);
        // 设置编辑框失去焦点监听器
        usernameEdidText.setOnFocusChangeListener(new UsernameFocusChangeListener());
        // passwordEditText.setOnFocusChangeListener(new PasswordFocusChangeListener());
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
                String name = getString(usernameEdidText);
                String password = getString(passwordEditText);
                boolean match = false;
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    checkPhoneNum(name);
                    //checkPassword(password);
                    ArrayList<User> data = mDBOpenHelper.getAllData();
                    for (int i = 0; i < data.size(); i++) {
                        User user = data.get(i);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if(match){
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, VocalVerifyDemo.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    }else{
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
    public void checkPhoneNum(String phoneNum){
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phoneNum.length() != 11){
            Toast.makeText(this,"请输入有效位数手机号",Toast.LENGTH_SHORT).show();
            //usernameEdidText.getText().clear();
        }
        boolean matches = Pattern.compile(regex).matcher(phoneNum).matches();
        if(!matches){
            Toast.makeText(this,"请检验手机号有效性",Toast.LENGTH_SHORT).show();
            //usernameEdidText.getText().clear();
        }
    }


    private String getString(EditText editText){
        if(editText != null){
            return editText.getText().toString().trim();
        }else{
            return null;
        }
    }
}
