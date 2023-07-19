package ru.hogwarts.school.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.Model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository <Faculty, Long> {
    Collection<Faculty> findByColor(String color);
}
