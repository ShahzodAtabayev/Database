package com.example.database.data;

public class GroupModel {
    private long id;
    private String name;
    private String image;
    private long facultyId;
    private long studentCount;

    public GroupModel(String name, String image, long facultyId, long studentCount) {
        this.name = name;
        this.image = image;
        this.facultyId = facultyId;
        this.studentCount = studentCount;
    }
    public GroupModel(String name, String image, long facultyId) {
        this.name = name;
        this.image = image;
        this.facultyId = facultyId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }

    public long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }
}
