package com.zc.video;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpalshActivity extends AppCompatActivity {

    private static final int GO_HOME = 1;
    private static final int ENTEER_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        mHandler.sendEmptyMessageDelayed(GO_HOME,ENTEER_DURATION);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GO_HOME) {
                startActivity(new Intent(SpalshActivity.this, HomeActivity.class));
                finish();
            }
        }
    };
}
