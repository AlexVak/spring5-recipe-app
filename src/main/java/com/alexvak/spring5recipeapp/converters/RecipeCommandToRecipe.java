package com.alexvak.spring5recipeapp.converters;

import com.alexvak.spring5recipeapp.commands.RecipeCommand;
import com.alexvak.spring5recipeapp.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryCommandToCategory;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final NotesCommandToNotes notesCommandToNotes;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryCommandToCategory, IngredientCommandToIngredient ingredientCommandToIngredient, NotesCommandToNotes notesCommandToNotes) {
        this.categoryCommandToCategory = categoryCommandToCategory;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.notesCommandToNotes = notesCommandToNotes;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        if (Objects.isNull(recipeCommand)) {
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setCookTime(recipeCommand.getCookTime());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setDifficulty(recipeCommand.getDifficulty());
        recipe.setDirections(recipeCommand.getDirections());
        recipe.setNotes(notesCommandToNotes.convert(recipeCommand.getNotes()));
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setServings(recipeCommand.getServings());
        recipe.setSource(recipeCommand.getSource());
        recipe.setUrl(recipeCommand.getUrl());

        if (Objects.nonNull(recipeCommand.getCategories()) && recipeCommand.getCategories().size() > 0) {
            recipeCommand.getCategories().forEach(c ->
                    recipe.getCategories().add(categoryCommandToCategory.convert(c)));
        }

        if (Objects.nonNull(recipeCommand.getIngredients()) && recipeCommand.getIngredients().size() > 0) {
            recipeCommand.getIngredients().forEach(i ->
                    recipe.getIngredients().add(ingredientCommandToIngredient.convert(i)));
        }

        return recipe;

    }
}
