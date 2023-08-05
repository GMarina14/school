package ru.hogwarts.school;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.Controller.FacultyController;
import ru.hogwarts.school.Controller.StudentController;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.FacultyRepository;
import ru.hogwarts.school.Service.FacultyService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTestWMT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyService facultyService;

    private Faculty createTestFaculty() {
        final String name = "Gryffindor";
        final String color = "Scarlet and gold";
        final long id = 1L;

        Faculty faculty = new Faculty(id, name, color);
        return faculty;

    }


    @Test
    public void shouldCorrectlyAddFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty = createTestFaculty();


        when(facultyService.createFaculty(faculty)).thenReturn(faculty);


        ResultActions resultActions = mockMvc.perform(post("/faculties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty))
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());

    }


    @Test
    public void shouldFindByFacultyId() throws Exception {
        Faculty faculty = new Faculty();
        faculty = createTestFaculty();


        when(facultyService.createFaculty(faculty)).thenReturn(faculty);


        when(facultyService.getFacultyId(faculty.getId())).thenReturn(faculty);

        ResultActions resultActions = mockMvc.perform(get("/faculties/by-id/" + faculty.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty))
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());

    }

    @Test
    public void shouldReturnCollectionByNameOrColor() throws Exception {
        Collection<Faculty> collection = new ArrayList<>();
        Faculty facultyOne = new Faculty(12L, "First", "White");
        Faculty facultyTwo = new Faculty(12L, "First", "Green");
        Faculty facultyThree = new Faculty(12L, "Three", "White");
        collection.add(facultyOne);
        collection.add(facultyTwo);
        collection.add(facultyThree);

        when(facultyService.getfacultiesByNameOrColor("First", "White")).thenReturn(collection);

        ResultActions resultActions = mockMvc.perform(get(("/faculties/by-name-or-color/First/White"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collection))

        );
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnAllFaculties() throws Exception {
        Collection<Faculty> collection = new ArrayList<>();
        Faculty facultyOne = new Faculty(12L, "First", "White");
        Faculty facultyTwo = new Faculty(12L, "First", "Green");
        Faculty facultyThree = new Faculty(12L, "Three", "White");
        collection.add(facultyOne);
        collection.add(facultyTwo);
        collection.add(facultyThree);

        when(facultyService.getAllFaculties()).thenReturn(collection);

        ResultActions resultActions = mockMvc.perform(get(("/faculties/all"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collection))

        );
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnStudentsOfFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty = createTestFaculty();
        Collection<Student> collection = new ArrayList<>();
        Student studentOne = new Student(1222L, "Sam", 25);
        studentOne.setFaculty(faculty);
        collection.add(studentOne);

        Student studentTwo = new Student(12L, "Nina", 18);
        studentTwo.setFaculty(faculty);
        collection.add(studentTwo);

        when(facultyService.getStudentsOfFaculty(faculty.getId())).thenReturn(collection);


        ResultActions resultActions = mockMvc.perform(get(("/faculties/students/" + faculty.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collection))

        );
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test // doesn't work
    public void shouldUpdateFacultyInfo() throws Exception {
        Long id = 12L;
        String name = "Some";
        String color = "color";

        Faculty faculty = new Faculty(id, color, name);

        when(facultyService.updateFaculty(faculty.getId(), faculty)).thenReturn(faculty);

        ResultActions resultActions = mockMvc.perform(put("/faculties/update/" + faculty.getId())
                .content(objectMapper.writeValueAsString(faculty))
                .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON)

        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void shouldDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty = createTestFaculty();

        ResultActions resultActions = mockMvc.perform(delete("/faculties/delete/" + faculty.getId())
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
                get("/faculties/by-id/10005000440")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        resultActionsId
                .andExpect(status().isNotFound());
        //**********************************************************************************************************
        ResultActions resultActionsColor = mockMvc.perform(
                get("/faculties/by-color/anyColor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        resultActionsColor
                .andExpect(status().isNotFound());
        //**********************************************************************************************************
        ResultActions resultActionsNameOrColor = mockMvc.perform(
                get("/faculties/by-name-or-color/anyName/anyColor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        resultActionsNameOrColor
                .andExpect(status().isNotFound());

        //**********************************************************************************************************
        Collection<Faculty> collection = new ArrayList<>();

        ResultActions resultActionsAllFaculties = mockMvc.perform(get(("/faculties/all"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collection))

        );
        resultActionsAllFaculties
                .andExpect(status().isNotFound());

        //**********************************************************************************************************
        Collection<Student> collectionOfStudents = new ArrayList<>();

        ResultActions resultActionsStudents = mockMvc.perform(get(("/faculties/students/5445454545"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(collectionOfStudents))

        );
        resultActionsStudents
                .andExpect(status().isBadRequest());
        //**********************************************************************************************************

    }


}
