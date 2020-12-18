package com.example.database.data;

public class HistoryModel {
    private long id;
    private long groupId;
    private long studentId;
    private String date;
    private String isChecked;

    public HistoryModel(long groupId, long studentId, String date, String isChecked) {
        this.groupId = groupId;
        this.studentId = studentId;
        this.date = date;
        this.isChecked = isChecked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

}
