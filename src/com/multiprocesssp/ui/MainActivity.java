package com.multiprocesssp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.multiprocesssp.R;
import android.view.View.OnClickListener;
import com.multiprocesssp.base.BaseApplication;

public class MainActivity extends Activity implements OnClickListener {
    Button mBtnA, mWriteBtn, mReadBtn;
    SharedPreferences mSP = BaseApplication.getInstance().getSharedPreferences("chen", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnA = (Button) findViewById(R.id.button_a);
        mBtnA.setOnClickListener(this);
        mWriteBtn = (Button) findViewById(R.id.write);
        mWriteBtn.setOnClickListener(this);
        mReadBtn = (Button) findViewById(R.id.read);
        mReadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_a:
                startActivity(new Intent(this, ActivityA.class));
                break;
            case R.id.write:
                mSP.edit().putBoolean("bool", true).commit();
                mSP.edit().putInt("int", 34).commit();
                mSP.edit().putString("string", "adf").commit();
                break;
            case R.id.read:
                boolean i = mSP.getBoolean("bool", false);
                Toast.makeText(this, i + "", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
