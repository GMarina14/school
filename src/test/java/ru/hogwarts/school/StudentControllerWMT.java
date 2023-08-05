package ru.hogwarts.school;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.Controller.StudentController;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.AvatarRepository;
import ru.hogwarts.school.Repository.StudentRepository;
import ru.hogwarts.school.Service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@WebMvcTest(StudentController.class)
public class StudentControllerWMT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    public StudentControllerWMT() throws Exception {
    }

    private Student createTestStudent() {
        final String name = "Sam";
        final int age = 25;
        final long id = 1L;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        return student;

    }

    @Test
    public void shouldCreateStudentTest() throws Exception {
        Student student = new Student();
        student = createTestStudent();

        when(studentService.createStudent(student)).thenReturn(student);

        ResultActions resultActions = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student))
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());

    }

    @Test
    public void shouldReturnStudentById() throws Exception {
        Student student = new Student();
        student = createTestStudent();

        when(studentService.getStudentById(student.getId())).thenReturn(student);

        ResultActions resultActions = mockMvc.perform(get("/students/by-id/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    public void shouldReturnStudentsByAge() throws Exception {
        Collection<Student> studentCollection = new ArrayList<>();

        Student studentOne = new Student(15L, "Linda", 14);
        Student studentTwo = new Student(16L, "Donna", 14);
        Student studentThree = new Student(18L, "David", 14);
        studentCollection.add(studentOne);
        studentCollection.add(studentTwo);
        studentCollection.add(studentThree);

        when((studentService.studentsInAge(14))).thenReturn(studentCollection);


        ResultActions resultActions = mockMvc.perform(get(("/students/by-age/14"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentCollection))

        );
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnAllStudents() throws Exception {
        Collection<Student> studentCollection = new ArrayList<>();

        Student studentOne = new Student(15L, "Linda", 11);
        Student studentTwo = new Student(16L, "Donna", 9);
        Student studentThree = new Student(18L, "David", 30);
        studentCollection.add(studentOne);
        studentCollection.add(studentTwo);
        studentCollection.add(studentThree);

        when((studentService.getAllStudents())).thenReturn(studentCollection);


        ResultActions resultActions = mockMvc.perform(get(("/students/all"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentCollection))

        );
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnStudentsInAgeGap() throws Exception {
        Collection<Student> studentCollection = new ArrayList<>();

        Student studentOne = new Student(15L, "Linda", 11);
        Student studentTwo = new Student(16L, "Donna", 9);
        Student studentThree = new Student(18L, "David", 30);
        studentCollection.add(studentOne);
        studentCollection.add(studentTwo);
        studentCollection.add(studentThree);

        when((studentService.getAgeGapStudents(9, 30))).thenReturn(studentCollection);

        ResultActions resultActions = mockMvc.perform(get(("/students/age-gap/9/30"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentCollection))

        );
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnFacultyOfStudent() throws Exception {
        Student student = new Student();
        student = createTestStudent();
        Faculty faculty = new Faculty(1L, "Gryffindor", "Scarlet and gold");
        student.setFaculty(faculty);

        when(studentService.getFacultyOfStudent(student.getId())).thenReturn(faculty);

        ResultActions resultActions = mockMvc.perform(get(("/students/" + student.getId() + "/student"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty))

        );
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void shouldUpdateStudent() throws Exception {
        final String name = "Sam";
        final int age = 25;
        final long id = 1L;

        Student student = new Student(id, name, age);

        when(studentService.updateStudent(student.getId(), student)).thenReturn(student);


        ResultActions resultActions = mockMvc.perform(put("/students/update/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student))
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    public void shouldExpelStudent() throws Exception {
        Student student = new Student();
        student = createTestStudent();

        ResultActions resultActions = mockMvc.perform(delete("/students/expel/" + student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnStatuses() throws Exception {
        ResultActions resultActionsId = mockMvc.perform(
                get("/students/by-id/10005000440")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        resultActionsId
                .andExpect(status().isNotFound());
        //**********************************************************************************************************
        Collection<Student> collectionOfStudents = new ArrayList<>();

        ResultActions resultActionsStudents = mockMvc.perform(
                get("/students/by-age/54")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collectionOfStudents))

        );
        resultActionsStudents
                .andExpect(status().isNotFound());
        //**********************************************************************************************************
        ResultActions resultActionsAllStudents = mockMvc.perform(
                get("/students/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(collectionOfStudents))

        );
        resultActionsAllStudents
                .andExpect(status().isNotFound());
        //**********************************************************************************************************
        Faculty faculty = new Faculty();
        ResultActions resultActionsFaculty = mockMvc.perform(
                get("/students/658885445}/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty))
        );
        resultActionsFaculty.andExpect(status().isBadRequest());
        //**********************************************************************************************************
        Student student = new Student();
        ResultActions resultActionsStudentUpdate = mockMvc.perform(put("/students/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));
        resultActionsStudentUpdate
                .andExpect(status().isBadRequest());
    }
}
