package com.example.englishwordrecite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ExamManagementActivity extends AppCompatActivity {

    private Button btnCreateExam;
    private ListView lvExams;
    private ExamAdapter examAdapter;
    private List<Exam> examList;
    private DatabaseHelper dbHelper;
    private ExamDao examDao;
    private WordDao wordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_management);

        btnCreateExam = findViewById(R.id.btnCreateExam);
        lvExams = findViewById(R.id.lvExams);

        dbHelper = new DatabaseHelper(this);
        examDao = new ExamDao(dbHelper.getWritableDatabase());
        wordDao = new WordDao(dbHelper.getWritableDatabase());

        loadExams();

        examAdapter = new ExamAdapter(examList);
        lvExams.setAdapter(examAdapter);

        btnCreateExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateExamDialog();
            }
        });

        lvExams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exam exam = examList.get(position);
                // 可以添加查看考试详情的功能
            }
        });
    }

    private void loadExams() {
        examList = examDao.getAllExams();
    }

    private void showCreateExamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_create_exam, null);
        builder.setView(view);

        final EditText etExamName = view.findViewById(R.id.etExamName);
        final EditText etNewTestPages = view.findViewById(R.id.etNewTestPages);
        final EditText etReviewPages = view.findViewById(R.id.etReviewPages);
        final Spinner spinnerDifficulty = view.findViewById(R.id.spinnerDifficulty);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnCreate = view.findViewById(R.id.btnCreate);

        // 设置默认值为1页新考、1页复习
        etNewTestPages.setText("1");
        etReviewPages.setText("1");

        // 设置难度选择器
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5"});
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);

        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examName = etExamName.getText().toString().trim();
                String newTestPagesStr = etNewTestPages.getText().toString().trim();
                String reviewPagesStr = etReviewPages.getText().toString().trim();
                int difficulty = spinnerDifficulty.getSelectedItemPosition() + 1;

                if (examName.isEmpty() || newTestPagesStr.isEmpty() || reviewPagesStr.isEmpty()) {
                    Toast.makeText(ExamManagementActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                int newTestPages = Integer.parseInt(newTestPagesStr);
                int reviewPages = Integer.parseInt(reviewPagesStr);

                // 随机选择单词（根据规则：只选择未毕业的单词）
                List<Word> nonGraduatedWords = wordDao.getNonGraduatedWords();
                if (nonGraduatedWords.isEmpty()) {
                    Toast.makeText(ExamManagementActivity.this, "所有单词都已毕业", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 分为新考和复习两部分
                List<Word> newTestWords = wordDao.getNewTestWords();
                List<Word> reviewWords = wordDao.getReviewWords();

                // 计算新考和复习的数量（每页25个）
                int newTestCount = newTestPages * 25;
                int reviewCount = reviewPages * 25;

                // 调整数量以适应实际可用的单词
                if (newTestWords.size() < newTestCount) {
                    newTestCount = newTestWords.size();
                }
                if (reviewWords.size() < reviewCount) {
                    reviewCount = reviewWords.size();
                }

                // 随机选择新考单词
                List<Word> selectedNewTestWords = new ArrayList<>();
                Random random = new Random();
                boolean[] selectedNew = new boolean[newTestWords.size()];

                for (int i = 0; i < newTestCount; i++) {
                    int index;
                    do {
                        index = random.nextInt(newTestWords.size());
                    } while (selectedNew[index]);
                    selectedNew[index] = true;
                    selectedNewTestWords.add(newTestWords.get(index));
                }

                // 随机选择复习单词
                List<Word> selectedReviewWords = new ArrayList<>();
                boolean[] selectedReview = new boolean[reviewWords.size()];

                for (int i = 0; i < reviewCount; i++) {
                    int index;
                    do {
                        index = random.nextInt(reviewWords.size());
                    } while (selectedReview[index]);
                    selectedReview[index] = true;
                    selectedReviewWords.add(reviewWords.get(index));
                }

                // 合并单词列表（新考在前，复习在后）
                List<Word> selectedWords = new ArrayList<>();
                selectedWords.addAll(selectedNewTestWords);
                selectedWords.addAll(selectedReviewWords);

                // 生成单词ID字符串
                StringBuilder wordIdsBuilder = new StringBuilder();
                for (Word word : selectedWords) {
                    wordIdsBuilder.append(word.getId()).append(",");
                }
                String wordIds = wordIdsBuilder.toString();
                if (!wordIds.isEmpty()) {
                    wordIds = wordIds.substring(0, wordIds.length() - 1);
                }

                // 创建考试
                Exam exam = new Exam(examName, selectedWords.size(), difficulty, wordIds);
                examDao.addExam(exam);

                Toast.makeText(ExamManagementActivity.this, "考试创建成功", Toast.LENGTH_SHORT).show();
                loadExams();
                examAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private class ExamAdapter extends ArrayAdapter<Exam> {

        public ExamAdapter(List<Exam> exams) {
            super(ExamManagementActivity.this, 0, exams);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_exam, parent, false);
            }

            final Exam exam = getItem(position);

            TextView tvExamName = convertView.findViewById(R.id.tvExamName);
            TextView tvWordCount = convertView.findViewById(R.id.tvWordCount);
            TextView tvDifficulty = convertView.findViewById(R.id.tvDifficulty);
            TextView tvCreateTime = convertView.findViewById(R.id.tvCreateTime);
            Button btnViewPaper = convertView.findViewById(R.id.btnViewPaper);
            Button btnDownloadPaper = convertView.findViewById(R.id.btnDownloadPaper);
            Button btnScanPaper = convertView.findViewById(R.id.btnScanPaper);

            tvExamName.setText(exam.getName());
            tvWordCount.setText("单词：" + exam.getWordCount() + "个");
            tvDifficulty.setText("难度：" + exam.getDifficultyLevel());
            tvCreateTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(exam.getCreateTime()));

            btnViewPaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 查看试卷
                    Intent intent = new Intent(ExamManagementActivity.this, PaperActivity.class);
                    // 需要根据exam.getWordIds()获取单词列表
                    String[] wordIdArray = exam.getWordIds().split(",");
                    List<Word> words = new ArrayList<>();
                    for (String idStr : wordIdArray) {
                        int id = Integer.parseInt(idStr);
                        Word word = wordDao.getWordById(id);
                        if (word != null) {
                            words.add(word);
                        }
                    }
                    intent.putExtra("words", words.toArray(new Word[0]));
                    startActivity(intent);
                }
            });

            btnDownloadPaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 下载试卷
                    downloadPaper(exam);
                }
            });

            btnScanPaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 扫描批卷
                    String[] wordIdArray = exam.getWordIds().split(",");
                    List<Word> words = new ArrayList<>();
                    for (String idStr : wordIdArray) {
                        int id = Integer.parseInt(idStr);
                        Word word = wordDao.getWordById(id);
                        if (word != null) {
                            words.add(word);
                        }
                    }
                    startScanActivity(words);
                }
            });

            return convertView;
        }
    }

    // 下载试卷为文本文件
    private void downloadPaper(Exam exam) {
        try {
            // 根据单词ID获取单词列表
            String[] wordIdArray = exam.getWordIds().split(",");
            List<Word> words = new ArrayList<>();
            for (String idStr : wordIdArray) {
                int id = Integer.parseInt(idStr);
                Word word = wordDao.getWordById(id);
                if (word != null) {
                    words.add(word);
                }
            }

            if (words.isEmpty()) {
                Toast.makeText(this, "试卷中没有单词", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建下载目录
            File downloadDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "exam_papers");
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }

            // 创建文本文件
            String fileName = exam.getName() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
            File paperFile = new File(downloadDir, fileName);

            // 写入试卷内容
            FileWriter writer = new FileWriter(paperFile);
            writer.write("英语单词测试卷\n");
            writer.write("考试名称：" + exam.getName() + "\n");
            writer.write("单词数量：" + exam.getWordCount() + "个\n");
            writer.write("难度等级：" + exam.getDifficultyLevel() + "\n");
            writer.write("创建时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(exam.getCreateTime()) + "\n\n");
            writer.write("====================================\n\n");
            writer.write(String.format("%-5s %-15s %-8s %-15s %-20s %-20s\n", "序号", "中文释义", "词性", "音标", "单词", "正确答案"));
            writer.write("------------------------------------------------------------------------------------------------------------\n\n");

            Random random = new Random();
            for (int i = 0; i < words.size(); i++) {
                Word word = words.get(i);
                
                // 复习卷随机隐藏中文或音标
                String chinese = word.getChinese();
                String phonetic = word.getPhonetic() != null ? word.getPhonetic() : "";
                
                if (word.isReview()) {
                    if (random.nextBoolean()) {
                        chinese = "_______";
                    }
                    if (random.nextBoolean()) {
                        phonetic = "_______";
                    }
                }
                
                writer.write(String.format("%-5d %-15s %-8s %-15s %-20s %-20s\n\n", 
                        (i + 1), 
                        chinese, 
                        word.getPartOfSpeech() != null ? word.getPartOfSpeech() : "", 
                        phonetic, 
                        "____________________", 
                        word.getEnglish()));
            }

            writer.close();

            // 显示下载成功信息
            Toast.makeText(this, "试卷已下载到：" + paperFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

            // 打开文件所在目录
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(downloadDir), "resource/folder");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "下载失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public static void start(android.content.Context context) {
        android.content.Intent intent = new android.content.Intent(context, ExamManagementActivity.class);
        context.startActivity(intent);
    }

    // 启动扫描活动并传递单词列表
    private void startScanActivity(List<Word> words) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra("words", words.toArray(new Word[0]));
        startActivity(intent);
    }
}
