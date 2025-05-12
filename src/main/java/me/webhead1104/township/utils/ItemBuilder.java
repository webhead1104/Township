package me.webhead1104.township.utils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.Unbreakable;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class ItemBuilder {

    private ItemStack item;
    @Getter
    private Material material;
    @Getter
    private int amount = 1;
    @Getter
    private int durability = 0;
    @Getter
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    @Getter
    private Component displayName;
    @Getter
    private List<Component> lore = new ArrayList<>();
    @Getter
    private String id;
    @Getter
    private ItemRarity itemRarity;
    @Getter
    private String skullTextureURL = "";
    @Getter
    private boolean glow = false;

    public ItemBuilder(Material material, String id) {
        if (material == null) material = Material.AIR;
        this.item = ItemStack.of(material);
        this.material = material;
        this.id = id;
    }


    public ItemBuilder(Material material, int amount, String id) {
        if (material == null) material = Material.AIR;
        if (((amount > material.getMaxStackSize()) || (amount <= 0))) amount = 1;
        this.amount = amount;
        this.item = ItemStack.of(material, amount);
        this.material = material;
        this.id = id;
    }

    public ItemBuilder(Material material, int amount, Component displayName, String id) {
        if (material == null) material = Material.AIR;
        Validate.notNull(displayName, "The Display name is null.");
        this.item = ItemStack.of(material, amount);
        this.material = material;
        if (((amount > material.getMaxStackSize()) || (amount <= 0))) amount = 1;
        this.amount = amount;
        this.displayName = displayName;
        this.id = id;
    }

    public ItemBuilder(Material material, int amount, Component displayName, List<Component> lore, String id) {
        if (material == null) material = Material.AIR;
        Validate.notNull(displayName, "The Display name is null.");
        this.item = ItemStack.of(material, amount);
        this.material = material;
        if (((amount > material.getMaxStackSize()) || (amount <= 0))) amount = 1;
        this.amount = amount;
        this.displayName = displayName;
        this.lore = lore;
        this.id = id;
    }

    public ItemBuilder(Material material, Component displayName, String id) {
        if (material == null) material = Material.AIR;
        Validate.notNull(displayName, "The Display name is null.");
        this.item = ItemStack.of(material);
        this.material = material;
        this.displayName = displayName;
        this.id = id;
    }

    public ItemBuilder(Material material, Component displayName, List<Component> lore, String id) {
        if (material == null) material = Material.AIR;
        Validate.notNull(displayName, "The Display name is null.");
        this.item = ItemStack.of(material);
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.id = id;
    }

    public ItemBuilder(ItemStack item) {
        Validate.notNull(item, "The Item is null.");
        this.item = item;
        this.material = item.getType();
        this.amount = item.getAmount();
        this.enchantments = item.getEnchantments();
        if (item.hasData(DataComponentTypes.DAMAGE)) {
            this.durability = Objects.requireNonNull(item.getData(DataComponentTypes.DAMAGE));
        }
        if (item.hasData(DataComponentTypes.ITEM_NAME)) {
            this.displayName = Objects.requireNonNull(item.getData(DataComponentTypes.ITEM_NAME));
        }
        if (item.hasData(DataComponentTypes.LORE)) {
            this.lore = new ArrayList<>(Objects.requireNonNull(item.getData(DataComponentTypes.LORE)).lines());
        }
        if (material.equals(Material.PLAYER_HEAD) && item.hasData(DataComponentTypes.PROFILE)) {
            Objects.requireNonNull(item.getData(DataComponentTypes.PROFILE)).resolve().whenComplete((profile, throwable) -> {
                if (throwable != null) {
                    throw new RuntimeException(throwable);
                }
                if (profile.hasTextures() && profile.getTextures().getSkin() != null) {
                    this.skullTextureURL = profile.getTextures().getSkin().toString();
                }
            });
        }
        if (pdcHas(Keys.townshipIdKey)) this.id = pdcGetString(Keys.townshipIdKey);
        if (item.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE)) {
            glow = Boolean.TRUE.equals(item.getData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE));
        }
    }

    public static ItemBuilder loading() {
        return new ItemBuilder(Material.COAL_BLOCK, Msg.format("<red>Loading..."), "loading");
    }

    public ItemBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ItemBuilder amount(int amount) {
        if (((amount > material.getMaxStackSize()) || (amount <= 0))) amount = 1;
        this.amount = amount;
        return this;
    }

    public ItemBuilder durability(short damage) {
        this.durability = damage;
        return this;
    }

    public ItemBuilder material(Material material) {
        Validate.notNull(material, "The Material is null.");
        this.material = material;
        return this;
    }

    @CanIgnoreReturnValue
    public ItemBuilder enchant(Enchantment enchant, int level) {
        Validate.notNull(enchant, "The Enchantment is null.");
        enchantments.put(enchant, level);
        return this;
    }

    public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, "The Enchantments are null.");
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder displayName(Component displayName) {
        Validate.notNull(displayName, "The Display name is null.");
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder lore(Component line) {
        Validate.notNull(line, "The Line is null.");
        lore.add(line);
        return this;
    }

    public ItemBuilder lore(List<Component> lore) {
        Validate.notNull(lore, "The Lore lines are null.");
        this.lore = lore;
        return this;
    }

    public ItemBuilder lore(Component... lines) {
        Validate.notNull(lines, "The Lines are null.");
        for (Component line : lines) {
            lore(line);
        }
        return this;
    }

    @CanIgnoreReturnValue
    public ItemBuilder lore(Component line, int index) {
        Validate.notNull(line, "The Line is null.");
        lore.set(index, line);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        if (unbreakable) {
            item.setData(DataComponentTypes.UNBREAKABLE, Unbreakable.unbreakable());
            return this;
        } else {
            item.unsetData(DataComponentTypes.UNBREAKABLE);
        }
        return this;
    }

    public ItemBuilder hideTooltip(boolean hide) {
        if (hide) {
            item.setData(DataComponentTypes.HIDE_TOOLTIP);
        } else {
            item.unsetData(DataComponentTypes.HIDE_TOOLTIP);
        }
        return this;
    }

    public ItemBuilder hideAdditionalTooltip(boolean hide) {
        if (hide) {
            item.setData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);
        } else {
            item.unsetData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);
        }
        return this;
    }

    public ItemBuilder glow() {
        return this;
    }

    @CanIgnoreReturnValue
    public ItemBuilder setRarity(ItemRarity rarity) {
        item.setData(DataComponentTypes.RARITY, rarity);
        return this;
    }

    public ItemBuilder setSkullTextureURL(String skullTextureURL) {
        this.skullTextureURL = skullTextureURL;
        return this;
    }

    /**
     * Converts the ItemBuilder to a {@link org.bukkit.inventory.ItemStack}
     */
    public ItemStack build() {
        item = item.withType(material);
        item.setAmount(amount);
        item.setData(DataComponentTypes.DAMAGE, durability);
        if (enchantments != null && !enchantments.isEmpty()) {
            item.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments(enchantments, true));
            item.addUnsafeEnchantments(enchantments);
        }
        if (displayName != null) {
            item.setData(DataComponentTypes.ITEM_NAME, displayName);
        }
        if (lore != null && !lore.isEmpty()) {
            item.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        }
        pdcSetString(Keys.townshipIdKey, id);
        return item;
    }

    @NotNull
    @CanIgnoreReturnValue
    public ItemBuilder pdcSet(NamespacedKey key, PersistentDataType type, Object value) {
        item.editPersistentDataContainer(pdc -> pdc.set(key, type, value));
        return this;
    }

    @NotNull
    public Object pdcGet(NamespacedKey key, PersistentDataType type) {
        return Objects.requireNonNull(item.getPersistentDataContainer().get(key, type));
    }

    public boolean pdcHas(NamespacedKey key) {
        return item.getPersistentDataContainer().has(key);
    }

    @NotNull
    public ItemBuilder pdcSetInt(NamespacedKey key, int value) {
        this.pdcSet(key, PersistentDataType.INTEGER, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetString(NamespacedKey key, String value) {
        this.pdcSet(key, PersistentDataType.STRING, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetBoolean(NamespacedKey key, boolean value) {
        this.pdcSet(key, PersistentDataType.BOOLEAN, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetByte(NamespacedKey key, byte value) {
        this.pdcSet(key, PersistentDataType.BYTE, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetByteArray(NamespacedKey key, byte[] value) {
        this.pdcSet(key, PersistentDataType.BYTE_ARRAY, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetDouble(NamespacedKey key, double value) {
        this.pdcSet(key, PersistentDataType.DOUBLE, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetFloat(NamespacedKey key, float value) {
        this.pdcSet(key, PersistentDataType.FLOAT, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetIntArray(NamespacedKey key, int[] value) {
        this.pdcSet(key, PersistentDataType.INTEGER_ARRAY, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetLong(NamespacedKey key, long value) {
        this.pdcSet(key, PersistentDataType.LONG, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetLongArray(NamespacedKey key, long[] value) {
        this.pdcSet(key, PersistentDataType.LONG_ARRAY, value);
        return this;
    }

    @NotNull
    public ItemBuilder pdcSetShort(NamespacedKey key, short value) {
        this.pdcSet(key, PersistentDataType.SHORT, value);
        return this;
    }

    public int pdcGetInt(NamespacedKey key) {
        return (int) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.INTEGER));
    }

    @NotNull
    public String pdcGetString(NamespacedKey key) {
        return String.valueOf(Objects.requireNonNull(this.pdcGet(key, PersistentDataType.STRING)));
    }

    public boolean pdcGetBoolean(NamespacedKey key) {
        return (boolean) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.BOOLEAN));
    }

    public byte pdcGetByte(NamespacedKey key) {
        return (byte) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.BYTE));
    }

    public byte[] pdcGetByteArray(NamespacedKey key) {
        return (byte[]) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.BYTE_ARRAY));
    }

    public double pdcGetDouble(NamespacedKey key) {
        return (double) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.DOUBLE));
    }

    public float pdcGetFloat(NamespacedKey key) {
        return (float) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.FLOAT));
    }

    public int[] pdcGetIntArray(NamespacedKey key) {
        return Objects.requireNonNull(item.getPersistentDataContainer().get(key, PersistentDataType.INTEGER_ARRAY));
    }

    public long pdcGetLong(NamespacedKey key) {
        return (long) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.LONG));
    }

    public long[] pdcGetLongArray(NamespacedKey key) {
        return (long[]) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.LONG_ARRAY));
    }

    public short pdcGetShort(NamespacedKey key) {
        return (short) Objects.requireNonNull(this.pdcGet(key, PersistentDataType.SHORT));
    }
}
