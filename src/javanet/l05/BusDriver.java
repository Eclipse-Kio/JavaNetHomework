package javanet.l05;

import javanet.l05.entity.Bus;
import javanet.l05.entity.Line;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 只用于启动公交车
 * 目前有的路线 1 233 9 18 25
 */
public class BusDriver {
    /**
     * 公交车线路原始数据
     */
    private static ArrayList<Line> lines;

    public static void main(String[] args) throws IOException, IllegalAccessException {
        lines = Server.loadStopData(Server.getFile());
        start(1);
        start(233);
        start(9);
        start(18);
        start(25);
    }

    private static void start(int code) throws IllegalAccessException {
        int num = 1 + (int) (Math.random() * 4);//最多四辆公交车
        int index = -1;
        for (int i = 0; i < lines.size(); i++)
            if (lines.get(i).getCode() == code)
                index = i;
        if (index == -1)
            throw new IllegalAccessException("没有该线路");
        for (int i = 0; i < num; i++) {
            new Bus(lines.get(index)).start();//启动这些公交车
        }
    }
}
