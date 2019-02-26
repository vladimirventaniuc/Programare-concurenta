import java.io.*;
import java.net.*;

public class Server {
    public static void TCP() throws Exception {
        ServerSocket s = new ServerSocket(9999);
        Socket ss = s.accept();

        System.out.println("connected");
        BufferedInputStream in =
                new BufferedInputStream(ss.getInputStream());


        byte[] buffer = new byte[65535];
        int len = 0;
        long numberOfMessages=0;
        long numberOfBytes =0;
        while ((len = in.read(buffer)) > 0) {
            numberOfMessages++;
            numberOfBytes+=len;
//            out.write(buffer, 0, len);
//            System.out.print("#");
        }
        String protocol = "TCP";
        printInfo(protocol, numberOfBytes, numberOfMessages);
        ss.close();
    }

    public static void UDP() throws IOException{

        DatagramSocket ds = new DatagramSocket(1234);
        byte[] receive = new byte[65535];
        long numberOfMessages=0;
        long numberOfBytes =0;
        DatagramPacket DpReceive = null;
        while (true)
        {
            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);


            ds.receive(DpReceive);

            if (data(receive).toString().equals("bye"))
            {
                break;
            }
            numberOfMessages++;
            numberOfBytes+=DpReceive.getLength();
            receive = new byte[65535];
        }
        printInfo("UDP",numberOfBytes, numberOfMessages);
    }


    public static void printInfo(String protocol, long bytes, long messages){
        System.out.println("____________________");
        System.out.println("Protocol: " + protocol);
        System.out.println("Number of messages: " + messages);
        System.out.println("Number of bytes: " + bytes);
    }
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().toString());
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



    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}
