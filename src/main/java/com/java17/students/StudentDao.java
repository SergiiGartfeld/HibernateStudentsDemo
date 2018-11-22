package com.java17.students;

import org.hibernate.*;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;


public class StudentDao {
    public boolean saveStudentWithGradesIntoDb(Student student) {
        // pobieramy session factory (fabryka połączenia z bazą)
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;

        try (Session session = sesssionFactory.openSession()) {
            // otwieram transakcję
            transaction = session.beginTransaction();

            for (Ocena oc : student.getOceny()) {
                session.save(oc);
            }

            session.save(student); // dokonujemy zapisu na bazie

            // zamykam transakcję i zatwierdzam zmiany
            transaction.commit();
        } catch (SessionException se) {
            // w razie błędu przywróć stan sprzed transakcji
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        return true;
    }


    public boolean saveIntoDb(BaseEntity entity) {
        // pobieramy session factory (fabryka połączenia z bazą)
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;

        try (Session session = sesssionFactory.openSession()) {
            // otwieram transakcję
            transaction = session.beginTransaction();

            session.save(entity); // dokonujemy zapisu na bazie

            // zamykam transakcję i zatwierdzam zmiany
            transaction.commit();
        } catch (SessionException se) {
            // w razie błędu przywróć stan sprzed transakcji
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        return true;
    }

    public List<Student> getAllStudentsFromDatabase() {
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sesssionFactory.openSession()) {

            // stwórz zapytanie
            Query<Student> query = session.createQuery("from Student st ", Student.class);

            // wywołaj zapytanie
            List<Student> students = query.list();

//            System.out.println(students);

            // zwróć wynik
            return students;
        } catch (SessionException se) {
            // jeśli coś pójdzie nietak - wypiszmy komunikat loggerem:
            // todo: logger
            System.err.println("Nie udało się pobrać z bazy!");
        }

        // jeśli nie uda się znaleźć zwracamy pustą listę.
        return new ArrayList<>();
    }


    public Optional<Student> getById(Long id) {
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sesssionFactory.openSession()) {
            Query<Student> query = session.createQuery("from Student where id = :id", Student.class);
            query.setParameter("id", id);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException se) {
            System.err.println("Nie udało się pobrać z bazy!");
        }
        return Optional.empty();
    }

    public List<Student> getById(List<Long> ids) {
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sesssionFactory.openSession()) {
            Query<Student> query = session.createQuery("from Student where id IN :ids", Student.class);
            query.setParameter("ids", ids);


            return query.list();
        } catch (PersistenceException se) {
            System.err.println("Nie udało się pobrać z bazy!");
        }
        return new ArrayList<>();
    }

 //  public boolean removeById(Long id) {

 //      SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();

 //      try (Session session = sesssionFactory.openSession()) {
 //          Query<Student> query = session.createQuery("delete from Student where id = :id", Student.class);
 //          query.setParameter("id", id).executeUpdate();


 //          return true;
 //      } catch (PersistenceException se) {
 //          System.err.println("Nie udało się pobrać z bazy!");
 //      }
 //      return false;
 //  }

    public boolean removeById(Long id) {
        // pobieramy session factory (fabryka połączenia z bazą)
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;

        try (Session session = sesssionFactory.openSession()) {
            // otwieram transakcję
            transaction = session.beginTransaction();



            Optional<Student>pomocnicza = getById(id);

            Student student = pomocnicza.isPresent() ? pomocnicza.get() : new Student();
            for (Ocena oc : student.getOceny()) {
                session.delete(oc);
            }
            session.delete(student);

            // zamykam transakcję i zatwierdzam zmiany
            transaction.commit();
        } catch (SessionException se) {
            // w razie błędu przywróć stan sprzed transakcji
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
        return true;
    }
}
