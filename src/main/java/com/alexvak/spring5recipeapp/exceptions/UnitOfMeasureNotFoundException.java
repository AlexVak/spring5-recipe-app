package com.alexvak.spring5recipeapp.exceptions;

public class UnitOfMeasureNotFoundException extends RuntimeException {

    public UnitOfMeasureNotFoundException(Long id) {
        super("Unit of measure not found. Id: " + id);
    }
}
