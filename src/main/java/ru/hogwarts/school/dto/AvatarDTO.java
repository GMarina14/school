package ru.hogwarts.school.dto;

import jakarta.persistence.Lob;

import java.util.Objects;

public class AvatarDTO {
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private Long studentId;


    public AvatarDTO(Long id, String filePath, long fileSize, String mediaType, Long studentId) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.studentId = studentId;
    }

    public AvatarDTO() {
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvatarDTO avatarDTO)) return false;
        return fileSize == avatarDTO.fileSize && id.equals(avatarDTO.id) && filePath.equals(avatarDTO.filePath) && mediaType.equals(avatarDTO.mediaType) && studentId.equals(avatarDTO.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, fileSize, mediaType, studentId);
    }

    @Override
    public String toString() {
        return "AvatarDTO: \n" +
                "id " + id + '\n' +
                "filePath " + filePath + '\n' +
                "fileSize " + fileSize + '\n' +
                "mediaType " + mediaType + '\n' +
                "studentId " + studentId;
    }
}
