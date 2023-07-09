package sumdu.edu.ua.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.entities.Review;
import sumdu.edu.ua.repository.AlreadyReadRepository;
import sumdu.edu.ua.repository.ReadingRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/review")
public class AlreadyReadController {
    private final AlreadyReadRepository alreadyReadRepository;
    private final ReadingRepository readingRepository;

    public AlreadyReadController(AlreadyReadRepository alreadyReadRepository, ReadingRepository readingRepository) {
        this.alreadyReadRepository = alreadyReadRepository;
        this.readingRepository = readingRepository;
    }

    @RequestMapping(
            path = "/average-rating/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getAverageRating(@PathVariable int id) {
        return alreadyReadRepository.getAverageRating(id);
    }

    @RequestMapping(
            path = "/my-rating/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getMyRating(@PathVariable int id, @PathVariable String username) {
        return alreadyReadRepository.getMyRating(id, username);
    }

    @RequestMapping(
            path = "{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviews(@PathVariable int id) {
        return alreadyReadRepository.getReviews(id);
    }

    @RequestMapping(
            path = "/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Review getReview(@PathVariable int id, @PathVariable String username) {
        return alreadyReadRepository.getReview(id, username);
    }

    @RequestMapping(
            path = "/already-read/{username}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAlreadyReadList(@PathVariable String username) {
        return alreadyReadRepository.getAlreadyReadList(username);
    }

    @RequestMapping(
            path = "/check/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean isBookInAlreadyRead(@PathVariable String username, @PathVariable int id) {
        return alreadyReadRepository.isBookInAlreadyRead(username, id);
    }

    @RequestMapping(
            produces = { "application/json"},
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addToAlreadyRead(@RequestBody Review review) {
        if (readingRepository.isBookInReadingList(review.getBookId(), review.getUsername())) {
            readingRepository.delete(review.getBookId(), review.getUsername());
        }
        alreadyReadRepository.insert(review);
    }

    @RequestMapping(
            produces = { "application/json"},
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateReview(@RequestBody Review review) {
        alreadyReadRepository.update(review);
    }

    @RequestMapping(
            path = "/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromAlreadyRead(@PathVariable String username, @PathVariable int id) {
        alreadyReadRepository.delete(username, id);
    }
}
