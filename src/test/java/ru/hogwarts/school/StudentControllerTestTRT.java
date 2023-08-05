package ru.hogwarts.school;

import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.Controller.StudentController;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.FacultyRepository;
import ru.hogwarts.school.Repository.StudentRepository;

import java.util.*;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestTRT {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepositoryTest;

    @MockBean
    private FacultyRepository facultyRepositoryMock;

    @AfterEach
    public void resetDb() {
        studentRepositoryTest.deleteAll();
    }


    @Test
    public void contexLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testCreateStudent() {
        //given
        Student student = new Student();
        student.setId(100500L);
        student.setName("Sammy");
        student.setAge(24);

        //when
        ResponseEntity<Student> response = restTemplate.postForEntity("/students", student, Student.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(student.getName());
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(student.getAge());
    }

    @Test
    public void testIfReturnsById() {
        //given
        String name = "Fred";
        int age = 17;
        Student student = persistTestStudent(name, age);
        Long studentId = student.getId();

        //when
        ResponseEntity<Student> response = restTemplate.getForEntity("/students/by-id/{studentId}", Student.class, studentId);

        //then
        Student studentResult = response.getBody();
        Assertions.assertThat(studentResult).isNotNull();
        Assertions.assertThat(studentResult).isEqualTo(student);
        /*Assertions.assertThat(studentResult.getId()).isEqualTo(studentId);
        Assertions.assertThat(studentResult.getName()).isEqualTo(name);
        Assertions.assertThat(studentResult.getAge()).isEqualTo(student.getAge());*/

    }

    @Test
    public void testIfReturnsByAge() {
        //given
        Collection<Student> studentCollection = new ArrayList<>();

        Student studentOne = persistTestStudent("Fred", 17);
        studentCollection.add(studentOne);
        Student studentTwo = persistTestStudent("Bill", 17);
        studentCollection.add(studentTwo);
        Student studentThree = persistTestStudent("Luna", 17);
        studentCollection.add(studentThree);

        int studentAge = 17;

        //when
        ResponseEntity<Collection<Student>> response = restTemplate.exchange("/students/by-age/17",
                HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
                });

        //then
        Collection<Student> studentResult = response.getBody();
        Assertions.assertThat(studentResult).isNotNull();
        Assertions.assertThat(studentResult).isEqualTo(studentCollection);
    }

    @Test
    public void returnsAllStudents() {
        //given
        Collection<Student> studentCollection = new ArrayList<>();

        Student studentOne = persistTestStudent("Fred", 14);
        studentCollection.add(studentOne);
        Student studentTwo = persistTestStudent("Bill", 17);
        studentCollection.add(studentTwo);
        Student studentThree = persistTestStudent("Luna", 11);
        studentCollection.add(studentThree);

        //when
        ResponseEntity<Collection<Student>> response = restTemplate.exchange("/students/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
                });

        //then
        Collection<Student> studentResult = response.getBody();
        Assertions.assertThat(studentResult).isNotNull();
        Assertions.assertThat(studentResult).isEqualTo(studentCollection);
    }

    @Test
    public void testIfReturnsStudentsInAgeGap() {
        //given
        Collection<Student> studentCollection = new ArrayList<>();

        Student studentOne = persistTestStudent("Fred", 14);
        studentCollection.add(studentOne);
        Student studentTwo = persistTestStudent("Bill", 17);
        studentCollection.add(studentTwo);
        Student studentThree = persistTestStudent("Luna", 9);
        studentCollection.add(studentThree);


        //when
        ResponseEntity<Collection<Student>> response = restTemplate.exchange("/students/age-gap/9/17",
                HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
                });

        //then
        Collection<Student> studentResult = response.getBody();
        Assertions.assertThat(studentResult).isNotNull();
        Assertions.assertThat(studentResult).isEqualTo(studentCollection);
    }

    @Test //doesn't work
    public void testIfReturnsFacultyByStudentId() {
        String name = "Fred";
        int age = 17;
        Student student = persistTestStudent(name, age);
        List <Student> students= new ArrayList<>();
        students.add(student);
        Faculty faculty = new Faculty(1L, "Gryffindor", "Scarlet and gold");
        student.setFaculty(faculty);
        faculty.setStudents(students);
        facultyRepositoryMock.save(faculty);

        HttpEntity<Faculty> entity = new HttpEntity<Faculty>(faculty);

        student.setId(1L);


        //when
        ResponseEntity<Faculty> response = restTemplate.exchange("/students/1/student", HttpMethod.GET, entity,
                Faculty.class);

        //then
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Gryffindor");


        facultyRepositoryMock.deleteAll();

    }

    @Test
    public void testIfUpdatesStudentInfo() {
        Student dummy = persistTestStudent("Sammy", 80);
        Long studentId= dummy.getId();
        HttpEntity<Student> entity = new HttpEntity<Student>(dummy);

        //when
        ResponseEntity<Student> response = restTemplate.exchange("/students/update/{studentId}", HttpMethod.PUT, entity, Student.class, studentId);

        //then
        Student studentResult = response.getBody();
        Assertions.assertThat(studentResult).isNotNull();
        Assertions.assertThat(studentResult).isEqualTo(dummy);
    }

    @Test
    public void testIfExpels(){
        Student dummy = persistTestStudent("Sammy", 80);
        Long studentId= dummy.getId();

       restTemplate.delete("/students/expel/{studentId}", studentId);

       Assertions.assertThat(studentRepositoryTest.findById(studentId)).isEmpty();
    }

    private Student persistTestStudent(String name, int age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);

        return studentRepositoryTest.save(student);

    }
}
