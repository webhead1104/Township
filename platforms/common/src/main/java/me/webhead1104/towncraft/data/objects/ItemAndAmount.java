package me.webhead1104.towncraft.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ConfigSerializable
@AllArgsConstructor
public final class ItemAndAmount {
    @Setting("item")
    private Key itemKey;
    private int amount;
    private transient ItemType.Item item;

    @PostProcess
    private void postProcess() {
        item = Towncraft.getDataLoader(ItemType.class).get(itemKey);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemAndAmount that = (ItemAndAmount) o;
        return amount == that.amount && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, amount);
    }

    @Override
    public String toString() {
        return "ItemAndAmount{" +
                "item=" + item +
                ", amount=" + amount +
                '}';
    }
}
