package com.wdy.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wdy.base.module.dialog.DialogSinge;
import com.wdy.base.module.dialog.DialogUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void singeDialog(View view){
        DialogSinge.getInstance().with(this,"这是一个单独的提示");
    }
}
