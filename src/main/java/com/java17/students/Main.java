package com.java17.students;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Student st = new Student(null, "Marian", "Kowalski", "123");

        StudentDao studentDao = new StudentDao();
        //studentDao.saveStudentIntoDataBase(st);
        System.out.println(studentDao.getAllStudentsFromDataBase());

        Scanner scanner = new Scanner(System.in);

        System.out.println("wpisz  co chcesz zrobić: \n  dodaj \n  listuj \n  exit");
        String odczyt;

        do {
            odczyt = scanner.nextLine();
            if (odczyt.equals("listuj")) {
                studentDao.getAllStudentsFromDataBase().stream().forEach(System.out::println);
            } else if (odczyt.equals("dodaj")) {
                System.out.println("podaj imie");
                String imie = scanner.nextLine();
                System.out.println("podaj nazwisko");
                String nazwisko = scanner.nextLine();
                System.out.println("podaj indeks");
                String indeks = scanner.nextLine();

                studentDao.saveStudentIntoDataBase(new Student(null, imie, nazwisko, indeks));
            }

        } while (!odczyt.equals("exit"));
        System.exit(0);
    }

}



