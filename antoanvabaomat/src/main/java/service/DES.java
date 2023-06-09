package service;

import java.util.BitSet;

public class DES {
    //khai bao bien final
    private static final int BLOCK_SIZE = 64;
    private static final int HALF_BLOCK_SIZE = BLOCK_SIZE / 2;
    private static final int SUBKEY_SIZE = 48;
    private static final int ROUND_COUNT = 16;
    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};
    //ham hoan vi nguoc
    private static final int[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    //ham mo rong
    private static final int[] E = {32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};
    //ham hoan vi
    private static final int[] P = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25};

    private static BitSet substitute(BitSet input) {
        BitSet substituted = new BitSet(32);
        int[][][] sBoxes = {
                { // S1
                        {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                        {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                        {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                },
                { // S2
                        {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                        {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                        {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                        {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                },
                //S3
                {
                        {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                        {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                        {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                        {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                }, //        //S4
                {
                        { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                        {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                        {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                        {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                },
                //S5
                {
                        {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                        {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                        {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                        {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
                },
                //S6
                {
                        {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                        {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                        {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                        {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
                },
                //S7
                {
                        {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                        {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                        {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                        {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                },
                //S8
                {
                        { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                        {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                        {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                        {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                }

                //            // S3, S4, S5, S6, S7, S8...
        };

        int bitIndex = 0;
        for (int i = 0; i < 8; i++) {
            int row = 2 * (input.get(bitIndex) ? 1 : 0) + (input.get(bitIndex + 5) ? 1 : 0);
            int col = 8 * (input.get(bitIndex + 1) ? 1 : 0)
                    + 4 * (input.get(bitIndex + 2) ? 1 : 0)
                    + 2 * (input.get(bitIndex + 3) ? 1 : 0)
                    + (input.get(bitIndex + 4) ? 1 : 0);

            int value = sBoxes[i][row][col];
            BitSet bits = BitSet.valueOf(new long[]{value});
            for (int j = 0; j < 4; j++) {
                substituted.set(bitIndex + j, bits.get(j));
            }
            bitIndex += 4;
        }
        return substituted;
    }

    private static BitSet xor(BitSet input1, BitSet input2) {
        BitSet output = (BitSet) input1.clone();
        output.xor(input2);
        return output;
    }

    public static BitSet encrypt(BitSet plaintext, BitSet key) {
        BitSet[] subKeys = DESKey.generateSubKeys(key);

        // Initial permutation (IP)
        BitSet permutedText = DESKey.permute(plaintext, IP);

        // Split into left and right halves
        BitSet left = permutedText.get(0, 32);
        BitSet right = permutedText.get(32, 64);

        // 16 rounds of encryption
        for (int i = 0; i < 16; i++) {
            BitSet previousLeft = left;
            left=right;
            // Expand right half and apply XOR with subkey
            BitSet expanded = DESKey.permute(right, E);
            BitSet subKey = subKeys[i];
            BitSet xored = xor(expanded, subKey);

            // Apply S-box substitution
            BitSet substituted = substitute(xored);

            // Permutation (P)
            BitSet permuted = DESKey.permute(substituted, P);

            // XOR with previous left half

            right = xor(previousLeft, permuted);
        }

        // Concatenate left and right halves
        BitSet concatenated = DESKey.concatenate(right, left, 32);

        // Final permutation (IP^-1)
        BitSet ciphertext = DESKey.permute(concatenated, FP);

        return ciphertext;
    }
    public static String encrypt(String plaintext) {
        BitSet key=new BitSet(64);
        key.set(10,20);


        StringBuilder banro = new StringBuilder();
        banro.append(Convert.TextToBlock(plaintext));

        StringBuilder banma = new StringBuilder();
        while (banro.length() >= 8) {
            String byteString = banro.substring(0, 64);

            BitSet a=Convert.BitToBitSet(byteString);
            banma.append(Convert.BitSetToBit(DES.encrypt(a, key)));

            banro.delete(0, 64);
        }
        return banma.toString();
    }
    public static BitSet decrypt(BitSet ciphertext, BitSet key) {
        BitSet[] subKeys = DESKey.generateSubKeys(key);

        // Initial permutation (IP)
        BitSet permutedText = DESKey.permute(ciphertext, IP);

        // Split into left and right halves
        BitSet left = permutedText.get(0, 32);
        BitSet right = permutedText.get(32, 64);

        // 16 rounds of decryption (reverse order of subkeys)
        for (int i = 15; i >= 0; i--) {
            BitSet previousLeft = left;
            left=right;
            // Expand right half and apply XOR with subkey
            BitSet expanded = DESKey.permute(right, E);
            BitSet subKey = subKeys[i];
            BitSet xored = xor(expanded, subKey);

            // Apply S-box substitution
            BitSet substituted = substitute(xored);

            // Permutation (P)
            BitSet permuted =DESKey. permute(substituted, P);

            // XOR with previous left half
            right = xor(previousLeft, permuted);
        }

        // Concatenate left and right halves
        BitSet concatenated = DESKey.concatenate(right, left,32);

        // Final permutation (IP^-1)
        BitSet decryptedPlaintext = DESKey.permute(concatenated, FP);

        return decryptedPlaintext;
    }
    public static String decrypt(String ciphertext) {
        BitSet key=new BitSet(64);
        key.set(10,20);


        StringBuilder banma = new StringBuilder();
        banma.append(ciphertext);

        StringBuilder banro = new StringBuilder();
        while (banma.length() >= 8) {
            String byteString = banma.substring(0, 64);

            BitSet a=Convert.BitToBitSet(byteString);

            String b=Convert.BitSetToBit(DES.decrypt(a, key));
            banro.append(b);

            banma.delete(0, 64);
        }
        return Convert.BitToText(banro.toString());
    }

}