import java.io.File;
import java.io.FileWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class server {

    public static void main(String[] args)throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        byte[] buffer = new byte[2048];
        String result, total_data;
        String[] temp;
        String[][] arr = new String[50][4];
        String []c = {"0", "1" , "2", "3", "4", "5", "6", "7", "8"
                , "9", "A", "B", "C", "D", "E", "F"};
        int client_portNo = 5555, server_portNo = 48484, readLine = 0;//server在5550監聽
        boolean flag = true, wrong_input_string = false;
        List<operator> grade = new ArrayList<>();
        System.out.println(Methods.getLocalHostLANAddress());
        System.out.println("Server端開始接受連線請求!");
        DatagramSocket socket = new DatagramSocket(server_portNo);//設定socket需要設定port
        DatagramPacket rcv_packet = new DatagramPacket(buffer, buffer.length);
        System.out.println("~~~~~~Wait client response~~~~~~");
        socket.receive(rcv_packet);
        while (flag)
        {
            System.out.println("請輸入 N 位數 :");
            int n = scanner.nextInt();
            DatagramPacket send_packet = new DatagramPacket(String.valueOf(n).getBytes()
                    ,String.valueOf(n).length(),rcv_packet.getAddress(),client_portNo);

            socket.send(send_packet);//send n to client
            //題目設定
            System.out.println("請輸入 N 位數正確字串");
            String ans = scanner.next();
            do{
                if(wrong_input_string)
                {
                    wrong_input_string = false;
                    System.out.println("請重新輸入 N 位數正確字串");
                    ans = scanner.next();
                }
                for(int i = 0; i < ans.length(); i++)
                {
                    wrong_input_string = true;
                    for(int j = 0; j < 16; j++)
                    {
                        if (ans.substring(i, i + 1).equals(c[j])) {
                            wrong_input_string = false;
                            break;
                        }
                    }
                }
            }while (ans.length() != n || wrong_input_string);

            System.out.println("開始遊戲!!在client端請輸入 N 位數字串");

            do{
                System.out.println("~~~~~~Wait client response~~~~~~");
                socket.receive(rcv_packet);//receive guess string
                String guess = new String(buffer,0, rcv_packet.getLength());//訊息轉字串
                System.out.println(guess);
                result = Methods.compare(guess, ans);
                send_packet = new DatagramPacket(result.getBytes(),result.length(),rcv_packet.getAddress(),client_portNo);
                socket.send(send_packet);
            }while (!result.equals(n+"A0B"));

            System.out.println("~~~~~~Wait client response~~~~~~");
            rcv_packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(rcv_packet);
            total_data = new String(rcv_packet.getData(), 0, rcv_packet.getLength());
            temp = total_data.split(",");
            grade = new ArrayList<>();
            operator tmp = new operator(temp[0],temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]));
            grade.add(tmp);

            result = temp[0] + ",恭喜你猜對了！你是第" + operator.getRank(grade, tmp.name) + "名";

            System.out.println(result);
            send_packet = new DatagramPacket(result.getBytes(StandardCharsets.UTF_8),result.length()
                    ,rcv_packet.getAddress(),client_portNo);
            socket.send(send_packet);

            System.out.println("~~~~~~Wait client response~~~~~~");

            socket.receive(rcv_packet);//是否繼續
            String f = new String(buffer, 0, rcv_packet.getLength());//訊息轉字串
            if(f.equalsIgnoreCase("N")) flag = false;
        }
        socket.close();
        operator.toTxt("UDP_socket.txt",grade);
        System.out.println("程式結束");
    }

}
