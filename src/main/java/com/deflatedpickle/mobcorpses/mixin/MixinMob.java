/* Copyright (c) 2022 DeflatedPickle under the MIT license */

package com.deflatedpickle.mobcorpses.mixin;

import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnusedMixin")
@Mixin(MobEntity.class)
public abstract class MixinMob {
  @Shadow
  protected abstract void dropLoot(DamageSource source, boolean causedByPlayer);

  @Shadow
  protected abstract void dropEquipment(
      DamageSource source, int lootingMultiplier, boolean allowDrops);

  @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
  public void onMobInteract(
      PlayerEntity player, Hand interactionHand, CallbackInfoReturnable<ActionResult> cir) {
    MobEntity me = (MobEntity) (Object) this;

    if (!me.isAlive() && player.getStackInHand(interactionHand).getItem() instanceof ShearsItem) {
      if (!player.world.isClient) {
        var source = DamageSource.player(player);

        this.dropLoot(source, true);
        this.dropEquipment(source, EnchantmentHelper.getLooting(player), true);

        me.setRemoved(Entity.RemovalReason.KILLED);
        me.emitGameEvent(GameEvent.ENTITY_KILLED);

        player
            .getStackInHand(interactionHand)
            .damage(1, player, (p) -> p.sendToolBreakStatus(interactionHand));
      } else {
        RedstoneOreBlock.spawnParticles(me.world, me.getBlockPos());
      }

      cir.setReturnValue(ActionResult.SUCCESS);
    } else {
      cir.setReturnValue(ActionResult.PASS);
    }
  }
}
