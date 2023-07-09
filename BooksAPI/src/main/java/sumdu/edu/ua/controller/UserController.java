package sumdu.edu.ua.controller;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sumdu.edu.ua.entities.User;
import sumdu.edu.ua.repository.UserRepository;
import sumdu.edu.ua.services.FileService;

import java.util.Objects;


@RestController
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Value("${default.image.avatar}")
    private String defaultAvatarName;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, FileService fileService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(
            path = "/{username}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @RequestMapping(
            produces = { "application/json"},
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody User user) {
        try {
            if (user.getPassword().isEmpty()) {
                userRepository.findByUsername(user.getUsername())
                        .ifPresent(oldUser -> user.setPassword(oldUser.getPassword()));
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.update(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @RequestMapping(
            path = "/avatar/{username}",
            produces = { "application/json"},
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void uploadAvatar(@RequestParam MultipartFile file, @PathVariable String username) {
        try {
            String oldFileName = userRepository.getAvatar(username);
            if (oldFileName != null) {
                fileService.deleteFile(oldFileName);
            }
            String fileName = file.getOriginalFilename();
            fileService.uploadFile(file);
            userRepository.updateAvatar(fileName, username);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @RequestMapping(
            path = "/avatar/{username}",
            produces = { "application/octet-stream"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ByteArrayResource downloadAvatar(@PathVariable String username) {
        String avatar = userRepository.getAvatar(username);
        return fileService.downloadFile(Objects.requireNonNullElse(avatar, defaultAvatarName));
    }

    @RequestMapping(
            path = "/check/{username}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean checkUserExists(@PathVariable String username) {
        return userRepository.existsByUsername(username);
    }
}
