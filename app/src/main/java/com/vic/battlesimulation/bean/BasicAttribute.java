package com.vic.battlesimulation.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BasicAttribute implements Parcelable{
    private String name ;
    private long power;
    private int fire ;
    private int defense ;
    private int speed ;
    private int luck ;
    private long basicDamage;
    private long damage;
    private double pjCoefficient;
    private double bjCoefficient;
    private long currentPower;

    public BasicAttribute(String name, long power, int fire, int defense, int speed, int luck) {
        this.name = name;
        this.power = power;
        this.fire = fire;
        this.defense = defense;
        this.speed = speed;
        this.luck = luck;
        this.currentPower = power;
    }

    protected BasicAttribute(Parcel in) {
        name = in.readString();
        power = in.readLong();
        fire = in.readInt();
        defense = in.readInt();
        speed = in.readInt();
        luck = in.readInt();
        basicDamage = in.readLong();
        damage = in.readLong();
        pjCoefficient = in.readDouble();
        bjCoefficient = in.readDouble();
        currentPower = in.readLong();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(power);
        parcel.writeInt(fire);
        parcel.writeInt(defense);
        parcel.writeInt(speed);
        parcel.writeInt(luck);
        parcel.writeLong(basicDamage);
        parcel.writeLong(damage);
        parcel.writeDouble(pjCoefficient);
        parcel.writeDouble(bjCoefficient);
        parcel.writeLong(currentPower);
    }

    public static final Creator<BasicAttribute> CREATOR = new Creator<BasicAttribute>() {
        @Override
        public BasicAttribute createFromParcel(Parcel in) {
            return new BasicAttribute(in);
        }

        @Override
        public BasicAttribute[] newArray(int size) {
            return new BasicAttribute[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int fire) {
        this.fire = fire;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public long getBasicDamage() {
        return basicDamage;
    }

    public void setBasicDamage(long basicDamage) {
        this.basicDamage = basicDamage;
    }

    public long getDamage() {
        return damage;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    public double getPjCoefficient() {
        return pjCoefficient;
    }

    public void setPjCoefficient(double pjCoefficient) {
        this.pjCoefficient = pjCoefficient;
    }

    public double getBjCoefficient() {
        return bjCoefficient;
    }

    public void setBjCoefficient(double bjCoefficient) {
        this.bjCoefficient = bjCoefficient;
    }

    public long getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(long currentPower) {
        this.currentPower = currentPower;
    }

}
