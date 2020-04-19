import java.awt.FlowLayout;
import java.awt.event.*;
import java.net.URL;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JOptionPane.*;

import java.awt.*; 
import java.sql.*;


class frame1 extends JFrame implements ActionListener{ 
    JRadioButton sup,res;
    JLabel u,p,error;
    JTextField uname;
    JPasswordField pass;
    JButton login;
    boolean flag=false;

    public frame1()
    {       
        sup=new JRadioButton("Superior");
        res=new JRadioButton("Researcher");
        ButtonGroup group1=new ButtonGroup();
        group1.add(sup);
        group1.add(res);
        u=new JLabel("User Name");
        p=new JLabel("Password");
        uname=new JTextField(20);
        pass=new JPasswordField(20);
        login=new JButton("Login");
        login.addActionListener(this);
        error=new JLabel();
        try
        {
            BufferedImage img = ImageIO.read(new URL("C:\\Users\\Shamila\\Desktop\\library.jpg"));
            ImageIcon icon = new ImageIcon(img);
            JLabel imgLabel=new JLabel(icon);
            add(imgLabel);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        login.setBackground(Color.green);
        add(sup);
        add(res);
        add(u);
        add(uname);
        add(p);
        add(pass);
        add(login);
        add(error);

        sup.setBounds(225,50,80,25);
        res.setBounds(305,50,100,25);
        u.setBounds(235,85,100,25);
        uname.setBounds(235,115,150,25);
        p.setBounds(235,150,100,25);
        pass.setBounds(235,180,150,25);
        login.setBounds(260,215,100,30);
        setLayout(null);
        setVisible(true);
        setSize(450,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent ae)
    {
        //connecting to the database
        try
        {  
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3308/library","root","");
            PreparedStatement stm=null;
            if(sup.isSelected())
            {  
                stm = con.prepareStatement("select * from superior where uname=? and pass=?");
                flag=true;
            }    
            else if(res.isSelected())
            {
                stm=con.prepareStatement("select * from researcher where uname=? and pass=?");
                flag=true;
            }    
            else       
            {
                JOptionPane.showMessageDialog(null,"Select whether you are superior or reseacher","Alert",JOptionPane.INFORMATION_MESSAGE);
                flag=false;
            } 
            if(flag)
            {
                String userName=uname.getText();
                String pwd=new String(pass.getPassword());
                stm.setString(1,userName);
                stm.setString(2,pwd);  
                ResultSet rs=stm.executeQuery();          
                if(!rs.next())
                    JOptionPane.showMessageDialog(null,"Invalid username or password","Error",JOptionPane.ERROR_MESSAGE);
                else
                {
                    if(sup.isSelected())
                    {
                        new update();//Update last modified date of files
                        new homesup(userName);
                    }
                    else
                    {
                        new update();//Update last modified date of files
                        new homeres(userName);
                    }
                    dispose();
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        } 
    }
}  
public class screen1
{
    public static void main(String[] args) {
        frame1 f=new frame1();
    }
}