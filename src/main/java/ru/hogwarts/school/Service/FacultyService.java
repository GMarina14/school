package ru.hogwarts.school.Service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private static Long generatedFacultiesId = 1L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(generatedFacultiesId);
        facultyMap.put(generatedFacultiesId, faculty);
        generatedFacultiesId++;
        return facultyMap.get(faculty.getId());
    }

    public Faculty getFacultyId(Long facultyId) {
        if (!(facultyMap.containsKey(facultyId)))
            return null;
        return facultyMap.get(facultyId);
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        if (!(facultyMap.containsKey(facultyId)))
            return null;

        Faculty facultyEdited = facultyMap.get(facultyId);
        facultyEdited.setName(faculty.getName());
        facultyEdited.setColor(faculty.getColor());

        return facultyEdited;
    }

    public Faculty deleteFaculty(Long facultyId) {
        if (!(facultyMap.containsKey(facultyId)))
            return null;

        return facultyMap.remove(facultyId);
    }

    public Collection<Faculty> facultiesOfColor(String color) {
        return facultyMap.values().stream()
                .filter(e -> e.getColor().equals(color))
                .collect(Collectors.toList());
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyMap.values().stream().toList();
    }
}
