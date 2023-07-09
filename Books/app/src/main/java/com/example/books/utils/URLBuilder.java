package com.example.books.utils;

public class URLBuilder {
    private static final String API_URL = "http://10.0.2.2:8080";

    //user URLs

    public static String getUserUrl() {
        return API_URL + "/user";
    }
    public static String getUserUrl(String username) {
        return getUserUrl() + "/" + username;
    }
    public static String getUserAvatarUrl() {
        return  getUserUrl() + "/avatar/";
    }
    public static String getUserAvatarUrl(String username) {
        return getUserAvatarUrl() + username;
    }
    public static String checkUsernameUrl(String username) {
        return getUserUrl() + "/check/" + username;
    }

    //book URLs

    public static String getBookUrl() {
        return API_URL + "/book/";
    }
    public static String getBookUrl(int id) {
        return getBookUrl() + id;
    }
    public static String getBookImageUrl() {
        return getBookUrl() + "image/";
    }
    public static String getBooksByAuthorUrl() {
        return getBookUrl() + "author/";
    }
    public static String getBooksByGenreUrl() {
        return getBookUrl() + "genre/";
    }
    public static String getRelatedBooksUrl(int id) {
        return getBookUrl() + "related/" + id;
    }
    public static String searchBooksUrl() {
        return getBookUrl() + "search?title=";
    }
    public static String searchBooksUrl(String title) {
        return searchBooksUrl() + title;
    }


    //author URLs

    public static String getAuthorsUrl() {
        return API_URL + "/author";
    }
    public static String getAuthorPhotoUrl() {
        return getAuthorsUrl() + "/image/";
    }

    public static String getAuthorsForSearchUrl() {
        return getAuthorsUrl() + "/search";
    }


    //genres URLs

    public static String getGenresUrl() {
        return API_URL + "/genre";
    }

    public static String getGenresForSearchUrl() {
        return getGenresUrl() + "/search";
    }


    //wishlist URLs

    public static String getWishlistUrl() {
        return API_URL + "/wishlist";
    }
    public static String getWishlistUrl(int bookId, String username) {
        return getWishlistUrl() + "/" + username + "/" + bookId;
    }
    public static String getWishlistUrl(String username) {
        return getWishlistUrl() + "/" + username;
    }
    public static String checkBookInWishlistUrl(int bookId, String username) {
        return getWishlistUrl() + "/check/" + username + "/" + bookId;
    }

    //reading list URLs

    public static String getReadingUrl() {
        return API_URL + "/reading";
    }
    public static String getReadingUrl(int bookId, String username) {
        return getReadingUrl() + "/" + username + "/" + bookId;
    }
    public static String getReadingUrl(String username) {
        return getReadingUrl() + "/" + username;
    }
    public static String checkBookInReadingUrl(int bookId, String username) {
        return getReadingUrl() + "/check/" + username + "/" + bookId;
    }


    //review URLs

    public static String getAlreadyReadUrl() {
        return API_URL + "/review";
    }
    public static String getAverageRatingUrl(int bookId) {
        return getAlreadyReadUrl() + "/average-rating/" + bookId;
    }
    public static String getMyRatingUrl(int bookId, String username) {
        return getAlreadyReadUrl() + "/my-rating/" + username + "/" + bookId;
    }
    public static String getReviewsUrl(int bookId) {
        return getAlreadyReadUrl() + "/" + bookId;
    }
    public static String getAlreadyReadUrl(int bookId, String username) {
        return getAlreadyReadUrl() + "/" + username + "/" + bookId;
    }
    public static String getReviewUrl(int bookId, String username) {
        return getAlreadyReadUrl(bookId, username);
    }
    public static String getAlreadyReadUrl(String username) {
        return getAlreadyReadUrl() + "/already-read/" + username;
    }
    public static String checkBookInAlreadyReadUrl(int bookId, String username) {
        return getAlreadyReadUrl() + "/check/" + username + "/" + bookId;
    }


    //auth URLs

    public static String getLoginUrl() {
        return API_URL + "/auth/login";
    }
    public static String getSignUpUrl() {
        return API_URL + "/auth/signup";
    }

}
