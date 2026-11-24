// 羽落：靴子持续给缓降，潜行可取消。

package com.cocreateenchantments.enchantment;

import com.cocreateenchantments.CoCreateEnchantments;
import com.cocreateenchantments.config.CceTunables;
import com.cocreateenchantments.content.CceContent;
import com.huziyang520.merlinlib.api.EntityCounter;
import com.huziyang520.merlinlib.event.EnchantmentEventRegistrar;
import com.huziyang520.merlinlib.event.GlobalEvents;
import com.huziyang520.merlinlib.event.LivingEntityTickEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class GentleDescentEnchantment {
	private static Holder<Enchantment> HOLDER;
	private static final Identifier SF_GRANTED = Identifier.parse(CceContent.SLOW_FALL_GRANTED_MARKER);

	private GentleDescentEnchantment() {
	}

	public static void registerCallbacks(EnchantmentEventRegistrar registrar, HolderLookup.Provider registries) {
		HOLDER = CoCreateEnchantments.resolveEnchantment(registries, CceContent.GENTLE_DESCENT);
		GlobalEvents.enableLivingEntityTick();
		GlobalEvents.LIVING_ENTITY_TICK.register(GentleDescentEnchantment::onLivingTick);
	}

	private static void onLivingTick(LivingEntityTickEvent event) {
		var entity = event.entity();
		ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
		boolean hasGentle = HOLDER != null && !boots.isEmpty()
				&& boots.getEnchantments().getLevel(HOLDER) > 0;
		MobEffectInstance current = entity.getEffect(MobEffects.SLOW_FALLING);
		if (hasGentle && (!CceTunables.gentleDescentCancelOnSneak() || !entity.isShiftKeyDown())) {
			if (current == null) {
				entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
						CceTunables.secondsToTicks(CceTunables.gentleDescentSeconds()), 0, true, false));
			} else if (isOurs(current)
					&& event.tickCount() % CceTunables.secondsToTicks(CceTunables.gentleDescentRefreshSeconds()) == 0) {
				entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
						CceTunables.secondsToTicks(CceTunables.gentleDescentSeconds()), 0, true, false));
			}
			EntityCounter.set(entity, SF_GRANTED, 1);
		} else {
			if (current != null && isOurs(current) && EntityCounter.get(entity, SF_GRANTED) > 0) {
				entity.removeEffect(MobEffects.SLOW_FALLING);
			}
			EntityCounter.set(entity, SF_GRANTED, 0);
		}
	}

	private static boolean isOurs(MobEffectInstance instance) {
		return !instance.isInfiniteDuration()
				&& instance.getAmplifier() == 0
				&& instance.getDuration() <= CceTunables.secondsToTicks(CceTunables.gentleDescentSeconds());
	}
}
