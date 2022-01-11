/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
  /*@Redirect(
      method = "updatePostDeath",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LivingEntity;remove(Lnet/minecraft/entity/Entity$RemovalReason;)V"))
  public void remove(LivingEntity instance, Entity.RemovalReason removalReason) {}*/

  @Redirect(
      method = "drop",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;shouldDropLoot()Z"))
  public boolean shouldDropLoot(LivingEntity instance) {
    return false;
  }
}
