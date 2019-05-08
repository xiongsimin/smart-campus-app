package com.example.smartcampus.util;

import com.example.smartcampus.R;
import com.example.smartcampus.entity.ExamInfo;
import com.example.smartcampus.entity.News;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ZhengFangUtil {
    private SemesterSchedule semesterSchedule;
    private Map<String, Integer> curriculumNameMap;//键值对：课程名-颜色名
    private List<Integer> colorList;//颜色池
    private Map<String, ExamInfo> examInfoMap;//考试信息
    private Map<String, News> newsMap;//新闻

    public SemesterSchedule getSemesterSchedule() {
        return semesterSchedule;
    }

    /**
     * 初始化
     */
    public void init() {
        initSchedule();
        initCurriculumNameMap();
        initColorMap();
    }

    /**
     * 初始化学期课表（填充25周，每周7天）
     */
    public void initSchedule() {
        semesterSchedule = new SemesterSchedule();
        List<WeekCurriculumSchedule> weekCurriculumScheduleList = new ArrayList<WeekCurriculumSchedule>();
        for (int i = 0; i < 25; i++) {
            WeekCurriculumSchedule weekCurriculumSchedule = new WeekCurriculumSchedule();
            List<DayCurriculumSchedule> dayCurriculumScheduleList = new ArrayList<DayCurriculumSchedule>();
            for (int j = 0; j < 7; j++) {
                DayCurriculumSchedule dayCurriculumSchedule = new DayCurriculumSchedule();
                List<Curriculum> curriculumList = new ArrayList<Curriculum>();
                for (int k = 0; k < 6; k++) {
                    curriculumList.add(new Curriculum());
                }
                dayCurriculumSchedule.setCurriculumList(curriculumList);//初始化日课表
                dayCurriculumSchedule.setDay(j + 1);
                dayCurriculumScheduleList.add(dayCurriculumSchedule);//往周课表中添加日课表
            }
            weekCurriculumSchedule.setWeek(i + 1);
            weekCurriculumSchedule.setDayCurriculumScheduleList(dayCurriculumScheduleList);//初始化周课表

            weekCurriculumScheduleList.add(weekCurriculumSchedule);//往学期课程表中添加周课表
        }
        semesterSchedule.setWeekCurriculumScheduleList(weekCurriculumScheduleList);//初始化学期课表
    }

    /**
     * 初始化课程名Map
     * 用HashMap来存储课程名
     */
    public void initCurriculumNameMap() {
        curriculumNameMap = new HashMap<>();
    }

    /**
     * 初始化颜色Map
     */
    public void initColorMap() {
        //TODO 改为List
        colorList = new ArrayList<>();
        colorList.add(R.color.white);
        /*colorList.add(R.color.ivory);
        colorList.add(R.color.lightyellow);*/
        colorList.add(R.color.yellow);
        /*colorList.add(R.color.snow);
        colorList.add(R.color.floralwhite);
        colorList.add(R.color.lemonchiffon);
        colorList.add(R.color.cornsilk);
        colorList.add(R.color.seaShell);
        colorList.add(R.color.lavenderblush);
        colorList.add(R.color.papayawhip);
        colorList.add(R.color.blanchedalmond);
        colorList.add(R.color.mistyrose);
        colorList.add(R.color.bisque);
        colorList.add(R.color.moccasin);
        colorList.add(R.color.navajowhite);
        colorList.add(R.color.peachpuff);*/
        colorList.add(R.color.gold);
//        colorList.add(R.color.pink);
        colorList.add(R.color.lightpink);
        colorList.add(R.color.orange);
//        colorList.add(R.color.lightsalmon);
        colorList.add(R.color.darkorange);
//        colorList.add(R.color.coral);
        colorList.add(R.color.hotpink);
        colorList.add(R.color.tomato);
//        colorList.add(R.color.orangered);
        colorList.add(R.color.deeppink);
//        colorList.add(R.color.fuchsia);
//        colorList.add(R.color.magenta);
//        colorList.add(R.color.red);
//        colorList.add(R.color.oldlace);
//        colorList.add(R.color.lightgoldenrodyellow);
//        colorList.add(R.color.linen);
//        colorList.add(R.color.antiquewhite);
//        colorList.add(R.color.salmon);
//        colorList.add(R.color.ghostwhite);
//        colorList.add(R.color.mintcream);
//        colorList.add(R.color.whitesmoke);
//        colorList.add(R.color.beige);
        colorList.add(R.color.wheat);
        colorList.add(R.color.sandybrown);
//        colorList.add(R.color.azure);
//        colorList.add(R.color.honeydew);
        colorList.add(R.color.aliceblue);
        colorList.add(R.color.khaki);
        colorList.add(R.color.lightcoral);
        colorList.add(R.color.palegoldenrod);
        colorList.add(R.color.violet);
        colorList.add(R.color.darksalmon);
        colorList.add(R.color.lavender);
        colorList.add(R.color.lightcyan);
//        colorList.add(R.color.powderblue);
        colorList.add(R.color.paleturquoise);
        colorList.add(R.color.palegreen);
        colorList.add(R.color.lightskyblue);

        /*colorMap = new LinkedHashMap<>();
        colorMap.put("white", R.color.white);
        colorMap.put("ivory", R.color.ivory);
        colorMap.put("lightyellow", R.color.lightyellow);
        colorMap.put("yellow", R.color.yellow);
        colorMap.put("snow", R.color.snow);
        colorMap.put("floralwhite", R.color.floralwhite);
        colorMap.put("lemonchiffon", R.color.lemonchiffon);
        colorMap.put("cornsilk", R.color.cornsilk);
        colorMap.put("seaShell", R.color.seaShell);
        colorMap.put("lavenderblush", R.color.lavenderblush);
        colorMap.put("papayawhip", R.color.papayawhip);
        colorMap.put("blanchedalmond", R.color.blanchedalmond);
        colorMap.put("mistyrose", R.color.mistyrose);
        colorMap.put("bisque", R.color.bisque);
        colorMap.put("moccasin", R.color.moccasin);
        colorMap.put("navajowhite", R.color.navajowhite);
        colorMap.put("peachpuff", R.color.peachpuff);
        colorMap.put("gold", R.color.gold);
        colorMap.put("pink", R.color.pink);
        colorMap.put("lightpink", R.color.lightpink);
        colorMap.put("orange", R.color.orange);
        colorMap.put("lightsalmon", R.color.lightsalmon);
        colorMap.put("darkorange", R.color.darkorange);
        colorMap.put("coral", R.color.coral);
        colorMap.put("hotpink", R.color.hotpink);
        colorMap.put("tomato", R.color.tomato);
        colorMap.put("orangered", R.color.orangered);
        colorMap.put("deeppink", R.color.deeppink);
        colorMap.put("fuchsia", R.color.fuchsia);
        colorMap.put("magenta", R.color.magenta);
        colorMap.put("red", R.color.red);
        colorMap.put("oldlace", R.color.oldlace);
        colorMap.put("lightgoldenrodyellow", R.color.lightgoldenrodyellow);
        colorMap.put("linen", R.color.linen);
        colorMap.put("antiquewhite", R.color.antiquewhite);
        colorMap.put("salmon", R.color.salmon);
        colorMap.put("ghostwhite", R.color.ghostwhite);
        colorMap.put("mintcream", R.color.mintcream);
        colorMap.put("whitesmoke", R.color.whitesmoke);
        colorMap.put("beige", R.color.beige);
        colorMap.put("wheat", R.color.wheat);
        colorMap.put("sandybrown", R.color.sandybrown);
        colorMap.put("azure", R.color.azure);
        colorMap.put("honeydew", R.color.honeydew);
        colorMap.put("aliceblue", R.color.aliceblue);
        colorMap.put("khaki", R.color.khaki);
        colorMap.put("lightcoral", R.color.lightcoral);
        colorMap.put("palegoldenrod", R.color.palegoldenrod);
        colorMap.put("violet", R.color.violet);
        colorMap.put("darksalmon", R.color.darksalmon);
        colorMap.put("lavender", R.color.lavender);
        colorMap.put("lightcyan", R.color.lightcyan);
        colorMap.put("powderblue", R.color.powderblue);
        colorMap.put("paleturquoise", R.color.paleturquoise);
        colorMap.put("palegreen", R.color.palegreen);
        colorMap.put("lightskyblue", R.color.lightskyblue);*/
    }

/*    public int randomColor() {
        Random random=new Random();
        colorMap.k
        return ;
    }*/

    public void dealSchedule(Document document) {
//        Document doc = Jsoup.parse(curriculumTable);
        Document doc = document;
        Element table = doc.getElementById("Table1");//获取课程表table
        List<Element> tbody = table.getElementsByTag("tbody");
        //遍历，横向遍历表格，第1、3、5、7、9、11节：
        //第1节课周一、第1节课周二.......第1节课周日；
        //第3节课周一  ........... ......第3节课周日；
        //  .
        //  .
        //  .
        //第11节课周一  ........... ......第11节课周日；
        int i = 0;
        for (Element tr : tbody.get(0).getElementsByTag("tr")) {
            if (i >= 2) {
                List<Element> tds = tr.getElementsByTag("td");
                //只对第1、3、5、7、9、11节课判断（2 4 6 8 10 12对应的td永远是空的）
                if (i == 2 || i == 6 || i == 10) {//这几种情况tr的第一项td是时间（上午、下午、晚上） 共9个td
                    for (int m = 2; m < 9; m++) {
                        if (tds.get(m).text().length() <= 1) {//这节没课（整个学期这节都没课）
                            //do nothing
                        } else {//有课（某些周的这节有课）
                            extractCurriculum(tds.get(m).html(), m - 1, i - 1, m);//提取出课程 存入课表
                        }
                    }
                } else if (i == 4 || i == 8 || i == 12) {//这几种情况tr没有 上午、下午、晚上 共8个td
                    for (int m = 1; m < 8; m++) {
                        if (tds.get(m).text().length() <= 1) {//这节没课（整个学期这节都没课）
                            //do nothing
                        } else {//有课（某些周的这节有课）
                            extractCurriculum(tds.get(m).html(), m, i - 1, m);//提取出课程 存入课表
                        }
                    }
                }
            }
            i++;
        }
//        System.out.println(table);
    }

    /**
     * 从文本中提取课程信息，并将课程存入学期课程表
     *
     * @param day                  周几
     * @param startTime            第几节（1代表1、2节；3代表3、4节。。。以此类推）
     * @param sourceCurriculumText
     */
    public void extractCurriculum(String sourceCurriculumText, int day, int startTime, int m) {
        //TODO 提取课程信息
        System.out.println(sourceCurriculumText);
//        sourceCurriculumText = sourceCurriculumText.trim();//去掉空格
        List<Curriculum> curriculumList = new ArrayList<Curriculum>();
        //判断一共有几个“<br><br>”以此来判断该节课有几门不同课程（周数不同）
        int countOfCurriculum = 1 + countShortStr(sourceCurriculumText, "<br><br>");
        for (int i = 0; i < countOfCurriculum; i++) {
            String curriculumName = sourceCurriculumText.substring(0, sourceCurriculumText.indexOf("<br>"));//提取课程名
            sourceCurriculumText = sourceCurriculumText.substring(sourceCurriculumText.indexOf("<br>") + "<br>".length());//截掉课程名那一段
            String type = sourceCurriculumText.substring(0, sourceCurriculumText.indexOf("<br>"));//提取课程类型
            sourceCurriculumText = sourceCurriculumText.substring(sourceCurriculumText.indexOf("<br>") + "<br>".length());//截掉课程类型这一段
            String startWeekAndEndWeek = sourceCurriculumText.substring(sourceCurriculumText.indexOf("{"), sourceCurriculumText.indexOf("<br>"));//提取出{第1-10周}子串，以便做进一步处理
            int startWeek = Integer.parseInt(startWeekAndEndWeek.substring(startWeekAndEndWeek.indexOf("第") + "第".length(), startWeekAndEndWeek.indexOf("-")));//提取出开始周“1”
            int endWeek = Integer.parseInt(startWeekAndEndWeek.substring(startWeekAndEndWeek.indexOf("-") + "-".length(), startWeekAndEndWeek.indexOf("周")));//提取出结束周“10”
            sourceCurriculumText = sourceCurriculumText.substring(sourceCurriculumText.indexOf("<br>") + "<br>".length());//截掉起始周这一段
            String teacher = sourceCurriculumText.substring(0, sourceCurriculumText.indexOf("<br>"));//提取出授课老师名
            sourceCurriculumText = sourceCurriculumText.substring(sourceCurriculumText.indexOf("<br>") + "<br>".length());//截掉老师名这一段
            String classRoom = "";
            if (sourceCurriculumText.indexOf("<br>") != -1) {//说明还有其他课程
                classRoom = sourceCurriculumText.substring(0, sourceCurriculumText.indexOf("<br>"));//提取出教室信息
            } else {//该节课的最后一门课程信息
                classRoom = sourceCurriculumText;
            }
            int color = R.color.turquoise;
            if (curriculumNameMap.containsKey(curriculumName)) {//已经为该课程设置过背景色
                color = curriculumNameMap.get(curriculumName);
            } else {//还没有设置过颜色
//                curriculumNameMap.put(curriculumName,0);
                System.out.println(colorList.size() - 1);
                RandomNumber randomNumber = new RandomNumber(0, colorList.size() - 1);
                int num = randomNumber.getRandomNumber();//获取0-colorList.size()-1之间的随机数
                color = colorList.get(num);
                curriculumNameMap.put(curriculumName, color);//为新课程设置背景色
                colorList.remove(num);//移除颜色池中用过的颜色
            }
            //设置课程各属性
            System.out.println("颜色是：" + color);
            Curriculum curriculum = new Curriculum(curriculumName, type, classRoom, startWeek, endWeek, (startTime + 1) / 2, teacher, color);
            /*curriculum.setCurriculumName(curriculumName);//
            curriculum.setType(type);//
            curriculum.setStartTime((startTime + 1) / 2);//
            curriculum.setStartWeek(startWeek);//
            curriculum.setEndWeek(endWeek);//
            curriculum.setTeacher(teacher);//
            curriculum.setClassRoom(classRoom);//*/
            curriculumList.add(curriculum);//将课程添加进curriculumList
            //将课程信息存入学期课程表中

            for (int n = startWeek; n <= endWeek; n++) {
                semesterSchedule.getWeekCurriculumScheduleList().get(n - 1).getDayCurriculumScheduleList().get(day - 1).getCurriculumList().set((startTime + 1) / 2 - 1, curriculum);
            }
            if (sourceCurriculumText.indexOf("<br><br>") != -1) {//如果还有“<br><br>”说明还有课程，为下一次循环做准备，先截掉无用的一段
                sourceCurriculumText = sourceCurriculumText.substring(sourceCurriculumText.indexOf("<br><br>") + "<br><br>".length());
            }
        }
    }

    /***
     * 统计长串中有几个短串
     * @param longStr
     * @param shortStr
     * @return
     */
    public int countShortStr(String longStr, String shortStr) {
        int count = 0;
        while (longStr.indexOf(shortStr) != -1) {
            count++;
            longStr = longStr.substring(longStr.indexOf(shortStr) + shortStr.length());
        }
        return count;
    }

    public void printSemesterSchedule() {
        Gson gson = new Gson();
        String ss = gson.toJson(semesterSchedule);
        System.out.println(ss);
    }

    /**
     * 将当前学期考试信息存入本地文件
     */
    public void initExamInfo() {
        examInfoMap = new HashMap<>();
        Connection connection = Jsoup.connect("http://localhost:80/content.html");
        Document document = null;
        try {
            Connection.Response response = connection.method(Connection.Method.GET).execute();
            document = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element examTable = document.getElementById("DataGrid1");
        List<Element> trList = new ArrayList<>();
        trList = examTable.getElementsByTag("tr");
        for (int i = 1; i < trList.size(); i++) {
            if (trList.get(i).getElementsByTag("td").get(3).text().length() > 1) {//考试时间已发布的才做统计
                examInfoMap.put(trList.get(i).getElementsByTag("td").get(1).text().toString(), new ExamInfo(trList.get(i).getElementsByTag("td").get(1).text().toString(), trList.get(i).getElementsByTag("td").get(3).text().toString(), trList.get(i).getElementsByTag("td").get(4).text().toString(), trList.get(i).getElementsByTag("td").get(6).text().toString()));
            }
        }
    }

    /**
     * 判断是否有新考试信息发布
     */
    public void detectionNewExamInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Connection connection = Jsoup.connect("http://localhost:80/content.html");
                    Document document = null;
                    try {
                        Connection.Response response = connection.method(Connection.Method.GET).execute();
                        document = response.parse();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Element examTable = document.getElementById("DataGrid1");
                    List<Element> trList = new ArrayList<>();
                    trList = examTable.getElementsByTag("tr");
                    for (int i = 1; i < trList.size(); i++) {
                        if (examInfoMap.containsKey(trList.get(i).getElementsByTag("td").get(1).text().toString())) {
                            System.out.println(trList.get(i).getElementsByTag("td").get(1).text().toString() + "存在！");
                        } else {
                            if (trList.get(i).getElementsByTag("td").get(3).text().length() > 1) {
                                examInfoMap.put(trList.get(i).getElementsByTag("td").get(1).text().toString(), new ExamInfo(trList.get(i).getElementsByTag("td").get(1).text().toString(), trList.get(i).getElementsByTag("td").get(3).text().toString(), trList.get(i).getElementsByTag("td").get(4).text().toString(), trList.get(i).getElementsByTag("td").get(6).text().toString()));
                                System.out.println(trList.get(i).getElementsByTag("td").get(1).text().toString() + "不存在！已添加！");
                            } else {
                                System.out.println(trList.get(i).getElementsByTag("td").get(1).text().toString() + "不存在！未添加！因为考试时间未发布！");
                            }
                        }
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 初始化新闻map
     */
    public void initNewsMap() {
        newsMap = new HashMap<>();
        Connection connection = Jsoup.connect("http://localhost:80/news.html");
        Document document = null;
        try {
            Connection.Response response = connection.method(Connection.Method.GET).execute();
            document = response.parse();
            Element element = document.getElementsByClass("list").get(0).getElementsByTag("ul").get(0);
            for (int i = 0; i < element.getElementsByTag("li").size(); i++) {
                System.out.println("新闻标题是：" + element.getElementsByTag("li").get(i).getElementsByClass("list-txt-1").get(0).text());
                System.out.println("新闻发布时间是：" + element.getElementsByTag("li").get(i).getElementsByClass("list-date").get(0).text());
                System.out.println("新闻链接是：" + element.getElementsByTag("li").get(i).getElementsByTag("a").get(0).attr("href"));
                newsMap.put(element.getElementsByTag("li").get(i).getElementsByClass("list-txt-1").get(0).text(), new News(element.getElementsByTag("li").get(i).getElementsByClass("list-txt-1").get(0).text(), element.getElementsByTag("li").get(i).getElementsByClass("list-date").get(0).text(), element.getElementsByTag("li").get(i).getElementsByTag("a").get(0).attr("href")));
            }
            Gson gson = new Gson();
            String newsJson = gson.toJson(newsMap);
            File file = new File("D:/temp/news.txt");
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(newsJson);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断官网是否有新发布的新闻
     */
    public void detectionNewNotice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Connection connection = Jsoup.connect("http://localhost:80/news.html");
                    Document document = null;
                    try {
                        Connection.Response response = connection.method(Connection.Method.GET).execute();
                        document = response.parse();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Element element = document.getElementsByClass("list").get(0).getElementsByTag("ul").get(0);
                    for (int i = 0; i < element.getElementsByTag("li").size(); i++) {
                        if (newsMap.containsKey(element.getElementsByTag("li").get(i).getElementsByClass("list-txt-1").get(0).text())) {
                            System.out.println("该新闻【\"+element.getElementsByTag(\"li\").get(i).getElementsByClass(\"list-txt-1\").get(0).text()+\"】已存在！");
                        } else {
                            newsMap.put(element.getElementsByTag("li").get(i).getElementsByClass("list-txt-1").get(0).text(), new News(element.getElementsByTag("li").get(i).getElementsByClass("list-txt-1").get(0).text(), element.getElementsByTag("li").get(i).getElementsByClass("list-date").get(0).text(), element.getElementsByTag("li").get(i).getElementsByTag("a").get(0).attr("href")));
                            System.out.println("该新闻【" + element.getElementsByTag("li").get(i).getElementsByClass("list-txt-1").get(0).text() + "】不存在！已加入newsMap");
                        }
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 从文件中读取newsMap
     * @param filePath
     */
    public void readFromTxt(String filePath) {
//        File file = new File("D:/temp/news.txt");
        File file = new File(filePath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String newsJson = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                newsJson += line;
            }
            Gson gson = new Gson();
            newsMap = gson.fromJson(newsJson, Map.class);
            System.out.println(newsMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ZhengFangUtil zhengFangUtil = new ZhengFangUtil();
//        zhengFangUtil.readFromTxt();
        /*zhengFangUtil.initNewsMap();
        zhengFangUtil.detectionNewNotice();*/
        /*
        Connection connection = Jsoup.connect("http://localhost:80/fdf.html");
        Document document = null;
        try {
            Connection.Response response = connection.method(Connection.Method.GET).execute();
            document = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZhengFangUtil zhengFangUtil = new ZhengFangUtil();
        zhengFangUtil.init();
        zhengFangUtil.dealSchedule(document);
        zhengFangUtil.printSemesterSchedule();*/
    }
}
