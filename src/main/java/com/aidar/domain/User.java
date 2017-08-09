package com.aidar.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (C), 2017, spring boot 自我学习
 *
 * @author upsmart
 * @since 17-6-2
 */
@Entity
@Table
public class User implements Serializable {

    // 用户ID
    @Id
    @Column(name = "uid", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    //用户名
    @Column(name = "uname", length = 40, nullable = false)
    private String uname;

    //密码
    @Column(name = "upwd", nullable = false)
    private String upwd;

    //性别
    @Column(name = "usex", nullable = false)
    private Boolean usex;

    //邮箱
    @Column(name = "uemail", nullable = false)
    private String uemail;

    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createtime", nullable = false)
    private Date createtime;

    //修改时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modtime", nullable = false)
    private Date modtime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public Boolean getUsex() {
        return usex;
    }

    public void setUsex(Boolean usex) {
        this.usex = usex;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModtime() {
        return modtime;
    }

    public void setModtime(Date modtime) {
        this.modtime = modtime;
    }
    @Override
    public String toString() {
        return "User{" +
            "uid=" + uid +
            ", uname=" + uname +
            ", uemail='" + uemail + '\'' +
            ", createtime='" + createtime + '\'' +
            '}';
    }
}
