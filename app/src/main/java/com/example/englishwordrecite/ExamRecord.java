package com.example.englishwordrecite;

import java.io.Serializable;
import java.util.Date;

public class ExamRecord implements Serializable {
    private int id;
    private int examId;
    private Date testTime;
    private int totalScore;
    private int correctCount;
    private int totalCount;
    private String answers;
    private String correctAnswers;

    public ExamRecord() {
    }

    public ExamRecord(int examId, int totalScore, int correctCount, int totalCount, String answers, String correctAnswers) {
        this.examId = examId;
        this.testTime = new Date();
        this.totalScore = totalScore;
        this.correctCount = correctCount;
        this.totalCount = totalCount;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }

    public ExamRecord(int id, int examId, Date testTime, int totalScore, int correctCount, int totalCount, String answers, String correctAnswers) {
        this.id = id;
        this.examId = examId;
        this.testTime = testTime;
        this.totalScore = totalScore;
        this.correctCount = correctCount;
        this.totalCount = totalCount;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
