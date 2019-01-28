package com.alexvak.spring5recipeapp.services;

import com.alexvak.spring5recipeapp.commands.IngredientCommand;
import com.alexvak.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.alexvak.spring5recipeapp.domain.Recipe;
import com.alexvak.spring5recipeapp.exceptions.IngredientNotFoundException;
import com.alexvak.spring5recipeapp.exceptions.RecipeNotFoundException;
import com.alexvak.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            throw new RecipeNotFoundException(recipeId);
        }

        Recipe recipe = optionalRecipe.get();

        Optional<IngredientCommand> optionalIngredientCommand = recipe.getIngredients().stream()
                .filter(i -> i.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert).findFirst();

        if (!optionalIngredientCommand.isPresent()) {
            throw new IngredientNotFoundException(ingredientId);
        }

        return optionalIngredientCommand.get();
    }
}
