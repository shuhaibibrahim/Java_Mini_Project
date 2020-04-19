import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.*;
import java.awt.Dimension;
import java.awt.*;

public class work extends JFrame
{
    JPanel topPanel=new JPanel();

    ResultSet rs,rs1;
    JButton view;
    Connection con=null;
    String fpath="",fname;
    Desktop desktop=Desktop.getDesktop();

    DefaultTableModel model=new DefaultTableModel(); 
    JTable wtable= new  JTable(model);
    JScrollPane scrollPane;

    JButton logout=new JButton("Logout");
    JButton back=new JButton("Back");

    //Table selected row
    int row;
    JLabel rlabel=new JLabel();
    public work(String rname,String supname)
    {
        topPanel.setBounds(0,0,500,45);
        topPanel.setBackground(Color.DARK_GRAY);
        add(topPanel);

        rlabel.setText("Reseacher Username : "+rname);
        try
        {
            //Connecting to database
            Class.forName("com.mysql.cj.jdbc.Driver");  
            con = DriverManager.getConnection("jdbc:mysql://localhost:3308/library","root","");

            PreparedStatement stm=con.prepareStatement("select * from works where rname=?");
            stm.setString(1,rname);
            rs=stm.executeQuery();

            model.addColumn("File Name");
            model.addColumn("File Location");
            model.addColumn("Year of Submission");
            model.addColumn("Last Modified");
            while(rs.next())
            {
                model.addRow(new String[] {rs.getString(2),rs.getString(5),rs.getString(4),rs.getString(6)});
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        } 
        scrollPane=new JScrollPane(wtable);
        view=new JButton("View File");
        view.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent ae)
                {
                    if(wtable.getSelectedRow()!=-1)
                    {
                        row=wtable.getSelectedRow();
                        fname=(String)wtable.getValueAt(row,0);
                        try {
                            PreparedStatement stm2=con.prepareStatement("select fpath from works where wname=?");
                            stm2.setString(1,fname);
                            rs1=stm2.executeQuery();
                            rs1.next();
                            fpath=rs1.getString(1);    
                            System.out.println(fpath);                              
                        } 
                        catch (Exception e) 
                        {
                            System.out.println(e);
                        }
                        if(!fpath.equals(""))
                        {
                            try 
                            {
                                File file=new File(fpath);
                                file.setWritable(false);
                                desktop.open(file);    
                            } 
                            catch (Exception e) 
                            {
                                JOptionPane.showMessageDialog(null,"File does not exist!","Error",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                            JOptionPane.showMessageDialog(null,"File Connot be opened!","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                        JOptionPane.showMessageDialog(null,"Select a File!","Alert",JOptionPane.WARNING_MESSAGE); 
                }
            }
        );
        logout.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    new frame1();
                    dispose();
                }
            }
        );
        logout.setBounds(400,10,90,25);
        add(logout);

        //back button
        back.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    new homesup(supname);
                    dispose();
                }
            }
        );
        back.setBounds(10,10,90,25);
        add(back);

        rlabel.setBounds(0,50,200,25);
        add(rlabel);
        
        
        wtable.setAutoResizeMode(0);
        wtable.setPreferredSize(new Dimension(500,300));
        scrollPane.setBounds(100,75,300,300);
        add(scrollPane);

        view.setBounds(300,387,100,25);
        add(view);

        setLayout(null);
        setSize(515,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
