import java.awt.Desktop;
import java.io.*;
import javax.swing.*;

public class showFile extends JFrame
{
    JTextArea tarea;
    public showFile(String fpath)
    {
        //tarea=new JTextArea(10,10);
        File file=new File(fpath);
        Desktop desktop=Desktop.getDesktop();
        try 
        {
            /*FileReader reader=new FileReader(fpath);
            BufferedReader read1=new BufferedReader(reader);
            tarea.read(read1,null);
            tarea.requestFocus();*/
            desktop.open(file);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        //add(tarea);
        setSize(300,300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
