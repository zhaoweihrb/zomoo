package com.example.englishwordrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class PaperActivity extends AppCompatActivity {

    private LinearLayout llQuestions;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        llQuestions = findViewById(R.id.llQuestions);

        // 获取传递的单词列表
        Word[] words = (Word[]) getIntent().getSerializableExtra("words");

        if (words != null) {
            // 添加表头
            LinearLayout headerLayout = new LinearLayout(this);
            LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            headerParams.setMargins(0, 0, 0, 24);
            headerLayout.setLayoutParams(headerParams);
            headerLayout.setOrientation(LinearLayout.HORIZONTAL);

            // 表头 - 序号
            TextView tvHeaderNumber = new TextView(this);
            LinearLayout.LayoutParams headerNumberParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            headerNumberParams.setMargins(0, 0, 16, 0);
            tvHeaderNumber.setLayoutParams(headerNumberParams);
            tvHeaderNumber.setText("序号");
            tvHeaderNumber.setTextSize(16);
            tvHeaderNumber.setTypeface(null, android.graphics.Typeface.BOLD);

            // 表头 - 中文释义
            TextView tvHeaderChinese = new TextView(this);
            LinearLayout.LayoutParams headerChineseParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            tvHeaderChinese.setLayoutParams(headerChineseParams);
            tvHeaderChinese.setText("中文释义");
            tvHeaderChinese.setTextSize(16);
            tvHeaderChinese.setTypeface(null, android.graphics.Typeface.BOLD);

            // 表头 - 词性
            TextView tvHeaderPartOfSpeech = new TextView(this);
            LinearLayout.LayoutParams headerPartOfSpeechParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            tvHeaderPartOfSpeech.setLayoutParams(headerPartOfSpeechParams);
            tvHeaderPartOfSpeech.setText("词性");
            tvHeaderPartOfSpeech.setTextSize(16);
            tvHeaderPartOfSpeech.setTypeface(null, android.graphics.Typeface.BOLD);

            // 表头 - 音标
            TextView tvHeaderPhonetic = new TextView(this);
            LinearLayout.LayoutParams headerPhoneticParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            tvHeaderPhonetic.setLayoutParams(headerPhoneticParams);
            tvHeaderPhonetic.setText("音标");
            tvHeaderPhonetic.setTextSize(16);
            tvHeaderPhonetic.setTypeface(null, android.graphics.Typeface.BOLD);

            // 表头 - 单词
            TextView tvHeaderEnglish = new TextView(this);
            LinearLayout.LayoutParams headerEnglishParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    2
            );
            tvHeaderEnglish.setLayoutParams(headerEnglishParams);
            tvHeaderEnglish.setText("单词");
            tvHeaderEnglish.setTextSize(16);
            tvHeaderEnglish.setTypeface(null, android.graphics.Typeface.BOLD);

            // 表头 - 正确答案
            TextView tvHeaderAnswer = new TextView(this);
            LinearLayout.LayoutParams headerAnswerParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    2
            );
            tvHeaderAnswer.setLayoutParams(headerAnswerParams);
            tvHeaderAnswer.setText("正确答案");
            tvHeaderAnswer.setTextSize(16);
            tvHeaderAnswer.setTypeface(null, android.graphics.Typeface.BOLD);

            headerLayout.addView(tvHeaderNumber);
            headerLayout.addView(tvHeaderChinese);
            headerLayout.addView(tvHeaderPartOfSpeech);
            headerLayout.addView(tvHeaderPhonetic);
            headerLayout.addView(tvHeaderEnglish);
            headerLayout.addView(tvHeaderAnswer);

            llQuestions.addView(headerLayout);

            // 显示单词题目
            for (int i = 0; i < words.length; i++) {
                Word word = words[i];
                
                // 创建题目行
                LinearLayout questionLayout = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                questionLayout.setLayoutParams(params);
                questionLayout.setOrientation(LinearLayout.HORIZONTAL);

                // 题目序号
                TextView tvNumber = new TextView(this);
                LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                numberParams.setMargins(0, 0, 16, 0);
                tvNumber.setLayoutParams(numberParams);
                tvNumber.setText((i + 1) + ".");
                tvNumber.setTextSize(14);
                tvNumber.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));

                // 中文意思
                TextView tvChinese = new TextView(this);
                LinearLayout.LayoutParams chineseParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                );
                tvChinese.setLayoutParams(chineseParams);
                // 复习卷随机隐藏中文
                if (word.isNewTest() || !word.isReview() || !random.nextBoolean()) {
                    tvChinese.setText(word.getChinese());
                } else {
                    tvChinese.setText("_______");
                }
                tvChinese.setTextSize(14);
                tvChinese.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));

                // 词性
                TextView tvPartOfSpeech = new TextView(this);
                LinearLayout.LayoutParams partOfSpeechParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                );
                tvPartOfSpeech.setLayoutParams(partOfSpeechParams);
                String partOfSpeech = word.getPartOfSpeech();
                tvPartOfSpeech.setText(partOfSpeech != null ? partOfSpeech : "");
                tvPartOfSpeech.setTextSize(14);
                tvPartOfSpeech.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));

                // 音标
                TextView tvPhonetic = new TextView(this);
                LinearLayout.LayoutParams phoneticParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                );
                tvPhonetic.setLayoutParams(phoneticParams);
                String phonetic = word.getPhonetic();
                // 复习卷随机隐藏音标
                if (word.isNewTest() || !word.isReview() || !random.nextBoolean()) {
                    tvPhonetic.setText(phonetic != null ? phonetic : "");
                } else {
                    tvPhonetic.setText("_______");
                }
                tvPhonetic.setTextSize(14);
                tvPhonetic.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));

                // 横线（用于填写答案）
                TextView tvLine = new TextView(this);
                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        2
                );
                tvLine.setLayoutParams(lineParams);
                tvLine.setText("________________________");
                tvLine.setTextSize(14);
                tvLine.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));

                // 正确答案
                TextView tvAnswer = new TextView(this);
                LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        2
                );
                tvAnswer.setLayoutParams(answerParams);
                tvAnswer.setText(word.getEnglish());
                tvAnswer.setTextSize(14);
                tvAnswer.setTypeface(android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL));

                questionLayout.addView(tvNumber);
                questionLayout.addView(tvChinese);
                questionLayout.addView(tvPartOfSpeech);
                questionLayout.addView(tvPhonetic);
                questionLayout.addView(tvLine);
                questionLayout.addView(tvAnswer);

                llQuestions.addView(questionLayout);

                // 每25行添加分页标记
                if ((i + 1) % 25 == 0 && i + 1 < words.length) {
                    TextView pageBreak = new TextView(this);
                    LinearLayout.LayoutParams pageBreakParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    pageBreakParams.setMargins(0, 24, 0, 24);
                    pageBreak.setLayoutParams(pageBreakParams);
                    pageBreak.setText("- 第 " + ((i + 1) / 25) + " 页结束 -\n\n");
                    pageBreak.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
                    llQuestions.addView(pageBreak);
                }
            }
        }
    }
}
