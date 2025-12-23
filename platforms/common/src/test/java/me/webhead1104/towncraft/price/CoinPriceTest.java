package me.webhead1104.towncraft.price;

import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.data.objects.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CoinPriceTest {

    @BeforeAll
    public static void beforeAll() {
        TowncraftPlatformManager.init();
    }

    @Test
    public void testHas() {
        CoinPrice price = new CoinPrice(100);
        User user = new User(UUID.randomUUID());
        user.setCoins(50);
        assertFalse(price.has(user));

        user.setCoins(100);
        assertTrue(price.has(user));

        user.setCoins(150);
        assertTrue(price.has(user));
    }

    @Test
    public void testTake() {
        CoinPrice price = new CoinPrice(100);
        User user = new User(UUID.randomUUID());
        user.setCoins(150);
        price.take(user);
        assertEquals(50, user.getCoins());
    }

    @Test
    public void testZeroPrice() {
        CoinPrice price = new CoinPrice(0);
        User user = new User(UUID.randomUUID());
        user.setCoins(0);
        assertTrue(price.has(user));
        price.take(user);
        assertEquals(0, user.getCoins());
    }
}
