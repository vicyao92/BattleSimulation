package com.vic.battlesimulation.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MyAttribute implements Parcelable{
    private BasicAttribute myAttribute ;
    private long mbZeng ;
    private long mbJian ;
    private long mbFan ;
    private long mbBao ;
    private long satellite ;
    private int cloneLoss;
    private int cloneLevel;
    private long cloneAdditionDamage ;
    private long cloneReductionDamage ;
    private long cloneCricDamage ;
    private long cloneReflectionDamage ;
    private int cloneAngel;
    private int cloneInsect;
    private int cloneNano;
    private int cloneMutant;
    private int cloneDragon;
    private int superCloneNum;
    private int mediumCloneNum;
    private int CloneNum;

    public MyAttribute(BasicAttribute myAttribute) {
        this.myAttribute = myAttribute;
    }

    protected MyAttribute(Parcel in) {
        myAttribute = in.readParcelable(BasicAttribute.class.getClassLoader());
        mbZeng = in.readLong();
        mbJian = in.readLong();
        mbFan = in.readLong();
        mbBao = in.readLong();
        satellite = in.readLong();
        cloneLoss = in.readInt();
        cloneLevel = in.readInt();
        cloneAdditionDamage = in.readLong();
        cloneReductionDamage = in.readLong();
        cloneCricDamage = in.readLong();
        cloneReflectionDamage = in.readLong();
        cloneAngel = in.readInt();
        cloneInsect = in.readInt();
        cloneNano = in.readInt();
        cloneMutant = in.readInt();
        cloneDragon = in.readInt();
    }

    public static final Creator<MyAttribute> CREATOR = new Creator<MyAttribute>() {
        @Override
        public MyAttribute createFromParcel(Parcel in) {
            return new MyAttribute(in);
        }

        @Override
        public MyAttribute[] newArray(int size) {
            return new MyAttribute[size];
        }
    };

    public BasicAttribute getMyAttribute() {
        return myAttribute;
    }

    public void setMyAttribute(BasicAttribute myAttribute) {
        this.myAttribute = myAttribute;
    }

    public long getMbZeng() {
        return mbZeng;
    }

    public void setMbZeng(long mbZeng) {
        this.mbZeng = mbZeng;
    }

    public long getMbJian() {
        return mbJian;
    }

    public void setMbJian(long mbJian) {
        this.mbJian = mbJian;
    }

    public long getMbFan() {
        return mbFan;
    }

    public void setMbFan(long mbFan) {
        this.mbFan = mbFan;
    }

    public long getMbBao() {
        return mbBao;
    }

    public void setMbBao(long mbBao) {
        this.mbBao = mbBao;
    }

    public long getSatellite() {
        return satellite;
    }

    public void setSatellite(long satellite) {
        this.satellite = satellite;
    }

    public int getCloneLoss() {
        return cloneLoss;
    }

    public void setCloneLoss(int cloneLoss) {
        this.cloneLoss = cloneLoss;
    }

    public int getCloneLevel() {
        return cloneLevel;
    }

    public void setCloneLevel(int cloneLevel) {
        this.cloneLevel = cloneLevel;
    }

    public long getCloneAdditionDamage() {
        return cloneAdditionDamage;
    }

    public void setCloneAdditionDamage(long cloneAdditionDamage) {
        this.cloneAdditionDamage = cloneAdditionDamage;
    }

    public long getCloneReductionDamage() {
        return cloneReductionDamage;
    }

    public void setCloneReductionDamage(long cloneReductionDamage) {
        this.cloneReductionDamage = cloneReductionDamage;
    }

    public long getCloneCricDamage() {
        return cloneCricDamage;
    }

    public void setCloneCricDamage(long cloneCricDamage) {
        this.cloneCricDamage = cloneCricDamage;
    }

    public long getCloneReflectionDamage() {
        return cloneReflectionDamage;
    }

    public void setCloneReflectionDamage(long cloneReflectionDamage) {
        this.cloneReflectionDamage = cloneReflectionDamage;
    }

    public int getCloneAngel() {
        return cloneAngel;
    }

    public void setCloneAngel(int cloneAngel) {
        this.cloneAngel = cloneAngel;
    }

    public int getCloneInsect() {
        return cloneInsect;
    }

    public void setCloneInsect(int cloneInsect) {
        this.cloneInsect = cloneInsect;
    }

    public int getCloneNano() {
        return cloneNano;
    }

    public void setCloneNano(int cloneNano) {
        this.cloneNano = cloneNano;
    }

    public int getCloneMutant() {
        return cloneMutant;
    }

    public void setCloneMutant(int cloneMutant) {
        this.cloneMutant = cloneMutant;
    }

    public int getCloneDragon() {
        return cloneDragon;
    }

    public void setCloneDragon(int cloneDragon) {
        this.cloneDragon = cloneDragon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeParcelable(myAttribute, i);
        parcel.writeLong(mbZeng);
        parcel.writeLong(mbJian);
        parcel.writeLong(mbFan);
        parcel.writeLong(mbBao);
        parcel.writeLong(satellite);
        parcel.writeInt(cloneLoss);
        parcel.writeInt(cloneLevel);
        parcel.writeLong(cloneAdditionDamage);
        parcel.writeLong(cloneReductionDamage);
        parcel.writeLong(cloneCricDamage);
        parcel.writeLong(cloneReflectionDamage);
        parcel.writeInt(cloneAngel);
        parcel.writeInt(cloneInsect);
        parcel.writeInt(cloneNano);
        parcel.writeInt(cloneMutant);
        parcel.writeInt(cloneDragon);
    }
}
