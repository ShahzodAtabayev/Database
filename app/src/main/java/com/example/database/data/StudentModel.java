package com.example.database.data;

public class StudentModel {
    private long id = 0;
    private String firstName;
    private String lastName;
    private String image;
    private long groupId;

    public StudentModel(String firstName, String lastName, long groupId, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getGroupId() {
        return groupId;
    }


    public String getImage() {
        return image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }


    public void setImage(String image) {
        this.image = image;
    }
}