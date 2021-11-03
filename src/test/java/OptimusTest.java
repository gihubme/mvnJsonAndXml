import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class OptimusTest {
    private final List<Optimus> optimuses = generateOptimuses(5);
    private final List<Integer> ids = Arrays.asList(0, 1, 2, 3, 5, 76, 77, 78, 999, 1000, 1001, 2147483645, 2147483646, 2147483647);

    private static Optimus generateOptimus() {
        int max = Optimus.getMAX_INT()/2;
        int greaterThan = Optimus.getMAX_INT() / (2+new Random().nextInt(100));
        int prime = Optimus.kthPrimeGreaterThanN(greaterThan, 3, max);//1580030173;
//        int modInverse = 59260789;
        int randomInt = new SecureRandom().nextInt(Optimus.getMAX_INT() / 3);//1163945558;
//        int hash = 1103647397;
//        int id = 15;
        return new OptimusWrapper(prime, randomInt);
    }

    @Test
    public void testEncodeDecode() {
        for (Optimus o : optimuses) {
            OptimusWrapper oo = (OptimusWrapper) o;
            log.info("Optimus, prime: {},modInverse: {},randomNumber: {}", oo.ns[0], oo.ns[1], oo.ns[2]);
            int n = 1;
            for (Integer id : ids) {
                int hash = oo.encode(id);
                log.info("n: {}\tid: {}\thash: {}", n, id, hash);
                Assert.assertTrue(id == oo.decode(hash));
                n++;
            }
            log.debug("==========================================");
        }
    }

    @Test
    public void testModInverse() throws Exception {
        for (Optimus o : optimuses) {
            OptimusWrapper oo = (OptimusWrapper) o;
            Assert.assertEquals(oo.ns[1], Optimus.ModInverse(oo.ns[0]));
        }
    }

    public void testIsProbablyPrime() {
        for (Optimus o : optimuses) {
            OptimusWrapper oo = (OptimusWrapper) o;
            Assert.assertTrue(Optimus.isProbablyPrime(oo.ns[0]));
        }
    }

    private List<Optimus> generateOptimuses(int i) {
        List<Optimus> oo = new ArrayList<>();
        for (int k = 0; k < i; k++) {
            oo.add(generateOptimus());
        }
        return oo;
    }

    static class OptimusWrapper extends Optimus {
        int[] ns = new int[3];//prime,modInverse,randomNumber

        OptimusWrapper(int prime, int randomNumber) {
            super(prime, randomNumber);
            ns[0] = prime;
            ns[1] = Optimus.ModInverse(prime);
            ns[2] = randomNumber;
        }
    }
}
//https://primes.utm.edu/lists/small/millions/
//https://www.geeksforgeeks.org/kth-prime-number-greater-than-n/
