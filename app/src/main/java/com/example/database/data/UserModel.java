package com.example.database.data;

public class UserModel {
    private long id = 0;
    private String name;
    private String login;
    private String password;
    private String image;
    private long studentCount;

    public UserModel() {

    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }

    public long getStudentCount() {
        return studentCount;
    }


    public UserModel(String name, String login, String password, String image) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.image = image;
    }

    public UserModel(String name, String login, String password, String image, long studentCount) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.image = image;
        this.studentCount = studentCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
