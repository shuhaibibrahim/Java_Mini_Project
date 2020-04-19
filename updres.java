import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;

import jdk.nashorn.internal.ir.CatchNode;

import java.awt.Dimension;
import java.awt.*;

public class updres extends JFrame 
{
    String fname,fpath,fname1="",fpath1="",lastModified;
    Connection con;

    DefaultTableModel model=new DefaultTableModel();
    JTable wtable=new JTable(model);
    JScrollPane scrollPane=new JScrollPane(wtable);
    
    JLabel mywork=new JLabel("My Works");
    JButton addWork=new JButton("Add new work");

    JButton view=new JButton("View/Edit File");
    Desktop desktop=Desktop.getDesktop();
    int row;

    File file1;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    JButton logout=new JButton("Logout");
    JButton back=new JButton("Back");
    public updres(String rname)
    {
        try
        {
            //Connecting to database
            Class.forName("com.mysql.cj.jdbc.Driver");  
            con = DriverManager.getConnection("jdbc:mysql://localhost:3308/library","root","");

            PreparedStatement stm=con.prepareStatement("select * from works where rname=?");
            model.addColumn("File Name");
            model.addColumn("File Location");
            model.addColumn("Year of submission");
            model.addColumn("Last Modified");
            stm.setString(1,rname);
            ResultSet rs=stm.executeQuery();
            while(rs.next())
            {
                model.addRow(new String[] {rs.getString(2),rs.getString(5),rs.getString(4),rs.getString(6)});
            }
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        } 

        addWork.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent ae)
                {                
                    JFileChooser fc=new JFileChooser();
                    File file=null;    
                    int i=fc.showOpenDialog(null);
                    if(i==JFileChooser.APPROVE_OPTION)
                    {
                        file=fc.getSelectedFile();
                        fname=file.getName();
                        fpath=file.getPath();
                    }
                    try
                    {      
                        Calendar cal=Calendar.getInstance();
                        int year=cal.get(Calendar.YEAR);
                        lastModified=sdf.format(file.lastModified())+"";
                        PreparedStatement stm=con.prepareStatement("insert into works values(?,?,?,?,?)");
                        stm.setString(1,fname);
                        stm.setString(2,rname);
                        stm.setInt(3,year);
                        stm.setString(4,fpath);
                        stm.setString(5,lastModified);
                        int success=stm.executeUpdate();
                        if(success>0)
                        {
                            new updres(rname);
                            JOptionPane.showMessageDialog(null,"Successfully inserted the file","Success",JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }    
                }
            }
        );

        view.addActionListener
        (
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent ae)
                    {
                        if(wtable.getSelectedRow()!=-1)
                        {
                            row=wtable.getSelectedRow();
                            fname1=(String)wtable.getValueAt(row,0);
                            try 
                            {
                                PreparedStatement stm2=con.prepareStatement("select fpath from works where wname=? and rname=?");
                                stm2.setString(1,fname1);
                                stm2.setString(2,rname);
                                ResultSet rs2=stm2.executeQuery();
                                rs2.next();
                                fpath1=rs2.getString(1);
                            } 
                            catch (Exception e) 
                            {
                                System.out.println(e);
                            }
                            if(!fpath1.equals(""))
                            {
                                try 
                                {
                                    file1=new File(fpath1);
                                    file1.setWritable(true);
                                    desktop.open(file1);
                                    System.out.println("over");
                                } 
                                catch (Exception e) 
                                {
                                    JOptionPane.showMessageDialog(null,"File does not exist","Error",JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            else
                                JOptionPane.showMessageDialog(null,"File cannot be opened","Error",JOptionPane.ERROR_MESSAGE);
                        }
                        else
                            JOptionPane.showMessageDialog(null,"Select a file to view","Alert",JOptionPane.INFORMATION_MESSAGE);
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
        logout.setBounds(500,0,90,25);
        add(logout);

        //back button
        back.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    new homesup();
                    dispose();
                }
            }
        );
        back.setBounds(0,0,90,25);
        add(back);

        wtable.setAutoResizeMode(0);
        wtable.setPreferredSize(new Dimension(500,300));
        scrollPane.setBounds(50,50,500,200);
        mywork.setBounds(50, 20, 100, 25);
        addWork.setBounds(400,20,150,25);
        view.setBounds(460,270,90,25);

        add(view);
        add(scrollPane);
        add(addWork);
        add(mywork);

        setLayout(null);
        setVisible(true);
        setSize(600,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
