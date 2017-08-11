package com.aidar.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (C), 2017, 银联智惠信息服务（上海）有限公司
 *
 * @author upsmart
 * @since 17-6-2
 */
@Entity
@Table(name = "Product")
public class Product implements Serializable {

    @Id
    @Column(name = "pId", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pId;

    //产品名称
    @Column(name = "pName", nullable = false)
    private String pName;

    //产品规格文本
    @Column(name = "pFile",nullable = false)
    private String pFile;

    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", nullable = false)
    private Date createTime;

    //修改时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modTime", nullable = false)
    private Date modTime;

    public String getpFile() {
        return pFile;
    }

    public void setpFile(String pFile) {
        this.pFile = pFile;
    }

    public int getPid() {
        return pId;
    }

    public void setPid(int pid) {
        this.pId = pid;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }
}
