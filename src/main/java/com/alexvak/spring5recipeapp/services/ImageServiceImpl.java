package com.alexvak.spring5recipeapp.services;

import com.alexvak.spring5recipeapp.domain.Recipe;
import com.alexvak.spring5recipeapp.exceptions.RecipeNotFoundException;
import com.alexvak.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent()) {
            try {
                Recipe recipe = recipeOptional.get();
                Byte[] bytes = new Byte[file.getBytes().length];
                int i = 0;
                for (byte b : file.getBytes()) {
                    bytes[i++] = b;
                }
                recipe.setImage(bytes);
                recipeRepository.save(recipe);
            } catch (IOException e) {
                log.error("Error occurred", e);
            }
        } else {
            throw new RecipeNotFoundException(recipeId);
        }
    }
}
