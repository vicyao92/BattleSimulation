package com.vic.battlesimulation.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vic.battlesimulation.R;
import com.vic.battlesimulation.app.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloneSettingActivity extends AppCompatActivity {

    @BindView(R.id.tvMediumClone)
    TextView tvMediumClone;
    @BindView(R.id.etQxbytNum)
    EditText etQxbytNum;
    @BindView(R.id.etYxNum)
    EditText etYxNum;
    @BindView(R.id.etYlNum)
    EditText etYlNum;
    @BindView(R.id.etCclzNum)
    EditText etCclzNum;
    @BindView(R.id.etBfsNum)
    EditText etBfsNum;
    @BindView(R.id.etKjsbNum)
    EditText etKjsbNum;
    @BindView(R.id.etTsNum)
    EditText etTsNum;
    @BindView(R.id.etChNum)
    EditText etChNum;
    @BindView(R.id.etBzrNum)
    EditText etBzrNum;
    @BindView(R.id.etNmjbNum)
    EditText etNmjbNum;
    @BindView(R.id.etLksNum)
    EditText etLksNum;
    @BindView(R.id.etXklNum)
    EditText etXklNum;
    @BindView(R.id.etCloneLoss)
    EditText etCloneLoss;
    @BindView(R.id.etCloneLevel)
    EditText etCloneLevel;
    @BindView(R.id.spinnerAngel)
    Spinner spinnerAngel;
    @BindView(R.id.spinnerInsectQuene)
    Spinner spinnerInsectQuene;
    @BindView(R.id.spinnerNano)
    Spinner spinnerNano;
    @BindView(R.id.spinnerMutant)
    Spinner spinnerMutant;
    @BindView(R.id.tvDragon)
    TextView tvDragon;
    @BindView(R.id.spinnerDragon)
    Spinner spinnerDragon;
    @BindView(R.id.tvSuperClone)
    TextView tvSuperClone;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.spinnerLksjs)
    Spinner spinnerLksjs;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clone_setting);
        ButterKnife.bind(this);
        Shader shader = new LinearGradient(0, 0, 0, 100, Color.BLUE, Color.RED, Shader.TileMode.REPEAT);
        tvMediumClone.getPaint().setShader(shader);
        tvSuperClone.getPaint().setShader(shader);
        loadDataFromSpf();
    }

    /**
     * 加载克隆体配置
     */
    private void loadDataFromSpf() {
        preferences = getSharedPreferences(MyApplication.EXTRA_SETTING, 0);
        etQxbytNum.setText(preferences.getString("qxbyt", ""));
        etYxNum.setText(preferences.getString("yx", ""));
        etYlNum.setText(preferences.getString("yl", ""));
        etCclzNum.setText(preferences.getString("cclz", ""));
        etBfsNum.setText(preferences.getString("bfs", ""));
        etKjsbNum.setText(preferences.getString("kjsb", ""));

        etTsNum.setText(preferences.getString("ts", ""));
        etChNum.setText(preferences.getString("ch", ""));
        etBzrNum.setText(preferences.getString("bzr", ""));
        etNmjbNum.setText(preferences.getString("nmjb", ""));
        etLksNum.setText(preferences.getString("lks", ""));
        etXklNum.setText(preferences.getString("xkl", ""));

        etCloneLoss.setText(preferences.getString("loss", "0"));
        etCloneLevel.setText(preferences.getString("level", "0"));
    }

    @OnClick({R.id.save})
    protected void MyClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                saveSetting();
                break;
        }
    }

    private void saveSetting() {
        preferences = getSharedPreferences(MyApplication.EXTRA_SETTING, 0);
        SharedPreferences.Editor editor = preferences.edit();
        //高级克隆体设置
        editor.putString("qxbyt", etQxbytNum.getText().toString());
        editor.putString("yx", etYxNum.getText().toString());
        editor.putString("yl", etYlNum.getText().toString());
        editor.putString("cclz", etCclzNum.getText().toString());
        editor.putString("bfs", etBfsNum.getText().toString());
        editor.putString("kjsb", etKjsbNum.getText().toString());
        //超级克隆体设置
        editor.putString("ts", etTsNum.getText().toString());
        editor.putString("ch", etChNum.getText().toString());
        editor.putString("bzr", etBzrNum.getText().toString());
        editor.putString("nmjb", etNmjbNum.getText().toString());
        editor.putString("lks", etLksNum.getText().toString());
        editor.putString("xkl", etXklNum.getText().toString());
        //克隆体战损及等级,默认为0
        editor.putString("loss", etCloneLoss.getText().toString());
        editor.putString("level", etCloneLevel.getText().toString());
        //克隆体强化
        editor.putString("qh_ts", spinnerAngel.getSelectedItemPosition() + "");
        editor.putString("qh_ch", spinnerInsectQuene.getSelectedItemPosition() + "");
        editor.putString("qh_bzr", spinnerMutant.getSelectedItemPosition() + "");
        editor.putString("qh_nmjb", spinnerNano.getSelectedItemPosition() + "");
        editor.putString("qh_xkl", spinnerDragon.getSelectedItemPosition() + "");
        editor.putString("qh_lks", spinnerLksjs.getSelectedItemPosition()+"");
        editor.apply();
        Toast.makeText(CloneSettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
