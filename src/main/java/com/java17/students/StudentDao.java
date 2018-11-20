package com.java17.students;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class StudentDao {
    public boolean saveStudentIntoDataBase(Student student) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            //zamyka transak i zatwierdza zmiany
            transaction.commit();
        } catch (SecurityException se) {
            //w razie błędu przyweóci stan sprzed transakcji
            if (transaction != null) {

                transaction.rollback();
            }
            return false;
        }
        return true;
    }

    public List<Student> getAllStudentsFromDataBase() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sessionFactory.openSession()) {

            Query<Student> query = session.createQuery("from Student st",Student.class);
          List<Student>students = query.list();
          return students;
        } catch (SecurityException se) {
        //jeśli coś pójdzie nietak - wypiszmy komunikat
            // todo: logger
            System.err.println("nie udało się pobrać z bazy");

        }
        return new ArrayList<>();
    }
}
