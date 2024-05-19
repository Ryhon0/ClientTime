package xyz.ryhon.clienttime;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuAPIImpl implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return (currentScreen) -> 
		{
			return new TimeScreen(currentScreen);
		};
	}
}
