package com.yc.ycandroidtool.fs;

public class Teacher {

    private int age;
    private String name;

    {
        System.out.println("反射-----载入了 Teacher.class 文档");
    }

    public Teacher(String name) {
        this.name = name;
        System.out.println("反射-----Teacher-------"+"----构造-----"+"----"+name);
    }

    public Teacher(Integer age, String name) {
        this.age = age;
        this.name = name;
        System.out.println("反射-----Teacher-------"+"----构造-----"+age+"----"+name);
    }

    public int getAge() {
        System.out.println("反射-----Teacher-------"+"----getAge-----"+age);
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        System.out.println("反射-----Teacher-------"+"----setAge-----"+age);
    }

    public String getName() {
        System.out.println("反射-----Teacher-------"+"----getName-----"+name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("反射-----Teacher-------"+"----setName-----"+name);
    }

    private void test1(){
        System.out.println("反射-----Teacher-------"+"----test1-----");
    }

    public void test2(){
        System.out.println("反射-----Teacher-------"+"----test2-----");
    }

    private void test3(String name , int age){
        System.out.println("反射-----Teacher-------"+"----test3-----"+name + "------"+age);
    }

    public void test4(String name , int age){
        System.out.println("反射-----Teacher-------"+"----test4-----"+name + "------"+age);
    }

}
