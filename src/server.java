import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class server {
    public static window panel = new window("Server of Game");
    public static JTextArea console = window.addTextArea("id", 10);
    public static JButton connect = window.addButton("connect", "connect");
    public static JLabel player = window.addLabel("Player","player");
    public static JTextField player_name = window.addTextField("guest", "player_name", 10);
    public static JLabel IP = window.addLabel("IP","IP");
    public static JTextField destination = window.addTextField("", "serverIP", 10);
    public static JTextField Answer = window.addTextField("", "answer", 10);
    public static String name="guest",ServerIP="0.0.0.0";
    public static int flag = 2;// 若flag = 2，則跳在IP、名字可編輯模式，flag = 0 >> 遊玩中
    public static void main(String[] args)throws Exception
    {
        Scanner scanner = new Scanner(System.in);
        String result, guess, total_data, ans="", operation = "1";
        String[] temp;
        int n = 0;
        boolean flag = true;
        List<operator> grade = new ArrayList<>();
        System.out.println(Methods.getLocalHostLANAddress());
        while (true) {
            server_socket cs = new server_socket(3300);
            switch (operation) {
                case "1" -> {
                    System.out.println("請輸入 N 位數 :");
                    n = scanner.nextInt();
                    cs.sendMessage(String.valueOf(n));//send n to client
                    operation = "2";
                }
                case "2" -> {
                    System.out.println("請輸入 N 位數正確字串");
                    ans = scanner.next();
                    while(!window.checkInput(n,ans)) {
                        System.out.println("重新輸入 N 位數正確字串");
                        ans = scanner.next();
                    }
                    System.out.println("開始遊戲!!在client端請輸入 N 位數字串");
                    operation = "3";
                }
                case "3" -> {
                    System.out.println("~~~~~~Wait client guess~~~~~~");
                    guess = cs.receive();//receive guess string
                    System.out.println(guess);
                    result = Methods.compare(guess, ans);
                    cs.sendMessage(result);
                    if(result == n+"A0B") operation = "4";
                }
                case "4" -> {
                    System.out.println("~~~~~~Wait client data~~~~~~");
                    total_data = cs.receive();
                    temp = total_data.split(",");
                    grade = new ArrayList<>();
                    operator.readFile("UDP_socket.txt",grade);
                    operator tmp = new operator(temp[0],temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]));
                    grade.add(tmp);
                    result = tmp.name + ",恭喜你猜對了！你是第" + operator.getRank(grade, tmp.name) + "名";
                    System.out.println(result);
                    cs.sendMessage(result);
                    operation = "5";
                }
                case "5" -> {
                    String f=cs.receive();//是否繼續
                    if(f.equalsIgnoreCase("N"))
                    {
                        operator.toTxt("UDP_socket.txt",grade);
                        System.out.println("程式結束");
                        operation = "1";
                    }

                }
            }
        }
    }

}
