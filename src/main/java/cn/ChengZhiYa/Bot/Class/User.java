package cn.ChengZhiYa.Bot.Class;

import lombok.Data;

@Data
public final class User {
    String Id;
    String UserName;
    String Password;
    String LastLoginIP;

    public User(String Id, String UserName, String Password, String LastLoginIP) {
        this.Id = Id;
        this.UserName = UserName;
        this.Password = Password;
        this.LastLoginIP = LastLoginIP;
    }
}
