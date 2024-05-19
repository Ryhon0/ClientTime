package xyz.ryhon.clienttime.mixin;

import net.minecraft.client.world.ClientWorld;
import xyz.ryhon.clienttime.ClientTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.Properties.class)
public class ClientWorldMixin {
	@Inject(at = @At("TAIL"), method = "getTimeOfDay", cancellable = true)
	private void getTimeOfDay(CallbackInfoReturnable<Long> ci) {
		if (ClientTime.timeEnabled) {
			ci.setReturnValue(ClientTime.time);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isRaining", cancellable = true)
	private void isRaining(CallbackInfoReturnable<Boolean> ci) {
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.rain);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isThundering", cancellable = true)
	private void isThundering(CallbackInfoReturnable<Boolean> ci) {
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.thunder);
			return;
		}
	}
}
