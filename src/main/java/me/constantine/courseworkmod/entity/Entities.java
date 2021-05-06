package me.constantine.courseworkmod.entity;

import me.constantine.courseworkmod.CourseWorkMod;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.RegistryEvent;

public class Entities {
    public static EntityType<?> CUSTOM_ENTITY = EntityType.Builder
            .create(Mob::new, EntityClassification.CREATURE)
            .build(CourseWorkMod.MOD_ID + ":mob_entity")
            .setRegistryName(CourseWorkMod.location("mob_entity"));

    public static void registerEntitySpawnEggs(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll
                (
                        Items.entityEgg =
                                registerEntitySpawnEgg(CUSTOM_ENTITY, 0x2f5882, 0x6f1499,
                                        "mob_entity_egg")
                );

    }

    public static void registerEntityWorldSpawns() {
        registerEntityWorldSpawn(CUSTOM_ENTITY, Biomes.PLAINS, Biomes.BEACH, Biomes.JUNGLE);
    }

    public static Item registerEntitySpawnEgg(EntityType<?> type, int color1, int color2, String name) {
        SpawnEggItem item = new SpawnEggItem(type, color1, color2,
                new Item.Properties().group(ItemGroup.MISC));
        item.setRegistryName(CourseWorkMod.location(name));
        return item;
    }

    public static void registerEntityWorldSpawn(EntityType<?> entity, Biome... biomes) {
        for (Biome biome : biomes) {
            if (biome != null) {
                biome.getSpawns(entity.getClassification())
                        .add(new Biome.SpawnListEntry(entity, 10, 1, 10));
            }
        }
    }
}
