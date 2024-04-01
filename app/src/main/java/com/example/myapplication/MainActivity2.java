package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private OneTimeWorkRequest workRequest1, workRequest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Data data = new Data.Builder().putInt("key",123).build();
        Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();
        workRequest1 = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();
        workRequest2 = new OneTimeWorkRequest.Builder(MyWorker.class).build();

        WorkManager.getInstance(this).enqueue(workRequest1);
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest1.getId()).observe(
                this, workInfo -> {
                    Log.d("RRR","State = "+workInfo.getState());
                    int x = workInfo.getOutputData().getInt("key",0);
                    //Log.d("RRR")
                }
        );

        List<OneTimeWorkRequest> list = new ArrayList<>();
        list.add(workRequest1);
        list.add(workRequest2);

        // параллельно
        WorkManager.getInstance(this).enqueue(list);
        // последовательно
        WorkManager.getInstance(this).beginWith(workRequest1).then(workRequest2).enqueue();

    }
}