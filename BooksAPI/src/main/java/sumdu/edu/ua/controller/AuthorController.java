package sumdu.edu.ua.controller;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.entities.Author;
import sumdu.edu.ua.repository.AuthorRepository;
import sumdu.edu.ua.services.FileService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/author")
public class AuthorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);
    @Value("${default.image.author}")
    private String defaultAuthorPhotoName;
    private final AuthorRepository authorRepository;
    private final FileService fileService;

    public AuthorController(AuthorRepository authorRepository, FileService fileService) {
        this.authorRepository = authorRepository;
        this.fileService = fileService;
    }

    @RequestMapping(
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @RequestMapping(
            value = "/image/{id}",
            produces = { "application/octet-stream"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ByteArrayResource downloadPhoto(@PathVariable int id) {
        String photo = authorRepository.getPhoto(id);
        return fileService.downloadFile(Objects.requireNonNullElse(photo, defaultAuthorPhotoName));
    }

    @RequestMapping(
            value = "/search",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAuthorsForSearch() {
        return authorRepository.getAuthorsForSearch();
    }
}
