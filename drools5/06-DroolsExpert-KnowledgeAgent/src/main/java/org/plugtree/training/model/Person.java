package org.plugtree.training.model;

public class Person {

    private Integer age;
    private String name;

    public Person() {
    }

    public Person(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}
