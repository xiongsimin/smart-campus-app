package com.example.smartcampus.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.smartcampus.R;
import com.example.smartcampus.fragment.CurriculumScheduleFragment;
import com.example.smartcampus.fragment.MessageFragment;
import com.example.smartcampus.fragment.PersonalCenterFragment;
import com.example.smartcampus.fragment.ToolsFragment;

public class IndexActivity extends AppCompatActivity {
    private ToolsFragment toolsFragment;
    private MessageFragment messageFragment;
    private PersonalCenterFragment personalCenterFragment;
    private CurriculumScheduleFragment curriculumScheduleFragment;
    private Fragment[] fragments;
    private int lastFragment;
//    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tools:
                    if(lastFragment!=0)
                    {
                        switchFragment(lastFragment,0);
                        lastFragment=0;
                    }
//                    mTextMessage.setText(R.string.title_tools);
//                    item.setIcon(R.drawable.ic_tools_selected);
                    return true;
                case R.id.navigation_curriculum_schedule:
                    if(lastFragment!=1)
                    {
                        switchFragment(lastFragment,1);
                        lastFragment=1;
                    }
//                    mTextMessage.setText(R.string.title_curriculum_schedule);
//                    item.setIcon(R.drawable.ic_curriculum_schedule_selected);
                    return true;
                case R.id.navigation_message:
                    if(lastFragment!=2)
                    {
                        switchFragment(lastFragment,2);
                        lastFragment=2;
                    }
//                    mTextMessage.setText(R.string.title_message);
//                    item.setIcon(R.drawable.ic_message_selected);
                    return true;
                case R.id.navigation_personal_center:
                    if(lastFragment!=3)
                    {
                        switchFragment(lastFragment,3);
                        lastFragment=3;
                    }
//                    mTextMessage.setText(R.string.title_personal_center);
//                    item.setIcon(R.drawable.ic_personal_center_selected);
                    return true;
            }
            return false;
        }
    };

    //初始化
    public void initFragment() {
        toolsFragment = new ToolsFragment();
        curriculumScheduleFragment = new CurriculumScheduleFragment();
        messageFragment = new MessageFragment();
        personalCenterFragment = new PersonalCenterFragment();
        fragments = new Fragment[]{toolsFragment, curriculumScheduleFragment, messageFragment, personalCenterFragment};
        lastFragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, toolsFragment).show(toolsFragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initFragment();
//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //切换Fragment
    private void switchFragment(int lastFragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastFragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.main_view,fragments[index]);


        }
        transaction.show(fragments[index]).commitAllowingStateLoss();


    }

}
