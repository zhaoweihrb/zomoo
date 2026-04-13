package com.example.englishwordrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView tvScore;
    private LinearLayout llResults;
    private DatabaseHelper dbHelper;
    private WordDao wordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = findViewById(R.id.tvScore);
        llResults = findViewById(R.id.llResults);

        // 初始化数据库
        dbHelper = new DatabaseHelper(this);
        wordDao = new WordDao(dbHelper.getWritableDatabase());

        // 获取识别的文字和单词列表
        String recognizedText = getIntent().getStringExtra("recognizedText");
        Word[] words = (Word[]) getIntent().getSerializableExtra("words");

        if (recognizedText != null && words != null) {
            // 处理识别结果
            processResult(recognizedText, words);
        } else {
            // 没有单词列表时的处理
            tvScore.setText("错误：没有单词列表");
            Toast.makeText(this, "请先创建考试并选择试卷", Toast.LENGTH_SHORT).show();
        }
    }

    private void processResult(String recognizedText, Word[] words) {
        // 简单的评分逻辑（实际应用中需要更复杂的文本匹配）
        int correctCount = 0;
        int totalCount = words.length;

        for (Word word : words) {
            boolean isCorrect = recognizedText.toLowerCase().contains(word.getEnglish().toLowerCase());
            if (isCorrect) {
                correctCount++;
            }
            
            // 记录考试结果
            word.recordTestResult(isCorrect);
            // 更新数据库
            wordDao.updateWord(word);
        }

        // 显示得分
        int score = (correctCount * 100) / totalCount;
        tvScore.setText(getString(R.string.score) + score + "分");

        // 显示详细结果
        for (Word word : words) {
            boolean isCorrect = recognizedText.toLowerCase().contains(word.getEnglish().toLowerCase());

            LinearLayout resultLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            resultLayout.setLayoutParams(params);
            resultLayout.setOrientation(LinearLayout.VERTICAL);

            TextView tvChinese = new TextView(this);
            tvChinese.setText(word.getChinese());
            tvChinese.setTextSize(16);

            TextView tvAnswer = new TextView(this);
            tvAnswer.setText(getString(R.string.correct_answers) + word.getEnglish());
            tvAnswer.setTextSize(14);

            TextView tvResult = new TextView(this);
            tvResult.setText(isCorrect ? "正确" : "错误");
            tvResult.setTextSize(14);
            tvResult.setTextColor(isCorrect ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));

            resultLayout.addView(tvChinese);
            resultLayout.addView(tvAnswer);
            resultLayout.addView(tvResult);

            llResults.addView(resultLayout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
