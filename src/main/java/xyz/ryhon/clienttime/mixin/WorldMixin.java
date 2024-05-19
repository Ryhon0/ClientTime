package xyz.ryhon.clienttime.mixin;

import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import xyz.ryhon.clienttime.ClientTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess {
	@Inject(at = @At("TAIL"), method = "getTimeOfDay", cancellable = true)
	private void getTimeOfDay(CallbackInfoReturnable<Long> ci) {
		if(ClientTime.timeEnabled)
		{
			ci.setReturnValue(ClientTime.time);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isRaining", cancellable = true)
	private void isRaining(CallbackInfoReturnable<Boolean> ci) {
		if(ClientTime.weatherEnabled)
		{
			ci.setReturnValue(ClientTime.rain);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isThundering", cancellable = true)
	private void isThundering(CallbackInfoReturnable<Boolean> ci) {
		if(ClientTime.weatherEnabled)
		{
			ci.setReturnValue(ClientTime.thunder);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "getRainGradient", cancellable = true)
	private void getRainGradient(float delta, CallbackInfoReturnable<Float> ci) {
		if(ClientTime.weatherEnabled)
		{
			ci.setReturnValue(ClientTime.rain ? 1f : 0f);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "getThunderGradient", cancellable = true)
	private void getThunderGradient(float delta, CallbackInfoReturnable<Float> ci) {
		if(ClientTime.weatherEnabled)
		{
			ci.setReturnValue(ClientTime.thunder ? 1f : 0f);
			return;
		}
	}

	@Override
	public int getMoonPhase() {
		if(ClientTime.moonPhaseEnabled)
			return ClientTime.moonPhase;
		return this.getDimension().getMoonPhase(this.getLunarTime());
	}

	@Override
	public float getMoonSize() {
		return DimensionType.MOON_SIZES[this.getMoonPhase()];
	}
}