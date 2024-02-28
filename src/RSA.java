import java.math.BigInteger;
import java.util.Random;

public class RSA {
    public static void main(String[] args) {
     BigInteger p = generateLargePrime();
     BigInteger q = generateLargePrime();
     BigInteger n = p.multiply(q);
     BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
     BigInteger e = generateE(phi);
     BigInteger d = e.modInverse(phi);
     System.out.println("Public Key (e, n): (" + e + ", " + n + ")");
     System.out.println("Private Key (d, n): (" + d + ", " + n + ")");
     String plaintext = "1089";
     //ciphertext = encrypt(plaintext,e,n);
     //System.out.println("ciphertext  :"+ciphertext);
     BigInteger enc=new BigInteger("15505144174396902363");
     String dec = decrypt(enc,new BigInteger("11774803670321060503"),new BigInteger("15733178689855659677"));
     System.out.println("decrypted  :"+dec);
    }
    public static BigInteger generateLargePrime() {
        return BigInteger.probablePrime(32, new Random());
    }
    public static BigInteger generateE(BigInteger phiN) {
        BigInteger e;
        do {
            e = new BigInteger(phiN.bitLength(), new Random());
        } while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phiN) >= 0 || !e.gcd(phiN).equals(BigInteger.ONE));
        return e;
    }
    public static BigInteger encrypt(String plaintext, BigInteger e, BigInteger n) {
        byte[] bytes =  plaintext.getBytes();
        BigInteger m = new BigInteger(bytes);
        return m.modPow(e, n);
    }

    public static String decrypt(BigInteger ciphertext, BigInteger d, BigInteger n) {
        BigInteger plaintext = ciphertext.modPow(d, n);
        return new String(plaintext.toByteArray());
    }

}