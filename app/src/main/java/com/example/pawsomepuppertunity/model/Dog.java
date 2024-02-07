package com.example.pawsomepuppertunity.model;

import java.util.Arrays;
import java.util.Objects;

public class Dog {

    private int id;
    private String name;
    private int age;
    private String breed;
    private String sex;
    private String size;
    private String description;
    private byte[] image;
    private String birthday;
    public Dog() {
    }

    public Dog(int id, String name, int age, String breed, String sex, String size, String description, byte[] image, String birthday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.sex = sex;
        this.size = size;
        this.description = description;
        this.image = image;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return id == dog.id && age == dog.age && Objects.equals(name, dog.name) && Objects.equals(breed, dog.breed) && Objects.equals(sex, dog.sex) && Objects.equals(size, dog.size) && Objects.equals(description, dog.description) && Arrays.equals(image, dog.image) && Objects.equals(birthday, dog.birthday);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, age, breed, sex, size, description, birthday);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", breed='" + breed + '\'' +
                ", sex='" + sex + '\'' +
                ", size='" + size + '\'' +
                ", description='" + description + '\'' +
                ", image=" + Arrays.toString(image) +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
