package com.ym.album.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "t_user")
public class UserInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;
    @ColumnInfo(name = "user_name")
    private String userName;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "telephone")
    private String telephone;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "age")
    private int age;
    @ColumnInfo(name = "birth_time")
    private Date birthTime;

    public UserInfo(String userName, String password, String telephone,
                    String email, int age, Date birthTime) {
//        this.id = id;
        this.userName = userName;
        this.password = password;
        this.telephone = telephone;
        this.email = email;
        this.age = age;
        this.birthTime = birthTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(Date birthTime) {
        this.birthTime = birthTime;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", birthTime=" + birthTime +
                '}';
    }
}
