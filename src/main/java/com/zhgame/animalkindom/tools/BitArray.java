package com.zhgame.animalkindom.tools;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class BitArray {
    private final byte[] MASK = new byte[]{1, 2, 4, 8, 16, 32, 64, (byte) 128};
    byte[] bits;

    public BitArray(int length) {
        this.bits = new byte[length / 8 + 1];
    }

    public BitArray(byte[] bits) {
        this.bits = bits;
    }

    public void set(int index, boolean value) {
        int i = index / 8;
        int move = index % 8;
        bits[i] = (byte) (bits[i] | MASK[move]);
    }

    public boolean get(int index) {
        int i = index / 8;
        int move = index % 8;
        return (bits[i] & MASK[move]) == MASK[move];
    }

    public byte[] getBits() {
        return bits;
    }

    public void show() {
        for (int i = 0; i < bits.length; i++) {
            byte b = bits[i];
            for (int bitIndex = 0; bitIndex < 8; bitIndex++) {
                System.out.print(((b >> bitIndex) & 1) + " ");
            }
            System.out.println();
        }
    }

    public Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
            private int i = 0;

            public boolean hasNext() {
                return i <= bits.length * 8;
            }

            public Boolean next() {
                return get(i++);
            }

        };
    }


    public void forEach(BiConsumer<Integer, Boolean> fun) {
        int total = 0;
        for (int i = 0; i < bits.length; i++) {
            byte b = bits[i];
            for (int bitIndex = 0; bitIndex < 8; bitIndex++, total++) {
                fun.accept(total, ((b >> bitIndex) & 1) == 1);
            }
        }
    }
}
