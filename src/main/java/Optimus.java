import lombok.Getter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

//https://github.com/pjebs/optimus-go/blob/v1.0.0/optimus.go

public class Optimus {
    @Getter
    private static final int MAX_INT = Integer.MAX_VALUE;//2147483647 = 2^31-1
    private final int prime;
    private final int modInverse;
    private final int randomNumber;

    public Optimus(int prime, int modInverse, int randomNumber) {
        if (!isProbablyPrime(prime))
            throw new IllegalArgumentException(String.format("%d is not a prime number", prime));

        this.prime = prime;
        this.modInverse = modInverse;
        this.randomNumber = randomNumber;
    }

    public Optimus(int prime, int randomNumber) {
        this(prime, ModInverse(prime), randomNumber);
    }

    public static int ModInverse(int n) {
        BigInteger p = BigInteger.valueOf(n);
        long l = Long.valueOf(MAX_INT) + 1L;
        BigInteger m = BigInteger.valueOf(l);
        return p.modInverse(m).intValue();
    }

    public static boolean isProbablyPrime(int n) {
        return BigInteger.valueOf(n - 1).nextProbablePrime().equals(BigInteger.valueOf(n));
    }

    //t schould be < MAX_INT, but we get out of memory VM error, MAX_INT/2??
    public static boolean[] sieveOfEratosthenes(int t) {
        boolean prime[] = new boolean[t+1];
        Arrays.fill(prime, true);// first let set all numbers as prime
        for (int p = 2; p * p <= t; p++) {
            if (prime[p]) {
                for (int i = p * 2; i <= t; i += p) {
                    prime[i] = false;
                }
            }
        }
        return prime;
    }

    public static List<Integer> sievePrimes(int t) {
        boolean prime[] = sieveOfEratosthenes(t);
        List<Integer> primeNumbers = new LinkedList<>();
        for (int i = 2; i <= t; i++) {
            if (prime[i]) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    // find the kth prime greater than n and where n<t and kth prime<t
    static int kthPrimeGreaterThanN(int n, int k, int t) {
        if(n>t || k>t)
            throw new IllegalArgumentException();
        boolean prime[] = sieveOfEratosthenes(t);
        int res = -1;
        for (int i = n + 1; i < t; i++) {
            // decrement k if i is prime
            if (prime[i] == true)
                k--;
            if (k == 0) {
                res = i;
                break;
            }
        }
        return res;
    }

    static class Prime {

        public static IntStream generate(int limit) {
            return IntStream.range(2, Integer.MAX_VALUE).filter(Prime::isPrime).limit(limit);
        }

        private static boolean isPrime(int n) {
            return IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0);
        }
    }

    public int encode(int n) {
        return ((n * this.prime) & MAX_INT) ^ this.randomNumber;
    }

    public int decode(int n) {
        return ((n ^ this.randomNumber) * this.modInverse) & MAX_INT;
    }

}