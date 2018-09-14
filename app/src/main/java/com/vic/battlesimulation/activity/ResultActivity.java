package com.vic.battlesimulation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;
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
import com.vic.battlesimulation.app.MyApplication;
import com.vic.battlesimulation.bean.Enemy;
import com.vic.battlesimulation.bean.MyAttribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {

    //超级克隆体特殊伤害及减免
    private long angel;
    private long insect;
    private long nano;
    private long mutant;
    private long dragon;
    //克隆体增减暴反伤
    private long cloneZeng;
    private long cloneBao;
    private long cloneReduction;
    private long cloneReflection;

    private Enemy enemy;
    private MyAttribute myAttribute;
    private long myDamage;
    private long enemyDamage;
    private long myCurrentPower;
    private long enemyCurrentPower;
    private boolean isDone = false;
    private double firstRoundEPj;
    private int mySpeed;
    private int enemySpeed;
    double lowNum,mediumNum,superNum;
    private List<Long> myCurrentPowerPerTurn;
    private List<Long> enemyCurrentPowerPerTurn;
    private static final int[] PIE_COLORS = {
            Color.rgb(100,149,237), Color.rgb(	128,128,128),
            Color.rgb(255, 208, 140)};
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        Intent intent;
        intent = getIntent();
        preferences = getSharedPreferences(MyApplication.EXTRA_SETTING,0);
        myAttribute = intent.getParcelableExtra("MyAttribute");
        enemy = intent.getParcelableExtra("EnemyAttr");
        mySpeed = myAttribute.getMyAttribute().getSpeed();
        enemySpeed = enemy.getEnemyAttribute().getSpeed();
        myCurrentPower = myAttribute.getMyAttribute().getCurrentPower();
        enemyCurrentPower = enemy.getEnemyAttribute().getCurrentPower();
        myCurrentPowerPerTurn = new ArrayList<Long>();
        enemyCurrentPowerPerTurn = new ArrayList<Long>();
        myCurrentPowerPerTurn.add(myCurrentPower);
        enemyCurrentPowerPerTurn.add(enemyCurrentPower);
        initialCloneDamge();
        initialBasicAttributes();
        fight();
        cacCloneLoss();
        initialChart();
    }

    /**
     * 计算克隆体战损数量
     */
    private void cacCloneLoss() {
        int loss = myAttribute.getCloneLoss();
        double powerLossRate = (1-(double)myCurrentPowerPerTurn.get(myCurrentPowerPerTurn.size()-1)/(double)myCurrentPowerPerTurn.get(0));
        double lossRate = 0.5/(1+((double)loss/100))*(powerLossRate);
        lowNum =Math.ceil((double)myAttribute.getLowCloneNum() * lossRate);
        mediumNum = Math.ceil(myAttribute.getMediumCloneNum() * lossRate);
        superNum =(int)(myAttribute.getSuperCloneNum() * lossRate);
    }

    /**
     * 图标配置初始化
     */
    private void initialChart() {
        ListView lv = findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<ChartItem>();

        for (int i = 0; i < 1; i++) {
            list.add(new LineChartItem(generateDataLine(), getApplicationContext()));
            list.add(new PieChartItem(generateDataPie(), getApplicationContext()));
            list.add(new BarChartItem(generateDataBar(),getApplicationContext()));
    }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
    }

    /**
     * @return
     * 生成克隆体战损饼图
     */
    private PieData generateDataPie() {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry((float) (lowNum), "低级克隆体"));
        entries.add(new PieEntry((float) ((mediumNum)), "高级克隆体"));
        entries.add(new PieEntry((float) (superNum), "超级克隆体"));
        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(PIE_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }

    /**
     * @return
     * 初始化线型图数据
     */
    private LineData generateDataLine() {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        for (int i = 0; i < myCurrentPowerPerTurn.size(); i++) {
            e1.add(new Entry(i,(float)myCurrentPowerPerTurn.get(i)/(float) myAttribute.getMyAttribute().getPower()*100,
                    myCurrentPowerPerTurn.get(i)));
        }

        LineDataSet d1 = new LineDataSet(e1, "回合结束我方剩余战力");
        d1.setLineWidth(3f);
        d1.setCircleRadius(5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setHighlightEnabled(true);
        d1.setColor(Color.BLUE);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < enemyCurrentPowerPerTurn.size(); i++) {
            e2.add(new Entry(i,(float)enemyCurrentPowerPerTurn.get(i)/(float)enemy.getEnemyAttribute().getPower()*100,enemyCurrentPowerPerTurn.get(i)));
        }
        LineDataSet d2 = new LineDataSet(e2, "回合结束敌方剩余战力");
        d2.setLineWidth(3f);
        d2.setCircleRadius(5f);
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        d2.setDrawValues(false);
        d2.setHighlightEnabled(true); // allow highlighting for DataSet
        d2.setDrawHighlightIndicators(true);
        d2.setHighLightColor(Color.BLUE);
        d2.setColor(Color.RED);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }

    /**
     * @return
     * 初始化条状图
     */
    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        entries.add(new BarEntry(0, (float) lowNum));
        entries.add(new BarEntry(1, (float) mediumNum));
        entries.add(new BarEntry(2, (float) superNum));

        BarDataSet d = new BarDataSet(entries, "克隆体损失数量");
        d.setColors(PIE_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

      /**
     * 初始化战斗相关的一些系数
     */
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

    /**
     * 开始模拟战斗流程
     */
    private void fight() {
        int round;
        round = 1;
        mySpeed = myAttribute.getMyAttribute().getSpeed();
        enemySpeed = enemy.getEnemyAttribute().getSpeed();
        while (!isDone) {
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
        enemyCurrentPower = enemyCurrentPower - myDamage;

        if (enemyCurrentPower <= 0) {
            isDone = true;
            enemyCurrentPower = 0;
            return true;
        } else {
            enemy.getEnemyAttribute().setCurrentPower(enemyCurrentPower);
            return false;
        }
    }

    private boolean beingAttacked(int round) {
        cacDamage(myAttribute.getMyAttribute().getCurrentPower(), enemy.getEnemyAttribute().getCurrentPower(), round);
        myCurrentPower = myCurrentPower - enemyDamage;
        enemyCurrentPower = enemyCurrentPower - myAttribute.getMbFan() - cloneReflection;
        if (myCurrentPower <= 0) {
            myCurrentPower = 0;
            return true;
        } else {
            myAttribute.getMyAttribute().setCurrentPower(myCurrentPower);
            enemy.getEnemyAttribute().setCurrentPower(enemyCurrentPower);
            return false;
        }
    }

    /**
     * 初始化克隆体的特殊伤害
     */
    private void initialCloneDamge() {
        long lks;
        int cloneLevel = Integer.valueOf(preferences.getString("level","0"));
        //超级克隆体数量
        int tsNuM = Integer.valueOf(preferences.getString("ts","0"));
        int nmjbNum = Integer.valueOf(preferences.getString("nmjb","0"));
        int chNum = Integer.valueOf(preferences.getString("ch","0"));
        int bzrNum = Integer.valueOf(preferences.getString("bzr","0"));
        int xklNum = Integer.valueOf(preferences.getString("xkl","0"));
        int lksNum = Integer.valueOf(preferences.getString("lks","0"));
        //超级克隆体强化状态
        int qhTs = Integer.valueOf(preferences.getString("qh_ts","0"));
        int qhCh = Integer.valueOf(preferences.getString("qh_ch","0"));
        int qhBzr = Integer.valueOf(preferences.getString("qh_bzr","0"));
        int qhNmjb = Integer.valueOf(preferences.getString("qh_nmjb","0"));
        int qhXkl = Integer.valueOf(preferences.getString("qh_xkl","0"));
        int qhLks = Integer.valueOf(preferences.getString("qh_lks","0"));
        //高级克隆体数量
        int qxbytNum = Integer.valueOf(preferences.getString("qxbyt","0"));
        int yxNum = Integer.valueOf(preferences.getString("yx","0"));
        int ylNum = Integer.valueOf(preferences.getString("yl","0"));
        int cclzNum = Integer.valueOf(preferences.getString("cclz","0"));
        int bfsNum = Integer.valueOf(preferences.getString("bfs","0"));
        int kjsbNum = Integer.valueOf(preferences.getString("kjsb","0"));
        angel = tsNuM == 0 ? 0 : 24000 * cloneLevel *tsNuM + 30000 * qhTs;
        nano = nmjbNum == 0 ? 0 : 16000 * cloneLevel *nmjbNum + 20000 * qhNmjb;
        mutant = bzrNum == 0 ? 0 : (long) (0.0048 * myAttribute.getMyAttribute().getPower() * cloneLevel);
        dragon = xklNum == 0 ? 0 : 5 * cloneLevel* xklNum + 25 * qhXkl;
        insect = 0;
        lks = qhLks == 0 ? 6000 * lksNum * cloneLevel: 6000 * lksNum * cloneLevel + 21000;

        cloneZeng = 1000 * cloneLevel *( yxNum + kjsbNum +tsNuM + bzrNum ) + 1000 * bzrNum;
        cloneReduction = lks + 1000 * cloneLevel * (qxbytNum + cclzNum + lksNum);
        cloneBao = 1200 * xklNum * cloneLevel + 2400 * chNum + 1200 * (cloneLevel -1) * chNum;
        cloneReflection = 1000 * cloneLevel * (bfsNum + ylNum + nmjbNum) + 1000 * nmjbNum ;
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
        long mbBao;
        mbBao = myAttribute.getMbBao();
        mbJian = myAttribute.getMbJian();
        //cloneBao = myAttribute.getCloneCricDamage();
        //cloneReduction = myAttribute.getCloneReductionDamage();
        mbZeng = myAttribute.getMbZeng();
        satellite = myAttribute.getSatellite();
        //cloneZeng = myAttribute.getCloneAdditionDamage();
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

    /**
     * @param round
     * 我方机动大于等于对方机动,我方先手
     */
    private void myTurnFirst(int round) {
        if (!isDone) {
            isDone = attack(round);
        }
        if (!isDone) {
            isDone = beingAttacked(round);
        }
    }

    /**
     * @param round
     * 我方机动小于敌方机动,我方后手
     */
    private void enemyTurnFirst(int round) {
        if (!isDone) {
            isDone = beingAttacked(round);
        }
        if (!isDone) {
            isDone = attack(round);
        }
    }

    /**
     * @param round
     * 根据回合数增加或者减免特殊伤害
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
