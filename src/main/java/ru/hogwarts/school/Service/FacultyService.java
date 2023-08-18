package ru.hogwarts.school.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.Exceptions.DataBaseIsEmptyException;
import ru.hogwarts.Exceptions.InfoInDBNotFoundException;
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
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);


    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method to create a faculty");

        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyId(Long facultyId) {
        logger.info("The method to get faculty by id was invoked");
        if (facultyRepository.findById(facultyId).isEmpty())
            logger.error("Faculty with id {} doesn't exist", facultyId);

        return facultyRepository.findById(facultyId).orElse(null);
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        logger.info("The method to update faculty info was invoked");

        Faculty facultyInDB = getFacultyId(facultyId);
        if (facultyInDB == null){
            logger.error("Faculty with id {} is not found", facultyId);
            throw new InfoInDBNotFoundException("Faculty is not found");
        }

        facultyInDB.setName(faculty.getName());
        facultyInDB.setColor(faculty.getColor());

        return facultyRepository.save(facultyInDB);
    }

    public void deleteFaculty(Long facultyId) {
        logger.info("The method to delete faculty info from DB was invoked");
        if (facultyRepository.findById(facultyId).isEmpty()) {
            logger.error("Faculty with id {} is not found", facultyId);
            throw new InfoInDBNotFoundException("Faculty is not found");
        }

        facultyRepository.deleteById(facultyId);

    }

    public Collection<Faculty> facultiesOfColor(String color) {
        logger.info("The method to find faculties matching by color {} was invoked", color);

        if (facultyRepository.findByColor(color).isEmpty())
            logger.error("Faculties matching the color {} weren't found", color);

        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("The method to get all faculties was invoked");

        if (facultyRepository.findAll().isEmpty()) {
            logger.warn("The DB is empty");
            throw new DataBaseIsEmptyException("DB doesn't contain faculties");
        }

        return facultyRepository.findAll();
    }

    public Collection<Faculty> getfacultiesByNameOrColor(String name, String color) {
        logger.info("The method to find faculties matching by name{} or color {} was invoked", name, color);
        if (facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color).isEmpty()) {
            logger.warn("The DB doesn't contain faculties matching by this name or this color");
        }

        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Student> getStudentsOfFaculty(Long facultyId) {
        logger.info("The method to get student's of a faculty (id {}) was invoked", facultyId);

        return facultyRepository.findById(facultyId).map(e -> e.getStudents()).orElse(null);
    }
}
