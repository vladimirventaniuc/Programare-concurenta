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
                        new FileInputStream("bsmx_logs.7z"));

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

        printInfo("UDP",endTime-startTime, numberOfBytes, numberOfMessages,0);
        buffer = "bye".getBytes();
        DatagramPacket DpSend =
                new DatagramPacket(buffer, buffer.length, ip, 1234);
        ds.send(DpSend);


    }

    public static void UDPACK() throws IOException{

        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(500);
        InetAddress ip = InetAddress.getLocalHost();
        byte buffer[] =new byte[BUFFER_SIZE];
        BufferedInputStream in =
                new BufferedInputStream(
                        new FileInputStream("XMLValidator.jar"));

        int len = 0;
        long numberOfMessages=0;
        long numberOfBytes =0;
        long startTime = System.currentTimeMillis();
        long lostPackages=0;
        while ((len = in.read(buffer)) > 0) {
            DatagramPacket DpSend =
                    new DatagramPacket(buffer, buffer.length, ip, 1234);
            ds.send(DpSend);
            DatagramPacket DpReceive2 = new DatagramPacket("received".getBytes(), "received".getBytes().length);
            try {
                ds.receive(DpReceive2);

            }catch (java.net.SocketTimeoutException e) {
            lostPackages++;
            }
            numberOfMessages++;
            numberOfBytes+=DpSend.getLength();
//            System.out.print("#");
        }

        long endTime = System.currentTimeMillis();

        printInfo("UDP",endTime-startTime, numberOfBytes, numberOfMessages,0);
        buffer = "bye".getBytes();
        DatagramPacket DpSend =
                new DatagramPacket(buffer, buffer.length, ip, 1234);
        ds.send(DpSend);


    }


    public static void printInfo(String protocol, long time, long bytes, long messages, long lostPackages){
        System.out.println("_______________________________________________");
        System.out.println("Protocol: " + protocol);
        System.out.println("Transfer time: " + (time) + " milliseconds");
        System.out.println("Number of messages: " + messages);
        System.out.println("Number of bytes: " + bytes);
//        System.out.println("Lost packages " + lostPackages);
    }
    public static void TCP() throws Exception{
        Socket s = new Socket("localhost", 9999);
//            DataOutputStream dout = new DataOutputStream(s.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        BufferedInputStream in =
                new BufferedInputStream(
                        new FileInputStream("bsmx_logs.7z"));

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

        printInfo("TCP",endTime-startTime, numberOfBytes, numberOfMessages,0);
        s.close();
    }
    public static void TCPACK() throws Exception{
        Socket s = new Socket("localhost", 9999);
//            DataOutputStream dout = new DataOutputStream(s.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        s.setSoTimeout(500);
        BufferedInputStream in =
                new BufferedInputStream(
                        new FileInputStream("bsmx_logs.7z"));

        BufferedOutputStream out =
                new BufferedOutputStream(s.getOutputStream());

        BufferedInputStream fromServer = new BufferedInputStream(s.getInputStream());
        byte[] buffer = new byte[BUFFER_SIZE];
        byte[] buffer2 = new byte[BUFFER_SIZE];
        int len = 0;
        long numberOfBytes =0;
        long numberOfMessages = 0;
        long startTime = System.currentTimeMillis();
        long lostPackages = 0;
        while ((len = in.read(buffer)) > 0) {
            numberOfMessages++;
            numberOfBytes+=len;
            out.write(buffer, 0, len);
            try {
                fromServer.read(buffer2);
            }catch (java.net.SocketTimeoutException e) {
                lostPackages++;
            }

        }
        long endTime = System.currentTimeMillis();

        printInfo("TCP",endTime-startTime, numberOfBytes, numberOfMessages, lostPackages);
        s.close();
    }
    public static void main(String args[])
    {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UDPACK();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TCPACK();
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
