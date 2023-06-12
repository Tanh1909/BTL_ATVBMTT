package service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;

public class ShamirSecretSharing {

    private final SecureRandom random;

    public ShamirSecretSharing() {
        random = new SecureRandom();
    }

    // Hàm tạo số nguyên ngẫu nhiên trong khoảng [1, prime)
    private BigInteger getRandomNumber(BigInteger prime) {
        BigInteger r;
        do {
            r = new BigInteger(prime.bitLength(), random);
        } while (r.compareTo(prime) >= 0 || r.compareTo(BigInteger.ONE) <= 0);
        return r;
    }

    // Hàm chia sẻ bí mật thành n phần
    public BigInteger[] shareSecret(BigInteger secret, int n, int k, BigInteger prime) {
        BigInteger[] coefficients = new BigInteger[k - 1];
        BigInteger[] shares = new BigInteger[n];

        // Tạo n-1 hệ số ngẫu nhiên
        for (int i = 0; i < k - 1; i++) {
            coefficients[i] = getRandomNumber(prime);
        }

        // Tạo các phần tử của mảng cổng thông tin
        for (int i = 1; i <= n; i++) {
            BigInteger x = BigInteger.valueOf(i);
            BigInteger y = secret;

            // Tính giá trị của đa thức tại điểm x
            for (int j = 0; j < k - 1; j++) {
                BigInteger term = coefficients[j].multiply(x.pow(j + 1)).mod(prime);
                y = y.add(term).mod(prime);
            }

            shares[i - 1] = y;
        }

        return shares;
    }

    // Hàm khôi phục bí mật từ một số phần
    public BigInteger recoverSecret(BigInteger[] shares, int[] indices, BigInteger primeModulo) {
        int threshold = indices.length;
        BigInteger secret = BigInteger.ZERO;

        for (int i = 0; i < threshold; i++) {
            BigInteger xi = BigInteger.valueOf(indices[i]+1);
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < threshold; j++) {
                if (j != i) {
                    BigInteger xj = BigInteger.valueOf(indices[j]+1);
                    numerator = numerator.multiply(xj.negate()).mod(primeModulo);
                    denominator = denominator.multiply(xi.subtract(xj)).mod(primeModulo);
                }
            }
            BigInteger term = shares[indices[i]].multiply(numerator).multiply(denominator.modInverse(primeModulo)).mod(primeModulo);
            secret = secret.add(term).mod(primeModulo);

        }

        return secret;
    }


    public static void main(String[] args) {
        ShamirSecretSharing shamir = new ShamirSecretSharing();

        BigInteger secret = new BigInteger("123456789");
        int n = 5; // Số lượng phần
        int k = 3; // Số lượng phần cần để khôi phục bí mật
        BigInteger prime = BigInteger.valueOf(999999937); // Số nguyên tố lớn

        // Chia sẻ bí mật
        BigInteger[] shares = shamir.shareSecret(secret, n, k, prime);

        // In ra các cổng thông tin
        for (int i = 0; i < shares.length; i++) {
            System.out.println("Share " + (i + 1) + ": " + shares[i]);
        }

        // Khôi phục lại bí mật từ một số phần có chỉ số ngẫu nhiên
        BigInteger[] shares1 = {shares[1],shares[2],shares[3]};
        int[] indices = {3,0,4};
        BigInteger recoveredSecret = shamir.recoverSecret(shares,indices, prime);
        System.out.println("Recovered secret: " + recoveredSecret);
    }
}
