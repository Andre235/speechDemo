package com.iflytek.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.iflytek.dao.DBOpenHelper;
import com.iflytek.voicedemo.R;

import java.util.regex.Pattern;


/**
 * @author zhaojingchao@kedacom.com
 * @date 2020/2/28 15:48
 * @description 注册页面逻辑
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private String realCode;
    private DBOpenHelper mDBOpenHelper;
    private Button mBtRegisteractivityRegister;
    private RelativeLayout mRlRegisteractivityTop;
    private ImageView mIvRegisteractivityBack;
    private LinearLayout mLlRegisteractivityBody;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;
    private EditText mEtRegisteractivityPhonecodes;
    private ImageView mIvRegisteractivityShowcode;
    private RelativeLayout mRlRegisteractivityBottom;


    private String getStringText(EditText editText){
        if(editText != null){
            return editText.getText().toString().trim();
        }else{
            return null;
        }
    }

    class userNameFocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String text = getStringText(mEtRegisteractivityUsername);
                checkPhoneNum(text);
            }
        }
    }

    class password1FocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String text = getStringText(mEtRegisteractivityPassword1);
                checkPassword(text);
            }
        }
    }

    class password2FocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String password1 = getStringText(mEtRegisteractivityPassword1);
                String password2 = getStringText(mEtRegisteractivityPassword2);
                if(!TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2) && !password2.equals(password1)){
                    toastInfo("两次输入密码不一致");
                }
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initView();

        mDBOpenHelper = new DBOpenHelper(this);

        //将验证码用图片的形式显示出来
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    /**
     * 初始化视图
     */
    private void initView(){
        mBtRegisteractivityRegister = findViewById(R.id.bt_registeractivity_register);
        mRlRegisteractivityTop = findViewById(R.id.rl_registeractivity_top);
        mIvRegisteractivityBack = findViewById(R.id.iv_registeractivity_back);
        mLlRegisteractivityBody = findViewById(R.id.ll_registeractivity_body);
        mEtRegisteractivityUsername = findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = findViewById(R.id.et_registeractivity_password2);
        mEtRegisteractivityPhonecodes = findViewById(R.id.et_registeractivity_phoneCodes);
        mIvRegisteractivityShowcode = findViewById(R.id.iv_registeractivity_showCode);
        mRlRegisteractivityBottom = findViewById(R.id.rl_registeractivity_bottom);

        mIvRegisteractivityBack.setOnClickListener(this);
        mIvRegisteractivityShowcode.setOnClickListener(this);
        mBtRegisteractivityRegister.setOnClickListener(this);

        mEtRegisteractivityUsername.setOnFocusChangeListener(new userNameFocusChangeListener());
        mEtRegisteractivityPassword1.setOnFocusChangeListener(new password1FocusChangeListener());
        mEtRegisteractivityPassword2.setOnFocusChangeListener(new password2FocusChangeListener());
    }

    /**
     * 点击事件监听器
     * @param view 视图
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.iv_registeractivity_showCode:    //改变随机验证码的生成
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password = mEtRegisteractivityPassword2.getText().toString().trim();
                String phoneCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneCode) ) {
                    checkPhoneNum(username);
                    checkPassword(password);
                    if (phoneCode.equals(realCode)) {
                        //将用户名和密码加入到数据库中
                        mDBOpenHelper.add(username, password);
                        Intent intent2 = new Intent(this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this,  "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "验证码错误,注册失败", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
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
            //mEtRegisteractivityUsername.getText().clear();
        }
        boolean matches = Pattern.compile(regex).matcher(phoneNum).matches();
        if(!matches){
            Toast.makeText(this,"请检验手机号有效性",Toast.LENGTH_SHORT).show();
            mEtRegisteractivityUsername.getText().clear();
        }

    }

    /**
     * 验证密码有效性 6-8位数字、字母组合
     * @param password 密码
     */
    public void checkPassword(String password){
        String regex = "^[a-zA-Z0-9]{6,8}$";
        boolean isMatch = Pattern.compile(regex).matcher(password).matches();
        if(!isMatch){
            Toast.makeText(this,"密码为6-8位数字、字母组合",Toast.LENGTH_SHORT).show();
            //mEtRegisteractivityPassword2.getText().clear();
        }
    }

    public void toastInfo(String info){
        Toast.makeText(this,info,Toast.LENGTH_SHORT).show();
    }
}
