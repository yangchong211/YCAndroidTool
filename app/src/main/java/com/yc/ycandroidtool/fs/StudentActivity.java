package com.yc.ycandroidtool.fs;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yc.toollib.tool.ToolLogUtils;
import com.yc.ycandroidtool.BaseActivity;
import com.yc.ycandroidtool.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class StudentActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTv0;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    private TextView mTv5;
    private TextView mTv6;
    private TextView mTv7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student);
        init();
    }

    private void init() {
        mTv0 = findViewById(R.id.tv_0);
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTv4 = findViewById(R.id.tv_4);
        mTv5 = findViewById(R.id.tv_5);
        mTv6 = findViewById(R.id.tv_6);
        mTv7 = findViewById(R.id.tv_7);

        mTv0.setOnClickListener(this);
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
        mTv4.setOnClickListener(this);
        mTv5.setOnClickListener(this);
        mTv6.setOnClickListener(this);
        mTv7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv0){
            test0();
        } if (v == mTv1){
            test1();
        }else if (v == mTv2){
            test2();
        } else if (v == mTv3){
            test3();
        } else if (v == mTv4){
            test4();
        } else if (v == mTv5){
            test5();
        } else if (v == mTv6){
            test6();
        } else if (v == mTv7){
            test7();
        }
    }


    private void test0() {
        //第一种方式 通过Class类的静态方法——forName()来实现
        //默认构造
        try {
            System.out.println("反射-----Student-------"+"----forName()来实-----");
            Class<?> class1 = Class.forName("com.yc.ycandroidtool.fs.Student");
            Student s = (Student) class1.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        //第一种方式 通过Class类的静态方法——forName()来实现
        //有参数
        try {
            System.out.println("反射-----Teacher-------"+"----forName()来实-----");
            Class<?> class1 = Class.forName("com.yc.ycandroidtool.fs.Teacher");
            //Teacher teacher = (Teacher) class1.newInstance();
            //获取指定声明构造函数
            //Constructor<?> constructor = class1.getConstructor(String.class);
            //Teacher t = (Teacher) constructor.newInstance("yangchong");
            Constructor<?> constructor = class1.getConstructor(Integer.class,String.class);
            Teacher t = (Teacher) constructor.newInstance(27,"yangchong");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void test1() {
        //第一种方式 通过Class类的静态方法——forName()来实现
        //默认构造
        try {
            System.out.println("反射-----Student-------"+"----forName()来实-----");
            Class<?> class1 = Class.forName("com.yc.ycandroidtool.fs.Student");
            Student s = (Student) class1.newInstance();
            // 取得包对象
            Package p = class1.getPackage();
            System.out.println("包名:" + p.getName());
            // 访问修饰符
            int modifier = class1.getModifiers();
            System.out.println("类访问修饰符：" + Modifier.toString(modifier));

            System.out.println();

            //取得构造函数信息
            Constructor[] constructors=class1.getConstructors();
            for(Constructor constructor:constructors){
                System.out.print("访问修饰符：" + Modifier.toString(constructor.getModifiers()));
                System.out.print("   构造函数名："+constructor.getName());
                System.out.println();
            }

            System.out.println();

            //取得声明的数据成员
            Field[] fields = class1.getDeclaredFields();
            for (Field field : fields) {
                System.out.print("访问修饰符：" + Modifier.toString(field.getModifiers()));
                System.out.print("   类型："+field.getType().getName());
                System.out.print("   成员名："+field.getName());
                System.out.println();
            }

            System.out.println();

            //取得成员方法息
            Method[] methods=class1.getDeclaredMethods();
            for(Method method : methods){
                System.out.print("访问修饰符：" + Modifier.toString(method.getModifiers()));
                System.out.print("   返回值类型："+method.getReturnType().getName());
                System.out.print("   方法名："+method.getName());
                System.out.println();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void test2() {
        try {
            System.out.println("反射-----Teacher-------"+"----forName()来实-----");
            Class<?> class1 = Class.forName("com.yc.ycandroidtool.fs.Teacher");
            //Teacher teacher = (Teacher) class1.newInstance();
            //获取指定声明构造函数
            //Constructor<?> constructor = class1.getConstructor(String.class);
            //Teacher t = (Teacher) constructor.newInstance("yangchong");
            Constructor<?> constructor = class1.getConstructor(Integer.class,String.class);
            Teacher t = (Teacher) constructor.newInstance(27,"yangchong");

            //首先需要获得与该方法对应的Method对象
            Method method = class1.getDeclaredMethod("setAge", int.class);
            //设置权限
            method.setAccessible(true);
            //调用指定的函数并传递参数
            method.invoke(t, 28);
            Method method1 = class1.getDeclaredMethod("setAge", int.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void test3() {
        try {
            System.out.println("反射-----Teacher-------"+"----forName()来实-----");
            Class<?> class1 = Class.forName("com.yc.ycandroidtool.fs.Teacher");
            //Teacher teacher = (Teacher) class1.newInstance();
            //获取指定声明构造函数
            //Constructor<?> constructor = class1.getConstructor(String.class);
            //Teacher t = (Teacher) constructor.newInstance("yangchong");
            Constructor<?> constructor = class1.getConstructor(Integer.class,String.class);
            Teacher t = (Teacher) constructor.newInstance(27,"yangchong");

            //首先需要获得与该方法对应的Method对象
            Field field = class1.getDeclaredField("age");
            //设置权限
            field.setAccessible(true);
            //将obj对象的age的值设置为10
            field.setInt(t, 10);
            //获取obj对象的age的值
            int anInt = field.getInt(t);
            ToolLogUtils.i("反射-----反射访问成员变量值------"+anInt);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    private void test4() {
        try {
            System.out.println("反射-----Teacher-------"+"----forName()来实-----");
            Class<?> class1 = Class.forName("com.yc.ycandroidtool.fs.Teacher");
            //Teacher teacher = (Teacher) class1.newInstance();
            //获取指定声明构造函数
            //Constructor<?> constructor = class1.getConstructor(String.class);
            //Teacher t = (Teacher) constructor.newInstance("yangchong");
            Constructor<?> constructor = class1.getConstructor(Integer.class,String.class);
            Teacher t = (Teacher) constructor.newInstance(27,"yangchong");

            // 指定方法名称来获取对应的公开的Method实例
            Method getName = class1.getMethod("getName");
            Method getName1 = class1.getDeclaredMethod("getName");
            getName.setAccessible(true);
            getName1.setAccessible(true);
            System.out.println("反射-----Teacher----getMethod---"+getName);
            System.out.println("反射-----Teacher----getDeclaredMethod---"+getName1);
            // 调用对象object的方法
            getName.invoke(t);

            // 指定方法名称来获取对应的公开的Method实例
            Method getAge = class1.getMethod("getAge");
            // 调用对象object的方法
            getAge.invoke(t);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private void test5(){
        Class cl = Student.class;
        System.out.println("反射-----test5方法-------"+"类名称:"+cl.getName());
        System.out.println("反射-----test5方法-------"+"简单类名称:"+cl.getSimpleName());
        System.out.println("反射-----test5方法-------"+"包名:"+cl.getPackage());
        System.out.println("反射-----test5方法-------"+"是否为接口:"+cl.isInterface());
        System.out.println("反射-----test5方法-------"+"是否为基本类型:"+cl.isPrimitive());
        System.out.println("反射-----test5方法-------"+"是否为数组对象:"+cl.isArray());
        System.out.println("反射-----test5方法-------"+"父类名称:"+cl.getSuperclass().getName());
    }

    private void test6(){
        Student student;
        System.out.println("反射-----声明了 Student 变量");
        student=new Student();
        System.out.println("反射-----生成了 Student 实例");
    }

    private void test7(){
//        try {
//            Sing palyer = (Sing) Class.forName("com.yc.ycandroidtool.fs.Sing").newInstance();
//            palyer.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            Sing palyer = (Sing) Class.forName("com.yc.ycandroidtool.fs.StudentSing").newInstance();
            palyer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
