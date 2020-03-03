package com.iflytek;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iflytek.voicedemo.R;

/**
 * @author zhaojingchao@kedacom.com
 * @date 2020/3/3 11:03
 * @description
 */
@SuppressLint("Registered")
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

}
