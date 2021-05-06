package me.constantine.courseworkmod.utils.recipes;

import java.util.AbstractMap;

public class RecipeReader {
    Recipe recipe;

    public RecipeReader(Recipe recipe) {
        this.recipe = recipe;
    }

    public String process() {
        StringBuilder builder = new StringBuilder();
        builder.append(recipe.getItem()).append(" -> ");
        for (AbstractMap.SimpleEntry<String, Integer> entry : recipe.getComponents()) {
            builder.append(entry.getKey()).append(" x").append(entry.getValue()).append(" + ");
        }
        return builder.toString();
    }
}