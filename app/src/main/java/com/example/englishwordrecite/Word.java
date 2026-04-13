package com.example.englishwordrecite;

import java.io.Serializable;
import java.util.Date;

public class Word implements Serializable {
    private int id;
    private String english; // 英文原文
    private String chinese; // 中文释义
    private String partOfSpeech; // 词性
    private String phonetic; // 音标
    private int difficulty; // 1-5，1最简单，5最难
    private int testCount; // 总考试次数
    private int correctCount; // 正确次数
    private int consecutiveSuccessCount; // 连续成功次数
    private double accuracy; // 正确率
    private Date firstTestTime; // 第一次考试时间
    private Date firstSuccessTime; // 第一次考试成功时间
    private Date lastTestTime; // 最近考试时间

    public Word() {
    }

    public Word(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
        this.partOfSpeech = "";
        this.phonetic = "";
        this.difficulty = 1;
        this.testCount = 0;
        this.correctCount = 0;
        this.consecutiveSuccessCount = 0;
        this.accuracy = 0.0;
    }

    public Word(String english, String chinese, String partOfSpeech, String phonetic) {
        this.english = english;
        this.chinese = chinese;
        this.partOfSpeech = partOfSpeech;
        this.phonetic = phonetic;
        this.difficulty = 1;
        this.testCount = 0;
        this.correctCount = 0;
        this.consecutiveSuccessCount = 0;
        this.accuracy = 0.0;
    }

    public Word(int id, String english, String chinese, String partOfSpeech, String phonetic, int difficulty, 
                int testCount, int correctCount, int consecutiveSuccessCount, double accuracy, 
                Date firstTestTime, Date firstSuccessTime, Date lastTestTime) {
        this.id = id;
        this.english = english;
        this.chinese = chinese;
        this.partOfSpeech = partOfSpeech;
        this.phonetic = phonetic;
        this.difficulty = difficulty;
        this.testCount = testCount;
        this.correctCount = correctCount;
        this.consecutiveSuccessCount = consecutiveSuccessCount;
        this.accuracy = accuracy;
        this.firstTestTime = firstTestTime;
        this.firstSuccessTime = firstSuccessTime;
        this.lastTestTime = lastTestTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getConsecutiveSuccessCount() {
        return consecutiveSuccessCount;
    }

    public void setConsecutiveSuccessCount(int consecutiveSuccessCount) {
        this.consecutiveSuccessCount = consecutiveSuccessCount;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public Date getFirstTestTime() {
        return firstTestTime;
    }

    public void setFirstTestTime(Date firstTestTime) {
        this.firstTestTime = firstTestTime;
    }

    public Date getFirstSuccessTime() {
        return firstSuccessTime;
    }

    public void setFirstSuccessTime(Date firstSuccessTime) {
        this.firstSuccessTime = firstSuccessTime;
    }

    public Date getLastTestTime() {
        return lastTestTime;
    }

    public void setLastTestTime(Date lastTestTime) {
        this.lastTestTime = lastTestTime;
    }

    // 更新正确率
    public void updateAccuracy() {
        if (testCount > 0) {
            this.accuracy = (double) correctCount / testCount;
        } else {
            this.accuracy = 0.0;
        }
    }

    // 记录考试结果
    public void recordTestResult(boolean correct) {
        Date now = new Date();
        
        // 更新考试时间
        if (firstTestTime == null) {
            firstTestTime = now;
        }
        lastTestTime = now;
        
        // 更新考试次数
        testCount++;
        
        // 更新连续成功次数
        if (correct) {
            consecutiveSuccessCount++;
            if (firstSuccessTime == null) {
                firstSuccessTime = now;
            }
            correctCount++;
        } else {
            consecutiveSuccessCount = 0;
        }
        
        // 更新正确率
        updateAccuracy();
    }

    // 检查是否已毕业（连续成功次数≥7）
    public boolean isGraduated() {
        return consecutiveSuccessCount >= 7;
    }

    // 检查是否为新考（连续成功次数=0）
    public boolean isNewTest() {
        return consecutiveSuccessCount == 0;
    }

    // 检查是否为复习（连续成功次数≥1且<7）
    public boolean isReview() {
        return consecutiveSuccessCount >= 1 && consecutiveSuccessCount < 7;
    }
}
