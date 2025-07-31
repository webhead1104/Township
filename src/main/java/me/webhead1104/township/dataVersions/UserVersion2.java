package me.webhead1104.township.dataVersions;

import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import static org.spongepowered.configurate.NodePath.path;

public class UserVersion2 {

    public static final ConfigurationTransformation VERSIONED_TRANSFORMATION = ConfigurationTransformation.builder()
            .addAction(path("trains", "trains", ConfigurationTransformation.WILDCARD_OBJECT), (path, value) -> {
                value.node("coins-needed-to-unlock").raw(null);
                value.node("level-needed-to-unlock").raw(null);
                return null;
            })
            .addAction(path("trains", "trains", ConfigurationTransformation.WILDCARD_OBJECT, "train-cars", ConfigurationTransformation.WILDCARD_OBJECT), (path, node) -> {
                int giveAmount = node.node("give-item", "amount").getInt();
                String giveType = node.node("give-item", "item-type").getString();
                node.node("give-item").raw(null);
                node.node("give-item-amount").raw(giveAmount);
                node.node("give-item-type").raw(giveType);

                int claimAmount = node.node("claim-item", "amount").getInt();
                String claimType = node.node("claim-item", "item-type").getString();
                node.node("claim-item").raw(null);
                node.node("claim-item-amount").raw(claimAmount);
                node.node("claim-item-type").raw(claimType);
                return null;
            })
            .build();
}
