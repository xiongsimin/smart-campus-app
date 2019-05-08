package com.example.smartcampus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChatClientService extends Service {
    public ChatClientService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("ChatClientService的onBind方法被调用");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("ChatClientService的onStartCommand方法被调用");
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
