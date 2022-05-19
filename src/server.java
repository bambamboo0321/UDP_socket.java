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
        String result, total_data, IP="",guess ="";
        String[] temp;
        String[][] arr = new String[50][4];
        int port = 48484;
        boolean flag = true;
        List<operator> grade ;
        server_socket cs = new server_socket(port);
        System.out.println(Methods.getLocalHostLANAddress());

        while (flag)
        {
            System.out.println("請輸入 N 位數 :");
            int n = scanner.nextInt();
            cs.sendMessage(String.valueOf(n));
            //題目設定
            System.out.println("請輸入 N 位數正確字串");
            String ans = scanner.next();

            while(!window.checkInput(n, ans))
            {
                System.out.println("請重新輸入 N 位數正確字串");
                ans = scanner.next();
            }

            System.out.println("開始遊戲!!在client端請輸入 N 位數字串");

            do{
                System.out.println("~~~~~~Wait client response~~~~~~");
                guess = cs.receive();
                System.out.println(guess);
                result = Methods.compare(guess, ans);
                cs.sendMessage(result);
            }while (!result.equals(n+"A0B"));

            System.out.println("~~~~~~Wait client response~~~~~~");
            total_data = cs.receive();
            temp = total_data.split(",");
            grade = new ArrayList<>();
            operator.readFile("UDP_socket.txt",grade);

            operator tmp = new operator(temp[0],temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]));
            grade.add(tmp);
            operator.toTxt("UDP_socket.txt",grade);

            result = temp[0] + ",恭喜你猜對了！你是第" + operator.getRank(grade, tmp.name) + "名";

            System.out.println(result);
           cs.sendMessage(result);

            System.out.println("~~~~~~Wait client response~~~~~~");

            String f = cs.receive();
            if(f.equalsIgnoreCase("N")) flag = false;
        }
        System.out.println("程式結束");
    }

}
