package ru.hogwarts.school.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Service.FacultyService;

import java.util.Collection;

@RequestMapping("/faculties")
@RestController
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/by-id/{facultyId}") //throws 500 if not found
    public ResponseEntity<Faculty> getfacultyByID(@PathVariable Long facultyId) {
        Faculty faculty = facultyService.getFacultyId(facultyId);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/by-color/{color}")
    public ResponseEntity<Collection<Faculty>> getFacultyByColor(@PathVariable String color) {
        Collection<Faculty> faculties = facultyService.facultiesOfColor(color);
        if (faculties.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/by-name-or-color/{name}/{color}")
    public ResponseEntity<Collection<Faculty>> getFacultyByNameOrByColor(@PathVariable String name, @PathVariable String color) {
        Collection<Faculty> faculties = facultyService.getfacultiesByNameOrColor(name, color);
        if (faculties.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Faculty>> getAll() {
        Collection<Faculty> faculties = facultyService.getAllFaculties();
        if (faculties.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(faculties);

    }

    @GetMapping("/students/{facultyId}")
    public ResponseEntity<Collection<Student>> getStudentsOfFaculty(@PathVariable Long facultyId) {
        Collection<Student> students = facultyService.getStudentsOfFaculty(facultyId);
        if (students == null || students.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/longest-name")
    public ResponseEntity<String> getLongestFacultyName() {
        String longestName = facultyService.getLongestFacultyName();
        if (longestName.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(longestName);
    }

    @PutMapping("/update/{facultyId}")
    public ResponseEntity<Faculty> editFacultyInfo(@PathVariable Long facultyId, @RequestBody Faculty faculty) {
        Faculty facultyEdited = facultyService.updateFaculty(facultyId, faculty);
        if (facultyEdited == null)
            return ResponseEntity.badRequest().build();

      /*  Faculty facultyEdited = facultyService.getFacultyId(facultyId);

        if (facultyEdited == null)
            return ResponseEntity.badRequest().build();

        facultyEdited = facultyService.updateFaculty(facultyId, faculty);*/
        return ResponseEntity.ok(facultyEdited);
    }

    @DeleteMapping("/delete/{facultyId}")
    public ResponseEntity deleteFaculty(@PathVariable Long facultyId) {
        facultyService.deleteFaculty(facultyId);
        return ResponseEntity.ok().build();
    }
}
