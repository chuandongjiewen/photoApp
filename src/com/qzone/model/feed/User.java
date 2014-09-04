package com.qzone.model.feed;

import android.os.Parcel;
import android.text.TextUtils;

/**
 * 这个类摘自手机QQ空间,并进行了大量精简,仅保留必要的变量和方法供测试用
 */
public class User  {
    
	private String uid;
	private String username;
	private String nickname;
	private String avatar;
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	


}