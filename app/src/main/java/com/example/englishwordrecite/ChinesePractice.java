package com.example.englishwordrecite;

import java.io.Serializable;
import java.util.Date;

public class ChinesePractice implements Serializable {
    private int id;
    private String content;
    private String type; // 类型：古诗、成语等
    private int practiceCount; // 练习次数
    private int lastPosition; // 上次练习的截断位置
    private Date lastPracticeTime; // 上次练习时间

    public ChinesePractice() {
    }

    public ChinesePractice(String content, String type) {
        this.content = content;
        this.type = type;
        this.practiceCount = 0;
        this.lastPosition = 0;
    }

    public ChinesePractice(int id, String content, String type, int practiceCount, int lastPosition, Date lastPracticeTime) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.practiceCount = practiceCount;
        this.lastPosition = lastPosition;
        this.lastPracticeTime = lastPracticeTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPracticeCount() {
        return practiceCount;
    }

    public void setPracticeCount(int practiceCount) {
        this.practiceCount = practiceCount;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public Date getLastPracticeTime() {
        return lastPracticeTime;
    }

    public void setLastPracticeTime(Date lastPracticeTime) {
        this.lastPracticeTime = lastPracticeTime;
    }

    // 增加练习次数
    public void incrementPracticeCount() {
        this.practiceCount++;
    }

    // 清理内容，移除标点符号和空格
    public String getCleanContent() {
        return content.replaceAll("[\p{P}\s]", "");
    }
}
