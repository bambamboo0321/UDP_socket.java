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
        String result, guess, total_data;
        String[] temp;
        boolean flag = true;
        List<operator> grade = new ArrayList<>();
        while (flag)
        {
            System.out.println(Methods.getLocalHostLANAddress());
            server_socket cs = new server_socket(48484);
            System.out.println("請輸入 N 位數 :");
            int n = scanner.nextInt();
            cs.sendMessage(String.valueOf(n));//send n to client

            //題目設定
            System.out.println("請輸入 N 位數正確字串");
            String ans = scanner.next();
            while(!window.checkInput(n,ans)) {

            }

            System.out.println("開始遊戲!!在client端請輸入 N 位數字串");

            do{
                System.out.println("~~~~~~Wait client response~~~~~~");
                guess = cs.receive();//receive guess string
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

            result = tmp.name + ",恭喜你猜對了！你是第" + operator.getRank(grade, tmp.name) + "名";

            System.out.println(result);
            cs.sendMessage(result);

            System.out.println("~~~~~~Wait client response~~~~~~");

            String f=cs.receive();//是否繼續
            if(f.equalsIgnoreCase("N")) flag = false;
        }
        operator.toTxt("UDP_socket.txt",grade);
        System.out.println("程式結束");
    }

}
