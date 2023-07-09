package com.example.books.utils;

import android.content.Context;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class GlideUrlBuilder {

    private static final String JWT_TOKEN_KEY = "jwtToken";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    public static GlideUrl getGlideUrl(String url, Context context) {
        SessionManager sessionManager = new SessionManager(context.getApplicationContext());
        String jwtToken = sessionManager.getStringData(JWT_TOKEN_KEY);
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_PREFIX + jwtToken)
                .build());
    }

}
