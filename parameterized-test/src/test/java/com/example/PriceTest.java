package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class PriceTest {
    static class コンストラクタ正常系Fixture {
        public final double price;
        public final String expected;

        コンストラクタ正常系Fixture(double price, String expected) {
            this.price = price;
            this.expected = expected;
        }
    }

    @DataPoints
    public static コンストラクタ正常系Fixture[] _コンストラクタ正常系Fixture = {
        new コンストラクタ正常系Fixture(1, "1.0"),
        new コンストラクタ正常系Fixture(2, "2.0"),
        new コンストラクタ正常系Fixture(3, "3.0"),
    };

    @Theory
    public void 正数を正しくインスタンス化できること(コンストラクタ正常系Fixture fixture) {
        Price price = new Price(fixture.price);
        String actual = Double.toString(price.price);
        assertEquals(fixture.expected, fixture.expected);
    }
}
