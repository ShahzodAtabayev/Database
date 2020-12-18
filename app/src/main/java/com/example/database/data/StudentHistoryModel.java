package com.example.database.data;

public class StudentHistoryModel {
    private long id=0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String firstName;
    private String lastName;
    private String image;
    private  String isChecked;


    public StudentHistoryModel(String firstName, String lastName, String image, String isChecked) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.isChecked = isChecked;
    }

    public String isChecked() {
        return isChecked;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
