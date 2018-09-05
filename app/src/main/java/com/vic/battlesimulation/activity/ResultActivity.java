package com.vic.battlesimulation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vic.battlesimulation.R;
import com.vic.battlesimulation.bean.BasicAttribute;
import com.vic.battlesimulation.bean.Enemy;
import com.vic.battlesimulation.bean.MyAttribute;

import java.math.BigDecimal;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent;
        intent = getIntent();
        myAttribute = intent.getParcelableExtra("MyAttribute");
        Log.d("3333", myAttribute.getMyAttribute().getName());
        enemy = intent.getParcelableExtra("Enemy");
        Log.d("3333", enemy.getEnemyAttribute().getName());
        mySpeed = myAttribute.getMyAttribute().getSpeed();
        enemySpeed = enemy.getEnemyAttribute().getSpeed();
        myCurrentPower = myAttribute.getMyAttribute().getCurrentPower();
        enemyCurrentPower = enemy.getEnemyAttribute().getCurrentPower();
        initialCloneDamge();
        initialBasicAttributes();
        fight();
    }

    private void initialBasicAttributes() {
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
            switch (round){
                case 1:
                    if (myAttribute.getCloneDragon() != 0) {
                        if (mySpeed >= enemySpeed - dragon) {
                            myTurnFirst(round);
                        } else {
                            enemyTurnFirst(round);
                        }
                    }else {
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
        }

    }

    private boolean attack(int round) {
        cacDamage(myAttribute.getMyAttribute().getCurrentPower(), enemy.getEnemyAttribute().getCurrentPower(), round);
        Log.d("3333", "我方造成" + myDamage + "伤害");
        enemyCurrentPower = enemyCurrentPower - myDamage;
        Log.d("3333", "敌方剩余" + enemyCurrentPower + "战力");

        if (enemyCurrentPower <= 0) {
            isDone = true;
            Log.d("3333", "战斗胜利");
            return true;
        } else {
            enemy.getEnemyAttribute().setCurrentPower(enemyCurrentPower);
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
     * @param round
     * 根据回合数增加或者减免特殊伤害
     */
    private void manageSpecialDamage(int round){
        switch (round){
            case 1:
                myDamage = myDamage + mutant ;
                enemyDamage = enemyDamage - nano;
                break;
            case 2:
                if (myAttribute.getCloneInsect() != 0) {
                    insect = myAttribute.getMyAttribute().getCurrentPower() < 1000000 ? 8400 * myAttribute.getCloneLevel() :
                            (long) (myCurrentPower * 0.0084 * myAttribute.getCloneLevel());
                }else {
                    insect = 0;
                }
                myDamage = myDamage +insect ;
                break;
            case 3:
                myDamage = myDamage + angel;
                break;
            default:
                break;
        }
    }
}
