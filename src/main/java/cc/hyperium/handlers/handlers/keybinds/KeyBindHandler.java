/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.GameShutDownEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.event.KeyreleaseEvent;
import cc.hyperium.event.MouseButtonEvent;
import cc.hyperium.handlers.handlers.keybinds.keybinds.DabKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FlipKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FlossKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FortniteDefaultDanceKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.FriendsKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.GuiDanceKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.GuiKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.NamesKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.RearCamKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.TPoseKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.TogglePerspectiveKeybind;
import cc.hyperium.handlers.handlers.keybinds.keybinds.ToggleSprintKeybind;
import net.minecraft.client.Minecraft;
import java.util.HashMap;
import java.util.Map;

public class KeyBindHandler {
    private static final Map<Integer, Integer> mouseBinds = new HashMap<>();
    private final KeyBindConfig keyBindConfig;
    private final Map<String, HyperiumBind> keybinds = new HashMap<>();

    public KeyBindHandler() {
        this.keyBindConfig = new KeyBindConfig(this, Hyperium.folder);

        registerKeyBinding(new FriendsKeybind());
        registerKeyBinding(new NamesKeybind());
        registerKeyBinding(new GuiKeybind());
        registerKeyBinding(new DabKeybind());
        registerKeyBinding(new FlipKeybind());
        registerKeyBinding(new FlossKeybind());
        registerKeyBinding(new ToggleSprintKeybind());
        registerKeyBinding(new TogglePerspectiveKeybind());
        registerKeyBinding(new FortniteDefaultDanceKeybind());
        registerKeyBinding(new TPoseKeybind());
        registerKeyBinding(new GuiDanceKeybind());
        registerKeyBinding(new RearCamKeybind());
        for (int i = 0; i < 16; i++) {
            mouseBinds.put(i, -100 + i);
        }
        this.keyBindConfig.load();
    }

    @InvokeEvent
    public void onKeyPress(KeypressEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : this.keybinds.values()) {
                if (bind.isConflicted()) continue;
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onPress();
                    bind.setWasPressed(true);
                }
            }
        }
    }

    @InvokeEvent
    public void onKeyRelease(KeyreleaseEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : this.keybinds.values()) {
                if (bind.isConflicted()) continue;
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onRelease();
                    bind.setWasPressed(false);
                }
            }
        }
    }

    @InvokeEvent
    public void onMouseButton(MouseButtonEvent event) {
        // Dismisses mouse movement input.
        if (event.getValue() >= 0 && Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (HyperiumBind bind : this.keybinds.values()) {
                if (bind.isConflicted()) {
                    continue;
                }
                // Gets Minecraft value of the mouse value and checks to see if it matches a keybind.
                if (mouseBinds.get(event.getValue()) == bind.getKeyCode()) {
                    if (event.getState()) {
                        bind.onPress();
                        bind.setWasPressed(true);
                    } else {
                        bind.onRelease();
                        bind.setWasPressed(false);
                    }
                }
            }
        }
    }

    @InvokeEvent
    public void onGameShutdown(GameShutDownEvent event) {
        this.keyBindConfig.save();
    }

    public void registerKeyBinding(HyperiumBind bind) {
        this.keybinds.put(bind.getRealDescription(), bind);
        this.keyBindConfig.attemptKeyBindLoad(bind);
    }

    public KeyBindConfig getKeyBindConfig() {
        return this.keyBindConfig;
    }

    public Map<String, HyperiumBind> getKeybinds() {
        return this.keybinds;
    }

    public void releaseAllKeybinds() {
        if (!keybinds.isEmpty()) {
            for (Map.Entry<String, HyperiumBind> map : keybinds.entrySet()) {
                HyperiumBind bind = map.getValue();
                if (bind.wasPressed()) {
                    bind.onRelease();
                    bind.setWasPressed(false);
                    return;
                }
                if (bind.wasPressed()) {
                    bind.onRelease();
                    return;
                }
            }
        }
    }
}
