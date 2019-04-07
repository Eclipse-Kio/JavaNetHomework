package javanet.c04.entity;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * 问题实体类
 */
public class Question implements ToJSONString {
    private int id;
    /**
     * 问题内容
     */
    private String questionContent;
    /**
     * 问题分值
     */
    private int Score;
    /**
     * 选项列表
     */
    private String[] choices;
    /**
     * 正确答案的下标
     */
    private int answerIndex;

    public Question() {
    }

    public Question(int id, String questionContent, int score, String[] choices, int answerIndex) {
        this.id = id;
        this.questionContent = questionContent;
        Score = score;
        this.choices = choices;
        this.answerIndex = answerIndex;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionContent='" + questionContent + '\'' +
                ", Score=" + Score +
                ", choices=" + Arrays.toString(choices) +
                ", answerIndex=" + answerIndex +
                '}';
    }

    @Override
    public String toJsonString() {
        JSONObject result = new JSONObject(this);
        //屏蔽正确答案
        result.remove("answerIndex");
        return result.toString();
    }
}
