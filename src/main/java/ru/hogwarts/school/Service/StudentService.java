package ru.hogwarts.school.Service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.Model.Student;

import java.util.Comparator;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private static Long generatedStudentsId = 1L;

    public Student createStudent(Student student) {
        student.setId(generatedStudentsId);
        studentMap.put(student.getId(), student);
        generatedStudentsId++;

        return studentMap.get(student.getId());
    }

    public Student getStudentId(Long studentId) {
        if (!(studentMap.containsKey(studentId)))
            return null;
        return studentMap.get(studentId);
    }

    public Student updateStudent(Long studentId, Student student) {
        if (!(studentMap.containsKey(studentId))) {
            return null;
        } else {
            Student studentUpdated = studentMap.get(studentId);
            studentUpdated.setName(student.getName());
            studentUpdated.setAge(student.getAge());
            return studentUpdated;
        }
    }

    public Student deleteStudent(Long studentId) {
        if (!(studentMap.containsKey(studentId))) {
            return null;
        } else {
            return studentMap.remove(studentId);
        }
    }

    public Collection<Student> studentsInAge(int age) {
        return studentMap.values().stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toList());
        //do I need Collectors.groupingBy(Student::getAge());
    }

    public Collection<Student> getAllStudents() {
        return studentMap.values().stream().toList();
    }
}
