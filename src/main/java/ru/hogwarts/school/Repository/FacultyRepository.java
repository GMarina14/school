package ru.hogwarts.school.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;
@Repository
public interface FacultyRepository extends JpaRepository <Faculty, Long> {
    Collection<Faculty> findByColor(String color);
    Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
}
