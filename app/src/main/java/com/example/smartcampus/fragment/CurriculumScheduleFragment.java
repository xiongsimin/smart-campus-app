package com.example.smartcampus.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.activity.CurriculumScheduleSettingActivity;
import com.example.smartcampus.activity.ImportScheduleActivity;
import com.example.smartcampus.activity.TermActivity;
import com.example.smartcampus.adapter.CurriculumAdapter;
import com.example.smartcampus.db.TermDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.Term;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.util.Curriculum;
import com.example.smartcampus.util.SemesterSchedule;
import com.example.smartcampus.util.WeekCurriculumSchedule;
import com.example.smartcampus.util.ZhengFangUtil;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class CurriculumScheduleFragment extends Fragment {
    private TextView tv_warning;
    private boolean tv_warning_visibility;//提示控件显示与否
    private TextView tv_week_selector;
    private ImageView iv_setting;
    private LinearLayout ll_week_switch;
    private LinearLayout ll_week_schedule;
    private boolean ll_week_switch_visibility;//周数切换器可视与否
    private SemesterSchedule semesterSchedule;
//    private List<Curriculum> curriculumList = new ArrayList<>();

    private Term term;
    private TermDatabaseHelper termDatabaseHelper;
    private SQLiteDatabase termDb;


    public void initView() {
        tv_warning = getActivity().findViewById(R.id.tv_warning);
        tv_week_selector = getActivity().findViewById(R.id.tv_week_selector);
        iv_setting = getActivity().findViewById(R.id.iv_setting);
        ll_week_switch = getActivity().findViewById(R.id.ll_week_switch);
        ll_week_schedule = getActivity().findViewById(R.id.ll_week_schedule);
        ll_week_switch_visibility = false;
        ll_week_switch.setVisibility(View.GONE);
        tv_warning_visibility = false;
        tv_warning.setVisibility(View.GONE);


        term = new Term();
        termDatabaseHelper = new TermDatabaseHelper(getActivity(), "smartcampus.db", null, 1);
        termDb = termDatabaseHelper.getWritableDatabase();

        for (int i = 0; i < 25; i++) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            TextView textView = (TextView) inflater.inflate(R.layout.switch_item, null);
            textView.setText("第" + (i + 1) + "周");
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //先将所有开关选项背景设为原始背景
                    int switchChildCount = ll_week_switch.getChildCount();
                    for (int t = 0; t < switchChildCount; t++) {
                        TextView tv = (TextView) ll_week_switch.getChildAt(t);
                        tv.setBackgroundColor(0);
                    }
                    //将按下的背景设为指定颜色
                    TextView thisTextView = (TextView) v;
                    Toast.makeText(getActivity(), "这是第" + ((int) thisTextView.getTag() + 1) + "周", Toast.LENGTH_SHORT).show();
                    thisTextView.setBackgroundColor(thisTextView.getResources().getColor(R.color.aliceblue));
                    //设置标题栏第N周
                    tv_week_selector.setText("第" + ((int) thisTextView.getTag() + 1) + "周");
                    //更新课表显示周为第i周
                    Message message = scheduleHandler.obtainMessage();
                    message.what = 1;
                    message.arg1 = (int) thisTextView.getTag();
                    scheduleHandler.sendMessage(message);
                }
            });
            ll_week_switch.addView(textView);
        }
    }

    private final Handler scheduleHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CurriculumAdapter curriculumAdapter_monday = new CurriculumAdapter(getActivity(), R.layout.curriculum_item, semesterSchedule.getWeekCurriculumScheduleList().get(term.getWhichTerm()).getDayCurriculumScheduleList().get(0).getCurriculumList());
                CurriculumAdapter curriculumAdapter_tuesday = new CurriculumAdapter(getActivity(), R.layout.curriculum_item, semesterSchedule.getWeekCurriculumScheduleList().get(term.getWhichTerm()).getDayCurriculumScheduleList().get(1).getCurriculumList());
                CurriculumAdapter curriculumAdapter_wednesday = new CurriculumAdapter(getActivity(), R.layout.curriculum_item, semesterSchedule.getWeekCurriculumScheduleList().get(term.getWhichTerm()).getDayCurriculumScheduleList().get(2).getCurriculumList());
                CurriculumAdapter curriculumAdapter_thursday = new CurriculumAdapter(getActivity(), R.layout.curriculum_item, semesterSchedule.getWeekCurriculumScheduleList().get(term.getWhichTerm()).getDayCurriculumScheduleList().get(3).getCurriculumList());
                CurriculumAdapter curriculumAdapter_friday = new CurriculumAdapter(getActivity(), R.layout.curriculum_item, semesterSchedule.getWeekCurriculumScheduleList().get(term.getWhichTerm()).getDayCurriculumScheduleList().get(4).getCurriculumList());
                CurriculumAdapter curriculumAdapter_saturday = new CurriculumAdapter(getActivity(), R.layout.curriculum_item, semesterSchedule.getWeekCurriculumScheduleList().get(term.getWhichTerm()).getDayCurriculumScheduleList().get(5).getCurriculumList());
                CurriculumAdapter curriculumAdapter_sunday = new CurriculumAdapter(getActivity(), R.layout.curriculum_item, semesterSchedule.getWeekCurriculumScheduleList().get(term.getWhichTerm()).getDayCurriculumScheduleList().get(6).getCurriculumList());

                ListView listView_monday = (ListView) getActivity().findViewById(R.id.lv_monday);
                listView_monday.setAdapter(curriculumAdapter_monday);

                ListView listView_tuesday = (ListView) getActivity().findViewById(R.id.lv_tuesday);
                listView_tuesday.setAdapter(curriculumAdapter_tuesday);

                ListView listView_wednesday = (ListView) getActivity().findViewById(R.id.lv_wednesday);
                listView_wednesday.setAdapter(curriculumAdapter_wednesday);

                ListView listView_thursday = (ListView) getActivity().findViewById(R.id.lv_thursday);
                listView_thursday.setAdapter(curriculumAdapter_thursday);

                ListView listView_friday = (ListView) getActivity().findViewById(R.id.lv_friday);
                listView_friday.setAdapter(curriculumAdapter_friday);

                ListView listView_saturday = (ListView) getActivity().findViewById(R.id.lv_saturday);
                listView_saturday.setAdapter(curriculumAdapter_saturday);

                ListView listView_sunday = (ListView) getActivity().findViewById(R.id.lv_sunday);
                listView_sunday.setAdapter(curriculumAdapter_sunday);

                /*//测试////////////////////////////////////////////////
                ObjectOutputStream oos = null;
                try {
                    System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempFile");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    oos = new ObjectOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempFile"));
                    oos.writeObject(semesterSchedule);
                    System.out.println(semesterSchedule);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(oos);
                }

                //Read Obj from File
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempFile");
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new FileInputStream(file));
                    semesterSchedule = (SemesterSchedule) ois.readObject();
                    System.out.println(semesterSchedule);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(ois);
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //测试////////////////////////////////////////////////*/
            } else if (msg.what == 2) {
                // 显示选择/新建学期控件
                tv_warning.setVisibility(View.VISIBLE);
                tv_warning_visibility = true;
                tv_warning.setText("请导入/添加学期");
                tv_warning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TermActivity.class);
                        startActivity(intent);
                    }
                });
            } else if (msg.what == 3) {
                //TODO 显示导入课表控件
                tv_warning.setVisibility(View.VISIBLE);
                tv_warning_visibility = true;
                tv_warning.setText("请导入课表");
                tv_warning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 添加导入课表代码
                        Intent intent = new Intent(getActivity(), ImportScheduleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("year", term.getYear());
                        bundle.putInt("whichTerm", term.getWhichTerm());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curriculum_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
//        initCurriculum();
        tv_week_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_week_switch_visibility) {
                    ll_week_switch_visibility = false;
                    ll_week_switch.setVisibility(View.GONE);
                } else {
                    ll_week_switch_visibility = true;
                    ll_week_switch.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CurriculumScheduleSettingActivity.class);
                startActivity(intent);
            }
        });

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect("http://192.168.23.1:80/fdf.html");
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
                semesterSchedule = zhengFangUtil.getSemesterSchedule();
//                curriculumList=zhengFangUtil.getSemesterSchedule().getWeekCurriculumScheduleList().get(10).getDayCurriculumScheduleList().get(1).getCurriculumList();
                Message msg = scheduleHandler.obtainMessage();
                msg.what = 1;
                msg.arg1 = 10;
                scheduleHandler.sendMessage(msg);//子线程中发送消息，由scheduleHandler处理
            }
        }).start();*/
        //测试/////////////////////////////////////////////
        termDatabaseHelper.create(termDb);
        //测试/////////////////////////////////////////////


        if (termDatabaseHelper.query(termDb)) {//查询已选中的学期
            term = termDatabaseHelper.getTerm();
            if (term.getScheduleDataPath() != null && !term.getScheduleDataPath().equals("")) {//该学期已创建且已导入课表
                //Read Obj from File
                File file = new File(term.getScheduleDataPath());
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new FileInputStream(file));
                    semesterSchedule = (SemesterSchedule) ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(ois);
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                Message message = scheduleHandler.obtainMessage();
                message.what = 1;
                scheduleHandler.sendMessage(message);
            } else {//该学期已创建但未导入课表
                Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                Message message = scheduleHandler.obtainMessage();
                message.what = 3;
                scheduleHandler.sendMessage(message);
            }
        } else {//当前没有选中学期或没有新建任何学期
            Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
            Message message = scheduleHandler.obtainMessage();
            message.what = 2;
            scheduleHandler.sendMessage(message);
        }
    }
}
