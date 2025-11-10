package me.webhead1104.towncraft.dataVersions;

import com.google.errorprone.annotations.Keep;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

@Keep
public final class UserVersion2 implements DataVersion {

    @Override
    public ConfigurationTransformation getTransformation() {
        return ConfigurationTransformation.chain(
                node -> node.node("trains", "trains").childrenMap().forEach((key, trainNode) -> {
                    trainNode.node("coins-needed-to-unlock").raw(null);
                    trainNode.node("level-needed-to-unlock").raw(null);
                }),
                node -> node.node("trains", "trains").childrenMap().forEach((key, trainNode) ->
                        trainNode.node("train-cars").childrenMap().forEach((carKey, carNode) -> {
                            int giveAmount = carNode.node("give-item", "amount").getInt();
                            String giveType = carNode.node("give-item", "item-type").getString();
                            carNode.node("give-item").raw(null);
                            carNode.node("give-item-amount").raw(giveAmount);
                            carNode.node("give-item-type").raw(giveType);

                            int claimAmount = node.node("claim-item", "amount").getInt();
                            String claimType = node.node("claim-item", "item-type").getString();
                            node.node("claim-item").raw(null);
                            node.node("claim-item-amount").raw(claimAmount);
                            node.node("claim-item-type").raw(claimType);
                        })));
    }

    @Override
    public int getVersion() {
        return 2;
    }
}
