package com.vic.battlesimulation.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @BindView(R.id.etQxbytNum)
    EditText etQxbytNum;
    @BindView(R.id.tvMediumClone)
    TextView tvMediumClone;
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
    @BindView(R.id.etCloneLoss)
    EditText etCloneLoss;
    @BindView(R.id.etCloneLevel)
    EditText etCloneLevel;
    @BindView(R.id.tvSuperClone)
    TextView tvSuperClone;
    @BindView(R.id.spinnerAngel)
    Spinner spinnerAngel;
    @BindView(R.id.spinnerInsectQuene)
    Spinner spinnerInsectQuene;
    @BindView(R.id.spinnerNano)
    Spinner spinnerNano;
    @BindView(R.id.spinnerMutant)
    Spinner spinnerMutant;
    @BindView(R.id.spinnerDragon)
    Spinner spinnerDragon;
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

        etCloneLoss.setText(preferences.getString("loss", ""));
        etCloneLevel.setText(preferences.getString("level", ""));

        spinnerAngel.setSelection(preferences.getInt("qh_ts",0));
        spinnerInsectQuene.setSelection(preferences.getInt("qh_ch",0));
        spinnerMutant.setSelection(preferences.getInt("qh_bzr",0));
        spinnerNano.setSelection(preferences.getInt("qh_nmjb",0));
        spinnerDragon.setSelection(preferences.getInt("qh_xkl",0));
        spinnerLksjs.setSelection(preferences.getInt("qh_lks",0));

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
        //克隆体战损及等级,默认为0
        editor.putString("loss", etCloneLoss.getText().toString());
        editor.putString("level", etCloneLevel.getText().toString());
        //克隆体强化
        editor.putInt("qh_ts", spinnerAngel.getSelectedItemPosition());
        editor.putInt("qh_ch", spinnerInsectQuene.getSelectedItemPosition());
        editor.putInt("qh_bzr", spinnerMutant.getSelectedItemPosition());
        editor.putInt("qh_nmjb", spinnerNano.getSelectedItemPosition());
        editor.putInt("qh_xkl", spinnerDragon.getSelectedItemPosition());
        editor.putInt("qh_lks", spinnerLksjs.getSelectedItemPosition());
        editor.apply();
        Toast.makeText(CloneSettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
