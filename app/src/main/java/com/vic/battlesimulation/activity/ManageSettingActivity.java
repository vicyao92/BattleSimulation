package com.vic.battlesimulation.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.vic.battlesimulation.R;
import com.vic.battlesimulation.adapter.SettingAdapter;
import com.vic.battlesimulation.app.MyApplication;
import com.vic.battlesimulation.db.MyDBHelper;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageSettingActivity extends AppCompatActivity{
    @BindView(R.id.settingList)
    SwipeMenuRecyclerView settingList;
    private List<String> settings;
    private SettingAdapter mAdapter;
    private int clickItemPosition;
    //数据库操作
    private static final String MY_TABLE = "MyAttr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_setting);
        ButterKnife.bind(this);
        settings = new ArrayList<>();
        MyDBHelper openHelper = new MyDBHelper(
                MyApplication.getContext(), "simulation", null, 1);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "select nickname from " + MY_TABLE, new String[]{});
        while (cursor.moveToNext()) {
            settings.add(cursor.getString(cursor.getColumnIndex("nickname")));
        }
        database.close();
        initialRecycleView(settings);
    }

    private void initialRecycleView(List<String> list) {
        //设置列表可侧滑
        //settingList.setItemViewSwipeEnabled(true);
        settingList.setSwipeMenuCreator(swipeMenuCreator);
        settingList.setSwipeMenuItemClickListener(mMenuItemClickListener);
        settingList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SettingAdapter(ManageSettingActivity.this);
        settingList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(settings);
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            SwipeMenuItem deleteItem = new SwipeMenuItem(ManageSettingActivity.this)
                    .setBackground(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            // RecyclerView的Item的position。
            clickItemPosition = menuBridge.getAdapterPosition();
            showConfirmDialog();
/*            // 左侧还是右侧菜单。
            int direction = menuBridge.getDirection();
            // 菜单在RecyclerView的Item中的Position。
            int menuPosition = menuBridge.getPosition();

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(ManageSettingActivity.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(ManageSettingActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }*/
        }
    };

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您确定要删除该配置吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDBHelper openHelper = new MyDBHelper(
                        MyApplication.getContext(), "simulation", null, 1);
                SQLiteDatabase db = openHelper.getWritableDatabase();
                db.execSQL("DELETE FROM "+ MY_TABLE+" WHERE nickname = ?",new String[]{settings.get(clickItemPosition)});
                settings.remove(clickItemPosition);
                mAdapter.notifyDataSetChanged(settings);
                settingList.setAdapter(mAdapter);
                db.close();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

}
