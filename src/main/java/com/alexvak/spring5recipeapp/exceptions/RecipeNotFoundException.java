package com.alexvak.spring5recipeapp.exceptions;

public class RecipeNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Recipe not found. Id: %s";

    public RecipeNotFoundException(Long id) {
        this(String.valueOf(id));
    }

    public RecipeNotFoundException(String id) {
        super(String.format(NOT_FOUND, id));
    }
}
