package ru.hogwarts.school.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;
@Repository
public interface StudentRepository extends JpaRepository  <Student, Long> {
    Collection<Student> findByAge(int age);
    Collection<Student> findByAgeBetween(int min, int max);
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> getLastByIdFiveStudents();
    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getStudentsQuantity();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getAverageAgeOfStudents();

}
