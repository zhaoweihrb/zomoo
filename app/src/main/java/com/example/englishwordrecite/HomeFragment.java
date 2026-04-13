package com.example.englishwordrecite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private Button btnGeneratePaper, btnScanPaper, btnChinesePractice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        btnGeneratePaper = view.findViewById(R.id.btnGeneratePaper);
        btnScanPaper = view.findViewById(R.id.btnScanPaper);
        btnChinesePractice = view.findViewById(R.id.btnChinesePractice);

        btnGeneratePaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到考试管理页面
                Intent intent = new Intent(getActivity(), ExamManagementActivity.class);
                startActivity(intent);
            }
        });

        btnScanPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到考试管理页面，需要先创建考试才能扫描阅卷
                Intent intent = new Intent(getActivity(), ExamManagementActivity.class);
                startActivity(intent);
            }
        });

        btnChinesePractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到语文练习管理页面
                Intent intent = new Intent(getActivity(), ChinesePracticeManagementActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
