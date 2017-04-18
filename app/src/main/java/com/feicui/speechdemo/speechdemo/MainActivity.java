package com.feicui.speechdemo.speechdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnReco, R.id.btnTran})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReco:
                startActivity(new Intent(MainActivity.this,RecoActivity.class));
                break;
            case R.id.btnTran:
                startActivity(new Intent(MainActivity.this,TranActivity.class));
                break;
        }
    }
}
