package com.java17.students;

import org.hibernate.*;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.awt.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;


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

    public List<Teacher> getAllTeacherFromDatabase() {
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sesssionFactory.openSession()) {

            // stwórz zapytanie
            Query<Teacher> query = session.createQuery("from Teacher tc ", Teacher.class);

            // wywołaj zapytanie
            List<Teacher> teachers = query.list();

            return teachers;
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
            return true;
        } catch (SessionException se) {
            // w razie błędu przywróć stan sprzed transakcji
            if (transaction != null) {
                transaction.rollback();
            }

        }
        return false;
    }

    public Optional<Teacher> getTeacherById(Long id) {
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sesssionFactory.openSession()) {
            Query<Teacher> query = session.createQuery("from Teacher where id = :id", Teacher.class);
            query.setParameter("id", id);

            return Optional.ofNullable(query.getSingleResult());
        } catch (PersistenceException se) {
            System.err.println("Nie udało się pobrać z bazy!");
        }
        return Optional.empty();
    }



public boolean addTeacherToStudent(Long teacherId, Long studentId){

    SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();
    Transaction transaction = null;
    try (Session session = sesssionFactory.openSession()) {
        transaction = session.beginTransaction();

        // 1 pobierz teachera o podanym id
        Query<Teacher> query = session.createQuery("from Teacher where id = :zmiennaX", Teacher.class);
        query.setParameter("zmiennaX", teacherId);
        Teacher teacher = query.getSingleResult();

        // 2 pobierz studenta o podanym id
        Query<Student> queryStudent = session.createQuery("from Student where id = :zmiennaY", Student.class);
        queryStudent.setParameter("zmiennaY", studentId);
        Student student = queryStudent.getSingleResult();

        // 3 do listy nauczycieli w studencie dodać teachera
        student.getTeachers().add(teacher);
        // 4 do listy studentów w teacherze dodać studenta
        teacher.getStudents().add(student);

        // zapisać obie encje (student i teacher)
        session.save(teacher);
        session.save(student);

        // UWAGA! PAMIĘTAJ BY ZAPISAĆ STUDENTA I TEACHERA W JEDNEJ SESJI/TRANZAKCJI!
        transaction.commit();
        return true;
    } catch (PersistenceException se) {
        if (transaction != null) {
            transaction.rollback();
        }
        System.err.println("Nie udało się wykonać zadania!");
    }

    return false;
}
    public List<Student> getAllStudents_fromTeacher(Long teacherId) {
        SessionFactory sesssionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sesssionFactory.openSession()) {

            Query<Teacher> query = session.createQuery("from Teacher where id = :zmienna", Teacher.class);
            query.setParameter("zmienna", teacherId);

            Teacher teacher = query.getSingleResult();

            // UWAGA! pobieram studentów przed zakończeniem sesji (sesja kończy się wraz z blokiem try)

            /**
             *  Tutaj jest tricky thing - zauważcie że umieściłem poniżej stream
             *  Celem tej operacji jest iterowanie wszystkich elementów. Dlaczego tak?
             *
             *  relacja teacher -> students jest lazy. Jeśli chcę odpytać o studentów muszę to zrobić wewnątrz sesji.
             *  Jeśli opuszczę metodę, to wszystkie pola studentów nie zostaną mi zwrócone, bo wyskoczy błąd końca sesji.
             *
             *  Dlatego taka iteracja powoduje, że każdy student z oddzielna jest przeiterowany, a następnie umieszczony w
             *  liście wynikowej - czyli taka operacja wymusza załadowanie wszystkich studentów.
             */
            return teacher.getStudents().stream().collect(Collectors.toList());
        } catch (PersistenceException se) {
            System.err.println("Nie udało się pobrać z bazy!");
        } // UWAGA: https://docs.oracle.com/javaee/6/api/javax/persistence/Query.html#getSingleResult()

        /// logika logika logika

        // coś pobiera z bazy nauczyciela o podanym id
        // po pobraniu nauczyciela używamy gettera i pobieramy pole studentów
        // (WEWNĄTRZ TRY/CATCH, KIEDY SESJA JEST JESZCZE OTWARTA)

        // zwracamy wynik, a jeśli wystąpi błąd - pustą listę:
        return new ArrayList<>();
    }

}


    //- dodaj_nauczyciela_studentowi (która służy do dodawania nauczyciela do zbioru nauczycieli studenta).
    //    Jaki masz pomysł na rozwiązanie drugiego polecenia? Jak zaadresować dwie encje i powiązać je ze sobą?

