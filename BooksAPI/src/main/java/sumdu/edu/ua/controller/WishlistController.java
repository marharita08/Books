package sumdu.edu.ua.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.repository.WishlistRepository;

import java.util.List;


@RestController
@RequestMapping(path = "/wishlist")
public class WishlistController {
    private final WishlistRepository wishlistRepository;

    public WishlistController(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @RequestMapping(
            path = "/{username}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getWishlist(@PathVariable String username) {
        return wishlistRepository.getWishlist(username);
    }

    @RequestMapping(
            path = "/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addBook(@PathVariable String username, @PathVariable int id) {
        wishlistRepository.save(id, username);
    }

    @RequestMapping(
            path = "/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable String username, @PathVariable int id) {
        wishlistRepository.delete(id, username);
    }

    @RequestMapping(
            path = "/check/{username}/{id}",
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean isBookInWishlist(@PathVariable String username, @PathVariable int id) {
        return wishlistRepository.isBookInWishList(id, username);
    }
}
