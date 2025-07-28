package me.webhead1104.township.managers;

import me.webhead1104.township.Township;
import me.webhead1104.township.menus.WorldMenu;
import org.bukkit.entity.Player;

public class WorldManager {

    public void openWorldMenu(Player player) {
        openWorldMenu(player, Township.getUserManager().getUser(player.getUniqueId()).getSection());
    }

    public void openWorldMenu(Player player, int section) {
        Township.getViewFrame().open(WorldMenu.class, player, section);
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately to the right
     */
    public boolean canMoveRight(int slotIndex) {
        int x = slotIndex % 8;
        return x < 8 - 1;
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately to the left
     */
    public boolean canMoveLeft(int slotIndex) {
        int x = slotIndex % 8;
        return x > 0;
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately below
     */
    public boolean canMoveDown(int slotIndex) {
        int y = slotIndex / 8;
        return y < 8 - 1;
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately above
     */
    public boolean canMoveUp(int slotIndex) {
        int y = slotIndex / 8;
        return y > 0;
    }
}
