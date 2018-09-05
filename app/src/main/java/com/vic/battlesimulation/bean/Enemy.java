package com.vic.battlesimulation.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Enemy implements Parcelable{
    private BasicAttribute enemyAttribute ;

    public Enemy(BasicAttribute enemyAttribute) {
        this.enemyAttribute = enemyAttribute;
    }

    protected Enemy(Parcel in) {
        enemyAttribute = in.readParcelable(BasicAttribute.class.getClassLoader());
    }

    public static final Creator<Enemy> CREATOR = new Creator<Enemy>() {
        @Override
        public Enemy createFromParcel(Parcel in) {
            return new Enemy(in);
        }

        @Override
        public Enemy[] newArray(int size) {
            return new Enemy[size];
        }
    };

    public BasicAttribute getEnemyAttribute() {
        return enemyAttribute;
    }

    public void setEnemyAttribute(BasicAttribute enemyAttribute) {
        this.enemyAttribute = enemyAttribute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(enemyAttribute, i);
    }
}
