package javanet.c04.entity;

public class Response implements ToJSONString {

    /**
     * 返回数据的类型，0代表获取题目(默认)，1代表获取统计
     */
    private int type;
    private ToJSONString content;

    public Response() {

    }

    public Response(int type, ToJSONString content) {
        //如果响应的内容既不是题目，也不是答题记录，报错
        if (!(content instanceof Question) && !(content instanceof Record))
            throw new UnsupportedOperationException("不支持" + content.getClass().getName());
        this.type = type;
        this.content = content;
        if (type != 1)
            this.type = 0;
    }

    public ToJSONString getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(ToJSONString content) {
        this.content = content;
    }

    @Override
    public String toJsonString() {
        return content.toJsonString();
    }
}
