package com.yc.ycandroidtool.fs;

public class Student {

    private int age;
    private String name;

    {
        System.out.println("反射-----载入了 Student.class 文档");
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void test1(){
        System.out.println("反射-----Student-------"+"----test1-----");
    }

    public void test2(){
        System.out.println("反射-----Student-------"+"----test2-----");
    }

    private void test3(String name , int age){
        System.out.println("反射-----Student-------"+"----test3-----"+name + "------"+age);
    }

    public void test4(String name , int age){
        System.out.println("反射-----Student-------"+"----test4-----"+name + "------"+age);
    }

}
