package com.yl.lenovo.kchat.server.request;


/**
 * Created by AMing on 15/12/23.
 * Company RongCloud
 */
public class RegisterNocodeRequest {


    private String nickname;

    private String password;


    public RegisterNocodeRequest(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
