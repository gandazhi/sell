package com.gandazhi.sell.pojo;

import java.util.Date;

public class CartInfo {
    private Integer id;

    private Integer openid;

    private Integer productId;

    private Integer quantity;

    private Date createTime;

    private Date updateTime;

    public CartInfo(Integer id, Integer openid, Integer productId, Integer quantity, Date createTime, Date updateTime) {
        this.id = id;
        this.openid = openid;
        this.productId = productId;
        this.quantity = quantity;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public CartInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpenid() {
        return openid;
    }

    public void setOpenid(Integer openid) {
        this.openid = openid;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}