package me.webhead1104.towncraft.menus;

public enum ClickType {

    LEFT,
    SHIFT_LEFT,
    RIGHT,
    SHIFT_RIGHT,
    WINDOW_BORDER_LEFT,
    WINDOW_BORDER_RIGHT,
    MIDDLE,
    NUMBER_KEY,
    DOUBLE_CLICK,
    DROP,
    CONTROL_DROP,
    CREATIVE,
    SWAP_OFFHAND;

    /**
     * Gets whether this ClickType represents the pressing of a key on a
     * keyboard.
     *
     * @return true if this ClickType represents the pressing of a key
     */
    public boolean isKeyboardClick() {
        return (this == ClickType.NUMBER_KEY) || (this == ClickType.DROP) || (this == ClickType.CONTROL_DROP) || (this == ClickType.SWAP_OFFHAND);
    }

    /**
     * Gets whether this ClickType represents a right click.
     *
     * @return true if this ClickType represents a right click
     */
    public boolean isRightClick() {
        return (this == ClickType.RIGHT) || (this == ClickType.SHIFT_RIGHT);
    }

    /**
     * Gets whether this ClickType represents a left click.
     *
     * @return true if this ClickType represents a left click
     */
    public boolean isLeftClick() {
        return (this == ClickType.LEFT) || (this == ClickType.SHIFT_LEFT) || (this == ClickType.DOUBLE_CLICK) || (this == ClickType.CREATIVE);
    }

    /**
     * Gets whether this ClickType indicates that the shift key was pressed
     * down when the click was made.
     *
     * @return true if the action uses Shift.
     */
    public boolean isShiftClick() {
        return (this == ClickType.SHIFT_LEFT) || (this == ClickType.SHIFT_RIGHT);
    }
}