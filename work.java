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
import java.awt.image.*;
import javax.imageio.*;

public class work extends JFrame
{
    
    BufferedImage img = null;

    String approvedFlag="";

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
            
        try 
        {
            img = ImageIO.read(new File("tick1.png"));
        } 
        catch (IOException e) 
        {
            System.out.println(e);
        }
        Image dimg = img.getScaledInstance(15, 15,Image.SCALE_SMOOTH);
        ImageIcon imgicon = new ImageIcon(dimg);

        JPanel topPanel=new JPanel();

        JButton approve=new JButton("Approve Researcher");
        JLabel approveLabel=new JLabel("Approved",imgicon,JLabel.CENTER);

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

        
        try
        {
            PreparedStatement stm=con.prepareStatement("select AdvisorApproved from researcher where uname=?");
            stm.setString(1,rname);
            rs=stm.executeQuery();
            rs.next();
            approvedFlag=rs.getString(1);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }        
        //Setting position of approve button and approve label initially
        approve.setBounds(390,50,210,25);
        approveLabel.setBounds(500,50,100,25);
        approveLabel.setForeground(Color.blue);

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

        approve.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e1)
                {
                    try
                    {
                        PreparedStatement stm3=con.prepareStatement("update researcher set AdvisorApproved=? where uname=?");
                        stm3.setString(1,"YES");
                        stm3.setString(2,rname);
                        int success=stm3.executeUpdate();
                        approve.setVisible(false);
                        add(approveLabel);
                        if(success>0)
                        {
                            JOptionPane.showMessageDialog(null, "Succefully approved", "Success",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }
                }
            }
        );

        //Adding approve button or approve label based on Advisor approved or not
        if(approvedFlag.equals("YES"))
            add(approveLabel);
        else
            add(approve);
        
        topPanel.setBounds(0,0,700,45);
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setLayout(null);
        
        topPanel.add(back);
        topPanel.add(logout);
        logout.setBounds(600,10,90,25);
        back.setBounds(10,10,90,25);
        add(topPanel);

        rlabel.setBounds(0,50,200,25);
        add(rlabel);
        
        
        wtable.setAutoResizeMode(0);
        wtable.setPreferredSize(new Dimension(500,300));
        scrollPane.setBounds(100,85,500,300);
        add(scrollPane);

        view.setBounds(500,397,100,25);
        add(view);

        setLayout(null);
        setSize(715,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
