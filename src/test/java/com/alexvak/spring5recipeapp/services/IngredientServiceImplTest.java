package com.alexvak.spring5recipeapp.services;

import com.alexvak.spring5recipeapp.commands.IngredientCommand;
import com.alexvak.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.alexvak.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.alexvak.spring5recipeapp.domain.Ingredient;
import com.alexvak.spring5recipeapp.domain.Recipe;
import com.alexvak.spring5recipeapp.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    private IngredientService ingredientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand);
    }

    @Test
    public void findByRecipeIdAndIngredientId() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);
        assertEquals(Long.valueOf(3L), ingredientCommand.getId());
        assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyLong());
    }
}