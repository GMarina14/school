package ru.hogwarts.school.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.Model.Avatar;
import ru.hogwarts.school.Service.AvatarService;
import ru.hogwarts.school.Service.StudentService;
import ru.hogwarts.school.dto.AvatarDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequestMapping("/avatars")
@RestController
public class AvatarController {

    private final StudentService studentService;
    private final AvatarService avatarService;

    public AvatarController(StudentService studentService, AvatarService avatarService) {
        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
      /*  if (photos.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }*/
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{studentId}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long studentId) {
        Avatar avatar = avatarService.findAvatar(studentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{studentId}/avatar-from-file")
    public void downloadAvatar(@PathVariable Long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(studentId);

        Path path = Path.of(avatar.getFilePath());

        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();) {
                response.setStatus(200);
                response.setContentType(avatar.getMediaType());
                response.setContentLength((int) avatar.getFileSize());
                is.transferTo(os);
        }
    }
    @GetMapping(value = "/list-from-file")
    public void listOfAvatars (@RequestParam("page") Integer pageNumber, @RequestParam ("size") Integer pageSize, HttpServletResponse response) throws IOException {
        Collection<Avatar> avatars = new ArrayList<>();
        avatars = avatarService.getAvatars(pageNumber, pageSize);
        Path path;

        for (Avatar avatar : avatars) {

             path = Path.of(avatar.getFilePath());

            try (
                    InputStream is = Files.newInputStream(path);
                    OutputStream os = response.getOutputStream();) {
                response.setStatus(200);
                response.setContentType(avatar.getMediaType());
                response.setContentLength((int) avatar.getFileSize());
                is.transferTo(os);
            }
        }

    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<AvatarDTO>>  getList(@RequestParam("page") Integer pageNumber, @RequestParam ("size") Integer pageSize){
        List<AvatarDTO> avatarList = avatarService.getAvatarsList(pageNumber, pageSize);
        if (avatarList.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(avatarList);
    }

    @GetMapping("/stream")
    public Integer getCalculationResult(){
        return avatarService.getCalculationResult();

    }
}
