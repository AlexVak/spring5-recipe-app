package com.alexvak.spring5recipeapp.exceptions;

public class IngredientNotFoundException extends RuntimeException {

    public IngredientNotFoundException(Long id) {
        super("Ingredient not found. Id: " + id);
    }
}
