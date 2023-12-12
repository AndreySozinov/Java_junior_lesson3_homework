package ru.savrey;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Написать класс с двумя методами:
 * 1. принимает объекты, имплементирующие интерфейс serializable, и сохраняет их в файл.
 * Название файла - class.getName() + "_" + UUID.randomUUID().toString()
 * 2. принимает строку вида class.getName() + "_" + UUID.randomUUID().toString() и загружает
 * объект из файла и удаляет этот файл.
 *
 * Что делать в ситуациях, когда файла нет или в нем лежит некорректные данные - подумать самостоятельно..
 */
public class Main {
    public static void main(String[] args) {
        Student PetrovPP = new Student("Petrov P.P.", 2001, "first");

        SaverLoader<Student> studentSaverLoader = new SaverLoader<>();

        //studentSaverLoader.saveObject(PetrovPP);

        Student loadedPetrov = studentSaverLoader.loadObject("ru.savrey.Student_d0b1fabf-8da2-4e66-aeaa-4f520b155628");
        System.out.println(loadedPetrov);
    }
}

class SaverLoader <T extends Serializable> {
    public void saveObject(T obj) {
        String fileName = obj.getClass().getName() + "_" + UUID.randomUUID();

        Path path = Path.of("objects", fileName);
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public T loadObject(String fileName) {
        T object;
        Path path = Path.of("objects" + fileName);
        try(ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
            object = (T) objectInputStream.readObject();
            File file = new File("objects" + fileName);
            boolean deleting = file.delete();
            if (deleting) System.out.println("Файл " + fileName + " удален.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return object;
    }
}

class Student implements Serializable {
    private String name;
    private int birthyear;
    private String course;

    public Student(String name, int birthyear, String course) {
        this.name = name;
        this.birthyear = birthyear;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", birthyear=" + birthyear +
                ", course='" + course + '\'' +
                '}';
    }
}