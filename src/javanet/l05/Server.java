package javanet.l05;


import javanet.l05.entity.Line;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    /**
     * 公交车线路原始数据
     */
    private ArrayList<Line> lines;

    /**
     * 正在运营的公交车数据
     */
    private HashMap<Integer, ArrayList<String>> codeToBus = new HashMap<>();
    private HashMap<String, Double> busToPosition = new HashMap<>();

    private class ClientServerThread extends Thread {
        private ServerSocket server;
        private boolean flag = true;

        public ClientServerThread(ServerSocket server) {
            this.server = server;
        }

        @Override
        public void run() {
            try {
                Socket socket = server.accept();
                new ClientServerThread(server).start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                System.out.println("accepted");
                while (flag) {
                    String line;
                    try {
                        line = reader.readLine();
                    } catch (SocketException e) {
                        System.out.println("客户端断开连接");
                        reader.close();
                        writer.close();
                        socket.close();
                        break;
                    }

                    JSONObject requestObject = new JSONObject(line);
                    int code = requestObject.getInt("code");//获取请求的公交车号

                    Line result = null;
                    for (Line item : lines) {
                        if (item.getCode() == code) {
                            result = item;
                            break;
                        }
                    }
                    JSONObject resultObject = new JSONObject();
                    if (result == null) {//没有该公交车
                        resultObject.put("code", 1);
                        resultObject.put("msg", "找不到该公交车路线!");
                    } else {
                        resultObject.put("code", 0);
                        resultObject.put("msg", new JSONObject(result));
                        JSONArray jsonArray = new JSONArray();
                        synchronized (Server.this){
                            ArrayList<String> busIDs = codeToBus.get(code);
                            for (String s : busIDs) {
                                jsonArray.put(busToPosition.get(s));
                            }
                        }
                        resultObject.put("busPosition",jsonArray);
                    }
                    writer.println(resultObject.toString());
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void kill() {
            this.flag = false;
        }

    }

    private class BusServerThread extends Thread {
        private DatagramSocket socket;

        public BusServerThread(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {//自动关闭socket
                byte[] buff = new byte[1024];
                DatagramPacket received = new DatagramPacket(buff, buff.length);
                socket.receive(received);
                new BusServerThread(socket).start();//启动新的线程
                String data = new String(buff);
                JSONObject jsonObject = new JSONObject(data);
                synchronized (Server.this) {
                    int code = jsonObject.getInt("code");
                    String id = jsonObject.getString("id");
                    double position = jsonObject.getDouble("position");

                    if (!codeToBus.containsKey(code))//若还没有这种路线，添加
                        codeToBus.put(code, new ArrayList<>());
                    ArrayList<String> ids = codeToBus.get(code);
                    int index = -1;
                    for (int i = 0; i < ids.size(); i++) {
                        if (ids.get(i).equals(id)) {
                            index = i;
                            break;
                        }
                    }
                    if (index == -1)//没有则添加
                        ids.add(id);
                    busToPosition.put(id, position);
                }
                //System.out.println(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public Server() throws IOException {
        //从JSON中加载原始数据
        lines = loadStopData(getFile());
        ServerSocket server = new ServerSocket(1314);
        new ClientServerThread(server).start();
        new BusServerThread(new DatagramSocket(1315)).start();
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public static ArrayList<Line> loadStopData(String rawData) {
        ArrayList<Line> lines = new ArrayList<>();
        JSONArray data = new JSONArray(rawData);
        for (int i = 0; i < data.length(); i++) {
            JSONObject item = data.getJSONObject(i);
            Line line = new Line();
            line.setCode(item.getInt("code"));
            ArrayList<String> stops = new ArrayList<>();
            JSONArray stopsData = item.getJSONArray("stops");
            for (int j = 0; j < stopsData.length(); j++) {
                stops.add(stopsData.getString(j));
            }
            line.setStops(stops);
            lines.add(line);
        }
        return lines;
    }

    public static String getFile() throws IOException {
        StringBuilder result = new StringBuilder();
        FileReader reader = new FileReader("files/l05/lines.json");
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null)
            result.append(line);
        return result.toString();
    }
}
