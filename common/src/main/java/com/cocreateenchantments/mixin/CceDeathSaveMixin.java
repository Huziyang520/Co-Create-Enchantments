// 在 ServerPlayer.die 头部拦截致命伤，交给掉落不死亡判定。

package com.cocreateenchantments.mixin;

import com.cocreateenchantments.enchantment.UndyingDropEnchantment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class CceDeathSaveMixin {
	@Inject(method = "die", at = @At("HEAD"), cancellable = true)
	private void cocreateenchantments$deathSave(DamageSource source, CallbackInfo ci) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		if (UndyingDropEnchantment.trySave(player, source)) {
			ci.cancel();
		}
	}
}
