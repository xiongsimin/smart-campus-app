package com.example.smartcampus.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartcampus.R;
import com.example.smartcampus.activity.ColumnActivity;
import com.example.smartcampus.activity.LoginActivity;
import com.example.smartcampus.activity.SecondhandMarketActivity;


public class ToolsFragment extends Fragment {
    private LinearLayout ll_grade_search;
    private  LinearLayout ll_secondhand_market;
    private LinearLayout ll_interest_block;
    public void initView(){
        ll_grade_search=getActivity().findViewById(R.id.ll_grade_search);
        ll_secondhand_market=getActivity().findViewById(R.id.ll_secondhand_market);
        ll_interest_block=getActivity().findViewById(R.id.ll_interest_block);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        ll_secondhand_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SecondhandMarketActivity.class);
                startActivity(intent);
            }
        });
        ll_interest_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ColumnActivity.class);
                startActivity(intent);
            }
        });
    }
}
