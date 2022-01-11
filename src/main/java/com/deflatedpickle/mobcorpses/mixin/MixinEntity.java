/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses.mixin;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("UnusedMixin")
@Mixin(Entity.class)
public abstract class MixinEntity {
  @Shadow
  public abstract List<Entity> getPassengerList();

  @Shadow
  public abstract void stopRiding();

  @Redirect(
      method = "remove",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/Entity;setRemoved(Lnet/minecraft/entity/Entity$RemovalReason;)V"))
  public void setRemoved(Entity instance, Entity.RemovalReason reason) {
    // stops the mob body from despawning
    if (instance instanceof MobEntity) {
      stopRiding();
      getPassengerList().forEach(Entity::stopRiding);
    } else {
      instance.setRemoved(reason);
    }
  }
}
