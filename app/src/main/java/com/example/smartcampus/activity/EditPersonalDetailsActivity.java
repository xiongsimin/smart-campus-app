package com.example.smartcampus.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.smartcampus.R;
import com.example.smartcampus.db.CampusDatabaseHelper;
import com.example.smartcampus.db.UserCampusDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.Campus;
import com.example.smartcampus.entity.MyDate;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.entity.UserCampus;
import com.example.smartcampus.storage.LocalStorage;
import com.example.smartcampus.util.CircleImageView;
import com.example.smartcampus.util.PhotoSelectUtils;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class EditPersonalDetailsActivity extends AppCompatActivity {
    //    private Spinner spinner_campus;
//    private Spinner spinner_degree;
    //    private Spinner spinner_attend_campus_time;
    /*private List<String> listCampus;//用于设置大学下拉列表条目内容
    private List<String> listDegree;//用于设置学位下拉列表条目内容*/
    private List<String> listAttendCampusTime;//用于设置入学时间下拉列表条目内容
    private TextView tv_email;
    private TextView tv_nickname;
    private TextView tv_sex;
    private TextView tv_academy;
    private TextView tv_major;
    private TextView tv_campus;
    private TextView tv_degree;
    private TextView tv_attend_campus_time;
    private ImageView iv_user_image;
    private RelativeLayout rl_user_image;
    private LinearLayout ll_sex;
    private LinearLayout ll_academy;
    private LinearLayout ll_major;
    private User user;
    private UserCampus userCampus;
    private UserDatabaseHelper userDatabaseHelper;
    private SQLiteDatabase userDb;
    private CampusDatabaseHelper campusDatabaseHelper;
    private SQLiteDatabase campusDb;
    private UserCampusDatabaseHelper userCampusDatabaseHelper;
    private SQLiteDatabase userCampusDb;

    private PhotoSelectUtils mLqrPhotoSelectUtils;

    private String encodeImg;
    private String imgSuffix;
    private FileInputStream fis = null;
    private final Handler reloadHandler = new Handler() {
        public void handleMessage(Message message) {
            userDatabaseHelper.query(userDb);
            user = userDatabaseHelper.getUser();
            if (userCampusDatabaseHelper.query(userCampusDb)) {
                campusDatabaseHelper.query(campusDb);
                user.setCampus(campusDatabaseHelper.getCampus());
                userCampusDatabaseHelper.query(userCampusDb);
                user.setUserCampus(userCampusDatabaseHelper.getUserCampus());
                tv_campus.setText(user.getCampus().getCampusName());
                tv_academy.setText(user.getUserCampus().getAcademy());
                tv_major.setText(user.getUserCampus().getMajor());
                tv_degree.setText(user.getUserCampus().getDegree());
                tv_attend_campus_time.setText(user.getUserCampus().getAttendCampusTime());
                System.out.println(user);
            }
            tv_email.setText(user.getEmail());
            tv_nickname.setText(user.getNickname());
            tv_sex.setText(user.getSex());
        }
    };
    private final Handler reloadUserImageHandler = new Handler() {
        public void handleMessage(Message message) {
            Glide.with(EditPersonalDetailsActivity.this).load(Server.Ip + ":80/smart_campus/user_images/" + user.getId() + "/" + user.getUserImagePath()).placeholder(R.drawable.ic_default_user_image).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_user_image);
        }
    };
    /*private final Handler campusHandle = new Handler() {
        public void handleMessage(Message message) {
            AlertDialog.Builder alertDialog =
                    new AlertDialog.Builder(EditPersonalDetailsActivity.this);
            alertDialog.setTitle("学校");
            alertDialog.setItems(campusItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // which 下标从0开始
                    // ...To-do
                        *//*Toast.makeText(EditPersonalDetailsActivity.this,
                                "你点击了" + items[which],
                                Toast.LENGTH_SHORT).show();*//*
//                                    user.setSex(campusItems[which]);
                }
            });
            alertDialog.show();
        }
    };*/


    public void initView() {
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_campus = findViewById(R.id.tv_campus);
        tv_email = findViewById(R.id.tv_email);
        tv_sex = findViewById(R.id.tv_sex);
        tv_major = findViewById(R.id.tv_major);
        tv_degree = findViewById(R.id.tv_degree);
        tv_academy = findViewById(R.id.tv_academy);
        tv_attend_campus_time = findViewById(R.id.tv_attend_campus_time);
        iv_user_image = findViewById(R.id.iv_user_image);
        rl_user_image = findViewById(R.id.rl_user_image);
        ll_sex = findViewById(R.id.ll_sex);
        ll_academy = findViewById(R.id.ll_academy);
        ll_major = findViewById(R.id.ll_major);
        /*listCampus = new ArrayList<>();
        listDegree = new ArrayList<>();*/
        listAttendCampusTime = new ArrayList<>();
        /*listDegree.add("本科/专科");
        listDegree.add("研究生");*/
        user = new User();
        userCampus = new UserCampus();
        userDatabaseHelper = new UserDatabaseHelper(this, "smartcampus.db", null, 1);
        userDb = userDatabaseHelper.getWritableDatabase();

        campusDatabaseHelper = new CampusDatabaseHelper(this, "smartcampus.db", null, 1);
        campusDb = campusDatabaseHelper.getWritableDatabase();

        userCampusDatabaseHelper = new UserCampusDatabaseHelper(this, "smartcampus.db", null, 1);
        userCampusDb = userCampusDatabaseHelper.getWritableDatabase();

        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new PhotoSelectUtils(this, new PhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(final File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调，将图片上传到服务器，并更新数据库用户头像路径
                /*mTvPath.setText(outputFile.getAbsolutePath());
                mTvUri.setText(outputUri.toString());*/
//                Glide.with(EditPersonalDetailsActivity.this).load(outputUri).into(iv_user_image);
                /*FileInputStream fis = null;
                // 读取图片字节数组
                try {
                    fis = new FileInputStream(outputFile);
                    StringBuffer sb = new StringBuffer();
                    int temp = 0;
                    while ((temp = fis.read()) != -1) {
                        sb.append((char) temp);
                    }
                    encodeImg = sb.toString();
                    imgSuffix = outputFile.getAbsolutePath().substring(outputFile.getAbsolutePath().lastIndexOf("."), outputFile.getAbsolutePath().length());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                imgSuffix = outputFile.getAbsolutePath().substring(outputFile.getAbsolutePath().lastIndexOf("."), outputFile.getAbsolutePath().length());
                try {
                    fis = new FileInputStream(outputFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Connection connection = Jsoup.connect(Server.Ip + ":8000/userImage");
                        Map<String, String> data = new HashMap<>();
                        data.put("email", user.getEmail());
//                        data.put("encodeImg", encodeImg);
                        data.put("imgSuffix", imgSuffix);
                        try {
                            connection.method(Connection.Method.PUT);
//                            connection.header("Content-Type", "multipart/form-data");
                            connection.data(data);
                            connection.data("userImage", outputFile.getName(), fis);
                            connection.ignoreHttpErrors(true);
                            connection.ignoreContentType(true);
                            connection.timeout(10000);
                            Connection.Response response = connection.execute();
                            Result result = gson.fromJson(response.body(), Result.class);
                            if (result.isSuccess()) {//上传头像成功，更新本地数据库用户头像路径
                                userDatabaseHelper.updateUserImagePath(userDb, "1" + imgSuffix);
                                userDatabaseHelper.query(userDb);
                                user = userDatabaseHelper.getUser();
                                Message message = reloadUserImageHandler.obtainMessage();
                                reloadUserImageHandler.sendMessage(message);
                            } else {
                                Looper.prepare();
                                Toast.makeText(EditPersonalDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(EditPersonalDetailsActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();

            }
        }, 1, 1, 800, 800);//true裁剪，false不裁剪
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);
        initView();
        userDatabaseHelper.query(userDb);
        user = userDatabaseHelper.getUser();
        if (userCampusDatabaseHelper.query(userCampusDb)) {
            campusDatabaseHelper.query(campusDb);
            user.setCampus(campusDatabaseHelper.getCampus());
            userCampusDatabaseHelper.query(userCampusDb);
            user.setUserCampus(userCampusDatabaseHelper.getUserCampus());
            tv_academy.setText(user.getUserCampus().getAcademy());
            tv_major.setText(user.getUserCampus().getMajor());
            tv_campus.setText(user.getCampus().getCampusName());
            tv_degree.setText(user.getUserCampus().getDegree());
            tv_attend_campus_time.setText(user.getUserCampus().getAttendCampusTime());
        }
        tv_email.setText(user.getEmail());
        tv_nickname.setText(user.getNickname());
        tv_sex.setText(user.getSex());
        //加载用户头像
        Glide.with(EditPersonalDetailsActivity.this).load(Server.Ip + ":80/smart_campus/user_images/" + user.getId() + "/" + user.getUserImagePath()).placeholder(R.drawable.ic_default_user_image).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_user_image);
        rl_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(EditPersonalDetailsActivity.this, PhotoActivity.class);
                startActivity(intent);*/
                final String[] items = {"拍照", "从相册选取"};
                AlertDialog.Builder alertDialog =
                        new AlertDialog.Builder(EditPersonalDetailsActivity.this);
                alertDialog.setTitle("请选择获取图片方式");
                alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {//拍照
                            // 3、调用拍照方法
                            PermissionGen.with(EditPersonalDetailsActivity.this)
                                    .addRequestCode(PhotoSelectUtils.REQ_TAKE_PHOTO)
                                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.CAMERA
                                    ).request();
                        } else {//从相册选取
                            // 3、调用从图库选取图片方法
                            PermissionGen.needPermission(EditPersonalDetailsActivity.this,
                                    PhotoSelectUtils.REQ_SELECT_PHOTO,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}
                            );
                        }
                    }
                });
                alertDialog.show();
            }
        });
        if (user.getRealNameOrNot() == 1) {//已实名，学院、性别等信息不设置点击事件，且去掉控件背景图片的右键标志
            ll_major.setBackground(getDrawable(R.drawable.ic_bottom_line));
            ll_academy.setBackground(getDrawable(R.drawable.ic_bottom_line));
            ll_sex.setBackground(getDrawable(R.drawable.ic_bottom_line));
        } else {
            tv_sex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] items = {"男", "女"};
                    AlertDialog.Builder alertDialog =
                            new AlertDialog.Builder(EditPersonalDetailsActivity.this);
                    alertDialog.setTitle("性别");
                    alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // which 下标从0开始
                            // ...To-do
                        /*Toast.makeText(EditPersonalDetailsActivity.this,
                                "你点击了" + items[which],
                                Toast.LENGTH_SHORT).show();*/
                            user.setSex(items[which]);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Connection connection = Jsoup.connect(Server.Ip + ":8000/sex");
                                    Gson gson = new Gson();
                                    String stringData = gson.toJson(user);
                                    try {
                                        Connection.Response response = connection.method(Connection.Method.PUT).header("Content-Type", "application/json").ignoreContentType(true).requestBody(stringData).timeout(Server.timeOut).execute();
                                        Result result = gson.fromJson(response.body(), Result.class);
                                        if (result.isSuccess()) {
                                            //更新本地数据库
                                            userDatabaseHelper.updateSex(userDb, user);
                                            Message message = reloadHandler.obtainMessage();
                                            reloadHandler.sendMessage(message);
                                        } else {
                                            System.out.println(result.getMsg());
                                            Looper.prepare();
                                            Toast.makeText(EditPersonalDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Looper.prepare();
                                        Toast.makeText(EditPersonalDetailsActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                        }
                    });
                    alertDialog.show();
                }
            });
            tv_degree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] items = {"本科/专科", "研究生"};
                    AlertDialog.Builder alertDialog =
                            new AlertDialog.Builder(EditPersonalDetailsActivity.this);
                    alertDialog.setTitle("学历");
                    alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // which 下标从0开始
                            // ...To-do
                        /*Toast.makeText(EditPersonalDetailsActivity.this,
                                "你点击了" + items[which],
                                Toast.LENGTH_SHORT).show();*/
                            userCampus.setDegree(items[which]);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Connection connection1 = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                                    Connection connection = Jsoup.connect(Server.Ip + ":8000/degree");
                                    Gson gson = new Gson();
                                    Map<String, String> data = new HashMap<>();
                                    data.put("email", user.getEmail());
                                    try {
                                        Connection.Response response1 = connection1.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                                        Result result1 = gson.fromJson(response1.body(), Result.class);
                                        if (result1.isSuccess()) {//获取个人信息成功！
                                            user = gson.fromJson(gson.toJson(result1.getData()), User.class);
                                            userDatabaseHelper.update(userDb, user);
                                            if (user.getUserCampus() != null) {
                                                user.getUserCampus().setDegree(userCampus.getDegree());
                                                String stringData = gson.toJson(user);
                                                Connection.Response response = connection.method(Connection.Method.PUT).header("Content-Type", "application/json").ignoreContentType(true).requestBody(stringData).timeout(Server.timeOut).execute();
                                                Result result = gson.fromJson(response.body(), Result.class);
                                                if (result.isSuccess()) {
                                                    //更新本地数据库
                                                    userCampusDatabaseHelper.updateDegree(userDb, user.getUserCampus());
                                                    Message message = reloadHandler.obtainMessage();
                                                    reloadHandler.sendMessage(message);
                                                } else {
                                                    System.out.println(result.getMsg());
                                                    Looper.prepare();
                                                    Toast.makeText(EditPersonalDetailsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                }
                                            } else {
                                                Looper.prepare();
                                                Toast.makeText(EditPersonalDetailsActivity.this, "请先设置学校！", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        } else {
                                            System.out.println(result1.getMsg());
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Looper.prepare();
                                        Toast.makeText(EditPersonalDetailsActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                        }
                    });
                    alertDialog.show();
                }
            });
            tv_nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditPersonalDetailsActivity.this, NicknameActivity.class);
                    startActivity(intent);
                }
            });
            tv_campus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditPersonalDetailsActivity.this, CampusActivity.class);
                    startActivity(intent);
                }
            });
            tv_academy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditPersonalDetailsActivity.this, AcademyActivity.class);
                    startActivity(intent);
                }
            });
            tv_major.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditPersonalDetailsActivity.this, MajorActivity.class);
                    startActivity(intent);
                }
            });
            tv_attend_campus_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditPersonalDetailsActivity.this, AttendCampusTimeActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    //该页面再现时更新页面控件内容
    @Override
    protected void onRestart() {
        super.onRestart();
        Message message = reloadHandler.obtainMessage();
        reloadHandler.sendMessage(message);
    }

    @PermissionSuccess(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @PermissionFail(requestCode = PhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-智慧校园-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + EditPersonalDetailsActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }
}
