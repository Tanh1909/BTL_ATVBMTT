package service;

import java.util.BitSet;

public class DESKey {

    public static final int[] PC1 = {57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4};

    // MANG PC2
    public static final int[] PC2 = {14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32};
    public static final int[] LEFT_SHIFTS = {1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1};

    public static BitSet circularLeftShift(BitSet input, int shift,int size) {
        BitSet shifted = new BitSet(size);
        for (int i = 0; i < size - shift; i++) {
            shifted.set(i, input.get(i + shift));
        }
        for (int i = size - shift; i < size; i++) {
            shifted.set(i, input.get(i - size + shift));
        }
        return shifted;
    }

    public static BitSet concatenate(BitSet left, BitSet right, int size) {
        BitSet concatenated = new BitSet(size * 2);
        for (int i = 0; i < size; i++) {
            concatenated.set(i, left.get(i));
        }
        for (int i = 0; i < size; i++) {
            concatenated.set(i + size, right.get(i));
        }
        return concatenated;
    }

    public static BitSet permute(BitSet input, int[] permutationTable) {
        BitSet output = new BitSet(permutationTable.length);
        for (int i = 0; i < permutationTable.length; i++) {
            output.set(i, input.get(permutationTable[i] - 1));
        }
        return output;
    }

    public static BitSet[] generateSubKeys(BitSet key) {
        BitSet[] subKeys = new BitSet[16];

        // Permutation Choice 1 (PC1)
        BitSet permutedKey = permute(key, PC1);

        // Split into left and right halves
        BitSet left = permutedKey.get(0, 28);
        BitSet right = permutedKey.get(28, 56);

        // Generate 16 subkeys
        for (int i = 0; i < 16; i++) {
            // Perform circular left shift on left and right halves
            int shift = LEFT_SHIFTS[i];
            left = circularLeftShift(left, shift,28);
            right = circularLeftShift(right, shift,28);

            // Permutation Choice 2 (PC2)
            BitSet concatenated = concatenate(left, right, 28);

            subKeys[i] = permute(concatenated, PC2);
        }

        return subKeys;
    }

}