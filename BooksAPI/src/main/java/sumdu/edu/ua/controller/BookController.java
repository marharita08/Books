package sumdu.edu.ua.controller;

import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.entities.SearchFilters;
import sumdu.edu.ua.repository.BookRepository;
import sumdu.edu.ua.services.GoogleCloudFileService;

import java.util.List;

@RestController
@RequestMapping(path = "/book")
public class BookController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    @Value("${default.image.book.cover}")
    private String defaultBookCover;
    private final BookRepository bookRepository;
    private final GoogleCloudFileService fileService;

    public BookController(BookRepository bookRepository, GoogleCloudFileService fileService) {
        this.bookRepository = bookRepository;
        this.fileService = fileService;
    }

    @RequestMapping(
            path = "/genre/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBooksByGenre(@PathVariable int id) {
        return bookRepository.findByGenreId(id);
    }

    @RequestMapping(
            path = "/author/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBooksByAuthor(@PathVariable int id) {
        return bookRepository.findByAuthorId(id);
    }

    @RequestMapping(
            path = "/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Book getBookById(@PathVariable int id) {
        return bookRepository.findById(id);
    }

    @RequestMapping(
            path = "/image/{file}",
            produces = { "application/octet-stream"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ByteArrayResource downloadImage(@PathVariable String file) {
        try {
            return fileService.downloadFile(file);
        } catch (Exception e) {
            LOGGER.error("Failed to load file " + file + "\n" + e.getMessage());
            return fileService.downloadFile(defaultBookCover);
        }
    }

    @RequestMapping(
            produces = { "application/json"},
            path = "/related/{id}",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getRelatedBooks(@PathVariable int id) {
        return bookRepository.findRelatedBooks(id);
    }

    @RequestMapping(
            produces = { "application/json"},
            path = "/search",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> searchByTitle(@RequestBody SearchFilters searchFilters, @RequestParam String title) {
        if (searchFilters != null) {
            return bookRepository.searchByTitle(title, searchFilters.getGenres(), searchFilters.getAuthors());
        } else {
            return bookRepository.searchByTitle(title, null, null);
        }
    }

}
