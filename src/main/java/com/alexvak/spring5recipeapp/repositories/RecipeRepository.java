package com.alexvak.spring5recipeapp.repositories;

import com.alexvak.spring5recipeapp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {


}
