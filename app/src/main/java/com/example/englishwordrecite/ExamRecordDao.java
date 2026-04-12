package com.example.englishwordrecite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamRecordDao {

    private SQLiteDatabase db;

    public ExamRecordDao(SQLiteDatabase db) {
        this.db = db;
    }

    // 添加考试记录
    public long addExamRecord(ExamRecord record) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RECORD_EXAM_ID, record.getExamId());
        values.put(DatabaseHelper.COLUMN_RECORD_TEST_TIME, record.getTestTime().getTime());
        values.put(DatabaseHelper.COLUMN_RECORD_TOTAL_SCORE, record.getTotalScore());
        values.put(DatabaseHelper.COLUMN_RECORD_CORRECT_COUNT, record.getCorrectCount());
        values.put(DatabaseHelper.COLUMN_RECORD_TOTAL_COUNT, record.getTotalCount());
        values.put(DatabaseHelper.COLUMN_RECORD_ANSWERS, record.getAnswers());
        values.put(DatabaseHelper.COLUMN_RECORD_CORRECT_ANSWERS, record.getCorrectAnswers());

        return db.insert(DatabaseHelper.TABLE_EXAM_RECORDS, null, values);
    }

    // 更新考试记录
    public int updateExamRecord(ExamRecord record) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RECORD_EXAM_ID, record.getExamId());
        values.put(DatabaseHelper.COLUMN_RECORD_TEST_TIME, record.getTestTime().getTime());
        values.put(DatabaseHelper.COLUMN_RECORD_TOTAL_SCORE, record.getTotalScore());
        values.put(DatabaseHelper.COLUMN_RECORD_CORRECT_COUNT, record.getCorrectCount());
        values.put(DatabaseHelper.COLUMN_RECORD_TOTAL_COUNT, record.getTotalCount());
        values.put(DatabaseHelper.COLUMN_RECORD_ANSWERS, record.getAnswers());
        values.put(DatabaseHelper.COLUMN_RECORD_CORRECT_ANSWERS, record.getCorrectAnswers());

        return db.update(DatabaseHelper.TABLE_EXAM_RECORDS, values, 
                DatabaseHelper.COLUMN_RECORD_ID + " = ?", 
                new String[]{String.valueOf(record.getId())});
    }

    // 删除考试记录
    public int deleteExamRecord(int id) {
        return db.delete(DatabaseHelper.TABLE_EXAM_RECORDS, 
                DatabaseHelper.COLUMN_RECORD_ID + " = ?", 
                new String[]{String.valueOf(id)});
    }

    // 根据ID获取考试记录
    public ExamRecord getExamRecordById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_EXAM_RECORDS, null, 
                DatabaseHelper.COLUMN_RECORD_ID + " = ?", 
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ExamRecord record = new ExamRecord(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_ID)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_EXAM_ID)),
                    new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TEST_TIME))),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TOTAL_SCORE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_CORRECT_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TOTAL_COUNT)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_ANSWERS)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_CORRECT_ANSWERS))
            );
            cursor.close();
            return record;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // 根据考试ID获取考试记录
    public List<ExamRecord> getExamRecordsByExamId(int examId) {
        List<ExamRecord> records = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_EXAM_RECORDS, null, 
                DatabaseHelper.COLUMN_RECORD_EXAM_ID + " = ?", 
                new String[]{String.valueOf(examId)}, null, null, 
                DatabaseHelper.COLUMN_RECORD_TEST_TIME + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ExamRecord record = new ExamRecord(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_ID)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_EXAM_ID)),
                        new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TEST_TIME))),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TOTAL_SCORE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TOTAL_COUNT)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_ANSWERS)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_CORRECT_ANSWERS))
                );
                records.add(record);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return records;
    }

    // 获取所有考试记录
    public List<ExamRecord> getAllExamRecords() {
        List<ExamRecord> records = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_EXAM_RECORDS, null, null, null, null, null, 
                DatabaseHelper.COLUMN_RECORD_TEST_TIME + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ExamRecord record = new ExamRecord(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_ID)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_EXAM_ID)),
                        new Date(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TEST_TIME))),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TOTAL_SCORE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_TOTAL_COUNT)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_ANSWERS)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RECORD_CORRECT_ANSWERS))
                );
                records.add(record);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return records;
    }

    // 获取考试记录总数
    public int getExamRecordCount() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_EXAM_RECORDS, null);
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
