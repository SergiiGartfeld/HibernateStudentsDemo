package com.java17.students;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data

@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"teachers"})
public class Student extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // identity - pobiera id, nastęonir przyjmuje wartość
    //
    private Long id;


    private String imie;


    private String nazwisko;


    private String indeks;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Ocena>oceny;

    @ManyToMany(mappedBy = "students",fetch = FetchType.LAZY)
    private List<Teacher> teachers;

}
