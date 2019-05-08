package com.example.smartcampus.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.adapter.SecondhandMarketGoodsAdapter;
import com.example.smartcampus.adapter.SecondhandMarketTabsAdapter;
import com.example.smartcampus.entity.Goods;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecondhandMarketActivity extends AppCompatActivity {
    //    private ListView lv_type_list;
    private ListView lv_goods_list;
    private LinearLayout ll_tab_list;
    private TextView tv_all;
    private List<String> tabList = new ArrayList<>();
    private List<Goods> goodsList = new ArrayList<Goods>();
    private SecondhandMarketGoodsAdapter secondhandMarketGoodsAdapter=null;

    public void initView() {
        lv_goods_list = findViewById(R.id.lv_goods_list);
        ll_tab_list = findViewById(R.id.ll_tab_list);
        tv_all = findViewById(R.id.tv_all);

//       lv_type_list = findViewById(R.id.lv_type_list);
        tabList.add("数码");
        tabList.add("衣物");
        tabList.add("书籍");
        tabList.add("化妆品");
        tabList.add("其他");

        goodsList.add(new Goods(1, "ss", 1, 100.00, new Timestamp(new Date().getTime()), 1));
        goodsList.add(new Goods(1, "ss", 1, 100.00, new Timestamp(new Date().getTime()), 1));
    }

    private final Handler secondhandMarketGoodsHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.arg1 == 1) {
                secondhandMarketGoodsAdapter = new SecondhandMarketGoodsAdapter(SecondhandMarketActivity.this, SecondhandMarketActivity.this,R.layout.secondhand_market_item, goodsList);
                lv_goods_list.setAdapter(secondhandMarketGoodsAdapter);
            }
        }
    };

    /*android:layout_width="wrap_content"
                    android:layout_height="match_parent"
                    android:layout_marginHorizontal="20dp"
                    android:gravity="center"
                    android:text="全部"
                    android:textSize="25sp"*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondhand_market);
        initView();
        //动态添加TextView
        for (int i = 0; i < tabList.size(); i++) {
            final int index = i;
            LayoutInflater inflater = this.getLayoutInflater();
            TextView textView = (TextView) inflater.inflate(R.layout.tab_item, null);
            textView.setText(tabList.get(i));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SecondhandMarketActivity.this, "这是" + tabList.get(index), Toast.LENGTH_SHORT).show();
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 0, 20, 0);
            ll_tab_list.addView(textView, lp);
        }
        //设置商品列表显示
        secondhandMarketGoodsAdapter = new SecondhandMarketGoodsAdapter(this,SecondhandMarketActivity.this, R.layout.secondhand_market_item, goodsList);
        lv_goods_list.setAdapter(secondhandMarketGoodsAdapter);
        /*SecondhandMarketTabsAdapter secondhandMarketTabsAdapter = new SecondhandMarketTabsAdapter(this, R.layout.secondhand_market_sub_tabs_item, tabList);
        lv_type_list.setAdapter(secondhandMarketTabsAdapter);*/

        //测试用
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsList.add(new Goods(1, "ss", 1, 100.00, new Timestamp(new Date().getTime()), 1));
                Message message = secondhandMarketGoodsHandler.obtainMessage();
                message.arg1 = 1;
                secondhandMarketGoodsHandler.sendMessage(message);
            }
        });
    }
}
