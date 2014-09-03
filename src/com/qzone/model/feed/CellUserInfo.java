package com.qzone.model.feed;

/**
 * 这个类摘自手机QQ空间,并进行了大量精简,仅保留必要的变量和方法供测试用
 */
public class CellUserInfo  {
    
    private User user;

    public void setUser(User u) {
        this.user = u;
    }
    
    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

}
