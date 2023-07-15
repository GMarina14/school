package ru.hogwarts.school.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Service.StudentService;

import java.util.Collection;

@RequestMapping("/students")
@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/by-id/{studentId}")
    public ResponseEntity<Student> getStudentByID(@PathVariable Long studentId) {
        Student student = studentService.getStudentId(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/by-age/{age}")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@PathVariable Integer age) {
        Collection<Student> students = studentService.studentsInAge(age);
        if(students.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(students);
    }
    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> getAll(){
        Collection<Student> students = studentService.getAllStudents();
        if(students.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(students);

    }
    @PutMapping("/update/{studentId}")
    public ResponseEntity<Student> editStudentInfo(@PathVariable Long studentId, @RequestBody Student student) {
        Student studentEdited = studentService.updateStudent(studentId, student);
        if (studentEdited == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(studentEdited);
    }

    @DeleteMapping("/expel/{studentId}")
    public ResponseEntity<Student> expelStudent(@PathVariable Long studentId) {
        Student expeledStudent = studentService.deleteStudent(studentId);
        if (expeledStudent == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(expeledStudent);
    }
}

