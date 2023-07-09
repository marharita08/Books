package sumdu.edu.ua.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.entities.Author;
import sumdu.edu.ua.entities.Book;
import sumdu.edu.ua.entities.Genre;
import sumdu.edu.ua.repository.AuthorRepository;
import sumdu.edu.ua.repository.BookRepository;
import sumdu.edu.ua.repository.GenreRepository;
import sumdu.edu.ua.services.FileService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(path = "/api")
public class ApiController {

    private static final String RAPID_API_KEY_HEADER = "X-RapidAPI-Key";
    private static final String RAPID_API_HOST_HEADER = "X-RapidAPI-Host";
    private static final String METHOD = "GET";
    private static final String MESSAGE = "Books was loaded successfully";
    private static final String BOOK_FINDER_URL = "https://book-finder1.p.rapidapi.com/api/search?";

    @Value("rapidapi.key")
    private String rapidApiKey;
    @Value("rapidapi.host")
    private String rapidApiHost;

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final FileService fileService;

    public ApiController(GenreRepository genreRepository,
                         BookRepository bookRepository,
                         AuthorRepository authorRepository,
                         FileService fileService) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.fileService = fileService;
    }

    @RequestMapping(
            produces = { "application/json"},
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getApiData(@RequestParam String author) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BOOK_FINDER_URL + author))
                .header(RAPID_API_KEY_HEADER, rapidApiKey)
                .header(RAPID_API_HOST_HEADER, rapidApiHost)
                .method(METHOD, HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray books = responseJson.getJSONArray("results");
        for (int i = 0; i < books.length(); i++) {
            JSONObject bookJson = books.getJSONObject(i);
            int id = bookJson.getInt("work_id");
            if (!bookRepository.checkExistence(id)) {
                Book book = new Book();
                book.setId(id);
                book.setTitle(bookJson.getString("title"));
                book.setDescription(bookJson.getString("summary"));
                try {
                    book.setPageAmount(bookJson.getInt("page_count"));
                    book.setCopyright(bookJson.getInt("copyright"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<Genre> allGenres = genreRepository.findAll();
                Set<Genre> bookGenres = new HashSet<>();
                JSONArray categories = bookJson.getJSONArray("categories");
                JSONArray subjectTags = bookJson.getJSONArray("subject_tags");
                for (Genre genre : allGenres) {
                    for (int j = 0; j < categories.length(); j++) {
                        if (categories.getString(j).toLowerCase().contains(genre.getGenre().toLowerCase())) {
                            bookGenres.add(genre);
                        }
                    }
                    for (int j = 0; j < subjectTags.length(); j++) {
                        if (subjectTags.getString(j).toLowerCase().contains(genre.getGenre().toLowerCase())) {
                            bookGenres.add(genre);
                        }
                    }
                }
                book.setGenres(new ArrayList<>(bookGenres));
                List<Author> authors = new ArrayList<>();
                JSONArray authorsJson = bookJson.getJSONArray("authors");
                for (int j = 0; j < authorsJson.length(); j++) {
                    String name = authorsJson.getString(j);
                    Author currAuthor;
                    if (!authorRepository.checkExistence(name)) {
                        currAuthor = new Author(name);
                        int authorId = authorRepository.save(currAuthor);
                        currAuthor.setId(authorId);
                    } else {
                        currAuthor = authorRepository.findByName(name);
                    }
                    authors.add(currAuthor);
                }
                book.setAuthors(authors);



                HttpURLConnection conn = null;
                JSONArray publishedWorks = bookJson.getJSONArray("published_works");
                try {
                    URL url = new URL(publishedWorks.getJSONObject(publishedWorks.length() - 1).get("cover_art_url").toString());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream input = conn.getInputStream();
                    String fileName = id + ".jpg";
                    fileService.uploadFile(input, fileName, "application/octet-stream");
                    book.setCoverPhoto(fileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                bookRepository.save(book);
            }
        }
        return MESSAGE;
    }
}
