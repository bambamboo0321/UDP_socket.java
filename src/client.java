import javax.swing.*;
import java.awt.*;
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
    public static JTextField Answer = window.addTextField("", "answer", 10);
    public static String checkInput(int n) {
        List<String> dir = Arrays.asList("0", "1" , "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F");
        Scanner scanner = new Scanner(System.in);
        System.out.print("輸入 "+n +" 位數猜測字串:");
        String msg = scanner.next();
        boolean pass = false;
        while(!pass) {
            if(msg.length() != n) {
                System.out.println("請重新輸入 "+n+" 位數猜測字串");
                msg = scanner.next();
            }
            else{
                pass = true;
                for(int i = 0; i < msg.length(); i++)
                {
                    if(!dir.contains(msg.substring(i, i + 1))) {
                        pass = false;
                        break;
                    }
                }
            }
        }
        return msg;
    }
    public static String name="guest",ServerIP="0.0.0.0";
    public static int flag = 2;// 若flag = 2，則跳在IP、名字可編輯模式，flag = 0 >> 遊玩中
    public static void main(String[] args)throws Exception
    {
        console.setBounds(10,10,window.screen.width/2-40,window.screen.height/5-10);
        console.setEditable(false);
        panel.frame.add(console);
        JPanel line1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line1.setBounds(10,window.screen.height/5,window.screen.width/2-40,50);

        connect.setSize(160,40);
        connect.addActionListener(e -> {
            name = player_name.getText();
            player_name.setEditable(false);
            ServerIP = destination.getText();
            destination.setEditable(false);
            if(flag == 2) console.setText(console.getText() + name + "," + ServerIP +"\r\n");
            flag = 0;
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

        JPanel line2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        line2.setBounds(10,window.screen.height/5+60,window.screen.width/2-40,50);

        Answer.setSize(160,40);
        line2.add(Answer);

        panel.frame.add(line1);
        panel.frame.add(line2);
        panel.setShow(true);

        String player_msg;
        while(flag != 1) {
            int n, fMin = 0, fSec = 0, lMin, lSec ,cost_time , count = 0;
            String result, rank;
            //receive server 傳送的n
            client_socket cs = new client_socket(48484, ServerIP);

            if(flag == 0) console.setText(console.getText() + "~~~~~~Wait server response~~~~~~" +"\r\n");
            n = Integer.parseInt(cs.receive());//訊息轉字串
            do {
                player_msg = checkInput(n);
                if (count == 0) {
                    Calendar fCal = Calendar.getInstance();
                    fMin = fCal.get(Calendar.MINUTE);
                    fSec = fCal.get(Calendar.SECOND);
                    if(flag == 0) console.setText(console.getText() + "min:" + fMin + " sec" + fSec +"\r\n");
                }
                cs.sendMessage(player_msg);
                count++;
                if(flag == 0) console.setText(console.getText() + "~~~~~~Wait server response~~~~~~" +"\r\n");
                result = cs.receive();
                if(flag == 0) console.setText(console.getText() + "count:" + count + " result" + result +"\r\n");
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

            if(flag == 0) console.setText(console.getText() + "~~~~~~Wait server response~~~~~~" +"\r\n");
            rank = cs.receive();
            if(flag == 0) console.setText(console.getText() + "~~~~~~" + rank + "~~~~~~" +"\r\n");

            flag = JOptionPane.showConfirmDialog(panel.frame,"是否繼續遊玩","選擇",JOptionPane.YES_NO_OPTION);
            if(flag == 1) cs.sendMessage("N");
            else cs.sendMessage("Y");
        }
        console.setText(console.getText() + "程式結束" +"\r\n");
    }
}
