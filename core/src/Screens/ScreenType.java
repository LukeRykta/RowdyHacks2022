package Screens;

import com.badlogic.gdx.Screen;

public enum ScreenType {
    OPENING(Opening.class),
    MENU(Menu.class),
    SOLO(Solo.class),
    MULTI(Multiplayer.class);
    private final Class<? extends AbstractScreen> screenClass;

    ScreenType(final Class<? extends AbstractScreen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }
}
