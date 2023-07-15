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

    @GetMapping("/by-id/{facultyId}")
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
        if(faculties.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(faculties);
    }
    @GetMapping("/all")
    public ResponseEntity<Collection<Faculty>> getAll(){
        Collection<Faculty> faculties = facultyService.getAllFaculties();
        if(faculties.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(faculties);

    }

    @PutMapping("/update/{facultyId}")
    public ResponseEntity<Faculty> editFacultyInfo(@PathVariable Long facultyId, @RequestBody Faculty faculty) {
        Faculty facultyEdited = facultyService.updateFaculty(facultyId, faculty);
        if (facultyEdited == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(facultyEdited);
    }

    @DeleteMapping("/delete/{facultyId}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long facultyId) {
        Faculty deletedFaculty = facultyService.deleteFaculty(facultyId);
        if (deletedFaculty == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(deletedFaculty);
    }
}
