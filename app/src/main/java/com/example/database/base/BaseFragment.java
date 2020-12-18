package com.example.database.base;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.database.apps.App;
import com.example.database.database.AppDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BaseFragment extends Fragment {
    public ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handle = new Handler(Looper.getMainLooper());
    public AppDatabase db = App.getAppDatabase();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void makeToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    public Bitmap resizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = Float.parseFloat(newWidth + "") / width;
        float scaleHeight = Float.parseFloat(newHeight + "") / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(
                bm, 0, 0, width, height,
                matrix, false
        );
    }

    public void saveBitmap(Bitmap bitmap, File destination) {
        destination.getParentFile().mkdir();
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destination.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void runOnUIThread(OnUIThread uiThread) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            uiThread.f();
        } else {
            handle.post(uiThread::f);
        }
    }

    public void runOnWorkerThread(OnWorkerThread workerThread) {
        executor.execute(workerThread::f);
    }
}




