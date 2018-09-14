package com.vic.battlesimulation.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.vic.battlesimulation.R;
import com.vic.battlesimulation.app.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RobotSettingActivity extends AppCompatActivity {

    @BindView(R.id.spinnerEdenGuard)
    Spinner spinnerEdenGuard;
    @BindView(R.id.spinnerEdenHunter)
    Spinner spinnerEdenHunter;
    @BindView(R.id.spinnerHaty)
    Spinner spinnerHaty;
    @BindView(R.id.button)
    Button button;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_setting);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(MyApplication.EXTRA_SETTING,0);
        loadData();
    }

    private void loadData() {
        spinnerEdenGuard.setSelection(preferences.getInt("guard",0));
        spinnerEdenHunter.setSelection(preferences.getInt("hunter",0));
        spinnerHaty.setSelection(preferences.getInt("haty",0));
    }

    @OnClick({R.id.button})
    protected void myClick(View v){
        switch (v.getId()){
            case R.id.button:
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("haty",spinnerHaty.getSelectedItemPosition());
                editor.putInt("guard",spinnerEdenGuard.getSelectedItemPosition());
                editor.putInt("hunter",spinnerEdenHunter.getSelectedItemPosition());
                editor.apply();
                Toast.makeText(RobotSettingActivity.this,"保存成功",Toast.LENGTH_LONG)
                        .show();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
