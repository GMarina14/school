package ru.hogwarts.school;

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
import ru.hogwarts.school.Controller.FacultyController;
import ru.hogwarts.school.Controller.StudentController;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.FacultyRepository;
import ru.hogwarts.school.Repository.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FacultyControllerTestTRT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyController facultyController;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @AfterEach
    public void resetDb() {
        facultyRepository.deleteAll();
    }

    @Test
    public void contexLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testCreateFaculty() {
        //given
        Faculty faculty = new Faculty();
        faculty = persistTestFaculty("Gryffindor", "Scarlet and gold");

        //when
        ResponseEntity<Faculty> response = restTemplate.postForEntity("/faculties", faculty, Faculty.class);

        //then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    public void testIfReturnsById() {
        //given
        Faculty faculty = new Faculty();
        faculty = persistTestFaculty("Gryffindor", "Scarlet and gold");

        Long facultyId = faculty.getId();

        //when
        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculties/by-id/{facultyId}", Faculty.class, facultyId);

        //then
        Faculty facultyResult = response.getBody();
        Assertions.assertThat(facultyResult).isNotNull();
        Assertions.assertThat(facultyResult).isEqualTo(faculty);

    }

    @Test
    public void testIfReturnsByColor() {
        //given
        Collection<Faculty> facultyCollection = new ArrayList<>();

        Faculty facultyOne = persistTestFaculty("First", "white");
        facultyCollection.add(facultyOne);
        Faculty facultyTwo = persistTestFaculty("Second", "white");
        facultyCollection.add(facultyTwo);
        Faculty facultyThree = persistTestFaculty("Three", "black");
        facultyCollection.add(facultyThree);

        //when
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange("/faculties/by-color/white",
                HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Faculty>>() {
                });

        //then
        Collection<Faculty> facultiesResult = response.getBody();
        Assertions.assertThat(facultiesResult).hasSize(2);
        Assertions.assertThat(facultiesResult.contains(facultyOne));
    }
    @Test
    public void testIfReturnsByNameOrColor() {
        //given
        Collection<Faculty> facultyCollection = new ArrayList<>();

        Faculty facultyOne = persistTestFaculty("First", "white");
        facultyCollection.add(facultyOne);
        Faculty facultyTwo = persistTestFaculty("Second", "white");
        facultyCollection.add(facultyTwo);
        Faculty facultyThree = persistTestFaculty("Three", "black");
        facultyCollection.add(facultyThree);

        //when
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange("/faculties/by-name-or-color/Three/white",
                HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Faculty>>() {
                });

        //then
        Collection<Faculty> facultiesResult = response.getBody();
        Assertions.assertThat(facultiesResult).hasSize(3);
        Assertions.assertThat(facultiesResult.contains(facultyTwo));
    }

    @Test
    public void returnsAllFaculties() {
        //given
        Collection<Faculty> facultyCollection = new ArrayList<>();

        Faculty facultyOne = persistTestFaculty("First", "white");
        facultyCollection.add(facultyOne);
        Faculty facultyTwo = persistTestFaculty("Second", "white");
        facultyCollection.add(facultyTwo);
        Faculty facultyThree = persistTestFaculty("Three", "black");
        facultyCollection.add(facultyThree);

        //when
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange("/faculties/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Faculty>>() {
                });

        //then
        Collection<Faculty> facultiesResult = response.getBody();
        Assertions.assertThat(facultiesResult).isNotNull();
        Assertions.assertThat(facultiesResult).isEqualTo(facultyCollection);
    }
    @Test //Doesnt work
    public void testIfReturnsStudentsOfFaculty(){
        //given
        Faculty faculty = new Faculty();
        faculty = persistTestFaculty("Gryffindor", "Scarlet and gold");

        Collection<Student> students = new ArrayList<>();
        Student student = new Student(1L, "Luna", 9);
        student.setFaculty(faculty);

        Student studentTwo = new Student(2L, "Fred", 14);
        studentTwo.setFaculty(faculty);
        students.add(student);
        students.add(studentTwo);
        studentRepositoryMock.save(student);
        studentRepositoryMock.save(studentTwo);

        ResponseEntity<Collection<Student>> response = restTemplate.exchange("/faculties/students/"+faculty.getId(),
                HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
                });

        //then
        Collection<Student> studentResult = response.getBody();
        Assertions.assertThat(studentResult).isNotNull();
        Assertions.assertThat(studentResult).isEqualTo(students);

        studentRepositoryMock.deleteAll();
    }


    @Test
    public void testIfUpdatesFacultyInfo() {
        Faculty faculty = persistTestFaculty("Hufflepuff", "yellow and black");
        Long facultyId= faculty.getId();
        HttpEntity<Faculty> entity = new HttpEntity<Faculty>(faculty);

        //when
        ResponseEntity<Faculty> response = restTemplate.exchange("/faculties/update/{facultyId}", HttpMethod.PUT, entity, Faculty.class, facultyId);

        //then
       Faculty facultyResult = response.getBody();
        Assertions.assertThat(facultyResult).isNotNull();
        Assertions.assertThat(facultyResult).isEqualTo(faculty);
    }
    @Test
    public void testIfDeletes(){
        Faculty faculty = persistTestFaculty("Hufflepuff", "yellow and black");
        Long facultyId= faculty.getId();

        restTemplate.delete("/faculties/delete/{facultyId}", facultyId);

        Assertions.assertThat(facultyRepository.findById(facultyId)).isEmpty();
    }


    private Faculty persistTestFaculty(String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);

        return facultyRepository.save(faculty);
    }


}
