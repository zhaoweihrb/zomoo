package com.example.englishwordrecite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "english_word_recite.db";
    private static final int DATABASE_VERSION = 1;

    // 单词表
    public static final String TABLE_WORDS = "words";
    public static final String COLUMN_WORD_ID = "id";
    public static final String COLUMN_WORD_ENGLISH = "english";
    public static final String COLUMN_WORD_CHINESE = "chinese";
    public static final String COLUMN_WORD_PART_OF_SPEECH = "part_of_speech";
    public static final String COLUMN_WORD_PHONETIC = "phonetic";
    public static final String COLUMN_WORD_DIFFICULTY = "difficulty";
    public static final String COLUMN_WORD_TEST_COUNT = "test_count";
    public static final String COLUMN_WORD_CORRECT_COUNT = "correct_count";
    public static final String COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT = "consecutive_success_count";
    public static final String COLUMN_WORD_ACCURACY = "accuracy";
    public static final String COLUMN_WORD_FIRST_TEST_TIME = "first_test_time";
    public static final String COLUMN_WORD_FIRST_SUCCESS_TIME = "first_success_time";
    public static final String COLUMN_WORD_LAST_TEST_TIME = "last_test_time";

    // 考试表
    public static final String TABLE_EXAMS = "exams";
    public static final String COLUMN_EXAM_ID = "id";
    public static final String COLUMN_EXAM_NAME = "name";
    public static final String COLUMN_EXAM_WORD_COUNT = "word_count";
    public static final String COLUMN_EXAM_DIFFICULTY = "difficulty_level";
    public static final String COLUMN_EXAM_CREATE_TIME = "create_time";
    public static final String COLUMN_EXAM_WORD_IDS = "word_ids";

    // 考试记录表
    public static final String TABLE_EXAM_RECORDS = "exam_records";
    public static final String COLUMN_RECORD_ID = "id";
    public static final String COLUMN_RECORD_EXAM_ID = "exam_id";
    public static final String COLUMN_RECORD_TEST_TIME = "test_time";
    public static final String COLUMN_RECORD_TOTAL_SCORE = "total_score";
    public static final String COLUMN_RECORD_CORRECT_COUNT = "correct_count";
    public static final String COLUMN_RECORD_TOTAL_COUNT = "total_count";
    public static final String COLUMN_RECORD_ANSWERS = "answers";
    public static final String COLUMN_RECORD_CORRECT_ANSWERS = "correct_answers";

    // 语文练习表
    public static final String TABLE_CHINESE_PRACTICES = "chinese_practices";
    public static final String COLUMN_PRACTICE_ID = "id";
    public static final String COLUMN_PRACTICE_CONTENT = "content";
    public static final String COLUMN_PRACTICE_TYPE = "type";
    public static final String COLUMN_PRACTICE_COUNT = "practice_count";
    public static final String COLUMN_PRACTICE_LAST_POSITION = "last_position";
    public static final String COLUMN_PRACTICE_LAST_PRACTICE_TIME = "last_practice_time";

    // 创建单词表的SQL语句
    private static final String CREATE_TABLE_WORDS = "CREATE TABLE " + TABLE_WORDS + " (" +
            COLUMN_WORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORD_ENGLISH + " TEXT UNIQUE NOT NULL, " +
            COLUMN_WORD_CHINESE + " TEXT NOT NULL, " +
            COLUMN_WORD_PART_OF_SPEECH + " TEXT, " +
            COLUMN_WORD_PHONETIC + " TEXT, " +
            COLUMN_WORD_DIFFICULTY + " INTEGER DEFAULT 1, " +
            COLUMN_WORD_TEST_COUNT + " INTEGER DEFAULT 0, " +
            COLUMN_WORD_CORRECT_COUNT + " INTEGER DEFAULT 0, " +
            COLUMN_WORD_CONSECUTIVE_SUCCESS_COUNT + " INTEGER DEFAULT 0, " +
            COLUMN_WORD_ACCURACY + " REAL DEFAULT 0.0, " +
            COLUMN_WORD_FIRST_TEST_TIME + " INTEGER, " +
            COLUMN_WORD_FIRST_SUCCESS_TIME + " INTEGER, " +
            COLUMN_WORD_LAST_TEST_TIME + " INTEGER" +
            ");";

    // 创建考试表的SQL语句
    private static final String CREATE_TABLE_EXAMS = "CREATE TABLE " + TABLE_EXAMS + " (" +
            COLUMN_EXAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_EXAM_NAME + " TEXT NOT NULL, " +
            COLUMN_EXAM_WORD_COUNT + " INTEGER NOT NULL, " +
            COLUMN_EXAM_DIFFICULTY + " INTEGER NOT NULL, " +
            COLUMN_EXAM_CREATE_TIME + " INTEGER NOT NULL, " +
            COLUMN_EXAM_WORD_IDS + " TEXT NOT NULL" +
            ");";

    // 创建考试记录表的SQL语句
    private static final String CREATE_TABLE_EXAM_RECORDS = "CREATE TABLE " + TABLE_EXAM_RECORDS + " (" +
            COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_RECORD_EXAM_ID + " INTEGER NOT NULL, " +
            COLUMN_RECORD_TEST_TIME + " INTEGER NOT NULL, " +
            COLUMN_RECORD_TOTAL_SCORE + " INTEGER NOT NULL, " +
            COLUMN_RECORD_CORRECT_COUNT + " INTEGER NOT NULL, " +
            COLUMN_RECORD_TOTAL_COUNT + " INTEGER NOT NULL, " +
            COLUMN_RECORD_ANSWERS + " TEXT NOT NULL, " +
            COLUMN_RECORD_CORRECT_ANSWERS + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_RECORD_EXAM_ID + ") REFERENCES " + TABLE_EXAMS + "(" + COLUMN_EXAM_ID + ")" +
            ");";

    // 创建语文练习表的SQL语句
    private static final String CREATE_TABLE_CHINESE_PRACTICES = "CREATE TABLE " + TABLE_CHINESE_PRACTICES + " (" +
            COLUMN_PRACTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PRACTICE_CONTENT + " TEXT NOT NULL, " +
            COLUMN_PRACTICE_TYPE + " TEXT NOT NULL, " +
            COLUMN_PRACTICE_COUNT + " INTEGER DEFAULT 0, " +
            COLUMN_PRACTICE_LAST_POSITION + " INTEGER DEFAULT 0, " +
            COLUMN_PRACTICE_LAST_PRACTICE_TIME + " INTEGER" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORDS);
        db.execSQL(CREATE_TABLE_EXAMS);
        db.execSQL(CREATE_TABLE_EXAM_RECORDS);
        db.execSQL(CREATE_TABLE_CHINESE_PRACTICES);

        // 初始化一些基础单词
        initBasicWords(db);
        // 初始化一些基础语文练习内容
        initBasicChinesePractices(db);
    }

    // 初始化基础语文练习内容
    private void initBasicChinesePractices(SQLiteDatabase db) {
        String[] basicPractices = {
                "静夜思,古诗,床前明月光，疑是地上霜。举头望明月，低头思故乡。",
                "春晓,古诗,春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
                "望庐山瀑布,古诗,日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。",
                "锄禾,古诗,锄禾日当午，汗滴禾下土。谁知盘中餐，粒粒皆辛苦。",
                "咏鹅,古诗,鹅鹅鹅，曲项向天歌。白毛浮绿水，红掌拨清波。",
                "成语,成语,一心一意,二话不说,三心二意,四面八方,五光十色,六神无主,七上八下,八仙过海,九牛一毛,十全十美"
        };

        for (String practice : basicPractices) {
            String[] parts = practice.split(",");
            if (parts.length == 3) {
                String name = parts[0];
                String type = parts[1];
                String content = parts[2];
                db.execSQL("INSERT INTO " + TABLE_CHINESE_PRACTICES + " (" +
                        COLUMN_PRACTICE_CONTENT + ", " +
                        COLUMN_PRACTICE_TYPE + ") VALUES (?, ?)",
                        new Object[]{content, type});
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级逻辑
        if (oldVersion < newVersion) {
            // 可以在这里添加数据库升级的代码
        }
    }

    // 初始化基础单词
    private void initBasicWords(SQLiteDatabase db) {
        String[] basicWords = {
                "apple,苹果", "banana,香蕉", "cat,猫", "dog,狗", "egg,鸡蛋",
                "fish,鱼", "girl,女孩", "house,房子", "ice,冰", "juice,果汁",
                "kite,风筝", "lion,狮子", "milk,牛奶", "notebook,笔记本", "orange,橙子",
                "pen,钢笔", "queen,女王", "rabbit,兔子", "sun,太阳", "tree,树",
                "umbrella,雨伞", "violin,小提琴", "water,水", "xylophone,木琴", "yogurt,酸奶", "zebra,斑马"
        };

        for (String word : basicWords) {
            String[] parts = word.split(",");
            if (parts.length == 2) {
                String english = parts[0];
                String chinese = parts[1];
                db.execSQL("INSERT INTO " + TABLE_WORDS + " (" +
                        COLUMN_WORD_ENGLISH + ", " +
                        COLUMN_WORD_CHINESE + ", " +
                        COLUMN_WORD_PART_OF_SPEECH + ", " +
                        COLUMN_WORD_PHONETIC + ", " +
                        COLUMN_WORD_DIFFICULTY + ") VALUES (?, ?, ?, ?, ?)",
                        new Object[]{english, chinese, "", "", 1});
            }
        }
    }
}
