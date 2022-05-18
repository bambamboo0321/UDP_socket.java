import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class client_socket {
    private final int portNo;
    private final InetAddress ipAddress;
    private final byte[] receiveBuffer = new byte[1000];

    public client_socket(int portNo,String serverIP) throws UnknownHostException {
        this.portNo = portNo;
        this.ipAddress = InetAddress.getByName(serverIP);
    }


    public void sendMessage(String msg) throws IOException {
        int oLength = msg.length();     //將訊息長度大小放到oLength
        byte[] buffer = new byte[oLength];
        buffer = msg.getBytes();
        DatagramPacket packet =
                new DatagramPacket(buffer, oLength, ipAddress, portNo);    //訊息封包(值,大小,位址,port)
        DatagramSocket socket = new DatagramSocket();  //建立socket
        socket.send(packet);    //送出packet封包訊息
        socket.close();         //關閉socket
    }

    public String receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(this.receiveBuffer, this.receiveBuffer.length);
        DatagramSocket socket = new DatagramSocket(this.portNo);
        socket.receive(packet);   //接收封包
        String msg = new String(this.receiveBuffer, 0, packet.getLength());   //將訊息轉為字串
        socket.close();    //關閉socket
        //System.out.println("msg：" + msg);
        return msg;
    }
}
