package com.example.englishwordrecite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WordDao {

    private SQLiteDatabase db;

    public WordDao(SQLiteDatabase db) {
        this.db = db;
    }

    // 添加单词
    public long addWord(Word word) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WORD_ENGLISH, word.getEnglish());
        values.put(DatabaseHelper.COLUMN_WORD_CHINESE, word.getChinese());
        values.put(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH, word.getPartOfSpeech());
        values.put(DatabaseHelper.COLUMN_WORD_PHONETIC, word.getPhonetic());
        values.put(DatabaseHelper.COLUMN_WORD_DIFFICULTY, word.getDifficulty());
        values.put(DatabaseHelper.COLUMN_WORD_TEST_COUNT, word.getTestCount());
        values.put(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT, word.getCorrectCount());
        values.put(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT, word.getConsecutiveSuccessCount());
        values.put(DatabaseHelper.COLUMN_WORD_ACCURACY, word.getAccuracy());
        if (word.getFirstTestTime() != null) {
            values.put(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME, word.getFirstTestTime().getTime());
        }
        if (word.getFirstSuccessTime() != null) {
            values.put(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME, word.getFirstSuccessTime().getTime());
        }
        if (word.getLastTestTime() != null) {
            values.put(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME, word.getLastTestTime().getTime());
        }

        return db.insert(DatabaseHelper.TABLE_WORDS, null, values);
    }

    // 更新单词
    public int updateWord(Word word) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WORD_ENGLISH, word.getEnglish());
        values.put(DatabaseHelper.COLUMN_WORD_CHINESE, word.getChinese());
        values.put(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH, word.getPartOfSpeech());
        values.put(DatabaseHelper.COLUMN_WORD_PHONETIC, word.getPhonetic());
        values.put(DatabaseHelper.COLUMN_WORD_DIFFICULTY, word.getDifficulty());
        values.put(DatabaseHelper.COLUMN_WORD_TEST_COUNT, word.getTestCount());
        values.put(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT, word.getCorrectCount());
        values.put(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT, word.getConsecutiveSuccessCount());
        values.put(DatabaseHelper.COLUMN_WORD_ACCURACY, word.getAccuracy());
        if (word.getFirstTestTime() != null) {
            values.put(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME, word.getFirstTestTime().getTime());
        }
        if (word.getFirstSuccessTime() != null) {
            values.put(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME, word.getFirstSuccessTime().getTime());
        }
        if (word.getLastTestTime() != null) {
            values.put(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME, word.getLastTestTime().getTime());
        }

        return db.update(DatabaseHelper.TABLE_WORDS, values, 
                DatabaseHelper.COLUMN_WORD_ID + " = ?", 
                new String[]{String.valueOf(word.getId())});
    }

    // 删除单词
    public int deleteWord(int id) {
        return db.delete(DatabaseHelper.TABLE_WORDS, 
                DatabaseHelper.COLUMN_WORD_ID + " = ?", 
                new String[]{String.valueOf(id)});
    }

    // 根据ID获取单词
    public Word getWordById(int id) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS, null, 
                DatabaseHelper.COLUMN_WORD_ID + " = ?", 
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Date firstTestTime = null;
            Date firstSuccessTime = null;
            Date lastTestTime = null;
            
            long firstTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME));
            if (firstTestTimeLong > 0) {
                firstTestTime = new Date(firstTestTimeLong);
            }
            
            long firstSuccessTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME));
            if (firstSuccessTimeLong > 0) {
                firstSuccessTime = new Date(firstSuccessTimeLong);
            }
            
            long lastTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME));
            if (lastTestTimeLong > 0) {
                lastTestTime = new Date(lastTestTimeLong);
            }
            
            Word word = new Word(
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CHINESE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PHONETIC)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_DIFFICULTY)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_TEST_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT)),
                    cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ACCURACY)),
                    firstTestTime,
                    firstSuccessTime,
                    lastTestTime
            );
            cursor.close();
            return word;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // 获取所有单词
    public List<Word> getAllWords() {
        List<Word> words = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Date firstTestTime = null;
                Date firstSuccessTime = null;
                Date lastTestTime = null;
                
                long firstTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME));
                if (firstTestTimeLong > 0) {
                    firstTestTime = new Date(firstTestTimeLong);
                }
                
                long firstSuccessTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME));
                if (firstSuccessTimeLong > 0) {
                    firstSuccessTime = new Date(firstSuccessTimeLong);
                }
                
                long lastTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME));
                if (lastTestTimeLong > 0) {
                    lastTestTime = new Date(lastTestTimeLong);
                }
                
                Word word = new Word(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CHINESE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PHONETIC)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_DIFFICULTY)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_TEST_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ACCURACY)),
                        firstTestTime,
                        firstSuccessTime,
                        lastTestTime
                );
                words.add(word);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return words;
    }

    // 根据难度获取单词
    public List<Word> getWordsByDifficulty(int difficulty) {
        List<Word> words = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS, null, 
                DatabaseHelper.COLUMN_WORD_DIFFICULTY + " = ?", 
                new String[]{String.valueOf(difficulty)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Date firstTestTime = null;
                Date firstSuccessTime = null;
                Date lastTestTime = null;
                
                long firstTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME));
                if (firstTestTimeLong > 0) {
                    firstTestTime = new Date(firstTestTimeLong);
                }
                
                long firstSuccessTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME));
                if (firstSuccessTimeLong > 0) {
                    firstSuccessTime = new Date(firstSuccessTimeLong);
                }
                
                long lastTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME));
                if (lastTestTimeLong > 0) {
                    lastTestTime = new Date(lastTestTimeLong);
                }
                
                Word word = new Word(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CHINESE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PHONETIC)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_DIFFICULTY)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_TEST_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ACCURACY)),
                        firstTestTime,
                        firstSuccessTime,
                        lastTestTime
                );
                words.add(word);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return words;
    }

    // 搜索单词
    public List<Word> searchWords(String keyword) {
        List<Word> words = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS, null, 
                DatabaseHelper.COLUMN_WORD_ENGLISH + " LIKE ? OR " + DatabaseHelper.COLUMN_WORD_CHINESE + " LIKE ?", 
                new String[]{"%" + keyword + "%", "%" + keyword + "%"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Date firstTestTime = null;
                Date firstSuccessTime = null;
                Date lastTestTime = null;
                
                long firstTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME));
                if (firstTestTimeLong > 0) {
                    firstTestTime = new Date(firstTestTimeLong);
                }
                
                long firstSuccessTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME));
                if (firstSuccessTimeLong > 0) {
                    firstSuccessTime = new Date(firstSuccessTimeLong);
                }
                
                long lastTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME));
                if (lastTestTimeLong > 0) {
                    lastTestTime = new Date(lastTestTimeLong);
                }
                
                Word word = new Word(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CHINESE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PHONETIC)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_DIFFICULTY)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_TEST_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ACCURACY)),
                        firstTestTime,
                        firstSuccessTime,
                        lastTestTime
                );
                words.add(word);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return words;
    }

    // 获取单词总数
    public int getWordCount() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_WORDS, null);
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

    // 批量添加单词
    public void batchAddWords(List<Word> words) {
        db.beginTransaction();
        try {
            for (Word word : words) {
                addWord(word);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // 批量更新单词
    public void batchUpdateWords(List<Word> words) {
        db.beginTransaction();
        try {
            for (Word word : words) {
                updateWord(word);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // 获取未毕业的单词（连续成功次数<7）
    public List<Word> getNonGraduatedWords() {
        List<Word> words = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS, null, 
                DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT + " < ?", 
                new String[]{String.valueOf(7)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Date firstTestTime = null;
                Date firstSuccessTime = null;
                Date lastTestTime = null;
                
                long firstTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME));
                if (firstTestTimeLong > 0) {
                    firstTestTime = new Date(firstTestTimeLong);
                }
                
                long firstSuccessTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME));
                if (firstSuccessTimeLong > 0) {
                    firstSuccessTime = new Date(firstSuccessTimeLong);
                }
                
                long lastTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME));
                if (lastTestTimeLong > 0) {
                    lastTestTime = new Date(lastTestTimeLong);
                }
                
                Word word = new Word(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CHINESE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PHONETIC)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_DIFFICULTY)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_TEST_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ACCURACY)),
                        firstTestTime,
                        firstSuccessTime,
                        lastTestTime
                );
                words.add(word);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return words;
    }

    // 获取新考单词（连续成功次数=0）
    public List<Word> getNewTestWords() {
        List<Word> words = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS, null, 
                DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT + " = ?", 
                new String[]{String.valueOf(0)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Date firstTestTime = null;
                Date firstSuccessTime = null;
                Date lastTestTime = null;
                
                long firstTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME));
                if (firstTestTimeLong > 0) {
                    firstTestTime = new Date(firstTestTimeLong);
                }
                
                long firstSuccessTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME));
                if (firstSuccessTimeLong > 0) {
                    firstSuccessTime = new Date(firstSuccessTimeLong);
                }
                
                long lastTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME));
                if (lastTestTimeLong > 0) {
                    lastTestTime = new Date(lastTestTimeLong);
                }
                
                Word word = new Word(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CHINESE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PHONETIC)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_DIFFICULTY)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_TEST_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ACCURACY)),
                        firstTestTime,
                        firstSuccessTime,
                        lastTestTime
                );
                words.add(word);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return words;
    }

    // 获取复习单词（连续成功次数≥1且<7）
    public List<Word> getReviewWords() {
        List<Word> words = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_WORDS, null, 
                DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT + " >= ? AND " + DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT + " < ?", 
                new String[]{String.valueOf(1), String.valueOf(7)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Date firstTestTime = null;
                Date firstSuccessTime = null;
                Date lastTestTime = null;
                
                long firstTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_TEST_TIME));
                if (firstTestTimeLong > 0) {
                    firstTestTime = new Date(firstTestTimeLong);
                }
                
                long firstSuccessTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_FIRST_SUCCESS_TIME));
                if (firstSuccessTimeLong > 0) {
                    firstSuccessTime = new Date(firstSuccessTimeLong);
                }
                
                long lastTestTimeLong = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_LAST_TEST_TIME));
                if (lastTestTimeLong > 0) {
                    lastTestTime = new Date(lastTestTimeLong);
                }
                
                Word word = new Word(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ENGLISH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CHINESE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PART_OF_SPEECH)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_PHONETIC)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_DIFFICULTY)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_TEST_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CORRECT_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORD_ACCURACY)),
                        firstTestTime,
                        firstSuccessTime,
                        lastTestTime
                );
                words.add(word);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        return words;
    }
}
