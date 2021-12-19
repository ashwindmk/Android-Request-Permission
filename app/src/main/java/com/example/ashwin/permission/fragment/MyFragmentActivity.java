package com.example.ashwin.permission.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ashwin.permission.Constant;
import com.example.ashwin.permission.R;

public class MyFragmentActivity extends AppCompatActivity {
    private static final String SUB_TAG = MyFragmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfragment);
        Log.d(Constant.APP_TAG, SUB_TAG + ": onCreate");
    }

    // If you do not override onRequestPermissionsResult, fragment will get the result by-default
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(Constant.APP_TAG, SUB_TAG + ": onRequestPermissionsResult(requestCode = " + requestCode + ")");

        // If you do not call super, fragment will not get the result.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
