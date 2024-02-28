import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Socket {
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
    public static void main(String[] args) {
        final int PORT = 12345; // Choose a port number
        BigInteger p = generateLargePrime();
        BigInteger q = generateLargePrime();
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = generateE(phi);
        BigInteger d = e.modInverse(phi);
        System.out.println("Public Key (e, n): (" + e + ", " + n + ")");
        System.out.println("Private Key (d, n): (" + d + ", " + n + ")");
        // Client thread
        new Thread(() -> {
            try {
                DatagramSocket socket = new DatagramSocket();
                socket.setSoTimeout(1000); // Timeout for receiving messages

                while (true) {
                    // Sending message to other client

                    InetAddress otherClientAddress = InetAddress.getByName("172.20.10.3"); // Replace with other client's IP

                    BigInteger message2 = e;
                    BigInteger message4 = n;
                    byte[] sendData = (message2.toString()+","+message4.toString()).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, otherClientAddress, PORT);
                    socket.send(sendPacket);


                     // Wait for a while before sending the next message
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);

                    String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Receiver: Message received: " + message);

                }
            } catch (IOException ee) {
                ee.printStackTrace();
            } 
        }).start();
    }
}
