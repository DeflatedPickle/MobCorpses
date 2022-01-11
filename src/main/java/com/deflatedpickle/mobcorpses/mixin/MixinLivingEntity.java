/* Copyright (c) 2021-2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"UnusedMixin"})
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
  @Redirect(
      method = "onDeath",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LivingEntity;drop(Lnet/minecraft/entity/damage/DamageSource;)V"))
  public void drop(LivingEntity instance, DamageSource source) {
    // stops items and xp being dropped when killed
    if (!(instance instanceof MobEntity)) {
      instance.drop(source);
    }
  }
}
