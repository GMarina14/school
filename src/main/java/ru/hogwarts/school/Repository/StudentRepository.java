package ru.hogwarts.school.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository  <Student, Long> {
    Collection<Student> findByAge(int age);

}