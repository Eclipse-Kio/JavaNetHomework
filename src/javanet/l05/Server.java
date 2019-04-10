package javanet.l05;


import javanet.l05.entity.Line;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Server {
    /**
     * 公交车线路原始数据
     */
    private ArrayList<Line> lines = new ArrayList<>();

    public Server() throws IOException {
        //从JSON中加载原始数据
        loadStopData(getFile());

    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    private void loadStopData(String rawData) {
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
            System.out.println(item);
            lines.add(line);
        }
    }

    public String getFile() throws IOException {
        StringBuilder result = new StringBuilder();
        FileReader reader = new FileReader("files/l05/lines.json");
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null)
            result.append(line);
        return result.toString();
    }
}
