package com.example.database.data;

public class FacultyModel {
    private long id = 0;
    private String name;
    private String image;
    private long userId;
    private long studentCount;

    public FacultyModel(String name, String image, long userId, long studentCount) {
        this.name = name;
        this.userId = userId;
        this.studentCount = studentCount;
        this.image = image;
    }

    public FacultyModel(String name, String image, long userId) {
        this.name = name;
        this.userId = userId;
        this.image = image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public long getStudentCount() {
        return studentCount;
    }

    public String getImage() {
        return image;
    }
}
