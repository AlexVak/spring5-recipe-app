package com.alexvak.spring5recipeapp.exceptions;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(Long id) {
        super("Recipe not found. Id: " + id);
    }
}
