import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.*;
import java.util.*;

public class homesup extends JFrame
{
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
        sname=supname;
        try
        {
            //Connecting to database
            Class.forName("com.mysql.cj.jdbc.Driver");  
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3308/library","root","");

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
                        JOptionPane.showMessageDialog(null,"Select a researcher!","Alert",JOptionPane.WARNING_MESSAGE);
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
        logout.setBounds(350,0,90,25);
        add(logout);

        scrollPane.setBounds(0,65, 300, 375);
        add(scrollPane);

        panel.add(l);
        panel.add(search);
        panel.setBounds(0,30,400,25);
        add(panel);

        b.setBounds(300,55,150,25);
        add(b);
        
        
        setLayout(null);
        setSize(480,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}