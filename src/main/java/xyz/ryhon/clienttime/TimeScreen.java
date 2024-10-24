package xyz.ryhon.clienttime;

import java.util.function.Consumer;

import org.joml.Math;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class TimeScreen extends Screen {
	Screen parent;

	TextWidget timePresetsText;
	ButtonWithIcon sunriseButton;
	ButtonWithIcon noonButton;
	ButtonWithIcon sunsetButton;
	ButtonWithIcon midnightButton;

	TextWidget timeText;
	SimpleSlider timeSlider;
	CheckboxButton timeEnabledButton;

	TextWidget moonPhaseText;
	SimpleSlider moonPhaseSlider;
	CheckboxButton moonPhaseEnabledButton;

	TextWidget weatherText;
	CheckboxButton weatherEnabledButton;
	CheckboxButton rainButton;
	CheckboxButton thunderButton;

	static final int CheckboxPadding = 4;

	public TimeScreen(Screen parent) {
		super(Text.empty());
		this.parent = parent;
	}

	@Override
	protected void init() {
		timeSlider = new SimpleSlider(0, 24000);
		timeSlider.setIValue(ClientTime.time);
		timeSlider.onValue = (Long l) -> {
			ClientTime.time = l;
		};
		timeSlider.setWidth(width / 4);
		timeSlider.setHeight(32);
		timeSlider.setPosition((width / 2) - (timeSlider.getWidth() / 2), (height / 2) - (timeSlider.getHeight() / 2));

		timeText = new TextWidget(Text.translatable("clienttime.timeScreen.time"), textRenderer);
		timeText.setPosition(timeSlider.getX(), timeSlider.getY() - textRenderer.fontHeight);
		addDrawableChild(timeText);

		sunriseButton = new ButtonWithIcon(Identifier.of("textures/item/clock_48.png"), 32,
				timeText.getX(), timeText.getY() - 32,
				32, 32,
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.time = 0;
					timeSlider.setIValue(0);
				});
		noonButton = new ButtonWithIcon(Identifier.of("textures/item/clock_00.png"), 32,
				sunriseButton.getX() + 32, sunriseButton.getY(),
				32, 32,
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.time = 6000;
					timeSlider.setIValue(6000);
				});
		sunsetButton = new ButtonWithIcon(Identifier.of("textures/item/clock_15.png"), 32,
				noonButton.getX() + 32, noonButton.getY(),
				32, 32,
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.time = 12000;
					timeSlider.setIValue(12000);
				});
		midnightButton = new ButtonWithIcon(Identifier.of("textures/item/clock_32.png"), 32,
				sunsetButton.getX() + 32, sunsetButton.getY(),
				32, 32,
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.time = 18000;
					timeSlider.setIValue(18000);
				});

		timePresetsText = new TextWidget(Text.translatable("clienttime.timeScreen.timePresets"), textRenderer);
		timePresetsText.setPosition(sunriseButton.getX(), sunriseButton.getY() - textRenderer.fontHeight);
		addDrawableChild(timePresetsText);

		addDrawable(sunriseButton);
		addSelectableChild(sunriseButton);
		addDrawable(noonButton);
		addSelectableChild(noonButton);
		addDrawable(sunsetButton);
		addSelectableChild(sunsetButton);
		addDrawable(midnightButton);
		addSelectableChild(midnightButton);

		timeEnabledButton = new CheckboxButton(ClientTime.timeEnabled,
				Identifier.of("client-time", "textures/gui/checkmark.png"), 16,
				timeSlider.getX() - 32 + CheckboxPadding, timeSlider.getY() + CheckboxPadding,
				32 - (CheckboxPadding * 2), 32 - (CheckboxPadding * 2),
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.timeEnabled = ((CheckboxButton) b).checked;
				});
		addDrawable(timeSlider);
		addSelectableChild(timeSlider);
		addDrawable(timeEnabledButton);
		addSelectableChild(timeEnabledButton);

		moonPhaseText = new TextWidget(Text.translatable("clienttime.timeScreen.moonPhase"), textRenderer);
		moonPhaseText.setPosition(timeSlider.getX(), timeSlider.getY() + timeSlider.getHeight());
		addDrawableChild(moonPhaseText);

		moonPhaseSlider = new SimpleSlider(0, 7);
		moonPhaseSlider.setIValue(ClientTime.moonPhase);
		moonPhaseSlider.onValue = (Long i) -> {
			long j = i;
			ClientTime.moonPhase = (int) j;
		};
		moonPhaseSlider.setWidth(timeSlider.getWidth());
		moonPhaseSlider.setHeight(timeSlider.getHeight());
		moonPhaseSlider.setPosition(moonPhaseText.getX(), moonPhaseText.getY() + moonPhaseText.getHeight());

		moonPhaseEnabledButton = new CheckboxButton(ClientTime.moonPhaseEnabled,
				Identifier.of("client-time", "textures/gui/checkmark.png"), 16,
				moonPhaseSlider.getX() - 32 + CheckboxPadding, moonPhaseSlider.getY() + CheckboxPadding,
				32 - (CheckboxPadding * 2), 32 - (CheckboxPadding * 2),
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.moonPhaseEnabled = ((CheckboxButton) b).checked;
				});
		addDrawable(moonPhaseSlider);
		addSelectableChild(moonPhaseSlider);
		addDrawable(moonPhaseEnabledButton);
		addSelectableChild(moonPhaseEnabledButton);

		weatherText = new TextWidget(Text.translatable("clienttime.timeScreen.weather"), textRenderer);
		weatherText.setPosition(moonPhaseSlider.getX(), moonPhaseSlider.getY() + moonPhaseSlider.getHeight());
		addDrawableChild(weatherText);

		rainButton = new CheckboxButton(ClientTime.rain,
				Identifier.of("client-time", "textures/gui/rain.png"), 8,
				weatherText.getX(), weatherText.getY() + weatherText.getHeight(),
				32, 32,
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.rain = ((CheckboxButton) b).checked;
				});

		thunderButton = new CheckboxButton(ClientTime.thunder,
				Identifier.of("client-time", "textures/gui/thunder.png"), 8,
				rainButton.getX() + 32, rainButton.getY(),
				32, 32,
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.thunder = ((CheckboxButton) b).checked;
				});

		weatherEnabledButton = new CheckboxButton(ClientTime.weatherEnabled,
				Identifier.of("client-time", "textures/gui/checkmark.png"), 16,
				rainButton.getX() - 32 + CheckboxPadding, rainButton.getY() + CheckboxPadding,
				32 - (CheckboxPadding * 2), 32 - (CheckboxPadding * 2),
				Text.empty(), (ButtonWidget b) -> {
					ClientTime.weatherEnabled = ((CheckboxButton) b).checked;
				});

		addDrawable(weatherEnabledButton);
		addSelectableChild(weatherEnabledButton);
		addDrawable(rainButton);
		addSelectableChild(rainButton);
		addDrawable(thunderButton);
		addSelectableChild(thunderButton);

		addDrawable(new Drawable() {
			@Override
			public void render(DrawContext context, int mouseX, int mouseY, float delta) {
				int clocktex = ((int) (ClientTime.time - 6000) * 62 / 24000);
				if (clocktex < 0)
					clocktex += 62;
				context.drawTexture(RenderLayer::getGuiTextured, Identifier.of(String.format("textures/item/clock_%02d.png", clocktex)),
						timeSlider.getX() + timeSlider.getWidth(), timeSlider.getY(),
						0,0,
						32, 32,
						16, 16,
						16, 16);

				int col = ClientTime.moonPhase / 4;
				int row = ClientTime.moonPhase % 4;
				context.drawTexture(RenderLayer::getGuiTextured, Identifier.of("textures/environment/moon_phases.png"),
						moonPhaseSlider.getX() + moonPhaseSlider.getWidth(), moonPhaseSlider.getY(),
						row * 32, col * 32,
						32, 32,
						32, 32,
						128, 64
						);
			}
		});
	}

	public static class SimpleSlider extends SliderWidget {
		long min, max;
		long iValue;
		public Consumer<Long> onValue;

		public SimpleSlider(long min, long max) {
			super(0, 0, 0, 0, Text.empty(), 0);
			this.min = min;
			this.max = max;
			updateMessage();
		}

		public void setIValue(long v) {
			iValue = v;
			setValue((v - min) / (double) (max - min));
		}

		@Override
		protected void applyValue() {
			iValue = (long) Math.round(value * (max - min)) + min;
			setIValue(iValue);

			updateMessage();
			if (onValue != null)
				onValue.accept(iValue);
		}

		@Override
		protected void updateMessage() {
			setMessage(Text.literal(iValue + " / " + max));
		}
	}

	public static class ButtonWithIcon extends ButtonWidget {
		Identifier texture;
		int texSize;

		protected ButtonWithIcon(Identifier texture, int texSize, int x, int y, int width, int height, Text message,
				PressAction onPress) {
			super(x, y, width, height, message, onPress, (textSupplier) -> {
				return (MutableText) textSupplier.get();
			});
			this.texSize = texSize;
			this.texture = texture;
		}

		@Override
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			super.renderWidget(context, mouseX, mouseY, delta);
			context.drawTexture(RenderLayer::getGuiTextured, texture,
				getX() + (getWidth() / 4), getY() + (getHeight() / 4),
				0,0,
				texSize/2, texSize/2,
				texSize, texSize,
				texSize, texSize);
		}
	}

	public static class CheckboxButton extends ButtonWidget {
		private static final Identifier TEXTURE = Identifier.of("widget/checkbox");
		Identifier checkTexture;
		int texSize;
		public boolean checked;

		protected CheckboxButton(boolean checked, Identifier checkTexture, int texSize, int x, int y, int width,
				int height, Text message, PressAction onPress) {
			super(x, y, width, height, message, onPress, (textSupplier) -> {
				return (MutableText) textSupplier.get();
			});

			this.checkTexture = checkTexture;
			this.texSize = texSize;
			this.checked = checked;
		}

		@Override
		public void onClick(double mouseX, double mouseY) {
			checked = !checked;
			super.onClick(mouseX, mouseY);
		}

		@Override
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			context.drawGuiTexture(RenderLayer::getGuiTextured, TEXTURE, this.getX(), this.getY(), width, height);
			context.drawTexture(RenderLayer::getGuiTextured, checkTexture,
					getX() + (getWidth() / 4), getY() + (getHeight() / 4),
					0,0,
					getWidth() / 2, getWidth() / 2,
					texSize, texSize,
					texSize, texSize, ColorHelper.getWhite(checked ? 1.0f : 0.1f));
		}
	}

	@Override
	public void close() {
		ClientTime.saveConfig();
		client.setScreen(parent);
	}
}