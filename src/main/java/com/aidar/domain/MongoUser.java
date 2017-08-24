package com.aidar.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@TypeAlias("mongoUser")
public class MongoUser {
	
	@Id
	private long uid;

	//用户名
	private String uname;

	//密码
	private String upwd;

	//性别
	private Boolean usex;

	//邮箱
	private String uemail;

	//创建时间
	private Date createtime;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
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
}
