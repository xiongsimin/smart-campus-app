package com.example.smartcampus.util;

import java.util.Random;

public class RandomNumber {
    private int start;
    private int end;

    public RandomNumber() {
        super();
    }

    public RandomNumber(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getRandomNumber() {
        //创建Random类对象
        Random random = new Random();
        //产生随机数
        int number = random.nextInt(end - start + 1) + start;
        return number;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //创建Random类对象
        Random random = new Random();
        RandomNumber randomNumber = new RandomNumber(0,100);
        System.out.println(randomNumber.getRandomNumber());

    }
}
