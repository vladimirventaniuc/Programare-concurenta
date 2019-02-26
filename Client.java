import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final int BUFFER_SIZE = 1024 * 50;
    public static void UDP() throws IOException{
        Scanner sc = new Scanner(System.in);

        DatagramSocket ds = new DatagramSocket();

        InetAddress ip = InetAddress.getLocalHost();
        byte buffer[] =new byte[BUFFER_SIZE];
        BufferedInputStream in =
                new BufferedInputStream(
                        new FileInputStream("inbox.zip"));

        int len = 0;
        long numberOfMessages=0;
        long numberOfBytes =0;
        long startTime = System.currentTimeMillis();

        while ((len = in.read(buffer)) > 0) {
            DatagramPacket DpSend =
                    new DatagramPacket(buffer, buffer.length, ip, 1234);
            ds.send(DpSend);
            numberOfMessages++;
            numberOfBytes+=DpSend.getLength();
//            System.out.print("#");
        }

        long endTime = System.currentTimeMillis();

        printInfo("UDP",endTime-startTime, numberOfBytes, numberOfMessages);
        buffer = "bye".getBytes();
        DatagramPacket DpSend =
                new DatagramPacket(buffer, buffer.length, ip, 1234);
        ds.send(DpSend);


    }
    public static void printInfo(String protocol, long time, long bytes, long messages){
        System.out.println("_______________________________________________");
        System.out.println("Protocol: " + protocol);
        System.out.println("Transfer time: " + (time) + " milliseconds");
        System.out.println("Number of messages: " + messages);
        System.out.println("Number of bytes: " + bytes);
    }
        public static void TCP() throws Exception{
            Socket s = new Socket("localhost", 9999);
//            DataOutputStream dout = new DataOutputStream(s.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            BufferedInputStream in =
                    new BufferedInputStream(
                            new FileInputStream("inbox.zip"));

            BufferedOutputStream out =
                    new BufferedOutputStream(s.getOutputStream());

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            long numberOfBytes =0;
            long numberOfMessages = 0;
            long startTime = System.currentTimeMillis();
            while ((len = in.read(buffer)) > 0) {
                numberOfMessages++;
                numberOfBytes+=len;
                out.write(buffer, 0, len);

            }
            long endTime = System.currentTimeMillis();

            printInfo("TCP",endTime-startTime, numberOfBytes, numberOfMessages);
            s.close();
        }

    public static void main(String args[])
    {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UDP();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TCP();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }
    public static void test(){

    }
}
