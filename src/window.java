import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class window {
    public JFrame frame;
    public static int font_size = 22;
    static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    public window(String title) {
        frame = new JFrame(title);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setBounds(screen.width/4,screen.height/4,screen.width/2,screen.height/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public window() {
        frame = new JFrame("window");
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setBounds(screen.width/4,screen.height/4,screen.width/2,screen.height/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void setShow(boolean show) {
        frame.setVisible(show);
    }
    public void addClientComponents() {


    }
    public static JLabel addLabel(String content, String ID) {
        JLabel label = new JLabel();
        label.setName(ID);
        label.setText(content);
        label.setFont(new Font(Font.DIALOG, Font.PLAIN, font_size));
        return label;
    }
    public static JTextField addTextField(String content, String ID, int length) {
        JTextField textField = new JTextField();
        textField.setName(ID);
        textField.setText(content);
        textField.setColumns(length);
        textField.setFont(new Font(Font.DIALOG, Font.PLAIN, font_size));
        return textField;
    }
    public static JTextArea addTextArea(String ID, int length) {
        JTextArea textArea = new JTextArea();
        textArea.setName(ID);
        textArea.setColumns(length);
        textArea.setLineWrap(true);
        textArea.setFont(new Font(Font.DIALOG, Font.PLAIN,font_size));
        return textArea;
    }
    public static JButton addButton(String name, String ID) {
        JButton jButton = new JButton(name);
        jButton.setName(ID);
        jButton.setFont(new Font(Font.DIALOG, Font.PLAIN, font_size));
        return jButton;
    }
    public static boolean checkInput(int n,String msg) {
        List<String> dir = Arrays.asList("0", "1" , "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F");
        if(msg.length() != n) {
            return false;
        }
        for(int i = 0; i < msg.length(); i++)
        {
            if(!dir.contains(msg.substring(i, i + 1))) {
                 return false;
            }
        }
        return true;
    }

}
