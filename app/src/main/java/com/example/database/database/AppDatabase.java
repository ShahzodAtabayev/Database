package com.example.database.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.database.data.FacultyModel;
import com.example.database.data.GroupModel;
import com.example.database.data.HistoryModel;
import com.example.database.data.StudentHistoryModel;
import com.example.database.data.StudentModel;
import com.example.database.data.UserModel;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "appDatabase.db";

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, login TEXT NOT NULL, password TEXT NOT NULL, image TEXT NOT NULL, student_count INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE faculties(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, user_id INTEGER NOT NULL, image TEXT NOT NULL, student_count INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE groups(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, faculty_id INTEGER NOT NULL, image TEXT NOT NULL, student_count INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE students(id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT NOT NULL, last_name TEXT NOT NULL, image TEXT NOT NULL, group_id INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE histories(id INTEGER PRIMARY KEY AUTOINCREMENT, group_id INTEGER NOT NULL,  student_id INTEGER NOT NULL, date TEXT NOT NULL, is_checked TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "users");
        db.execSQL("DROP TABLE IF EXISTS " + "faculties");
        db.execSQL("DROP TABLE IF EXISTS " + "groups");
        db.execSQL("DROP TABLE IF EXISTS " + "students");
        db.execSQL("DROP TABLE IF EXISTS " + "histories");
        onCreate(db);
    }

    public void addStudent(StudentModel student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", student.getFirstName());
        values.put("last_name", student.getLastName());
        values.put("image", student.getImage());
        values.put("group_id", student.getGroupId());
        db.insert("students", null, values);
        db.close();
    }

    public void addGroup(GroupModel groupModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", groupModel.getName());
        values.put("image", groupModel.getImage());
        values.put("faculty_id", groupModel.getFacultyId());
        values.put("student_count", 0);
        db.insert("groups", null, values);
        db.close();
    }

    public void addFaculty(FacultyModel facultyModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", facultyModel.getName());
        values.put("image", facultyModel.getImage());
        values.put("student_count", 0);
        values.put("user_id", facultyModel.getUserId());
        db.insert("faculties", null, values);
        db.close();
    }

    public void addUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", userModel.getName());
        values.put("login", userModel.getLogin());
        values.put("password", userModel.getPassword());
        values.put("image", userModel.getImage());
        values.put("student_count", 0);
        db.insert("users", null, values);
        db.close();
    }

    public void addHistory(HistoryModel historyModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("group_id", historyModel.getGroupId());
        cv.put("student_id", historyModel.getStudentId());
        cv.put("date", historyModel.getDate());
        cv.put("is_checked", historyModel.getIsChecked());
        db.insert("histories", null, cv);
        db.close();
    }

    public List<StudentModel> getStudentsByGroupId(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT*FROM " + "students" + " WHERE " + "students.group_id" + "=" + groupId;
        List<StudentModel> studentList;
        try (Cursor cursor = db.rawQuery(query, null)) {
            studentList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int firstNameIndex = cursor.getColumnIndex("first_name");
                    int lastNameIndex = cursor.getColumnIndex("last_name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int classIdIndex = cursor.getColumnIndex("group_id");
                    StudentModel studentModel = new StudentModel(
                            cursor.getString(firstNameIndex), cursor.getString(lastNameIndex), cursor.getLong(classIdIndex), cursor.getString(imageIndex));
                    studentModel.setId(cursor.getLong(idIndex));
                    studentList.add(studentModel);
                }
        }
        return studentList;
    }

    public List<GroupModel> getGroupsByFacultyId(long facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String a = "SELECT " +
                "g.id," +
                "g.name," +
                "g.image," +
                "(select count(students.id) student_count from students where group_id=g.id) as student_count" +
                " from groups g" +
                " WHERE g.faculty_id=" + facultyId;

        List<GroupModel> groupModelList;
        try (Cursor cursor = db.rawQuery(a, null)) {
            groupModelList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
//                    int facultyIdIndex = cursor.getColumnIndex("faculty_id");
                    int studentCountIndex = cursor.getColumnIndex("student_count");
                    GroupModel groupModel = new GroupModel(
                            cursor.getString(nameIndex), cursor.getString(imageIndex), facultyId, cursor.getLong(studentCountIndex));
                    groupModel.setId(cursor.getLong(idIndex));
                    groupModelList.add(groupModel);
                }
        }
        return groupModelList;
    }

    public List<FacultyModel> getFacultyByUserId(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String h = "SELECT " +
                "f.id," +
                "f.image," +
                "f.name," +
                "(select count(students.id) student_count from students where group_id in (select id from groups where faculty_id=f.id)) as student_count" +
                " from faculties f" +
                " WHERE f.user_id=" + userId;
        List<FacultyModel> facultyModelList;
        try (Cursor cursor = db.rawQuery(h, null)) {
            facultyModelList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int studentCountIndex = cursor.getColumnIndex("student_count");
                    FacultyModel facultyModel = new FacultyModel(
                            cursor.getString(nameIndex), cursor.getString(imageIndex), userId, cursor.getLong(studentCountIndex));
                    facultyModel.setId(cursor.getInt(idIndex));
                    facultyModelList.add(facultyModel);
                }
        }
        return facultyModelList;
    }

    public List<FacultyModel> getFaculty() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM faculties";
        List<FacultyModel> facultyModelList;
        try (Cursor cursor = db.rawQuery(query, null)) {
            facultyModelList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int userIdIndex = cursor.getColumnIndex("user_id");
                    int studentCountIndex = cursor.getColumnIndex("student_count");
                    FacultyModel facultyModel = new FacultyModel(
                            cursor.getString(nameIndex), cursor.getString(imageIndex), cursor.getLong(userIdIndex), cursor.getLong(studentCountIndex));
                    facultyModel.setId(cursor.getInt(idIndex));
                    facultyModelList.add(facultyModel);
                }
        }
        return facultyModelList;
    }

    public List<Long> getFacultyStudentCount(int userId, int facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT count(students.id) student_count " +
                " FROM faculties, groups, students" +
                " WHERE faculties.user_id" + "=" + userId + " AND groups.faculty_id=faculties.id AND students.group_id=groups.id";
        List<Long> studentCount;
        try (Cursor cursor = db.rawQuery(query, null)) {
            studentCount = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int studentCountIndex = cursor.getColumnIndex("student_count");
                    studentCount.add(cursor.getLong(studentCountIndex));
                }
        }
        return studentCount;
    }


    public List<UserModel> getUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT users.id, users.name, users.login, users.password, users.image, count(students.id) student_count " +
                "FROM users, faculties, groups, students " +
                "WHERE  users.id=faculties.user_id AND groups.faculty_id=faculties.id AND groups.id= students.group_id " +
                "GROUP BY users.id";
        List<UserModel> userModelList;
        try (Cursor cursor = db.rawQuery(query, null)) {
            userModelList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int loginIndex = cursor.getColumnIndex("login");
                    int passwordIndex = cursor.getColumnIndex("password");
                    int imageIndex = cursor.getColumnIndex("image");
                    int studentCountIndex = cursor.getColumnIndex("student_count");
                    UserModel userModel = new UserModel(
                            cursor.getString(nameIndex), cursor.getString(loginIndex), cursor.getString(passwordIndex), cursor.getString(imageIndex), cursor.getLong(studentCountIndex));
                    userModel.setId(cursor.getLong(idIndex));
                    userModelList.add(userModel);
                }
        } catch (Exception e) {
            userModelList = new ArrayList<>();
        }
        return userModelList;
    }

    public List<StudentHistoryModel> getHistories(String date, long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.first_name, s.last_name, s.image, h.is_checked FROM  histories h  inner join students s on s.id=h.student_id" +
                " WHERE h.date =" + date + " AND h.group_id =" + groupId + " GROUP BY s.id";
        List<StudentHistoryModel> userModelList;
        try (Cursor cursor = db.rawQuery(query, null)) {
            userModelList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int firstNameIndex = cursor.getColumnIndex("first_name");
                    int lastNameIndex = cursor.getColumnIndex("last_name");
                    int imageIndex = cursor.getColumnIndex("image");
                    int isCheckedIndex = cursor.getColumnIndex("is_checked");
                    StudentHistoryModel studentHistoryModel = new StudentHistoryModel(
                            cursor.getString(firstNameIndex), cursor.getString(lastNameIndex), cursor.getString(imageIndex), cursor.getString(isCheckedIndex));
                    userModelList.add(studentHistoryModel);
                }
        } catch (Exception e) {
            userModelList = new ArrayList<>();
        }
        return userModelList;
    }

    public List<HistoryModel> getHistories1(String date, long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT*FROM  histories";
        List<HistoryModel> userModelList;
        try (Cursor cursor = db.rawQuery(query, null)) {
            userModelList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int groupIdIndex = cursor.getColumnIndex("group_id");
                    int studentIdIndex = cursor.getColumnIndex("student_id");
                    int imageIndex = cursor.getColumnIndex("date");
                    int isCheckedIndex = cursor.getInt(cursor.getColumnIndex("is_checked"));
                    HistoryModel studentHistoryModel = new HistoryModel(
                            cursor.getLong(groupIdIndex), cursor.getLong(studentIdIndex), cursor.getString(imageIndex), cursor.getString(isCheckedIndex));
                    userModelList.add(studentHistoryModel);
                }
        } catch (Exception e) {
            userModelList = new ArrayList<>();
        }
        return userModelList;
    }

    public List<UserModel> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT*FROM users";
        List<UserModel> userModelList;
        try (Cursor cursor = db.rawQuery(query, null)) {
            userModelList = new ArrayList<>();
            if (cursor != null)
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int loginIndex = cursor.getColumnIndex("login");
                    int passwordIndex = cursor.getColumnIndex("password");
                    int imageIndex = cursor.getColumnIndex("image");
                    int studentCountIndex = cursor.getColumnIndex("student_count");
                    UserModel userModel = new UserModel(
                            cursor.getString(nameIndex), cursor.getString(loginIndex), cursor.getString(passwordIndex), cursor.getString(imageIndex), cursor.getLong(studentCountIndex));
                    userModel.setId(cursor.getLong(idIndex));
                    userModelList.add(userModel);
                }
        } catch (Exception e) {
            Log.d("TTT", e.toString());
            userModelList = new ArrayList<>();
        }
        return userModelList;
    }

    public void addHistories(List<HistoryModel> list) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            for (HistoryModel historyModel : list) {
                cv.put("group_id", historyModel.getGroupId());
                cv.put("student_id", historyModel.getStudentId());
                cv.put("date", historyModel.getDate());
                cv.put("is_checked  ", historyModel.getIsChecked());
                db.insert("histories", null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<StudentModel> getAllStudents() {
        List<StudentModel> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + "users";
        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    StudentModel studentModel = new StudentModel(
                            cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getString(4));
                    studentModel.setId(cursor.getInt(0));
                    contactList.add(studentModel);
                } while (cursor.moveToNext());
            }
        }
        return contactList;
    }

    public int updateUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", userModel.getName());
        values.put("login", userModel.getLogin());
        values.put("password", userModel.getPassword());
        values.put("image", userModel.getImage());
        values.put("student_count", userModel.getStudentCount());
        return db.update("users", values, "id" + " = ?",
                new String[]{String.valueOf(userModel.getId())});
    }

    public int updateFaculty(FacultyModel facultyModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", facultyModel.getName());
        values.put("image", facultyModel.getImage());
        values.put("user_id", facultyModel.getUserId());
        values.put("student_count", facultyModel.getStudentCount());
        return db.update("faculties", values, "id" + " = ?",
                new String[]{String.valueOf(facultyModel.getId())});
    }

    public int updateGroup(GroupModel groupModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", groupModel.getName());
        values.put("image", groupModel.getImage());
        values.put("faculty_id", groupModel.getFacultyId());
        values.put("student_count", groupModel.getStudentCount());
        return db.update("groups", values, "id" + " = ?",
                new String[]{String.valueOf(groupModel.getId())});
    }

    public int updateStudent(StudentModel studentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", studentModel.getFirstName());
        values.put("last_name", studentModel.getLastName());
        values.put("image", studentModel.getImage());
        values.put("group_id", studentModel.getGroupId());
        return db.update("students", values, "id" + " = ?",
                new String[]{String.valueOf(studentModel.getId())});
    }

    public void removeStudent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("students", "id" + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeUser(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "id" + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeFaculty(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("faculties", "id" + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeGroup(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("groups", "id" + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getStudentCount() {
        String countQuery = "SELECT * FROM " + "users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
}
