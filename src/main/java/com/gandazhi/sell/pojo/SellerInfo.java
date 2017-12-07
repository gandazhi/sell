package com.gandazhi.sell.pojo;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class SellerInfo {
    @Expose
    private Integer id;

    @Expose
    private String username;

    @Expose(serialize = false)
    private String password;

    @Expose
    private String openid;

    @Expose
    private Date createTime;

    @Expose
    private Date updateTime;

    public SellerInfo(Integer id, String username, String password, String openid, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.openid = openid;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public SellerInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
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