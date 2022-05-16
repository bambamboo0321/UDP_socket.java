import java.io.File;
import java.io.FileWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Scanner;

public class server {
        private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遞迴所有網路通道
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的網路通道下遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback類型的IP位址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-localIP位址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local類型的IP位址沒被發現，先記錄起來
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 若只有 loopback IP位址,則只能選其他的
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
    public static void main(String args[])throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        byte buffer[] = new byte[2048];
        String msg, result, total_data;
        String temp[] = new String[4];
        String arr[][] = new String[50][4];
        String []c = {"0", "1" , "2", "3", "4", "5", "6", "7", "8"
                , "9", "A", "B", "C", "D", "E", "F"};
        int client_portNo = 5555, server_portNo = 5550, readLine = 0;//server在5550監聽
        Boolean flag = true, wrong_input_string = false;
        System.out.println(getLocalHostLANAddress().toString());
        System.out.println("Server端開始接受連線請求!");

        File doc = new File("E:\\college\\college2-2\\Network\\UDP_socket.txt");

        DatagramSocket socket = new DatagramSocket(server_portNo);//設定socket需要設定port
        DatagramPacket rcv_packet = new DatagramPacket(buffer, buffer.length);
        System.out.println("~~~~~~Wait client response~~~~~~");
        socket.receive(rcv_packet);
        while (flag)
        {
            int i = 0;
            System.out.println("請輸入 N 位數 :");
            int n = scanner.nextInt();
            DatagramPacket send_packet = new DatagramPacket(String.valueOf(n).getBytes()
                    ,String.valueOf(n).length(),rcv_packet.getAddress(),client_portNo);

            socket.send(send_packet);//send n to client

            System.out.println("請輸入 N 位數正確字串");
            String ans = scanner.next();
            do{
                if(wrong_input_string)
                {
                    wrong_input_string = false;
                    System.out.println("請重新輸入 N 位數正確字串");
                    ans = scanner.next();
                }
                for(i = 0; i < ans.length(); i++)
                {
                    wrong_input_string = true;
                    for(int j = 0; j < 16; j++)
                    {
                        if(ans.substring(i,i+1).equals(c[j]))
                        {
                            wrong_input_string = false;
                        }
                    }
                    if(wrong_input_string) continue;
                }
            }while (ans.length() != n || wrong_input_string);

            System.out.println("開始遊戲!!在client端請輸入 N 位數字串");

            do{
                System.out.println("~~~~~~Wait client response~~~~~~");
                socket.receive(rcv_packet);//receive guess string
                String guess = new String(buffer,0, rcv_packet.getLength());//訊息轉字串
                System.out.println(guess);
                result = compare(guess, ans);
                send_packet = new DatagramPacket(result.getBytes()
                        ,result.length(),rcv_packet.getAddress(),client_portNo);
                socket.send(send_packet);
            }while (!result.equals(Integer.toString(n)+"A0B"));

            System.out.println("~~~~~~Wait client response~~~~~~");
            socket.receive(rcv_packet);
            total_data = new String(rcv_packet.getData(), 0, rcv_packet.getLength());
            temp = total_data.split("，");
            Scanner obj = new Scanner(doc);

            while (obj.hasNextLine())
            {
                arr[readLine] = obj.nextLine().split("，");
                readLine++;
            }
            obj.close();
            for(i = 0;i<readLine;i++)
                if(Integer.parseInt(temp[3])<Integer.parseInt(arr[i][3]))break;

            FileWriter fw = new FileWriter("E:\\college\\college2-2\\Network\\UDP_socket.txt");

            for(int j = 0;j<readLine;i++)
            {
                if(i == j)
                    fw.write(total_data);
                fw.write(arr[j][0]+"，"+arr[j][1]+"，"+arr[j][2]+"，"+arr[j][3]);
            }
            result = temp[0]+",恭喜你猜對了!你是第"+i+1+"名";
            
            System.out.println(result);
            send_packet = new DatagramPacket(result.getBytes("GBK"),result.length()
                    ,rcv_packet.getAddress(),client_portNo);
            socket.send(send_packet);
            System.out.println("~~~~~~Wait client response~~~~~~");
            socket.receive(rcv_packet);
            String f = new String(buffer, 0, rcv_packet.getLength());//訊息轉字串
            if(f == "N") flag = false;
        }
        System.out.println("程式結束");
    }
    public static String compare(String x, String ans)
    {
        int a = 0, b = 0;
        for(int i = 0; i < ans.length();i++)
        {
            for(int j = 0; j < x.length();j++)
            {
                if(i == j && (ans.substring(i,i+1).equals(x.substring(j,j+1)) ))
                {
                    a++;
                    //System.out.println("a i:"+i+" j:"+j);
                }
                else if(i!=j && ans.substring(i,i+1).equals(x.substring(j,j+1)) )
                {
                    b++;
                    //System.out.println("b i:"+i+" j:"+j);
                }
            }
        }
        return (a+"A"+b+"B");
    }
}
