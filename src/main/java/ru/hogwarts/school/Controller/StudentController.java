package ru.hogwarts.school.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.Model.Faculty;
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
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/by-age/{age}")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@PathVariable Integer age) {
        Collection<Student> students = studentService.studentsInAge(age);
        if (students.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> getAll() {
        Collection<Student> students = studentService.getAllStudents();
        if (students.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(students);

    }
    @GetMapping("/all-students-info")
    public ResponseEntity<Integer> getQuantityOfAll() {

        Integer quantityOfStudents = studentService.getStudentsQuantity();
        if (quantityOfStudents==0)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(quantityOfStudents);
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getAverageAgeOfStudents() {

       Double averageAgeOfStudents = studentService.getAverageAgeOfStudents();
        if (averageAgeOfStudents==0)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(averageAgeOfStudents);
    }

    @GetMapping("/last-by-id")
    public ResponseEntity<Collection<Student>> getLastStudentsById(){
        Collection<Student> students = studentService.getLastByIdFiveStudents();
        if (students.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(students);

    }

    @GetMapping("/age-gap/{min}/{max}")
    public ResponseEntity<Collection<Student>> getStudentsInAgeGap(@PathVariable Integer min, @PathVariable Integer max){
        Collection<Student> students = studentService.getAgeGapStudents(min, max);
        if (students.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(students);

    }
    @GetMapping ("/{studentId}/student")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable Long studentId){
        Faculty facultyOfStudent = studentService.getFacultyOfStudent(studentId);
        if(facultyOfStudent==null )
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(facultyOfStudent);
    }
    @PutMapping("/update/{studentId}")
    public ResponseEntity<Student> editStudentInfo(@PathVariable Long studentId, @RequestBody Student student) {
        Student studentEdited = studentService.updateStudent(studentId, student);
        if (studentEdited == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(studentEdited);
    }

    @DeleteMapping("/expel/{studentId}")
    public ResponseEntity expelStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }
}

