package javanet.c04.entity;

import org.json.JSONObject;

public class Record implements ToJSONString {

    private int totalNum = 0;
    private int correctNum = 0;
    private int totalScore = 0;
    private int userScore = 0;

    public void answer(int currentScore, boolean right) {

        if (currentScore <= 0)
            throw new ArithmeticException("题目的分数不得小于等于0");

        this.totalNum++;
        this.totalScore += currentScore;
        if (right) {
            this.correctNum++;
            this.userScore += currentScore;
        }
    }

    public int getTotalNum() {
        return totalNum;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getUserScore() {
        return userScore;
    }

    @Override
    public String toJsonString() {
        JSONObject result = new JSONObject();
        result.put("totalNum", this.totalNum);
        result.put("correctNum", this.correctNum);
        result.put("totalScore", this.totalScore);
        result.put("userScore", this.userScore);
        return result.toString();
    }
}
