package com.alexvak.spring5recipeapp.controllers;

import com.alexvak.spring5recipeapp.services.IngredientService;
import com.alexvak.spring5recipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;


    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String ingredientsList(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredient list for recipe Id: " + recipeId);

        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/list";
    }


    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(
            @PathVariable String recipeId,
            @PathVariable String ingredientId,
            Model model) {
        log.debug(String.format("Showing ingredient [id:%s] for recipe [id:%s]", ingredientId, recipeId));

        model.addAttribute("ingredient",
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        return "recipe/ingredient/show";
    }
}
