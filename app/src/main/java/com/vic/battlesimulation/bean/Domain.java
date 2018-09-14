package com.vic.battlesimulation.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class Domain implements IPickerViewData{
    private String type;
    private String id;

    public Domain(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getPickerViewText() {
        return type+id;
    }
}
