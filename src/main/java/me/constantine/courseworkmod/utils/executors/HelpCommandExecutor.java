package me.constantine.courseworkmod.utils.executors;

import me.constantine.courseworkmod.CourseWorkMod;
import me.constantine.courseworkmod.utils.containers.MessagesContainer;
import net.minecraft.util.text.StringTextComponent;

public class HelpCommandExecutor {
    public static void process(){
        CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.COMMAND_HELP));
    }
}
