package javanet.c04.entity;

import com.sun.istack.internal.Nullable;
import org.json.JSONObject;

/**
 * 代表一次请求的内容实体类
 */
public class Request implements ToJSONString {
    private String JSessionID;
    private byte answer;
    private byte id;
    /**
     * 请求的类型，0代表请求题目(默认)，1代表请求统计,2代表答题
     */
    private int type;

    public Request(String JSessionID, int type, @Nullable byte id, @Nullable byte answer) {
        this.JSessionID = JSessionID;
        this.type = type;
        this.id = id;
        this.answer = answer;
        if (type != 1 && type != 2)
            this.type = 0;
    }

    public Request() {
        this.JSessionID = "Undefined";
    }

    @Override
    public String toJsonString() {
        JSONObject result = new JSONObject();
        result.put("JSessionID", this.JSessionID);
        result.put("type", this.type);
        if (this.type == 2) {
            result.put("answer", this.answer);
            result.put("id", this.id);
        }

        return result.toString();
    }
}
