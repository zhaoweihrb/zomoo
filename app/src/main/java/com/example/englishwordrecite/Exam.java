package com.example.englishwordrecite;

import java.io.Serializable;
import java.util.Date;

public class Exam implements Serializable {
    private int id;
    private String name;
    private int wordCount;
    private int difficultyLevel;
    private Date createTime;
    private String wordIds;

    public Exam() {
    }

    public Exam(String name, int wordCount, int difficultyLevel, String wordIds) {
        this.name = name;
        this.wordCount = wordCount;
        this.difficultyLevel = difficultyLevel;
        this.createTime = new Date();
        this.wordIds = wordIds;
    }

    public Exam(int id, String name, int wordCount, int difficultyLevel, Date createTime, String wordIds) {
        this.id = id;
        this.name = name;
        this.wordCount = wordCount;
        this.difficultyLevel = difficultyLevel;
        this.createTime = createTime;
        this.wordIds = wordIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWordIds() {
        return wordIds;
    }

    public void setWordIds(String wordIds) {
        this.wordIds = wordIds;
    }
}
