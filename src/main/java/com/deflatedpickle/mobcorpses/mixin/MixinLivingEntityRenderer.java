/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"UnusedMixin", "rawtypes"})
@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity> {
  @Shadow
  protected abstract float getAnimationProgress(T entity, float tickDelta);

  @Redirect(
      method =
          "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getAnimationProgress(Lnet/minecraft/entity/LivingEntity;F)F"))
  public float stopAnimating(LivingEntityRenderer instance, T entity, float tickDelta) {
    // stops mobs from continuing to do their animation
    if (entity.isDead()) {
      return 0;
    }
    return getAnimationProgress(entity, tickDelta);
  }
}
