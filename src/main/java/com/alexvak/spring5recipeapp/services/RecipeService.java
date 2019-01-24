package com.alexvak.spring5recipeapp.services;

import com.alexvak.spring5recipeapp.commands.RecipeCommand;
import com.alexvak.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long Id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

}
