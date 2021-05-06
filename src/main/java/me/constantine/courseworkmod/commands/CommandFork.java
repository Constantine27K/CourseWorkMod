package me.constantine.courseworkmod.commands;

import me.constantine.courseworkmod.utils.executors.HelpCommandExecutor;
import me.constantine.courseworkmod.utils.executors.MobCommandExecutor;
import me.constantine.courseworkmod.utils.executors.RecipeCommandExecutor;

public class CommandFork {
    private String command;

    public CommandFork(String command) {
        this.command = command;
        process();
    }

    public boolean process() {
        if (command.length() <= 3) return false;
        String cut = command.substring(3);
        String[] args = cut.split(" ");
        if (args.length == 0) return false;

        if (args[0].equals("help")) {
            HelpCommandExecutor.process();
            return true;
        } else if (args[0].equals("mob")) {
            MobCommandExecutor.process(args);
            return true;
        } else if (args[0].contains("recipe")) {
            RecipeCommandExecutor.process(args);
            return true;
        } else
            return false;
    }
}
