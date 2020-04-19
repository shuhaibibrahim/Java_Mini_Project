import javax.swing.*;
import java.awt.FlowLayout;
public class screen2 extends JFrame
{
    JLabel l;
    static JList lst;
    screen2()
    {
        l=new JLabel();
        l.setText("Welcome Superior");
        String week[]= { "Monday","Tuesday","Wednesday", 
                         "Thursday","Friday","Saturday","Sunday"};
        lst=new JList(week);
        lst.setSelectedIndex(0);
        add(l);
        add(lst);
        setSize(250,250);
        setLayout(new FlowLayout());
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}