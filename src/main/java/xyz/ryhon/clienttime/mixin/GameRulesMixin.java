package xyz.ryhon.clienttime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Key;
import xyz.ryhon.clienttime.ClientTime;

@Mixin(GameRules.class)
public class GameRulesMixin {
	@Inject(at = @At("TAIL"), method = "getBoolean", cancellable = true)
	private void getTimeOfDay(Key<BooleanRule> rule, CallbackInfoReturnable<Boolean> ci) {
		if(rule == GameRules.DO_DAYLIGHT_CYCLE && ClientTime.timeEnabled)
		{
			ci.setReturnValue(false);
			return;
		}

		if(rule == GameRules.DO_WEATHER_CYCLE && ClientTime.weatherEnabled)
		{
			ci.setReturnValue(false);
			return;
		}
	}
}
