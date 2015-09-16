package com.multiprocesssp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.multiprocesssp.R;
import com.multiprocesssp.base.BaseApplication;

public class ActivityA extends Activity implements OnClickListener {
    Button mWriteBtn, mReadBtn;
    SharedPreferences mSP = BaseApplication.getInstance().getSharedPreferences("chen", Context.MODE_MULTI_PROCESS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);

        mWriteBtn = (Button) findViewById(R.id.write);
        mWriteBtn.setOnClickListener(this);
        mReadBtn = (Button) findViewById(R.id.read);
        mReadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.write:
                break;
            case R.id.read:
                boolean b = mSP.getBoolean("bool", false);
                int i = mSP.getInt("int", 0);
                String s = mSP.getString("string", "");
                Toast.makeText(this, b + "", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
