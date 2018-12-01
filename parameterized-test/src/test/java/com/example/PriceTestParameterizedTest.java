package com.example;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class PriceTestParameterizedTest {
    @RunWith(Parameterized.class)
    public static class コンストラクタ正常系Test {
        public double price;
        public String expected;

        public コンストラクタ正常系Test(double price, String expected) {
            this.price = price;
            this.expected = expected;
        }

        @Parameterized.Parameters(name="{index}: {0} == {1} ?")
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {1, "1.0"},
                    {2, "2.0"},
                    {3, "3.0"}
                });
        };

        @Test
        public void 正数を正しくインスタンス化できること() {
            Price aPrice = new Price(price);
            String actual = Double.toString(aPrice.price);
            assertEquals(actual, expected);
        }
    }

    public static class 普通 {
        @Test
        public void 足し算() {
            assertThat(3, is(3));
        }
    }
}
