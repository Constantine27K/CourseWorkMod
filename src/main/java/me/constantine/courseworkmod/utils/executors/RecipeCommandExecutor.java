package me.constantine.courseworkmod.utils.executors;

import me.constantine.courseworkmod.CourseWorkMod;
import me.constantine.courseworkmod.utils.containers.MessagesContainer;
import me.constantine.courseworkmod.utils.recipes.RecipeReader;
import net.minecraft.util.text.StringTextComponent;

public class RecipeCommandExecutor {
    static RecipeReader recipeReader;

    public static void process(String[] args) {
        if (args.length == 1) {
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.BAD_RECIPE));
            return;
        }
        if (args[1].equals("help")) {
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.RECIPE_HELP));
        }
    }
}