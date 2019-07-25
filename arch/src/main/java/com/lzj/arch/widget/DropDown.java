package com.lzj.arch.widget;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 下拉式列表项数据类型
 */
@Setter
@Getter
@AllArgsConstructor
public class DropDown implements Serializable {
    private int id;
    private String name;
    /**
     * 所属类型
     * 0排序，1状态，2标签，3主标签
     */
    private int type;
    /**
     * 当前标签是否被选中
     */
    private boolean check;

    public DropDown(String name) {
        this.name = name;
    }

    public DropDown(int id, String name) {
        this.id = id;
        this.name = name;
        this.type = 2;
    }

    public DropDown(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
