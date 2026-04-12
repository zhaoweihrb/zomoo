package com.example.englishwordrecite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamDao {

    private SQLiteDatabase db;

    public ExamDao(SQLiteDatabase db) {
        this.db = db;
    }

    // 添加考试
    public long addExam(Exam exam) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EXAM_NAME, exam.getName());
        values.put(DatabaseHelper.COLUMN_EXAM_WORD_COUNT, exam.getWordCount());
        values.put(DatabaseHelper.COLUMN_EXAM_DIFFICULTY, exam.getDifficultyLevel());
        values.put(DatabaseHelper.COLUMN_EXAM_CREATE_TIME, exam.getCreateTime().getTime());
        values.put(DatabaseHelper.COLUMN_EXAM_WORD_IDS, exam.getWordIds());

        return db.insert(DatabaseHelper.TABLE_EXAMS, null, values);
    }

    // 更新考试
    public int updateExam(Exam exam) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EXAM_NAME, exam.getName());
        values.put(DatabaseHelper.COLUMN_EXAM_WORD_COUNT, exam.getWordCount());
        values.put(DatabaseHelper.COLUMN_EXAM_DIFFICULTY, exam.getDifficultyLevel());
        values.put(DatabaseHelper.COLUMN_EXAM_CREATE_TIME, exam.getCreateTime().getTime());
        values.put(DatabaseHelper.COLUMN_EXAM_WORD_IDS, exam.getWordIds());

        return db.update(DatabaseHelper.TABLE_EXAMS, values, 
                DatabaseHelper.COLUMN_EXAM_ID + " = ?", 
                new String[]{String.valueOf(exam.getId())});
    }

    // 删除考试
    public int deleteExam(int id) {
        return db.delete(DatabaseHelper.TABLE_EXAMS, 
                DatabaseHelper.COLUMN_EXAM_ID + " = ?", 
                new String[]{String.valueOf(id)});
    }

    // 根据ID获取考试
    public Exam getExamById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_EXAMS, null, 
                DatabaseHelper.COLUMN_EXAM_ID + " = ?", 
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Exam exam = new Exam(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_NAME)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_WORD_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_DIFFICULTY)),
                    new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_CREATE_TIME))),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_WORD_IDS))
            );
            cursor.close();
            return exam;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // 获取所有考试
    public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_EXAMS, null, null, null, null, null, 
                DatabaseHelper.COLUMN_EXAM_CREATE_TIME + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exam exam = new Exam(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_NAME)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_WORD_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_DIFFICULTY)),
                        new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_CREATE_TIME))),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXAM_WORD_IDS))
                );
                exams.add(exam);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return exams;
    }

    // 获取考试总数
    public int getExamCount() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_EXAMS, null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        if (cursor != null) {
            cursor.close();
        }
        return 0;
    }
}
