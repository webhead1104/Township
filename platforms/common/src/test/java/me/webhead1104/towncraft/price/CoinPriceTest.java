/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
