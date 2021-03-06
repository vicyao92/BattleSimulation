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
    private int lowCloneNum;

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
        superCloneNum = in.readInt();
        mediumCloneNum = in.readInt();
        lowCloneNum = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(myAttribute, flags);
        dest.writeLong(mbZeng);
        dest.writeLong(mbJian);
        dest.writeLong(mbFan);
        dest.writeLong(mbBao);
        dest.writeLong(satellite);
        dest.writeInt(cloneLoss);
        dest.writeInt(cloneLevel);
        dest.writeLong(cloneAdditionDamage);
        dest.writeLong(cloneReductionDamage);
        dest.writeLong(cloneCricDamage);
        dest.writeLong(cloneReflectionDamage);
        dest.writeInt(cloneAngel);
        dest.writeInt(cloneInsect);
        dest.writeInt(cloneNano);
        dest.writeInt(cloneMutant);
        dest.writeInt(cloneDragon);
        dest.writeInt(superCloneNum);
        dest.writeInt(mediumCloneNum);
        dest.writeInt(lowCloneNum);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getSuperCloneNum() {
        return superCloneNum;
    }

    public void setSuperCloneNum(int superCloneNum) {
        this.superCloneNum = superCloneNum;
    }

    public int getMediumCloneNum() {
        return mediumCloneNum;
    }

    public void setMediumCloneNum(int mediumCloneNum) {
        this.mediumCloneNum = mediumCloneNum;
    }

    public int getLowCloneNum() {
        return lowCloneNum;
    }

    public void setLowCloneNum(int lowCloneNum) {
        this.lowCloneNum = lowCloneNum;
    }

    public BasicAttribute getMyAttribute() {
        return myAttribute;
    }

    public void setMyAttribute(BasicAttribute myAttribute) {
        this.myAttribute = myAttribute;
    }
}
