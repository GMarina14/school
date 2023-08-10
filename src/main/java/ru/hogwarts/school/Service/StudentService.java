package ru.hogwarts.school.Service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.StudentRepository;


import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    public Collection<Student> getAgeGapStudents(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Student updateStudent(Long studentId, Student student) {
        Student studentInDB = getStudentById(studentId);
        if (studentInDB == null)
            return null;// exception needed

        studentInDB.setName(student.getName());
        studentInDB.setAge(student.getAge());


        return studentRepository.save(studentInDB);
    }

    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    public Collection<Student> studentsInAge(int age) {

        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Faculty getFacultyOfStudent(Long studentId) {
        Student studentInDB = getStudentById(studentId);
        if (studentInDB == null)
            return null;
        return studentRepository.findById(studentId).map(Student::getFaculty).orElse(null);
    }

    public Collection<Student> getLastByIdFiveStudents() {
        return studentRepository.getLastByIdFiveStudents();
    }

    public Integer getStudentsQuantity() {
        return studentRepository.getStudentsQuantity();
    }

    public Double getAverageAgeOfStudents() {
        return studentRepository.getAverageAgeOfStudents();
    }


}
