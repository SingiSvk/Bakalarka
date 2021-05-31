package com.example.bakalarka.data.room.patient;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String name;
    private int age;
    private final List<Illness> illnesses;

    public Patient() {
        this.name = "";
        this.age = 0;
        this.illnesses = new ArrayList<>();
    }

    public Patient(String name) {
        this.name = name;
        this.age = 0;
        this.illnesses = new ArrayList<>();
    }

    public Patient(int age) {
        this.name = "";
        this.age = age;
        this.illnesses = new ArrayList<>();
    }

    public Patient(String name, int age) {
        this.name = name;
        this.age = age;
        this.illnesses = new ArrayList<>();
    }

    public Patient(String name, int age, List<Illness> illnesses) {
        this.name = name;
        this.age = age;
        this.illnesses = illnesses;
    }

    public List<Illness> getIllnesses() {
        return illnesses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
