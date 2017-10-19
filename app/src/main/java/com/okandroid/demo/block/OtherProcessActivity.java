package com.okandroid.demo.block;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by idonans on 2017/10/19.
 */

public class OtherProcessActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_other_process);

    TextView text = (TextView) findViewById(R.id.text);
    text.setText("process:" + getApplicationInfo().processName);
  }
}
