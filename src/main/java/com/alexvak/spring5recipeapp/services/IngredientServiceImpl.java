package com.alexvak.spring5recipeapp.services;

import com.alexvak.spring5recipeapp.commands.IngredientCommand;
import com.alexvak.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.alexvak.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.alexvak.spring5recipeapp.domain.Ingredient;
import com.alexvak.spring5recipeapp.domain.Recipe;
import com.alexvak.spring5recipeapp.exceptions.IngredientNotFoundException;
import com.alexvak.spring5recipeapp.exceptions.RecipeNotFoundException;
import com.alexvak.spring5recipeapp.exceptions.UnitOfMeasureNotFoundException;
import com.alexvak.spring5recipeapp.repositories.RecipeRepository;
import com.alexvak.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
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

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        Optional<Recipe> optionalRecipe = recipeRepository.findById(command.getRecipeId());

        if (!optionalRecipe.isPresent()) {
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        }

        Recipe recipe = optionalRecipe.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(i -> i.getId().equals(command.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredient = ingredientOptional.get();
            ingredient.setDescription(command.getDescription());
            ingredient.setAmount(command.getAmount());
            ingredient.setUom(unitOfMeasureRepository.findById(command.getUom().getId())
                    .orElseThrow(() -> new UnitOfMeasureNotFoundException(command.getUom().getId())));
        } else {
            recipe.addIngredient(ingredientCommandToIngredient.convert(command));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<IngredientCommand> ingredientCommandOptional = savedRecipe.getIngredients().stream().filter(i -> i.getId().equals(command.getId()))
                .map(ingredientToIngredientCommand::convert).findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            throw new IngredientNotFoundException(command.getId());
        }

        return ingredientCommandOptional.get();
    }
}
