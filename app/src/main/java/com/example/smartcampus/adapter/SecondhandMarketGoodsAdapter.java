package com.example.smartcampus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.activity.SecondhandMarketActivity;
import com.example.smartcampus.entity.Goods;

import java.util.List;

public class SecondhandMarketGoodsAdapter extends ArrayAdapter {
    private final int resourceId;
    private LinearLayout ll_goods_images;
    private final Context myContext;
    private final SecondhandMarketActivity secondhandMarketActivity;

    public SecondhandMarketGoodsAdapter(@NonNull Context context, SecondhandMarketActivity secondhandMarketActivity, int resource, @NonNull List goodsList) {
        super(context, resource, goodsList);
        this.resourceId = resource;
        this.myContext = context;
        this.secondhandMarketActivity = secondhandMarketActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Goods goods = (Goods) getItem(position);//获取当前项
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
        tv_price.setText("￥99999999.99");//设置
        //设置商品图片
        ll_goods_images = (LinearLayout) view.findViewById(R.id.ll_goods_images);
        for(int i=0;i<10;i++){
            LayoutInflater inflater = secondhandMarketActivity.getLayoutInflater();
            ImageView imageView = (ImageView) inflater.inflate(R.layout.goods_image_item, null);
            imageView.setImageResource(R.drawable.ic_user_image4);
            ll_goods_images.addView(imageView);
            /*//设置图片宽高
            ViewGroup.LayoutParams params=imageView.getLayoutParams();
            params.width=400;
            params.height=400;
            imageView.setLayoutParams(params);*/
            //设置图片内边距
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5,5,5,5);//4个参数按顺序分别是左上右下
            layoutParams.width=400;
            layoutParams.height=400;
            imageView.setLayoutParams(layoutParams);
        }
        return view;
    }
}
