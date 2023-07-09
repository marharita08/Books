package sumdu.edu.ua.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sumdu.edu.ua.entities.Genre;
import sumdu.edu.ua.repository.GenreRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/genre")
public class GenreController {
    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @RequestMapping(
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @RequestMapping(
            path = "/search",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getGenresForSearch() {
        return genreRepository.findGenresForSearch();
    }
}
