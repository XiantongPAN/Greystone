package com.panxiantong.gomoku;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * It is a tool class to provide some useful methods.
 */
public class Tool {

    private Tool(){
    }

    public static int trim(int a, int min, int max) {
        int b = a;
        if (min < max) {
            if (a < min)
                b = min;
            if (a > max)
                b = max;
        } else {
            if (a < max)
                b = max;
            if (a > min)
                b = min;
        }
        return b;
    }

    public static Map<String, String> importType(String input) {
        Scanner in = null;
        try {
            in = new Scanner(new File(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> output = new HashMap<>();
        while (in.hasNextLine()) {
            String[] s = in.nextLine().split("\t");
            output.put(s[0], s[1]);
        }
        return output;
    }

    public static Map<String, Integer> importData(String input) {
        Scanner in = null;
        try {
            in = new Scanner(new File(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Integer> output = new HashMap<>();
        while (in.hasNextLine()) {
            String[] s = in.nextLine().split("\t");
            output.put(s[0], Integer.parseInt(s[1]));
        }
        return output;
    }

    public static List<Integer> importScore(String input) {
        Scanner in = null;
        try {
            in = new Scanner(new File(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Integer> output = new ArrayList<>();
        while (in!=null && in.hasNextLine()) {
            String[] s = in.nextLine().split("\t");
            output.add(Integer.parseInt(s[1]));
        }
        return output;
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static byte[] readFileByBytes(String file) {

        byte[] bt = null;

        try (InputStream is = new FileInputStream(file)) {
            int length = is.available();
            bt = new byte[length];
            is.read(bt);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bt;

    }

    public static String runExe(String dir, String input) {
        String output = "";
        try {
            final Process proc = Runtime.getRuntime().exec(dir);

            BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            OutputStream stdin = proc.getOutputStream();

            stdin.write((input + "\n").getBytes());
            stdin.flush();
            String m = "";
            while ((m = stdout.readLine()) != null) {
                output += m;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            output = "can not run this file";
        }

        return output;

    }

    public static JsonObject readJSONData(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        String lineString = null;
        try {
            BufferedReader reader = request.getReader();
            while ((lineString = reader.readLine()) != null) {
                json.append(lineString);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new JsonParser().parse(json.toString()).getAsJsonObject();

    }

    public static void main(String args[]) throws IOException {
        System.out.println(trim(4, -4, -5));

        String file = "C:\\Users\\xiant\\Dropbox\\附件\\花月一二打.lib";
        byte[] bs = readFileByBytes(file);
        System.out.println(bs.length);
    }
}

