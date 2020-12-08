package com.example.capstoneproject;

public class Pet {
    private String name;
    private String sex;
    private String type;
    private String age;
    private String weight;
    private String specialCareNeeds;

    public Pet() {}

    public Pet(String name, String sex, String type, String age, String weight, String specialCareNeeds) {
        this.name = name;
        this.sex = sex;
        this.type = type;
        this.age = age;
        this.weight = weight;
        this.specialCareNeeds = specialCareNeeds;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getType() {
        return type;
    }

    public String getAge() {
        return age;
    }

    public String getWeight() {
        return weight;
    }

    public String getSpecialCareNeeds() {
        return specialCareNeeds;
    }
}
