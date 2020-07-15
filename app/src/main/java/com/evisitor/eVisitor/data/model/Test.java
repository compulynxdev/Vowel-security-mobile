package com.evisitor.eVisitor.data.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Test {
    private String name;

    @Generated(hash = 1115628523)
    public Test(String name) {
        this.name = name;
    }

    @Generated(hash = 372557997)
    public Test() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
