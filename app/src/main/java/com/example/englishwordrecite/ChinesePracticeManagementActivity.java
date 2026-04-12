package com.example.englishwordrecite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChinesePracticeManagementActivity extends AppCompatActivity {

    private Button btnAddPractice;
    private Button btnGenerateCopybook;
    private ListView lvPractices;
    private PracticeAdapter practiceAdapter;
    private List<ChinesePractice> practiceList;
    private DatabaseHelper dbHelper;
    private ChinesePracticeDao practiceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_practice_management);

        btnAddPractice = findViewById(R.id.btnAddPractice);
        btnGenerateCopybook = findViewById(R.id.btnGenerateCopybook);
        lvPractices = findViewById(R.id.lvPractices);

        dbHelper = new DatabaseHelper(this);
        practiceDao = new ChinesePracticeDao(dbHelper.getWritableDatabase());

        loadPractices();

        practiceAdapter = new PracticeAdapter(practiceList);
        lvPractices.setAdapter(practiceAdapter);

        btnAddPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPracticeDialog();
            }
        });

        btnGenerateCopybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCopybook();
            }
        });

        lvPractices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChinesePractice practice = practiceList.get(position);
                // 可以添加查看练习详情的功能
            }
        });
    }

    private void loadPractices() {
        practiceList = practiceDao.getAllChinesePractices();
    }

    private void showAddPracticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_practice, null);
        builder.setView(view);

        final EditText etContent = view.findViewById(R.id.etContent);
        final Spinner spinnerType = view.findViewById(R.id.spinnerType);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAdd = view.findViewById(R.id.btnAdd);

        // 设置类型选择器
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"古诗", "成语", "其他"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString().trim();
                String type = spinnerType.getSelectedItem().toString();

                if (content.isEmpty()) {
                    Toast.makeText(ChinesePracticeManagementActivity.this, "请填写练习内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 创建并添加语文练习内容
                ChinesePractice practice = new ChinesePractice(content, type);
                practiceDao.addChinesePractice(practice);

                Toast.makeText(ChinesePracticeManagementActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                loadPractices();
                practiceAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void generateCopybook() {
        // 获取使用次数最少的练习内容
        ChinesePractice practice = practiceDao.getLeastPracticedChinesePractice();
        if (practice == null) {
            Toast.makeText(this, "没有可用的练习内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 生成字帖
        Intent intent = new Intent(this, ChineseCopybookActivity.class);
        intent.putExtra("practice", practice);
        startActivity(intent);
    }

    private class PracticeAdapter extends ArrayAdapter<ChinesePractice> {

        public PracticeAdapter(List<ChinesePractice> practices) {
            super(ChinesePracticeManagementActivity.this, 0, practices);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_practice, parent, false);
            }

            final ChinesePractice practice = getItem(position);

            TextView tvType = convertView.findViewById(R.id.tvType);
            TextView tvContent = convertView.findViewById(R.id.tvContent);
            TextView tvPracticeCount = convertView.findViewById(R.id.tvPracticeCount);
            Button btnDelete = convertView.findViewById(R.id.btnDelete);

            tvType.setText(practice.getType());
            tvContent.setText(practice.getContent());
            tvPracticeCount.setText("练习次数：" + practice.getPracticeCount());

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    practiceDao.deleteChinesePractice(practice.getId());
                    loadPractices();
                    practiceAdapter.notifyDataSetChanged();
                    Toast.makeText(ChinesePracticeManagementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public static void start(android.content.Context context) {
        android.content.Intent intent = new android.content.Intent(context, ChinesePracticeManagementActivity.class);
        context.startActivity(intent);
    }
}
