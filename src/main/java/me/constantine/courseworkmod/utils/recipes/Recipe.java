package me.constantine.courseworkmod.utils.recipes;

import java.util.AbstractMap;
import java.util.List;

public class Recipe {
    private String item;
    private List<AbstractMap.SimpleEntry<String, Integer>> components;

    public Recipe(String item, List<AbstractMap.SimpleEntry<String, Integer>> components) {
        this.item = item;
        this.components = components;
    }

    public String getItem() {
        return item;
    }

    public List<AbstractMap.SimpleEntry<String, Integer>> getComponents() {
        return components;
    }
}