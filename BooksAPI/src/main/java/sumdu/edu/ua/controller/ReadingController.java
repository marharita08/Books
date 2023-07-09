package sumdu.edu.ua.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.repository.ReadingRepository;
import sumdu.edu.ua.repository.WishlistRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/reading")
public class ReadingController {

    private final ReadingRepository readingRepository;
    private final WishlistRepository wishlistRepository;

    public ReadingController(ReadingRepository readingRepository, WishlistRepository wishlistRepository) {
        this.readingRepository = readingRepository;
        this.wishlistRepository = wishlistRepository;
    }

    @RequestMapping(
            path = "/{username}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getReadingList(@PathVariable String username) {
        return readingRepository.getReadingList(username);
    }

    @RequestMapping(
            path = "/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addBook(@PathVariable String username, @PathVariable int id) {
        if (wishlistRepository.isBookInWishList(id, username)) {
            wishlistRepository.delete(id, username);
        }
        readingRepository.save(id, username);
    }

    @RequestMapping(
            path = "/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable String username, @PathVariable int id) {
        readingRepository.delete(id, username);
    }

    @RequestMapping(
            path = "/check/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean isBookInReadingList(@PathVariable String username, @PathVariable int id) {
        return readingRepository.isBookInReadingList(id, username);
    }
}
