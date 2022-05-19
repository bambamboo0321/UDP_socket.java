import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class client {
    public static  void main(String[] args)throws Exception
    {
        String msg, result, total_data, f, rank, tmp;
        String []c = {"0", "1" , "2", "3", "4", "5", "6", "7", "8"
                , "9", "A", "B", "C", "D", "E", "F"};
        int port = 48484, n, first_time = 0 ,cost_time , count = 0;
        boolean flag = true;

        Scanner scanner = new Scanner(System.in);
        System.out.println(Methods.getLocalHostLANAddress());
        System.out.println("Please input your name :");
        String name = scanner.next();
        System.out.println("Please input the IP address of destination :");
        String ServerIP = scanner.next();
        client_socket cs = new client_socket(port, ServerIP);
        //訊息封包(值 大小 位址 port)
        while(flag)
        {

            //receive server 傳送的n
            System.out.println("~~~~~~Wait server response~~~~~~");
            tmp = cs.receive();
            n = Integer.parseInt(tmp);//訊息轉字串
            do{
                System.out.print("輸入 "+n +" 位數猜測字串:");
                msg = scanner.next();
                while (!window.checkInput(n,msg))
                {
                    System.out.print("請重新輸入 "+n +" 位數猜測字串:");
                    msg = scanner.next();
                }
                if(count == 0)
                {
                    Calendar fCal = Calendar.getInstance();
                    first_time = fCal.get(Calendar.MINUTE)*60 + fCal.get(Calendar.SECOND);
                }
                cs.sendMessage(msg);
                count++;

                System.out.println("~~~~~~Wait server response~~~~~~");

                result = cs.receive();
                System.out.println("count:"+count+" result"+result);
            }while(!result.equals(n+"A0B"));
            //calculate cost time
            Calendar lCal = Calendar.getInstance();

            cost_time = lCal.get(Calendar.MINUTE)*60 + lCal.get(Calendar.SECOND) - first_time;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            //send total data to server

            operator temp = new operator(name, dtf.format(LocalDateTime.now()), count, cost_time);
            cs.sendMessage(temp.getString());

            System.out.println("~~~~~~Wait server response~~~~~~");

            rank = cs.receive();
            System.out.println(rank);

            System.out.print("是否繼續( Y / N ) ? ");
            f = scanner.next();
            while (!f.equals("Y") && !f.equals("N"))
            {
                System.out.print("請重新輸入是否繼續( Y / N ) ? ");
                f = scanner.next();
            }
            if(f.equals("N")) flag = false;

            cs.sendMessage(f);
        }
        System.out.println("程式結束");
    }
}
