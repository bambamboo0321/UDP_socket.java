import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

}
