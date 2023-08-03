package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.Controller.StudentController;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;

import java.util.Collection;
import java.util.Map;
import java.util.SimpleTimeZone;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestTRT {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contexLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setId(100500L);
        student.setName("Sammy");
        student.setAge(24);

        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/students", student, Student.class))
                .isNotNull();
    }

    @Test
    public void testIfReturnsById() {
        Assertions.assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/students/by-id" + 102, Student.class))
                .isNotNull();
    }

    @Test
    public void testIfReturnsByAge() {
        Assertions.assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/students/by-age" + 25, Student.class))
                .isNotNull();
    }

    @Test
    public void testIfReturnsStudentsInAgeGap() {
        Assertions.assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/students/age-gap/10/25", Collection.class))
                .isNotNull();
    }

    @Test
    public void testIfReturnsFacultyByStudentId() {
        Assertions.assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/students/5/student", Faculty.class))
                .isNotNull();
    }

    @Test
    public void testIfUpdatesStudentInfo(){

        Student dummy = new Student(102L, "Sammy", 80 );


   //  Assertions.assertThat(this.restTemplate.put("http://localhost:" + port + "/students/update/102", dummy)));


    }

/*

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
    }*/


    @Test
    public void returnsAllStudents() {
        Assertions.assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/students/all", Collection.class))
                .isNotNull();
    }


}
