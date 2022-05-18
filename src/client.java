import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class client {
    public static window panel = new window("Client of Game");
    public static JTextArea console = window.addTextArea("id", 10);
    public static JButton connect = window.addButton("connect", "connect");
    public static JLabel player = window.addLabel("Player","player");
    public static JTextField player_name = window.addTextField("guest", "player_name", 10);
    public static JLabel IP = window.addLabel("IP","IP");
    public static JTextField destination = window.addTextField("", "serverIP", 10);

    public static String name="guest",ServerIP="0.0.0.0";
    public static void addComponents() {
        console.setBounds(10,10,window.screen.width/2-40,window.screen.height/5-10);
        console.setEditable(false);
        panel.frame.add(console);
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line1.setBounds(10,window.screen.height/5,window.screen.width/2-40,50);

        connect.setSize(160,40);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = player_name.getText();
                ServerIP = destination.getText();
            }
        });
        line1.add(connect);


        player.setSize(80,40);
        line1.add(player);


        player_name.setSize(160,40);
        line1.add(player_name);


        IP.setSize(40,40);
        line1.add(IP);

        destination.setSize(320,40);
        line1.add(destination);

        panel.frame.add(line1);
    }
    public static void main(String[] args)throws Exception
    {
        int port = 48484;
        addComponents();
        panel.setShow(true);
        String result, f, rank;
        int n, fMin = 0, fSec = 0, lMin, lSec ,cost_time , count = 0;
        boolean flag = true;
        String player_msg;
        while(flag) {
            //receive server 傳送的n
            client_socket cs = new client_socket(port, ServerIP);
            System.out.println("~~~~~~Wait server response~~~~~~");
            n = Integer.parseInt(cs.receive());//訊息轉字串

            do {
                player_msg = window.checkInput(n);
                if (count == 0) {
                    Calendar fCal = Calendar.getInstance();
                    fMin = fCal.get(Calendar.MINUTE);
                    fSec = fCal.get(Calendar.SECOND);
                    System.out.println("min:" + fMin + " sec" + fSec);
                }
                cs.sendMessage(player_msg);
                count++;
                System.out.println("~~~~~~Wait server response~~~~~~");
                result = cs.receive();
                System.out.println("count:" + count + " result" + result);
            } while (!result.equals(n + "A0B"));//答對
            //calculate cost time
            Calendar lCal = Calendar.getInstance();
            lMin = lCal.get(Calendar.MINUTE);
            lSec = lCal.get(Calendar.SECOND);

            if (lSec - fSec < 0) lMin -= 1;
            cost_time = 60 * ((lMin - fMin < 0) ? lMin + 60 - fMin : lMin - fMin) + ((lSec - fSec < 0) ? lSec + 60 - lSec : lSec - fSec);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            //send total data to server

            operator temp = new operator(name, dtf.format(LocalDateTime.now()), count, cost_time);
            cs.sendMessage(temp.getString());

            System.out.println("~~~~~~Wait server response~~~~~~");
            rank = cs.receive();
            System.out.println(rank);

            Scanner scanner = new Scanner(System.in);
            System.out.print("是否繼續( Y / N ) ? ");
            f = scanner.next();
            while (!f.equals("Y") && !f.equals("N")) {
                System.out.print("請重新輸入是否繼續( Y / N ) ? ");
                f = scanner.next();
            }
            if (f.equals("N")) flag = false;
            cs.sendMessage(f);
        }
        System.out.println("程式結束");
    }
}
