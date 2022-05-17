import java.net.*;
import java.util.Calendar;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class client {
    public static  void main(String[] args)throws Exception
    {
        String msg, result, total_data, f, rank;
        String []c = {"0", "1" , "2", "3", "4", "5", "6", "7", "8"
                , "9", "A", "B", "C", "D", "E", "F"};
        int client_portNo = 5555, server_portNo = 5550, n, fMin = 0, fSec = 0, lMin, lSec ,cost_time , count = 0;
        boolean wrong_input_string = false, flag = true;
        byte[] rcv_buf = new byte[2048];
        byte[] buf = new byte[2048];
        Scanner scanner = new Scanner(System.in);
        DatagramSocket socket = new DatagramSocket(client_portNo);//設定socket需要設定port
        DatagramPacket rcv_packet = new DatagramPacket(rcv_buf, rcv_buf.length);
        //建立Datagram packet資料封包,限制packet值和長度大小
        System.out.println(Methods.getLocalHostLANAddress());
        System.out.println("Please input your name :");
        String name = scanner.next();
        System.out.println("Please input the IP address of destination :");
        String ServerIP = scanner.next();
        InetAddress addr = InetAddress.getByName("192.168.0.11");

        //訊息封包(值 大小 位址 port)
        DatagramPacket send_packet = new DatagramPacket(ServerIP.getBytes(), ServerIP.length(), addr, server_portNo);
        socket.send(send_packet);
        while(flag)
        {
            //receive server 傳送的n
            System.out.println("~~~~~~Wait server response~~~~~~");
            socket.receive(rcv_packet);//接受封包
            n = Integer.parseInt(new String(rcv_packet.getData(), 0, rcv_packet.getLength()));//訊息轉字串
            do{
                System.out.print("輸入 "+n +" 位數猜測字串:");
                msg = scanner.next();
                if(count == 0)
                {
                    Calendar fCal = Calendar.getInstance();
                    fMin = fCal.get(Calendar.MINUTE);
                    fSec = fCal.get(Calendar.SECOND);
                    System.out.println("min:"+fMin+" sec"+fSec);
                }
                do{
                    if(wrong_input_string || msg.length() != n)
                    {
                        wrong_input_string = false;
                        System.out.println("請重新輸入 "+n+" 位數猜測字串");
                        msg = scanner.next();
                    }
                    for(int i = 0; i < msg.length(); i++)
                    {
                        wrong_input_string = true;
                        for(int j = 0; j < 16; j++)
                        {
                            if (msg.substring(i, i + 1).equals(c[j])) {
                                wrong_input_string = false;
                                break;
                            }
                        }
                    }
                }while (msg.length() != n || wrong_input_string);

                send_packet = new DatagramPacket(msg.getBytes(), msg.length(), addr, server_portNo);//訊息封包(值 大小 位址 port)
                socket.send(send_packet);//send guess
                count++;

                //rcv_packet = new DatagramPacket(rcv_buf, rcv_buf.length);
                System.out.println("~~~~~~Wait server response~~~~~~");

                socket.receive(rcv_packet);//get result of compare
                result = new String(rcv_buf, 0, rcv_packet.getLength());
                System.out.println("count:"+count+" result"+result);
            }while(!result.equals(n+"A0B"));
            //calculate cost time
            Calendar lCal = Calendar.getInstance();
            lMin = lCal.get(Calendar.MINUTE);
            lSec = lCal.get(Calendar.SECOND);
            System.out.println("lmin:"+ lMin +" fmin:"+fMin+" sec:"+lSec+" fSec:"+fSec);

            if(lSec-fSec<0) lMin -= 1;
            cost_time = 60 * ((lMin-fMin<0)?lMin+60-fMin:lMin-fMin)  + ((lSec-fSec<0)?lSec+60-lSec:lSec-fSec);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            //send total data to server
            total_data=name+"，"+dtf.format(LocalDateTime.now())+"，"+count+"，"+cost_time;
            System.out.println(total_data);
            send_packet = new DatagramPacket(total_data.getBytes(), total_data.length()
                    , addr, server_portNo);//訊息封包(值 大小 位址 port)
            socket.send(send_packet);

            System.out.println("~~~~~~Wait server response~~~~~~");

            rcv_packet = new DatagramPacket(buf, buf.length);
            socket.receive(rcv_packet);//receive rank from server
            rank = new String(rcv_packet.getData(), 0, rcv_packet.getLength(),"GBK");
            System.out.println(rank);

            System.out.print("是否繼續( Y / N ) ? ");
            f = scanner.next();
            while (!f.equals("Y") && !f.equals("N"))
            {
                System.out.print("請重新輸入是否繼續( Y / N ) ? ");
                f = scanner.next();
            }
            if(f.equals("N")) flag = false;

            send_packet = new DatagramPacket(f.getBytes(), f.length()
                    , addr, server_portNo);//訊息封包(值 大小 位址 port)
            socket.send(send_packet);
        }
        socket.close();
        System.out.println("程式結束");
    }
}
