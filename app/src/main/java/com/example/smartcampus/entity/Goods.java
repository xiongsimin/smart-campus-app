package com.example.smartcampus.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Goods implements Serializable {
    private long id;
    private String goodsName;
    private long userId;
    private Double goodsPrice;
    private Timestamp time;
    private int typeId;

    public Goods() {
        super();
    }

    public Goods(long id, String goodsName, long userId, Double goodsPrice, Timestamp time, int typeId) {
        this.id = id;
        this.goodsName = goodsName;
        this.userId = userId;
        this.goodsPrice = goodsPrice;
        this.time = time;
        this.typeId = typeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", userId=" + userId +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", time=" + time +
                ", typeId=" + typeId +
                '}';
    }
}
