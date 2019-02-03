package com.alexvak.spring5recipeapp.controllers;

import com.alexvak.spring5recipeapp.commands.IngredientCommand;
import com.alexvak.spring5recipeapp.commands.RecipeCommand;
import com.alexvak.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.alexvak.spring5recipeapp.exceptions.RecipeNotFoundException;
import com.alexvak.spring5recipeapp.services.IngredientService;
import com.alexvak.spring5recipeapp.services.RecipeService;
import com.alexvak.spring5recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final UnitOfMeasureService unitOfMeasureService;


    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
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

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(
            @PathVariable String recipeId,
            @PathVariable String ingredientId,
            Model model) {

        model.addAttribute("ingredient",
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(
            @PathVariable String recipeId,
            Model model) {

        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        if (Objects.isNull(recipeCommand)) {
            throw new RecipeNotFoundException(recipeId);
        }

        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(recipeCommand.getId());
        model.addAttribute("ingredient", command);

        command.setUom(new UnitOfMeasureCommand());
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());


        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved recipe id: " + command.getRecipeId());
        log.debug("saved ingredient id: " + command.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredients";
    }

}
