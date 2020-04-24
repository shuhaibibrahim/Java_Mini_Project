import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.*;
import java.util.*;

import java.awt.Dimension;

import connectpack.connect;

public class homesup extends JFrame
{
    JPanel topPanel=new JPanel();

    String sname;

    JButton logout=new JButton("Logout");
    
    DefaultTableModel model=new DefaultTableModel();
    JTable rtable=new JTable(model);

    ResultSet rs=null;

    JButton b=new JButton("Show Reseacher");
    JScrollPane scrollPane=new JScrollPane(rtable);
    TableRowSorter<DefaultTableModel> filtered=new TableRowSorter<DefaultTableModel>(model);

    JPanel panel=new JPanel();
    JLabel l=new JLabel("Search for suboordinate : ");
    JTextField search=new JTextField(10);
    homesup(String supname)
    {

        topPanel.setBounds(0,0,500,45);
        topPanel.setBackground(Color.DARK_GRAY);
        add(topPanel);

        sname=supname;
        try
        {
            //Connecting to database
            Class.forName("com.mysql.cj.jdbc.Driver");  
            connect c=new connect();
            Connection con = c.gConnection();

            PreparedStatement stm=con.prepareStatement("select name from researcher where sname=?");
            stm.setString(1,sname);
            rs=stm.executeQuery();
            model.addColumn("My Suboordinates");
            while(rs.next())
                model.addRow(new String[] {rs.getString(1)});
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        } 

        //filtered search
        search.addKeyListener(
            new KeyAdapter()
            {
                public void keyReleased(KeyEvent e)
                {
                    rtable.setRowSorter(filtered);
                    filtered.setRowFilter(RowFilter.regexFilter("(?i)"+search.getText()));
                }
            }
        );

        //To view each researchers work
        b.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                    if(rtable.getSelectedRow()!=-1)
                    {
                        int row=rtable.getSelectedRow();
                        String rname=(String)rtable.getValueAt(row,0);
                        new work(rname,supname);
                        dispose();                
                    }
                    else
                        JOptionPane.showMessageDialog(homesup.this,"Select a researcher!","Alert",JOptionPane.WARNING_MESSAGE);
                }
            }
        );
        logout.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent ae)
                {
                    new frame1();
                    dispose();
                }
            }
        );
        logout.setBounds(390,10,90,25);
        add(logout);

        rtable.setAutoResizeMode(0);
        rtable.setPreferredSize(new Dimension(300,300));
        scrollPane.setBounds(100,85, 300, 300);
        add(scrollPane);

        panel.setAlignmentX(0);
        panel.add(l);
        panel.add(search);
        panel.setBounds(30,50,400,25);
        add(panel);

        b.setBounds(250,397,150,25);
        add(b);
        
        
        setTitle("Research Artifact System");
        setLayout(null);
        setSize(500,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}