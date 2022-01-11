/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses.mixin;

import java.util.List;
import net.minecraft.entity.Entity;
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
  public void setRemove(Entity instance, Entity.RemovalReason reason) {
    stopRiding();
    getPassengerList().forEach(Entity::stopRiding);
  }
}
