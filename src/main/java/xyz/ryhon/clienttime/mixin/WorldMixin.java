package xyz.ryhon.clienttime.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import xyz.ryhon.clienttime.ClientTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
@Environment(EnvType.CLIENT)
public abstract class WorldMixin implements WorldAccess {
	@Override
	public int getMoonPhase() {
		if(ClientTime.moonPhaseEnabled && this.getServer() == null)
			return ClientTime.moonPhase;
		return this.getDimension().getMoonPhase(this.getLunarTime());
	}

	@Override
	public float getMoonSize() {
		return DimensionType.MOON_SIZES[this.getMoonPhase()];
	}

	@Inject(at = @At("TAIL"), method = "getRainGradient", cancellable = true)
	private void getRainGradient(float delta, CallbackInfoReturnable<Float> ci) {
		if(this.getServer() != null) return;
		if(ClientTime.weatherEnabled)
		{
			ci.setReturnValue(ClientTime.rain ? 1f : 0f);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "getThunderGradient", cancellable = true)
	private void getThunderGradient(float delta, CallbackInfoReturnable<Float> ci) {
		if(this.getServer() != null) return;
		if(ClientTime.weatherEnabled)
		{
			ci.setReturnValue(ClientTime.thunder ? 1f : 0f);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isRaining", cancellable = true)
	private void isRaining(CallbackInfoReturnable<Boolean> ci) {
		if(this.getServer() != null) return;
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.rain);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isThundering", cancellable = true)
	private void isThundering(CallbackInfoReturnable<Boolean> ci) {
		if(this.getServer() != null) return;
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.thunder);
			return;
		}
	}
}