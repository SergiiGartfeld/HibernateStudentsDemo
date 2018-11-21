package com.java17.students;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        Student st = new Student(null, "Marian", "Kowalski", "123", null);

        StudentDao studentDao = new StudentDao();
//<<<<<<< HEAD
//        //studentDao.saveStudentIntoDataBase(st);
//        System.out.println(studentDao.getAllStudentsFromDataBase());
//=======
//       // studentDao.saveStudentIntoDataBase(st);
//       // System.out.println(studentDao.getAllStudentsFromDataBase());
//>>>>>>> 273db36698430f173362439d4633e410aa93e18a

        Scanner scanner = new Scanner(System.in);

        System.out.println("wpisz  co chcesz zrobić: \n  dodaj \n  listuj \n  exit");
        String odczyt;
        Scanner scanDlaOcen = new Scanner(System.in);
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

                System.out.println("Podaj ilość ocen: ");
                int iloscOcen = scanDlaOcen.nextInt();
                List<Ocena>ocenaList = new ArrayList<>();
                Student student = new Student();
                for(int i = 0; i<iloscOcen; i++){
                    System.out.println("Podaj nazwę przedmiotu: ");
                    Przedmiot przedmiot = Przedmiot.valueOf(scanner.nextLine());

                    System.out.println("Podaj ocenę: ");
                    int ocena = scanDlaOcen.nextInt();
                    ocenaList.add(new Ocena(null, ocena, przedmiot, student));
                }
                student.setImie(imie);
                student.setNazwisko(nazwisko);
                student.setIndeks(indeks);
                student.setOceny(ocenaList);

                studentDao.saveStudentIntoDataBase(student);
            }

        } while (!odczyt.equals("exit"));
        System.exit(0);
        //albo
    }

}



