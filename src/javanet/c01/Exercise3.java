package javanet.c01;

import javanet.c01.entity.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 已知学生成绩信息存储在随机存取文件score.data当中，每条记录的格式为：
 * 	学号：8字节；姓名：20字节；成绩：1字节
 *
 * 1.	编写一个Java程序，该程序能够实现学生成绩的增加、删除、修改与查询；
 */
public class Exercise3 {

    private final static String FILE_PATH = "files/c01/score.data";
    private static Scanner input;

    static {
        if (!new File(FILE_PATH).exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(FILE_PATH, false);
                fos.write("学号                      姓名                      成绩\r\n".getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        showAll();
        input = new Scanner(System.in);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("1-add 2-delete 3-modify 4-search 5-exit");
        String option = input.next();

        switch (option) {
            case "1":
                add();
                break;
            case "2":
                delete();
                break;
            case "3":
                modify();
                break;
            case "4":
                search();
                break;
            case "5":
                input.close();
                return;
            default:
                System.out.println("UnKnow Command");
        }

        main(args);
    }

    private static void search() {
        System.out.println("please input text to found it");
        String match = input.next();

        try {
            //一行行读取文件
            FileInputStream fis = new FileInputStream(FILE_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            int num = 0;
            System.out.println("##########SEARCH#RESULT############");
            while ((line = reader.readLine()) != null) {
                if (line.contains(match)) {
                    System.out.println(line);
                    num++;
                }
            }
            System.out.println("total " + num + "record(s)");
            System.out.println("###################################");
            reader.close();
            fis.close();
        } catch (Exception e) {
            System.out.println("error");
        }

    }

    private static void showAll() {
        File file = new File(FILE_PATH);
        System.out.println("-----------FILE——CONTENT------------");
        try {
            FileInputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No Data File Found!");
        } catch (IOException e) {
            System.out.println("Error During Read File!");
        }
        System.out.println("------------------------------------");
    }

    private static void add() {
        Student student = inputStudent();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(FILE_PATH, true);
            fos.write(student.getId().getBytes());
            fos.write("   ".getBytes());
            fos.write(student.getName().getBytes());
            fos.write("   ".getBytes());
            fos.write(student.getGrade().getBytes());
            fos.write("\r\n".getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ignored) {

        }

    }

    private static void delete() {
        System.out.println("input line number to delete(from 1)");
        String choose = input.next();
        try {
            int i = Integer.parseInt(choose);

            String line;
            ArrayList<String> content = new ArrayList<>();

            FileInputStream is = new FileInputStream(FILE_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
            content.remove(i);
            override(content, reader);

        } catch (Exception e) {
            System.out.println("error!");
        }
    }

    private static void modify() {
        System.out.println("input line number to modify(from 1)");
        String choose = input.next();
        try {
            String line;
            int i = Integer.parseInt(choose);
            FileInputStream is = new FileInputStream(FILE_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> content = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
            content.remove(i);
            Student student = inputStudent();
            content.add(i, student.getId() + "   " + student.getName() + "   " + student.getGrade());
            override(content, reader);

        } catch (Exception e) {
            System.out.println("error!");
        }
    }

    private static void override(ArrayList<String> content, BufferedReader reader) throws IOException {
        reader.close();
        FileOutputStream fos = new FileOutputStream(FILE_PATH, false);
        for (String item : content) {
            fos.write(item.getBytes());
            fos.write("\r\n".getBytes());
        }
        fos.flush();
        fos.close();
    }

    private static Student inputStudent() {
        Student student = new Student();

        System.out.println("id");
        student.setId(input.next());
        System.out.println("name");
        student.setName(input.next());
        System.out.println("grade");
        student.setGrade(input.next());

        return student;
    }
}
