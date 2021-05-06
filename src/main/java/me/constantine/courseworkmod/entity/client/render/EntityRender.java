package me.constantine.courseworkmod.entity.client.render;

import me.constantine.courseworkmod.CourseWorkMod;
import me.constantine.courseworkmod.entity.Mob;
import me.constantine.courseworkmod.entity.client.models.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class EntityRender extends LivingRenderer<Mob, EntityModel> {
    public EntityRender(EntityRendererManager manager) {
        super(manager, new EntityModel(1.0f, false), 0f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Mob entity) {
        return CourseWorkMod.location("textures/entity/mob_entity.png");
    }

    public static class RenderFactory implements IRenderFactory<Mob> {
        @Override
        public EntityRenderer<? super Mob> createRenderFor(EntityRendererManager manager) {
            return new EntityRender(manager);
        }

    }
}
