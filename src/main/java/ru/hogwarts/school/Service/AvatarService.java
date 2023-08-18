package ru.hogwarts.school.Service;

import io.github.classgraph.ResourceList;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.Mapper.AvatarMapper;
import ru.hogwarts.school.Model.Avatar;
import ru.hogwarts.school.Model.Student;
import ru.hogwarts.school.Repository.AvatarRepository;
import ru.hogwarts.school.dto.AvatarDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarDir;

    private final StudentService studentService; // studentRepository
    private final AvatarRepository avatarRepository;
    private final AvatarMapper avatarMapper;

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);


    public AvatarService(StudentService studentService, AvatarRepository avatarRepository, AvatarMapper avatarMapper) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
        this.avatarMapper = avatarMapper;
    }

    public void uploadAvatar(Long studentId, MultipartFile photos) throws IOException {
        logger.info("The method to upload an avatar of a student by id {} was invoked", studentId);

        Student student = studentService.getStudentById(studentId);
        Path filePath = saveToDisk(student, photos);
        saveToDb(student, photos, filePath);
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("The method to find an avatar by student's id {} was invoked", studentId);

        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    public Collection<Avatar> getAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("Method for paginating students avatars was invoked");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        return avatarRepository.findAll(pageRequest).getContent();
    }

    public List<AvatarDTO> getAvatarsList(Integer pageNumber, Integer pageSize) {
        logger.info("Method for convenient paginating students avatars was invoked");

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        return avatarRepository.findAll(pageRequest).getContent().stream().map(avatarMapper::mapToDTO).collect(Collectors.toList());

    }

    private void saveToDb(Student student, MultipartFile photos, Path filePath) throws IOException {
        logger.info("Method to save avatar to DB was invoked");

        Avatar avatar = findAvatar(student.getId());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(photos.getSize());
        avatar.setMediaType(photos.getContentType());
        avatar.setData(generatePhotoData(filePath));//photos.getBytes()
        avatarRepository.save(avatar);

    }

    private Path saveToDisk(Student student, MultipartFile photos) throws IOException {
        logger.info("Method to save avatar to disk was invoked");

        Path filePath = Path.of(avatarDir, student.getId() + "." + getExtension(photos.getOriginalFilename()));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = photos.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        return filePath;
    }

    private byte[] generatePhotoData(Path filePath) throws IOException {
        logger.info("Method to generate avatar data was invoked");

        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage photo = ImageIO.read(bis);

            int height = photo.getHeight() / (photo.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, photo.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(photo, 0, 0, 100, height, null);
            graphics2D.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();

        }
    }

    private String getExtension(String fileName) {
        logger.info("Method to get file extension was invoked");

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
