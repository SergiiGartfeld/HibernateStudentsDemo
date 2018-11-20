package com.java17.students;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

//niezbędna klasa jako punkt kondiguracyjny połączenia z hibernate'm
public class HibernateUtil {

    private static SessionFactory sessionFactory;

static {
    //Tworzymy sonie obiekt który pobiera konfiguracje z pliku hibernate cfg xml
    StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    //metadata to informacje dotyczące plikow. zdanych załadowanych wcześniej
    //tworzymy sobie obiekt metadata.
    Metadata metadata = new MetadataSources(standardServiceRegistry).getMetadataBuilder().build();

    //stworzynie sesji z danych zawartych w pliku hibernate cfg xml
    sessionFactory = metadata.getSessionFactoryBuilder().build();
}


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
