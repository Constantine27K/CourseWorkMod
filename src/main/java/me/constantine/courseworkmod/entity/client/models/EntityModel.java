package me.constantine.courseworkmod.entity.client.models;

import me.constantine.courseworkmod.entity.Mob;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EntityModel extends PlayerModel<Mob> {

    public EntityModel(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
    }
}

