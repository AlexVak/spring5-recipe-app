package com.alexvak.spring5recipeapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Recipe not found. ID: %s";

    public RecipeNotFoundException(Long id) {
        this(String.valueOf(id));
    }

    public RecipeNotFoundException(String id) {
        super(String.format(NOT_FOUND, id));
    }
}
