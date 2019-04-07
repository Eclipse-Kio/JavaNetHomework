package javanet.c04;

import javanet.c04.entity.Question;
import javanet.c04.entity.Record;
import javanet.c04.entity.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 1.	基于UDP协议编写一个考试程序，其要求如下：
 * 每个客户端连接上服务器之后，服务器将随机向客户端发送考题，每次一题；
 * 每答完一题客户端可以选择继续答题或结束答题；
 * 结束答题后，客户端可以查看自己的成绩；
 * 客户端基于Java FX实现，服务端基于多线程技术实现，考试题存储在文件当中。
 * <code>
 * 思路：客户端第一次发送请求时可随机生成一个UUID（类似于SessionID）,
 * 每次发送请求时应当附带SessionID以便服务器辨别不同客户端
 * 发送请求还应当携带一个标识，0代表获取题目（默认），1代表获取得分
 * </code>
 */
public class Exercise3S {

    /**
     * 系统题库
     */
    private static ArrayList<Question> questions;
    /**
     * 需要维护的sessionId与答题记录的键值对
     */
    private static HashMap<String, Record> clientRecordMap;

    private class ResponseThread extends Thread {
        private byte[] data;
        private InetAddress ip;
        private int port;

        ResponseThread(byte[] data, InetAddress ip, int port) {
            super();
            this.data = data;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            super.run();
            String JSONFyData = new String(data);
            System.out.println(JSONFyData);
            JSONObject request = new JSONObject(JSONFyData);
            Response response;
            if (request.getInt("type") == 0) {//获取题目
                Question question = questions.get((int) (Math.random() * questions.size()));
                response = new Response(0, question);
                send(response);
            } else if (request.getInt("type") == 1) {//获取答题记录
                Record record;
                if (clientRecordMap.containsKey(request.getString("JSessionID"))) {
                    record = clientRecordMap.get(request.getString("JSessionID"));
                } else
                    record = new Record();
                response = new Response(1, record);
                send(response);
            } else if (request.getInt("type") == 2) {//增加答题记录
                String JSessionId = request.getString("JSessionID");
                if (!clientRecordMap.containsKey(JSessionId)) {
                    Record record = new Record();
                    clientRecordMap.put(JSessionId, record);
                }

                int userAnswer = request.getInt("answer");
                int id = request.getInt("id");

                clientRecordMap.get(JSessionId).answer(questions.get(id).getScore(),
                        questions.get(id).getAnswerIndex() == userAnswer);
            }
        }

        private void send(Response response) {
            try {
                DatagramSocket responseSocket = new DatagramSocket();
                byte[] responseData = response.toJsonString().getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, ip, port);
                responseSocket.send(responsePacket);
                responseSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        //初始化题库
        String content = readFile("files/c04/questions.json");
        questions = getQuestionFromString(content);
        clientRecordMap = new HashMap<>();
        new Exercise3S();
    }

    private Exercise3S() {
        try {
            DatagramSocket server = new DatagramSocket(1314);
            System.out.println("服务器已开启");

            while (true) {
                byte[] requestData = new byte[1024];
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length);
                server.receive(requestPacket);
                System.out.println("Received");
                //交给响应线程处理
                new Exercise3S.ResponseThread(
                        requestPacket.getData(),
                        requestPacket.getAddress(),
                        requestPacket.getPort()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中读取文件的内容
     *
     * @param path 要读取的文件路径
     * @return 文件中的所有内容
     */
    private static String readFile(String path) throws IOException {
        StringBuilder result = new StringBuilder();
        String line;
        FileInputStream fis = new FileInputStream(path);

        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    /**
     * 根据JSON字符串自动返回一个Question列表
     *
     * @param sources 源JSON字符串
     * @return 该字符串代表的Question列表
     */
    private static ArrayList<Question> getQuestionFromString(String sources) {
        ArrayList<Question> result = null;
        JSONArray array = new JSONArray(sources);
        if (array.length() > 0) {
            result = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                Question question = new Question();
                result.add(question);
                question.setQuestionContent(item.getString("questionContent"));
                question.setScore(item.getInt("score"));
                question.setAnswerIndex(item.getInt("answerIndex"));
                JSONArray answers = item.getJSONArray("choices");
                String[] choices = new String[4];
                choices[0] = answers.getString(0);
                choices[1] = answers.getString(1);
                choices[2] = answers.getString(2);
                choices[3] = answers.getString(3);
                question.setChoices(choices);
                question.setId(item.getInt("id"));
            }
        }
        return result;
    }
}
