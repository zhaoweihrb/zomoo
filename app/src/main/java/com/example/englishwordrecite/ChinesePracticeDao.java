package com.example.englishwordrecite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChinesePracticeDao {

    private SQLiteDatabase db;

    public ChinesePracticeDao(SQLiteDatabase db) {
        this.db = db;
    }

    // 添加语文练习内容
    public long addChinesePractice(ChinesePractice practice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRACTICE_CONTENT, practice.getContent());
        values.put(DatabaseHelper.COLUMN_PRACTICE_TYPE, practice.getType());
        values.put(DatabaseHelper.COLUMN_PRACTICE_COUNT, practice.getPracticeCount());
        values.put(DatabaseHelper.COLUMN_PRACTICE_LAST_POSITION, practice.getLastPosition());
        if (practice.getLastPracticeTime() != null) {
            values.put(DatabaseHelper.COLUMN_PRACTICE_LAST_PRACTICE_TIME, practice.getLastPracticeTime().getTime());
        }

        return db.insert(DatabaseHelper.TABLE_CHINESE_PRACTICES, null, values);
    }

    // 更新语文练习内容
    public int updateChinesePractice(ChinesePractice practice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRACTICE_CONTENT, practice.getContent());
        values.put(DatabaseHelper.COLUMN_PRACTICE_TYPE, practice.getType());
        values.put(DatabaseHelper.COLUMN_PRACTICE_COUNT, practice.getPracticeCount());
        values.put(DatabaseHelper.COLUMN_PRACTICE_LAST_POSITION, practice.getLastPosition());
        if (practice.getLastPracticeTime() != null) {
            values.put(DatabaseHelper.COLUMN_PRACTICE_LAST_PRACTICE_TIME, practice.getLastPracticeTime().getTime());
        }

        return db.update(DatabaseHelper.TABLE_CHINESE_PRACTICES, values, 
                DatabaseHelper.COLUMN_PRACTICE_ID + " = ?", 
                new String[]{String.valueOf(practice.getId())});
    }

    // 删除语文练习内容
    public int deleteChinesePractice(int id) {
        return db.delete(DatabaseHelper.TABLE_CHINESE_PRACTICES, 
                DatabaseHelper.COLUMN_PRACTICE_ID + " = ?", 
                new String[]{String.valueOf(id)});
    }

    // 根据ID获取语文练习内容
    public ChinesePractice getChinesePracticeById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_CHINESE_PRACTICES, null, 
                DatabaseHelper.COLUMN_PRACTICE_ID + " = ?", 
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Date lastPracticeTime = null;
            
            long lastPracticeTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_LAST_PRACTICE_TIME));
            if (lastPracticeTimeLong > 0) {
                lastPracticeTime = new Date(lastPracticeTimeLong);
            }
            
            ChinesePractice practice = new ChinesePractice(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_LAST_POSITION)),
                    lastPracticeTime
            );
            cursor.close();
            return practice;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // 获取所有语文练习内容
    public List<ChinesePractice> getAllChinesePractices() {
        List<ChinesePractice> practices = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CHINESE_PRACTICES, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Date lastPracticeTime = null;
                
                long lastPracticeTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_LAST_PRACTICE_TIME));
                if (lastPracticeTimeLong > 0) {
                    lastPracticeTime = new Date(lastPracticeTimeLong);
                }
                
                ChinesePractice practice = new ChinesePractice(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_LAST_POSITION)),
                        lastPracticeTime
                );
                practices.add(practice);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return practices;
    }

    // 获取使用次数最少的语文练习内容
    public ChinesePractice getLeastPracticedChinesePractice() {
        Cursor cursor = db.query(DatabaseHelper.TABLE_CHINESE_PRACTICES, null, null, null, null, null, 
                DatabaseHelper.COLUMN_PRACTICE_COUNT + " ASC, " + DatabaseHelper.COLUMN_PRACTICE_LAST_PRACTICE_TIME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            Date lastPracticeTime = null;
            
            long lastPracticeTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_LAST_PRACTICE_TIME));
            if (lastPracticeTimeLong > 0) {
                lastPracticeTime = new Date(lastPracticeTimeLong);
            }
            
            ChinesePractice practice = new ChinesePractice(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRACTICE_LAST_POSITION)),
                    lastPracticeTime
            );
            cursor.close();
            return practice;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // 批量添加语文练习内容
    public void batchAddChinesePractices(List<ChinesePractice> practices) {
        db.beginTransaction();
        try {
            for (ChinesePractice practice : practices) {
                addChinesePractice(practice);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
