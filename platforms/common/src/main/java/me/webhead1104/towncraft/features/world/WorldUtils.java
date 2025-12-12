package me.webhead1104.towncraft.features.world;

import lombok.experimental.UtilityClass;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.TileSize;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.tiles.BuildingTile;
import me.webhead1104.towncraft.tiles.Tile;
import me.webhead1104.towncraft.utils.Msg;

@UtilityClass
public class WorldUtils {

    public static void openWorldMenu(TowncraftPlayer player) {
        Towncraft.getViewFrame().open(WorldMenu.class, player, player.getUser().getSection());
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately to the right
     */
    public static boolean canMoveRight(int slotIndex) {
        int x = slotIndex % 8;
        return x < 8 - 1;
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately to the left
     */
    public static boolean canMoveLeft(int slotIndex) {
        int x = slotIndex % 8;
        return x > 0;
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately below
     */
    public static boolean canMoveDown(int slotIndex) {
        int y = slotIndex / 8;
        return y < 8 - 1;
    }

    /**
     * @param slotIndex 0‑based index [0..53], row‑major order
     * @return true if there is a slot immediately above
     */
    public static boolean canMoveUp(int slotIndex) {
        int y = slotIndex / 8;
        return y > 0;
    }

    public static void applyArrows(TowncraftPlayer player, int section) {
        if (canMoveRight(section)) {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.ARROW);
            itemStack.setName(Msg.format("<dark_green>Click to scroll right!"));
            player.getInventory().setItem(23, itemStack);
        } else {
            player.getInventory().clear(23);
        }

        if (canMoveDown(section)) {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.ARROW);
            itemStack.setName(Msg.format("<dark_green>Click to scroll down!"));
            player.getInventory().setItem(31, itemStack);
        } else {
            player.getInventory().clear(31);
        }

        if (canMoveLeft(section)) {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.ARROW);
            itemStack.setName(Msg.format("<dark_green>Click to scroll left!"));
            player.getInventory().setItem(21, itemStack);
        } else {
            player.getInventory().clear(21);
        }

        if (canMoveUp(section)) {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.ARROW);
            itemStack.setName(Msg.format("<dark_green>Click to scroll up!"));
            player.getInventory().setItem(13, itemStack);
        } else {
            player.getInventory().clear(13);
        }
    }

    public static boolean canPlace(int startSlot, TileSize tileSize) {
        int startX = startSlot % 9;
        int startY = startSlot / 9;

        for (int dy = 0; dy < tileSize.height(); dy++) {
            for (int dx = 0; dx < tileSize.width(); dx++) {
                int x = startX + dx;
                int y = startY + dy;

                if (x >= 9 || y * 9 + x >= 54) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int adjustPlacement(int startSlot, TileSize tileSize) {
        int startX = startSlot % 9;
        int startY = startSlot / 9;

        if (startX + tileSize.width() > 9) {
            startX = 9 - tileSize.width();
        }

        int totalRows = 54 / 9;
        if (startY + tileSize.height() > totalRows) {
            startY = totalRows - tileSize.height();
        }

        return startY * 9 + startX;
    }

    public static int findAnchor(int clickedSlot, TileSize size, WorldSection section, BuildingTile matchTile) {
        int clickedX = clickedSlot % 9;
        int clickedY = clickedSlot / 9;
        for (int dy = 0; dy < size.height(); dy++) {
            for (int dx = 0; dx < size.width(); dx++) {
                int anchorX = clickedX - dx;
                int anchorY = clickedY - dy;
                if (anchorX < 0 || anchorY < 0) continue;
                int anchor = anchorY * 9 + anchorX;
                boolean ok = true;
                for (Integer s : size.toList(anchor)) {
                    if (s < 0 || s >= 54) {
                        ok = false;
                        break;
                    }
                    Tile candidate = section.getSlot(s);
                    if (!matchTile.equals(candidate)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) return anchor;
            }
        }
        return -1;
    }
}
