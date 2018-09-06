package com.vic.battlesimulation.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vic.battlesimulation.R;
import com.vic.battlesimulation.adapter.BarChartItem;
import com.vic.battlesimulation.adapter.ChartDataAdapter;
import com.vic.battlesimulation.adapter.ChartItem;
import com.vic.battlesimulation.adapter.LineChartItem;
import com.vic.battlesimulation.adapter.PieChartItem;
import com.vic.battlesimulation.bean.Enemy;
import com.vic.battlesimulation.bean.MyAttribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {

    long angel;
    long insect;
    long nano;
    long mutant;
    long dragon;
    long myDamage;
    long enemyDamage;
    private Enemy enemy;
    private MyAttribute myAttribute;
    private long myCurrentPower;
    private long enemyCurrentPower;
    private boolean isDone = false;
    private double firstRoundEPj;
    private int mySpeed;
    private int enemySpeed;
    private List<Long> myCurrentPowerPerTurn;
    private List<Long> enemyCurrentPowerPerTurn;
    private List<Long> myDamagePerTurn;
    private List<Long> enemyDamagePerTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        Intent intent;
        intent = getIntent();
        myAttribute = intent.getParcelableExtra("MyAttribute");
        enemy = intent.getParcelableExtra("Enemy");
        mySpeed = myAttribute.getMyAttribute().getSpeed();
        enemySpeed = enemy.getEnemyAttribute().getSpeed();
        myCurrentPower = myAttribute.getMyAttribute().getCurrentPower();
        enemyCurrentPower = enemy.getEnemyAttribute().getCurrentPower();
        myCurrentPowerPerTurn = new ArrayList<Long>();
        enemyCurrentPowerPerTurn = new ArrayList<Long>();
        myDamagePerTurn = new ArrayList<Long>();
        enemyDamagePerTurn = new ArrayList<Long>();
        myCurrentPowerPerTurn.add(myCurrentPower);
        enemyCurrentPowerPerTurn.add(enemyCurrentPower);
        initialCloneDamge();
        initialBasicAttributes();
        fight();
        initialChart();
    }

    private void initialChart() {
/*        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0;i < myCurrentPowerPerTurn.size();i++){
            entries.add(new Entry(i,(float)myCurrentPowerPerTurn.get(i)/(float) myAttribute.getMyAttribute().getPower()*100));
        }*/
        ListView lv = findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        // 30 items
        for (int i = 0; i < 3; i++) {
            list.add(new LineChartItem(generateDataLine(), getApplicationContext()));
            list.add(new BarChartItem(generateDataBar(), getApplicationContext()));
            list.add(new PieChartItem(generateDataPie(), getApplicationContext()));
        }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
    }

    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }

    private LineData generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
/*        int temp;
        temp = myCurrentPowerPerTurn.size()>enemyCurrentPowerPerTurn.size()?myCurrentPowerPerTurn.size():
                enemyCurrentPowerPerTurn.size();*/
        for (int i = 0; i < myCurrentPowerPerTurn.size(); i++) {
            e1.add(new Entry(i,(float)myCurrentPowerPerTurn.get(i)/(float) myAttribute.getMyAttribute().getPower()*100));
            //e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(e1, "回合结束我方剩余战力");
        d1.setLineWidth(4f);
        d1.setCircleRadius(6f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < enemyCurrentPowerPerTurn.size(); i++) {
            //e2.add(new Entry(i, e1.get(i).getY() - 30));
            e2.add(new Entry(i,(float)enemyCurrentPowerPerTurn.get(i)/(float) enemy.getEnemyAttribute().getPower()*100));
        }

        LineDataSet d2 = new LineDataSet(e2, "回合结束敌方剩余战力");
        d2.setLineWidth(4f);
        d2.setCircleRadius(6f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }

    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "测试");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private void initialBasicAttributes(){
        double temp;
        long myFire;
        long myLuck;
        double myPjCoefficient;
        long myPower;
        long enemyPower;
        double enemyPjCoefficient;
        long enemyDefence;
        long enemyLuck;
        long enemyFire;
        long myDefence;
        BigDecimal bigDecimal;
        myPower = myAttribute.getMyAttribute().getPower();
        enemyPower = enemy.getEnemyAttribute().getPower();
        //计算我方破甲系数
        myFire = myAttribute.getMyAttribute().getFire();
        enemyDefence = enemy.getEnemyAttribute().getDefense();
        temp = myFire - enemyDefence;
        myPjCoefficient = temp > 0 ? (1 + temp / (200 + temp)) : (1 - Math.abs(temp) / (200 + 2 * Math.abs(temp)));
        //计算敌方破甲系数
        myDefence = myAttribute.getMyAttribute().getDefense();
        enemyFire = enemy.getEnemyAttribute().getFire();
        temp = enemyFire - myDefence;
        enemyPjCoefficient = temp > 0 ? (1 + temp / (200 + temp)) : (1 - Math.abs(temp) / (200 + 2 * Math.abs(temp)));
        //单独计算第一回合虚空龙效果的破甲效果
        if (myAttribute.getCloneDragon() != 0) {
            temp = enemyFire - myDefence - dragon;
            firstRoundEPj = temp > 0 ? (1 + temp / (200 + temp)) : (1 - Math.abs(temp) / (200 + 2 * Math.abs(temp)));
        } else {
            firstRoundEPj = enemyPjCoefficient;
        }
        myAttribute.getMyAttribute().setPjCoefficient(myPjCoefficient);
        enemy.getEnemyAttribute().setPjCoefficient(enemyPjCoefficient);
        //计算双方暴击系数
        myLuck = myAttribute.getMyAttribute().getLuck();
        enemyLuck = enemy.getEnemyAttribute().getLuck();
        if ((myLuck - enemyLuck) > 0) {
            bigDecimal = new BigDecimal((double) myLuck / (double) enemyLuck - 1);
            myAttribute.getMyAttribute()
                    .setBjCoefficient(bigDecimal.setScale(3, bigDecimal.ROUND_DOWN)
                            .doubleValue());
            enemy.getEnemyAttribute().setBjCoefficient(0);
        } else {
            bigDecimal = new BigDecimal((double) enemyLuck / (double) myLuck - 1);
            enemy.getEnemyAttribute()
                    .setBjCoefficient(bigDecimal.setScale(3, bigDecimal.ROUND_DOWN).doubleValue());
            myAttribute.getMyAttribute().setBjCoefficient(0);
        }
    }

    private void fight() {
        int round;
        round = 1;
        mySpeed = myAttribute.getMyAttribute().getSpeed();
        enemySpeed = enemy.getEnemyAttribute().getSpeed();
        while (!isDone) {
            Log.d("3333", "----------------------" + "第" + round + "回合" + "-----------------");
            switch (round) {
                case 1:
                    if (myAttribute.getCloneDragon() != 0) {
                        if (mySpeed >= enemySpeed - dragon) {
                            myTurnFirst(round);
                        } else {
                            enemyTurnFirst(round);
                        }
                    } else {
                        if (mySpeed >= enemySpeed) {
                            myTurnFirst(round);
                        } else {
                            enemyTurnFirst(round);
                        }
                    }
                    break;
                default:
                    if (mySpeed >= enemySpeed) {
                        myTurnFirst(round);
                    } else {
                        enemyTurnFirst(round);
                    }
                    break;
            }
            round++;
            enemyCurrentPowerPerTurn.add(enemyCurrentPower);
            myCurrentPowerPerTurn.add(myCurrentPower);
        }
    }

    private boolean attack(int round) {
        cacDamage(myAttribute.getMyAttribute().getCurrentPower(), enemy.getEnemyAttribute().getCurrentPower(), round);
        Log.d("3333", "我方造成" + myDamage + "伤害");
        enemyCurrentPower = enemyCurrentPower - myDamage;

        if (enemyCurrentPower <= 0) {
            isDone = true;
            Log.d("3333", "战斗胜利");
            enemyCurrentPower = 0;
            Log.d("3333",enemyCurrentPowerPerTurn.size()+"         "+myCurrentPowerPerTurn.size());
            Log.d("3333", "当前回合" + round);
            Log.d("3333", "敌方剩余" + enemyCurrentPower + "战力");
            return true;
        } else {
            enemy.getEnemyAttribute().setCurrentPower(enemyCurrentPower);
            Log.d("3333", "敌方剩余" + enemyCurrentPower + "战力");
            return false;
        }
    }

    private boolean beingAttacked(int round) {
        cacDamage(myAttribute.getMyAttribute().getCurrentPower(), enemy.getEnemyAttribute().getCurrentPower(), round);
        myCurrentPower = myCurrentPower - enemyDamage;
        enemyCurrentPower = enemyCurrentPower - myAttribute.getMbFan() - myAttribute.getCloneReflectionDamage();
        Log.d("3333", "敌方造成" + enemyDamage + "伤害");
        if (myCurrentPower <= 0) {
            Log.d("3333", "失败");
            Log.d("3333", "我方剩余 0 战力");
            myCurrentPower = 0;
            return true;
        } else {
            Log.d("3333", "我方剩余" + myCurrentPower + "战力");
            myAttribute.getMyAttribute().setCurrentPower(myCurrentPower);
            enemy.getEnemyAttribute().setCurrentPower(enemyCurrentPower);
            return false;
        }
    }

    private void initialCloneDamge() {
        angel = myAttribute.getCloneAngel() == 0 ? 0 : 24000 * myAttribute.getCloneLevel() +
                30000 * (myAttribute.getCloneAngel() - 1);
        /*insect = myAttribute.getCloneInsect() == 0? 0 : 24000 * myAttribute.getCloneLevel() +
                30000*(myAttribute.getCloneAngel()-1);*/
        nano = myAttribute.getCloneNano() == 0 ? 0 : 16000 * myAttribute.getCloneLevel() +
                20000 * (myAttribute.getCloneNano() - 1);
        mutant = myAttribute.getCloneMutant() == 0 ? 0 : (long) (0.0048 *
                myAttribute.getMyAttribute().getPower() * myAttribute.getCloneLevel());
        dragon = myAttribute.getCloneDragon() == 0 ? 0 : 5 * myAttribute.getCloneLevel() +
                25 * (myAttribute.getCloneDragon() - 1);
        insect = 0;
    }

    /**
     * @param mcPower
     * @param ecPower 根据现在剩余战力计算伤害
     */
    private void cacDamage(long mcPower, long ecPower, int round) {
        int temp;
        long myBasicDamage;
        long enemyBasicDamage;
        double myBj = myAttribute.getMyAttribute().getBjCoefficient();
        double myPj = myAttribute.getMyAttribute().getPjCoefficient();
        double eBj = enemy.getEnemyAttribute().getBjCoefficient();
        double ePj = enemy.getEnemyAttribute().getPjCoefficient();
        long satellite;
        long mbZeng;
        long mbJian;
        long cloneZeng;
        long mbBao;
        long cloneBao;
        long cloneReduction;
        mbBao = myAttribute.getMbBao();
        mbJian = myAttribute.getMbJian();
        cloneBao = myAttribute.getCloneCricDamage();
        cloneReduction = myAttribute.getCloneReductionDamage();
        mbZeng = myAttribute.getMbZeng();
        satellite = myAttribute.getSatellite();
        cloneZeng = myAttribute.getCloneAdditionDamage();
        temp = myAttribute.getMyAttribute().getLuck() - enemy.getEnemyAttribute().getLuck();
        myBasicDamage = (long) (0.2 * (myAttribute.getMyAttribute().getPower() * 0.75 + 0.25 * mcPower));
        enemyBasicDamage = (long) (0.2 * (enemy.getEnemyAttribute().getPower() * 0.75 + 0.25 * ecPower));

        myDamage = Double.valueOf(myBasicDamage * (myPj + myBj) + mbZeng + satellite + cloneZeng).longValue();
        if (temp > 0) {
            myDamage = myDamage + mbBao + cloneBao;
        }
        if (round == 1) {
            enemyDamage = Double.valueOf(enemyBasicDamage * (firstRoundEPj + eBj) - mbJian - satellite - cloneReduction).longValue();
        } else {
            enemyDamage = Double.valueOf(enemyBasicDamage * (ePj + eBj) - mbJian - satellite - cloneReduction).longValue();
        }
        manageSpecialDamage(round);
        //设置最终伤害
        myAttribute.getMyAttribute().setDamage(myDamage);
        enemy.getEnemyAttribute().setDamage(enemyDamage);
    }

    private void myTurnFirst(int round) {
        if (!isDone) {
            isDone = attack(round);
        }
        if (!isDone) {
            isDone = beingAttacked(round);
        }
    }

    private void enemyTurnFirst(int round) {
        if (!isDone) {
            isDone = beingAttacked(round);
        }
        if (!isDone) {
            isDone = attack(round);
        }
    }

    /**
     * @param round 根据回合数增加或者减免特殊伤害
     */
    private void manageSpecialDamage(int round) {
        switch (round) {
            case 1:
                myDamage = myDamage + mutant;
                enemyDamage = enemyDamage - nano;
                break;
            case 2:
                if (myAttribute.getCloneInsect() != 0) {
                    insect = myAttribute.getMyAttribute().getCurrentPower() < 1000000 ? 8400 * myAttribute.getCloneLevel() :
                            (long) (myCurrentPower * 0.0084 * myAttribute.getCloneLevel());
                } else {
                    insect = 0;
                }
                myDamage = myDamage + insect;
                break;
            case 3:
                myDamage = myDamage + angel;
                break;
            default:
                break;
        }
    }
}
