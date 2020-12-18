package com.example.database.apps;

import android.app.Application;

import com.example.database.data.FacultyModel;
import com.example.database.data.GroupModel;
import com.example.database.data.StudentModel;
import com.example.database.database.AppDatabase;
import com.example.database.database.LocalStorage;


public class App extends Application {
    private static App instance;
    private static AppDatabase appDatabase;

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appDatabase = new AppDatabase(this);
        LocalStorage.init(this);
        if (!LocalStorage.read("isAddDefaultValueDatabase", false)) {
            appDatabase.addFaculty(new FacultyModel("Marketing", "", 1, 0));
            appDatabase.addFaculty(new FacultyModel("Software engineering", "", 1, 0));
            appDatabase.addFaculty(new FacultyModel("Information security", "", 1, 0));
            appDatabase.addGroup(new GroupModel("316-18", "", 1));
            appDatabase.addGroup(new GroupModel("317-18", "", 1));
            appDatabase.addGroup(new GroupModel("731-17", "", 2));
            appDatabase.addStudent(new StudentModel("David", "Alaba", 1, ""));
            appDatabase.addStudent(new StudentModel("Jone", "Stones", 1, ""));
            appDatabase.addStudent(new StudentModel("Harry", " Kane", 2, ""));
            appDatabase.addStudent(new StudentModel("Robert", "Levandovskiy", 2, ""));
            LocalStorage.write("isAddDefaultValueDatabase", true);
        }
//        Executors.newSingleThreadExecutor().execute(() -> DataBase.init(this));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
