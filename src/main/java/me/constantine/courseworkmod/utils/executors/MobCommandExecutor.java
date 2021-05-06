package me.constantine.courseworkmod.utils.executors;

import me.constantine.courseworkmod.CourseWorkMod;
import me.constantine.courseworkmod.entity.Items;
import me.constantine.courseworkmod.utils.containers.MessagesContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class MobCommandExecutor {
    public static void process(String[] args) {
        if (args.length == 1)
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.COMMAND_ERROR));
        else if (args[1].equals("back"))
            back();
        else if (args[1].equals("spawn"))
            spawn();
        else if (args[1].equals("standby"))
            standBy();
        else if (args[1].equals("notstandby"))
            notStandBy();
        else if (args[1].equals("teleport"))
            teleport();
        else if (args[1].equals("die"))
            die();
        else if (args[1].equals("help"))
            help();
    }

    private static void back() {
        if (CourseWorkMod.MOB != null)
            CourseWorkMod.MOB.goalsPresent = false;
        else
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.MOB_NULL));
    }

    private static void spawn() {
        if (CourseWorkMod.PLAYER.isCreative())
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.PLAYER_CREATIVE));
        CourseWorkMod.PLAYER.inventory.addItemStackToInventory(new ItemStack(Items.entityEgg));
        CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.EGG_ADDED));
    }

    private static void standBy() {
        if (CourseWorkMod.MOB != null)
            CourseWorkMod.MOB.standBy = false;
        else
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.MOB_NULL));
    }

    private static void notStandBy() {
        if (CourseWorkMod.MOB != null)
            CourseWorkMod.MOB.standBy = true;
        else
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.MOB_NULL));
    }

    private static void help() {
        CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.MOB_HELP));
    }

    private static void die() {
        if (CourseWorkMod.MOB != null)
            CourseWorkMod.MOB.setHealth(0);
        else
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent(MessagesContainer.MOB_NULL));
    }

    private static void teleport() {

    }
}