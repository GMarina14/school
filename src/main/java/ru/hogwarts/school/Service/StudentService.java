package ru.hogwarts.school.Service;

import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.Exceptions.DataBaseIsEmptyException;
import ru.hogwarts.Exceptions.InfoInDBNotFoundException;
import ru.hogwarts.school.Model.Faculty;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.StudentRepository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final Object object = new Object();

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method to create a student");
        return studentRepository.save(student);
    }

    public Student getStudentById(Long studentId) {
        logger.info("The method to get student by id was invoked");
        if (studentRepository.findById(studentId).isEmpty())
            logger.error("Student with id {} doesn't exist", studentId);
        return studentRepository.findById(studentId).orElse(null);
    }

    public Collection<Student> getAgeGapStudents(int min, int max) {
        logger.info("The method to get students in age gap was invoked");
        if (studentRepository.findByAgeBetween(min, max).isEmpty())
            logger.error("Can't find students in age gap");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Student updateStudent(Long studentId, Student student) {
        logger.info("The method to update student's info was invoked");

        Student studentInDB = getStudentById(studentId);
        if (studentInDB == null) {
            logger.error("Student with id {} is not found", studentId);
            throw new InfoInDBNotFoundException("Student is not found");
        }
        studentInDB.setName(student.getName());
        studentInDB.setAge(student.getAge());


        return studentRepository.save(studentInDB);
    }

    public void deleteStudent(Long studentId) {
        logger.info("The method to delete student's info from DB was invoked");
        if (studentRepository.findById(studentId).isEmpty()) {
            logger.error("Student with id {} is not found", studentId);
            throw new InfoInDBNotFoundException("Student is not found");
        }

        studentRepository.deleteById(studentId);
    }

    public Collection<Student> studentsInAge(int age) {
        logger.info("The method to find students in age of {} was invoked", age);

        if (studentRepository.findByAge(age).isEmpty())
            logger.error("Students in age of {} weren't found", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAllStudents() {
        logger.info("The method to get all students was invoked");

        if (studentRepository.findAll().isEmpty()) {
            logger.warn("The DB is empty");
            throw new DataBaseIsEmptyException("DB doesn't contain students");
        }
        return studentRepository.findAll();
    }

    public Faculty getFacultyOfStudent(Long studentId) {
        logger.info("The method to get information about student's faculty was invoked");

        Student studentInDB = getStudentById(studentId);
        if (studentInDB == null) {
            logger.error("Student with id {} is not found", studentId);
            throw new InfoInDBNotFoundException("Student is not found");
        }

        return studentRepository.findById(studentId).map(Student::getFaculty).orElse(null);
    }

    public Collection<Student> getLastByIdFiveStudents() {
        logger.info("The method to get five students with higher id values was invoked");

        return studentRepository.getLastByIdFiveStudents();
    }

    public Integer getStudentsQuantity() {
        logger.info("The method to get information about students quantity was invoked");
        if (studentRepository.getStudentsQuantity() == 0) {
            logger.warn("Can't count students, because DB is empty");
        }
        return studentRepository.getStudentsQuantity();
    }

    public Double getAverageAgeOfStudents() {
        logger.info("The method to get information about average age of students was invoked");

        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .getAsDouble();
    }

    public Collection<Student> getNamesStartingWithLetter(String letter) {
        return studentRepository.findAll().stream()
                .filter(e -> e.getName().startsWith(letter.toUpperCase()))
                .collect(Collectors.toList());
    }

    public String getStudentsInThreads() {
        List<Student> students = studentRepository.findAll();
        if (students.size() < 6)
            throw new RuntimeException("Not enough students");

        for (Student student : students) {
            System.out.println(student.getName());
        }

        System.out.println();

        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());

        new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }).start();

        System.out.println(students.get(4).getName());
        new Thread(() -> {
            System.out.println(students.get(5).getName());
            System.out.println(students.get(6).getName());
        }).start();

        return "All Good";
    }

    public String getStudentsSynchronized() {
        List<Student> students = studentRepository.findAll().subList(0,7);
        for (int i = 0; i <6; i++) {
            System.out.println(students.get(i).getName());
        }

        System.out.println();

        output(students.get(0));
        output(students.get(1));


        Thread one = new Thread(() -> {
            output(students.get(2));
            if (Thread.currentThread().isInterrupted())
                throw new RuntimeException();

            output(students.get(3));
        });
        one.start();

      //  output(students.get(4));

        Thread two = new Thread(() -> {
            output(students.get(4));
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException();
            }
            output(students.get(5));
        });
        two.start();

        return "All good";
    }

    private void output(Student student) {
        synchronized (object) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(student.getName());
        }
    }


}
