import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class client {
    public static window panel = new window("Client of Game");
    public static JTextArea console = window.addTextArea("id", 10);
    public static JButton connect = window.addButton("connect", "connect");
    public static JLabel player = window.addLabel("Player","player");
    public static JTextField player_name = window.addTextField("guest", "player_name", 10);
    public static JLabel IP = window.addLabel("IP","IP");
    public static JTextField destination = window.addTextField("", "serverIP", 10);
    public static JTextField Answer = window.addTextField("", "answer", 10);
    public static String name="guest",ServerIP="0.0.0.0";
    public static int flag = 2;// 若flag = 2，則跳在IP、名字可編輯模式，flag = 0 >> 遊玩中
    public static boolean button_clicked = false;
    public static void main(String[] args)throws Exception {
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
            button_clicked = true;
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
        String operation = "1";

        int length = -1,count =0,time_before=0,cost_time,pass;
        while (true) {
            client_socket cs = new client_socket(48484, ServerIP);
            switch (operation) {
                case "1" -> {
                    length = Integer.parseInt(cs.receive());//訊息轉字串
                    if (length != -1) operation = "2";
                }
                case "2" -> {
                    if (button_clicked && window.checkInput(length, Answer.getText())) {
                        cs.sendMessage(Answer.getText());
                        if (count == 0) {
                            Calendar fCal = Calendar.getInstance();
                            time_before = fCal.get(Calendar.MINUTE) * 60 + fCal.get(Calendar.SECOND);
                        }
                        count++;
                    }
                    String temp = cs.receive();
                    if (Objects.equals(temp, length + "A0B")) {
                        cost_time = Calendar.getInstance().get(Calendar.MINUTE) * 60 + Calendar.getInstance().get(Calendar.SECOND) - time_before;
                        operator opt = new operator(name, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()), count, cost_time);
                        cs.sendMessage(opt.getString());
                        operation = "3";
                    }
                }
                case "3" -> {
                    String rank = cs.receive();
                    if (rank != null) {
                        console.setText(console.getText() + "~~~~~~" + rank + "~~~~~~" + "\r\n");
                        operation = "4";
                    }
                }
                case "4" -> {
                    pass = JOptionPane.showConfirmDialog(panel.frame, "是否繼續遊玩", "選擇", JOptionPane.YES_NO_OPTION);
                    if (pass == 1) {
                        cs.sendMessage("N");
                        player_name.setEditable(true);
                        destination.setEditable(true);
                        console.setText(console.getText() + "程式結束" + "\r\n");
                    } else cs.sendMessage("Y");
                }
            }
        }
    }
}
