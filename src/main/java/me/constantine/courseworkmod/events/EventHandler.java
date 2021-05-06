package me.constantine.courseworkmod.events;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.constantine.courseworkmod.CourseWorkMod;
import me.constantine.courseworkmod.commands.CommandFork;
import me.constantine.courseworkmod.utils.containers.MessagesContainer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = CourseWorkMod.MOD_ID, bus = Bus.FORGE)
public class EventHandler {
    @SubscribeEvent
    public static void onMessage(ServerChatEvent event) {
        if (event.getMessage().contains(".c ")) {
            ServerPlayerEntity player = event.getPlayer();
            player.sendMessage(new StringTextComponent(MessagesContainer.COMMAND_USED));
            CommandFork fork = new CommandFork(event.getMessage());
            if (!fork.process())
                player.sendMessage(new StringTextComponent(MessagesContainer.COMMAND_ERROR));
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            CourseWorkMod.PLAYER = ((PlayerEntity) event.getEntity());
            CourseWorkMod.PLAYER.sendMessage(new StringTextComponent("Welcome " +
                    ChatFormatting.AQUA + CourseWorkMod.PLAYER.getName().getFormattedText() +
                    ChatFormatting.WHITE + "!"));
        }
    }

    @SubscribeEvent
    public static void onDamage(AttackEntityEvent event) {
        if (event.getTarget() instanceof PigEntity && event.getPlayer().equals(CourseWorkMod.PLAYER)) {
            if (!CourseWorkMod.MOB.goalsPresent) {
                CourseWorkMod.MOB.goalsPresent = true;
            }
        }
    }
}
