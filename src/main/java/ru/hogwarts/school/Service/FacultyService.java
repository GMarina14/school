package ru.hogwarts.school.Service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.FacultyRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyId(Long facultyId) {
        return facultyRepository.findById(facultyId).orElse(null);
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        Faculty facultyInDB = getFacultyId(facultyId);
        if(facultyInDB==null)
            return null; // exception needed


        facultyInDB.setName(faculty.getName());
        facultyInDB.setColor(faculty.getColor());

        return facultyRepository.save(facultyInDB);
    }

    public void deleteFaculty(Long facultyId) {
        facultyRepository.deleteById(facultyId);

    }

    public Collection<Faculty> facultiesOfColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
    public Collection<Faculty>  getfacultiesByNameOrColor(String name, String color){
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Student> getStudentsOfFaculty(Long facultyId){
        return facultyRepository.findById(facultyId).map(e->e.getStudents()).orElse(null);
    }
}
