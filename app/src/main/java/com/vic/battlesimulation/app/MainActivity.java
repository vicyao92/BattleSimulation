package com.vic.battlesimulation.app;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.MenuItemView;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;
import com.vic.battlesimulation.R;
import com.vic.battlesimulation.Utils.TextUtils;
import com.vic.battlesimulation.activity.CloneSettingActivity;
import com.vic.battlesimulation.activity.ManageSettingActivity;
import com.vic.battlesimulation.activity.ResultActivity;
import com.vic.battlesimulation.activity.RobotSettingActivity;
import com.vic.battlesimulation.bean.BasicAttribute;
import com.vic.battlesimulation.bean.Domain;
import com.vic.battlesimulation.bean.Enemy;
import com.vic.battlesimulation.bean.MyAttribute;
import com.vic.battlesimulation.db.MyDBHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.faction)
    Spinner faction;
    @BindView(R.id.etPower)
    EditText etPower;
    @BindView(R.id.etFire)
    EditText etFire;
    @BindView(R.id.etDefence)
    EditText etDefence;
    @BindView(R.id.etSpeed)
    EditText etSpeed;
    @BindView(R.id.etLuck)
    EditText etLuck;
    @BindView(R.id.etSatellite)
    EditText etSatellite;
    @BindView(R.id.btnSimulation)
    Button btnSimulation;
    @BindView(R.id.etSuperClone)
    EditText etSuperClone;
    @BindView(R.id.etLowClone)
    EditText etLowClone;
    @BindView(R.id.etMediumClone)
    EditText etMediumClone;
    @BindView(R.id.btnTarget)
    Button btnTarget;

    private MyAttribute myAttribute;
    private Enemy enemy;
    private SharedPreferences preferences;
    //数据库操作
    private MyDBHelper openHelper;
    private SQLiteDatabase database;
    private static final String ENEMY_TABLE = "Enemy";
    private static final String MY_TABLE = "MyAttr";
    //floatingActionBar动画操作
    private int frameDuration = 20;
    private AnimationDrawable frameAnim;
    private AnimationDrawable frameReverseAnim;
    private SpringFloatingActionMenu springFloatingActionMenu;
    private static int[] frameAnimRes = new int[]{
            R.mipmap.compose_anim_1, R.mipmap.compose_anim_2, R.mipmap.compose_anim_3,
            R.mipmap.compose_anim_4, R.mipmap.compose_anim_5, R.mipmap.compose_anim_6,
            R.mipmap.compose_anim_7, R.mipmap.compose_anim_8, R.mipmap.compose_anim_9,
            R.mipmap.compose_anim_10, R.mipmap.compose_anim_11, R.mipmap.compose_anim_12,
            R.mipmap.compose_anim_13, R.mipmap.compose_anim_14, R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_15, R.mipmap.compose_anim_16, R.mipmap.compose_anim_17,
            R.mipmap.compose_anim_18, R.mipmap.compose_anim_19
    };
    private AlertDialog dialog;
    private MyHandler handler;
    private static String loadTarget;
    private ArrayList<Domain> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private SharedPreferences autoSave;
    private boolean isReady;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handler = new MyHandler(MainActivity.this);
        firstRun();
        createFabFrameAnim();
        createFabReverseFrameAnim();
        initialFab();
        initialTargetData();
    }

    private void initialTargetData() {
        //初始化目标数据源
        options1Items.add(new Domain("M", "01"));
        options1Items.add(new Domain("M", "02"));
        options1Items.add(new Domain("M", "03"));

        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("神龙");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("元星曼徳斯");
        options2Items_02.add("浮丘");
        options2Items_02.add("海尼拉星炽天使");
        options2Items_02.add("元星掠夺第4波");
        options2Items_02.add("元星掠夺第5波");
        options2Items_02.add("投影5级");
        options2Items_02.add("徇星金蝰蛇");
        options2Items_02.add("元星霍德尔");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("待更新");

        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);
    }

    /**
     * 初始化浮动按钮
     */
    private void initialFab() {
        final FloatingActionButton fab = new FloatingActionButton(this);
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setImageDrawable(frameAnim);
        fab.setColorPressedResId(R.color.colorPrimary);
        fab.setColorNormalResId(R.color.colorAccent);
        fab.setColorRippleResId(R.color.color_text);
        fab.setShadow(true);
        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(this)
                .fab(fab)
                //添加菜单按钮参数依次是背景颜色,图标,标签,标签的颜色,点击事件
                .addMenuItem(R.color.add, R.drawable.save24, "保存配置", R.color.white, this)
                .addMenuItem(R.color.load, R.drawable.load24, "读取配置", R.color.white, this)
                .addMenuItem(R.color.delete, R.drawable.manage24, "管理配置", R.color.white, this)
                .addMenuItem(R.color.purple, R.drawable.clone, "克隆体配置", R.color.white, this)
                .addMenuItem(R.color.robot, R.drawable.robot, "机器人配置", R.color.white, this)
                //设置动画类型
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                //设置reveal效果的颜色
                .revealColor(R.color.color_text)
                //设置FAB的位置,只支持底部居中和右下角的位置
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fab.setImageDrawable(frameAnim);
                        frameReverseAnim.stop();
                        frameAnim.start();
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageDrawable(frameReverseAnim);
                        frameAnim.stop();
                        frameReverseAnim.start();
                    }
                })
                .build();
    }

    @OnClick({R.id.btnSimulation,R.id.btnTarget})
    protected void MyClick(View v) {
        switch (v.getId()) {
            case R.id.btnSimulation:
                switch (btnTarget.getText().toString()) {
                    case "请选择攻击目标":
                        Toast.makeText(MainActivity.this, "未选择攻击目标", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case "M03待更新":
                        Toast.makeText(MainActivity.this, "M03数据未更新,敬请期待", Toast.LENGTH_LONG)
                                .show();
                        break;
                    default:
                        isReady = notNullJudge();
                        if (isReady){
                            doSimulation();
                        }else {
                            Toast.makeText(MainActivity.this,"四维及战力不能为空",
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                break;
            case R.id.btnTarget:
                selectTarget();
                break;
        }
    }

    /**
     * 开始模拟
     */
    private void doSimulation() {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        int fire;
        int luck;
        int defence;
        int speed;
        long power;
        String name;

        //初始化我的属性
        name = faction.getSelectedItem().toString();
        fire = Integer.valueOf(TextUtils.textFormat(etFire.getText().toString()));
        luck = Integer.valueOf(TextUtils.textFormat(etLuck.getText().toString()));
        speed = Integer.valueOf(TextUtils.textFormat(etSpeed.getText().toString()));
        defence = Integer.valueOf(TextUtils.textFormat(etDefence.getText().toString()));
        power = Long.valueOf(TextUtils.textFormat(etPower.getText().toString()));
        initialMyAttribute(name, power, fire, defence, speed, luck);

        //根据所选目标,生成敌人属性
        String target = btnTarget.getText().toString().trim();
        openHelper = new MyDBHelper(this, "simulation", null, 1);
        database = openHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + ENEMY_TABLE + " where name = ?", new String[]{target});
        while (cursor.moveToNext()) {
            power = cursor.getLong(cursor.getColumnIndex("power"));
            fire = cursor.getInt(cursor.getColumnIndex("fire"));
            defence = cursor.getInt(cursor.getColumnIndex("defence"));
            speed = cursor.getInt(cursor.getColumnIndex("speed"));
            luck = cursor.getInt(cursor.getColumnIndex("luck"));
        }
        cursor.close();
        database.close();
        SharedPreferences sp = getSharedPreferences(MyApplication.EXTRA_SETTING,0);
        if (sp.getInt("guard",0)!=0){
            fire -=1;
            defence -= 1;
            speed -= 1;
            luck -=1;
        }
        if (sp.getInt("haty",0)!=0){
            fire -= 3;
        }
        initialEnemyAttribute(target, power, fire, defence, speed, luck);
        intent.putExtra("MyAttribute", myAttribute);
        intent.putExtra("EnemyAttr", enemy);
        startActivity(intent);
    }

    /**
     * @param name
     * @param power
     * @param fire
     * @param defence
     * @param speed
     * @param luck    初始化我的属性
     */
    private void initialMyAttribute(String name, long power, int fire, int defence, int speed, int luck) {
        BasicAttribute basicAttribute;
        basicAttribute = new BasicAttribute(name, power, fire, defence, speed, luck);
        myAttribute = new MyAttribute(basicAttribute);
        myAttribute.setSatellite(Long.valueOf(TextUtils.textFormat(etSatellite.getText().toString())));
        myAttribute.setLowCloneNum(Integer.valueOf(TextUtils.textFormat(etLowClone.getText().toString())));
        myAttribute.setMediumCloneNum(Integer.valueOf(TextUtils.textFormat(etMediumClone.getText().toString())));
        myAttribute.setSuperCloneNum(Integer.valueOf(TextUtils.textFormat(etSuperClone.getText().toString())));
        initialMibao(name);
    }

    /**
     * @param name
     * @param power
     * @param fire
     * @param defence
     * @param speed
     * @param luck    初始化敌人属性
     */
    private void initialEnemyAttribute(String name, long power, int fire, int defence, int speed, int luck) {
        BasicAttribute basicAttribute;
        basicAttribute = new BasicAttribute(name, power, fire, defence, speed, luck);
        enemy = new Enemy(basicAttribute);
    }

    /**
     * 第一次运行时,添加攻击目标的数据至数据库中
     */
    private void firstRun() {
        preferences = getSharedPreferences("setting", 0);
        Boolean first_run = preferences.getBoolean("First", true);
        if (first_run) {
            openHelper = new MyDBHelper(this, "simulation", null, 1);
            database = openHelper.getWritableDatabase();
            database.beginTransaction();
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02元星掠夺第4波',2229474,762,534,375,253)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02元星掠夺第5波',2580575,587,830,281,522)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02元星曼徳斯',2375600,609,703,421,515)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02浮丘',3205550,603,724,358,852)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02投影5级',3155075,611,488,677,782)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02海尼拉星炽天使',5760000,1072,680,804,742)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02徇星金蝰蛇',2023200,634,317,529,420)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M02元星霍德尔',2860625,656,396,524,791)");
            database.execSQL("insert into Enemy(name,power,fire,defence,speed,luck) values(" +
                    "'M01神龙',4830000,885,483,676,732)");
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();
            preferences.edit().putBoolean("First", false).apply();
        }else {
            loadLastData();
        }
    }

    /**
     * @param faction 根据阵营初始化秘宝伤害
     */
    private void initialMibao(String faction) {
        switch (faction) {
            case "奥鲁维之刃":
                myAttribute.setMbZeng(0);
                myAttribute.setMbJian(45000);
                myAttribute.setMbBao(0);
                myAttribute.setMbFan(0);
                break;
            case "卡纳斯的启示":
                myAttribute.setMbZeng(0);
                myAttribute.setMbJian(0);
                myAttribute.setMbBao(54000);
                myAttribute.setMbFan(0);
                break;
            case "游荡者之歌":
                myAttribute.setMbZeng(0);
                myAttribute.setMbJian(0);
                myAttribute.setMbBao(0);
                myAttribute.setMbFan(45000);
                break;
            case "深渊的咆哮":
                myAttribute.setMbZeng(45000);
                myAttribute.setMbJian(0);
                myAttribute.setMbBao(0);
                myAttribute.setMbFan(0);
                break;
        }
    }

    private void createFabFrameAnim() {
        frameAnim = new AnimationDrawable();
        frameAnim.setOneShot(true);
        Resources resources = getResources();
        for (int res : frameAnimRes) {
            frameAnim.addFrame(resources.getDrawable(res), frameDuration);
        }
    }

    private void createFabReverseFrameAnim() {
        frameReverseAnim = new AnimationDrawable();
        frameReverseAnim.setOneShot(true);
        Resources resources = getResources();
        for (int i = frameAnimRes.length - 1; i >= 0; i--) {
            frameReverseAnim.addFrame(resources.getDrawable(frameAnimRes[i]), frameDuration);
        }
    }

    @Override
    public void onBackPressed() {
        if (springFloatingActionMenu.isMenuOpen()) {
            springFloatingActionMenu.hideMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        MenuItemView menuItemView = (MenuItemView) view;
        switch (menuItemView.getLabelTextView().getText().toString()) {
            case "保存配置":
                springFloatingActionMenu.hideMenu();
                showSaveDialog();
                break;
            case "读取配置":
                springFloatingActionMenu.hideMenu();
                showLoadDialog();
                break;
            case "管理配置":
                springFloatingActionMenu.hideMenu();
                Intent intent = new Intent(MainActivity.this, ManageSettingActivity.class);
                startActivity(intent);
                break;
            case "克隆体配置":
                springFloatingActionMenu.hideMenu();
                Intent clone= new Intent(MainActivity.this, CloneSettingActivity.class);
                startActivity(clone);
                break;
            case "机器人配置":
                springFloatingActionMenu.hideMenu();
                Intent robot = new Intent(MainActivity.this, RobotSettingActivity.class);
                startActivity(robot);
                break;
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入配置名称");    //设置对话框标题
        final EditText edit = new EditText(this);
        builder.setView(edit);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nickname = edit.getText().toString();
                if (nickname.equals("")) {
                    Toast.makeText(MainActivity.this, "名字不能为空", Toast.LENGTH_LONG).show();
                } else {
                    saveSetting(nickname);
                    Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(false); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }

    private void showLoadDialog() {
        //读取已保存配置名称
        openHelper = new MyDBHelper(this, "simulation", null, 1);
        database = openHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("select nickname from " + MY_TABLE, new String[]{});
        final List<String> temp = new ArrayList<String>();
        while (cursor.moveToNext()) {
            temp.add(cursor.getString(cursor.getColumnIndex("nickname")));
        }
        final String[] items = new String[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            items[i] = temp.get(i);
        }
        cursor.close();
        database.close();

        //弹出对话框选择要加载的配置
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择要加载的配置");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                loadTarget = items[which];
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                        dialog.dismiss();
                    }
                }).start();
                dialog.dismiss();
            }
        }).create();
        builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
        dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(false); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }

    /**
     * @param nickname 保存配置
     */
    private void saveSetting(String nickname) {
        openHelper = new MyDBHelper(this, "simulation", null, 1);
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        database.beginTransaction();
        cv.put("name", faction.getSelectedItem().toString());
        cv.put("power", etPower.getText().toString());
        cv.put("fire", etFire.getText().toString());
        cv.put("defence", etDefence.getText().toString());
        cv.put("speed", etSpeed.getText().toString());
        cv.put("luck", etLuck.getText().toString());
        cv.put("supernum", etSuperClone.getText().toString());
        cv.put("medium", etMediumClone.getText().toString());
        cv.put("lownum", etLowClone.getText().toString());
        cv.put("satellite", etSatellite.getText().toString());
        cv.put("nickname", nickname);
        database.insertOrThrow(MY_TABLE, null, cv);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    /**
     * 攻击目标选择器
     */
    private void selectTarget() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是每个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(options2);
                btnTarget.setText(tx);
            }
        })
                .setTitleText("攻打目标选择")
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setSubmitColor(R.color.add)//确定按钮文字颜色
                .setCancelColor(R.color.load)//取消按钮文字颜色
                .setBgColor(Color.LTGRAY)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setBackgroundId(0x00000000) //设置外部遮罩颜色
                .build();

        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.show();
    }

    private static class MyHandler extends Handler {
        WeakReference<MainActivity> activity;

        MyHandler(MainActivity activity) {
            this.activity = new WeakReference<MainActivity>(activity);
        }

        /**
         * @param msg 读取配置,更新UI
         */
        @Override
        public void handleMessage(Message msg) {
            MainActivity theClass = activity.get();
            switch (msg.what) {
                case 0: {
                    MyDBHelper openHelper = new MyDBHelper(
                            MyApplication.getContext(), "simulation", null, 1);
                    SQLiteDatabase database = openHelper.getWritableDatabase();
                    Cursor cursor = database.rawQuery(
                            "select * from " + MY_TABLE + " WHERE nickname = ?", new String[]{loadTarget});
                    while (cursor.moveToNext()) {
                        int faction = 0;
                        switch (cursor.getString(cursor.getColumnIndex("name"))) {
                            case "奥鲁维之刃":
                                faction = 0;
                                break;
                            case "卡纳斯的启示":
                                faction = 1;
                                break;
                            case "游荡者之歌":
                                faction = 2;
                                break;
                            case "深渊的咆哮":
                                faction = 3;
                                break;
                        }
                        theClass.faction.setSelection(faction, true);
                        theClass.etPower.setText(String.valueOf(cursor.getLong(cursor.getColumnIndex("power"))));
                        theClass.etFire.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("fire"))));
                        theClass.etDefence.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("defence"))));
                        theClass.etSpeed.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("speed"))));
                        theClass.etLuck.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("luck"))));
                        theClass.etSuperClone.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("supernum"))));
                        theClass.etMediumClone.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("medium"))));
                        theClass.etLowClone.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("lownum"))));
                        theClass.etSatellite.setText(String.valueOf(cursor.getLong(cursor.getColumnIndex("satellite"))));
                    }
                    cursor.close();
                    database.close();
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        autoSave = getSharedPreferences(MyApplication.LAST_SETTING, 0);
        SharedPreferences.Editor editor = autoSave.edit();
        editor.putInt("faction",faction.getSelectedItemPosition());
        editor.putString("power", etPower.getText().toString());
        editor.putString("fire", etFire.getText().toString());
        editor.putString("defense", etDefence.getText().toString());
        editor.putString("speed", etSpeed.getText().toString());
        editor.putString("luck", etLuck.getText().toString());
        editor.putString("satellite", etSatellite.getText().toString());
        editor.putString("lowNum", etLowClone.getText().toString());
        editor.putString("mediumNum", etMediumClone.getText().toString());
        editor.putString("superNum", etSuperClone.getText().toString());
        editor.apply();
        super.onDestroy();
    }

    /**
     * @return
     * 四维及战力非空判断
     */
    private boolean notNullJudge() {
        if (etFire.getText().toString().equals("") || etDefence.getText().toString().equals("") ||
                etSpeed.getText().toString().equals("") || etLuck.getText().toString().equals("") ||
                etPower.getText().toString().equals("")) {
            return false;
        }else {
            return true;
        }
    }

    private void loadLastData(){
        autoSave = getSharedPreferences(MyApplication.LAST_SETTING, 0);
        faction.setSelection(autoSave.getInt("faction",0));
        etPower.setText(autoSave.getString("power",""));
        etFire.setText(autoSave.getString("fire",""));
        etDefence.setText(autoSave.getString("defense",""));
        etSpeed.setText(autoSave.getString("speed",""));
        etLuck.setText(autoSave.getString("luck",""));
        etSatellite.setText(autoSave.getString("satellite",""));
        etLowClone.setText(autoSave.getString("lowNum",""));
        etMediumClone.setText(autoSave.getString("mediumNum",""));
        etSuperClone.setText(autoSave.getString("superNum",""));
    }
}
