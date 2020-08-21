package com.yc.ycandroidtool;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                //CrashToolUtils.reStartApp1(this,0);
                break;
            case R.id.tv_2:
                //CrashToolUtils.reStartApp2(this,0);
                break;
            case R.id.tv_3:
                Activity activity = null;
                activity.finish();
                break;
        }
    }

    //多组合，少继承
    //比如。会不会飞，会不会叫，会不会游泳，这样定义抽象类会比较多。有的可以不用
    public abstract class AbstractBird {
        //...省略其他属性和方法...
        public void fly() {
            //...
        }

        public void eat(){

        }

        public void call(){

        }

        public void swimming(){

        }
    }
    public class BirdA extends AbstractBird {
        //A ， 不会飞，会叫，会游泳
        //...省略其他属性和方法...
        public void fly() {
            throw new RuntimeException("I can't fly.'");
        }

        @Override
        public void call() {
            super.call();
        }

        @Override
        public void swimming() {
            super.swimming();
        }
    }

    public class BirdB extends AbstractBird {
        //B， 不会飞，不会叫，会游泳
        //...省略其他属性和方法...
        public void fly() {
            throw new RuntimeException("I can't fly.'");
        }

        @Override
        public void call() {
            throw new RuntimeException("I can't call.'");
        }

        @Override
        public void swimming() {
            super.swimming();
        }
    }

    public class BirdC extends AbstractBird {
        //C，， 会飞，不会叫，不会游泳
        //...省略其他属性和方法...
        public void fly() {

        }

        @Override
        public void call() {
            super.call();
        }

        @Override
        public void swimming() {
            super.swimming();
        }
    }

}
