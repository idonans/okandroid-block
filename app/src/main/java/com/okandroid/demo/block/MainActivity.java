package com.okandroid.demo.block;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.item_content)
  TextView mItemContent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mItemContent.setText("hello, butter knife");
  }

  public void onClickOtherActivity(View view) {
    startActivity(new Intent(this, OtherProcessActivity.class));
  }
}
