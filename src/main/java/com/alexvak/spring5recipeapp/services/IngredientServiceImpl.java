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

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()) {
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                        .findById(command.getUom().getId())
                        .orElseThrow(() -> new UnitOfMeasureNotFoundException(command.getUom().getId())));
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            if (!savedIngredientOptional.isPresent()) {
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            //to do check for fail
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }
    }
}
