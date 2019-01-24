package com.alexvak.spring5recipeapp.converters;

import com.alexvak.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.alexvak.spring5recipeapp.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasureCommand convert(UnitOfMeasure unitOfMeasure) {

        if (Objects.isNull(unitOfMeasure)) {
            return null;
        }

        UnitOfMeasureCommand command = new UnitOfMeasureCommand();
        command.setId(unitOfMeasure.getId());
        command.setDescription(unitOfMeasure.getDescription());
        return command;
    }
}
