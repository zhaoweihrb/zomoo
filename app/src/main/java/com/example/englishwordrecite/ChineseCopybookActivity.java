package com.example.englishwordrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

public class ChineseCopybookActivity extends AppCompatActivity {

    private LinearLayout llCopybook;
    private DatabaseHelper dbHelper;
    private ChinesePracticeDao practiceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_copybook);

        llCopybook = findViewById(R.id.llCopybook);

        // 初始化数据库
        dbHelper = new DatabaseHelper(this);
        practiceDao = new ChinesePracticeDao(dbHelper.getWritableDatabase());

        // 获取传递的练习内容
        ChinesePractice practice = (ChinesePractice) getIntent().getSerializableExtra("practice");

        if (practice != null) {
            // 生成字帖
            generateCopybook(practice);

            // 更新练习记录
            practice.incrementPracticeCount();
            practice.setLastPracticeTime(new Date());
            practiceDao.updateChinesePractice(practice);
        }
    }

    private void generateCopybook(ChinesePractice practice) {
        // 获取清理后的内容（移除标点符号和空格）
        String cleanContent = practice.getCleanContent();
        int lastPosition = practice.getLastPosition();

        // 每页A4纸，12字每行，假设每页20行
        int wordsPerLine = 12;
        int linesPerPage = 20;
        int totalWordsPerPage = wordsPerLine * linesPerPage;

        // 计算本次生成的内容范围
        int startPosition = lastPosition;
        int endPosition = Math.min(startPosition + totalWordsPerPage, cleanContent.length());

        // 更新截断位置
        if (endPosition < cleanContent.length()) {
            practice.setLastPosition(endPosition);
            practiceDao.updateChinesePractice(practice);
        } else {
            // 内容结束，重置位置
            practice.setLastPosition(0);
            practiceDao.updateChinesePractice(practice);
        }

        // 生成字帖内容
        String copybookContent = cleanContent.substring(startPosition, endPosition);

        // 创建米字格布局
        for (int i = 0; i < linesPerPage; i++) {
            LinearLayout lineLayout = new LinearLayout(this);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    100
            );
            lineParams.setMargins(0, 0, 0, 10);
            lineLayout.setLayoutParams(lineParams);
            lineLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < wordsPerLine; j++) {
                int index = i * wordsPerLine + j;
                String character = " ";
                if (index < copybookContent.length()) {
                    character = String.valueOf(copybookContent.charAt(index));
                }

                // 创建米字格
                ChineseCharacterView characterView = new ChineseCharacterView(this, character);
                LinearLayout.LayoutParams characterParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                );
                characterView.setLayoutParams(characterParams);
                lineLayout.addView(characterView);
            }

            llCopybook.addView(lineLayout);
        }
    }

    // 自定义米字格视图
    private class ChineseCharacterView extends View {

        private String character;
        private Paint gridPaint;
        private Paint textPaint;

        public ChineseCharacterView(android.content.Context context, String character) {
            super(context);
            this.character = character;

            // 初始化画笔
            gridPaint = new Paint();
            gridPaint.setColor(Color.BLACK);
            gridPaint.setStrokeWidth(1);

            textPaint = new Paint();
            textPaint.setColor(Color.parseColor("#FFCCCC")); // 浅红色
            textPaint.setTextSize(40);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // 设置背景色为绿色
            canvas.drawColor(Color.parseColor("#E6F7EE"));

            // 绘制米字格
            int width = getWidth();
            int height = getHeight();

            // 绘制外框
            canvas.drawRect(0, 0, width, height, gridPaint);

            // 绘制十字线
            canvas.drawLine(0, height / 2, width, height / 2, gridPaint);
            canvas.drawLine(width / 2, 0, width / 2, height, gridPaint);

            // 绘制对角线
            canvas.drawLine(0, 0, width, height, gridPaint);
            canvas.drawLine(width, 0, 0, height, gridPaint);

            // 绘制文字
            if (!character.equals(" ")) {
                canvas.drawText(character, width / 2, height / 2 + 15, textPaint);
            }
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
