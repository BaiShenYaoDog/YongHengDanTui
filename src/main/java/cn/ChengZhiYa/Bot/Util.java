package cn.ChengZhiYa.Bot;

import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Util {
    public static String RandomText() {
        List<String> StringList = new ArrayList<>();
        StringList.add("5x8=35");
        StringList.add("5x8=42");
        StringList.add("3x5=16");
        StringList.add("7x9=null");
        StringList.add("5x6=35");
        StringList.add("2x9=12");
        StringList.add("4x9=54");
        StringList.add("5x9=35");
        StringList.add("平板打快板，这个好欸");
        StringList.add("「我不玩了！」");
        StringList.add("「我真的服了！」");
        StringList.add("「我要把你们都sa了！」");
        StringList.add("飞了飞了");
        StringList.add("笑死了，什么V什么粉");
        StringList.add("v我50");
        Random random = new Random();
        return StringList.get(random.nextInt(StringList.size()));
    }
    public static String getDate() {
        LocalDate Date = LocalDate.now();
        return Date.getYear() + "年" + Date.getMonthValue() + "月" + Date.getDayOfMonth() + "日";
    }
    public static String getDay() {
        LocalDate Date = LocalDate.now();
        String Day = "";
        switch (Date.getDayOfWeek().getValue()) {
            case 1:
                Day = "星期一";
                break;
            case 2:
                Day = "星期二";
                break;
            case 3:
                Day = "星期三";
                break;
            case 4:
                Day = "星期四";
                break;
            case 5:
                Day = "星期五";
                break;
            case 6:
                Day = "星期六";
                break;
            case 7:
                Day = "星期日";
                break;
        }
        return Day;
    }
    public static String getTime() {
        LocalDateTime Time = LocalDateTime.now();
        String Hour = String.valueOf(Time.getHour());
        String Minute = String.valueOf(Time.getMinute());
        if (Hour.length() == 1) {
            Hour = "0" + Hour;
        }
        if (Minute.length() == 1) {
            Minute = "0" + Minute;
        }
        return  Hour + ":" + Minute;
    }
    public static String getHitokoto(){
        try {
            URL url = new URL("https://v1.hitokoto.cn/");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            JSONObject Json = JSONObject.parseObject(reader.readLine());
            return Json.getString("hitokoto");
        } catch (Exception e) {
            return "呜呜呜,一言服务器炸了!";
        }
    }
}
