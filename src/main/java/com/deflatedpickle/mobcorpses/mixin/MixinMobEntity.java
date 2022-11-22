/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses.mixin;

import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShearsItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(MobEntity.class)
public abstract class MixinMobEntity {
  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void onMobInteract(
      PlayerEntity player, Hand interactionHand, CallbackInfoReturnable<ActionResult> cir) {
    MobEntity me = (MobEntity) (Object) this;

    if (!me.isAlive() && player.getStackInHand(interactionHand).getItem() instanceof ShearsItem) {
      if (!player.world.isClient) {
        me.drop(DamageSource.player(player));
        me.setRemoved(Entity.RemovalReason.KILLED);
        me.emitGameEvent(GameEvent.ENTITY_DIE);

        player
            .getStackInHand(interactionHand)
            .damage(1, player, (p) -> p.sendToolBreakStatus(interactionHand));
      }

      me.world.playSound(
          me.getX(),
          me.getY(),
          me.getZ(),
          SoundEvents.ENTITY_SHEEP_SHEAR,
          SoundCategory.PLAYERS,
          1.0f,
          1.0f,
          false);

      RedstoneOreBlock.spawnParticles(me.world, me.getBlockPos());

      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }
}
