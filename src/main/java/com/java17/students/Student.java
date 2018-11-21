package com.java17.students;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Student extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // identity - pobiera id, nastęonir przyjmuje wartość
    //
    private Long id;

    @Column(name = "name")
    private String imie;

    @Column(name = "surname")
    private String nazwisko;

    @Column(name = "indeksik")
    private String indeks;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private List<Ocena>oceny;

}
