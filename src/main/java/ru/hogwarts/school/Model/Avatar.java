package ru.hogwarts.school.Model;
import jakarta.persistence.*;
import ru.hogwarts.school.Model.Student;


@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar(Long id, String filePath, long fileSize, String mediaType, byte[] data) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
    }

    public Avatar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
