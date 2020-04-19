import java.awt.BorderLayout;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.TableView.TableRow;
import java.awt.event.*;
import java.io.File;
import java.awt.*;
import java.awt.Dimension;

public class homeres extends JFrame 
{
    JPanel topPanel=new JPanel();

    Connection con=null;
    JLabel mywork=new JLabel("My Works");

    JTextField search=new JTextField(10);
    JLabel l=new JLabel("Enter the topic or year to search other research works : ");
    JPanel panel=new JPanel();

    DefaultTableModel model=new DefaultTableModel(); 
    DefaultTableModel model1=new DefaultTableModel();
    
    JTable wtable=new JTable(model);
    JScrollPane scrollPane=new JScrollPane(wtable);

    JTable rtable=new JTable(model1);
    JScrollPane scrollPane1=new JScrollPane(rtable);

    TableRowSorter<DefaultTableModel> filtered=new TableRowSorter<DefaultTableModel>(model1);

    JButton view=new JButton("View File");
    int row;
    String fname,fpath="",advisorApproved="";
    Desktop desktop=Desktop.getDesktop();

    JButton update=new JButton("Update");

    JButton logout=new JButton("Logout");
    public homeres(String rname)
    {

        //Adding current researchers work information to wtable wrapped in scrollpane
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
        try
        {
            PreparedStatement stm3=con.prepareStatement("select AdvisorApproved from researcher where uname=?");
            stm3.setString(1,rname);
            ResultSet rs3=stm3.executeQuery();
            rs3.next();
            advisorApproved=rs3.getString(1);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }         
        update.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent ae2)
                {
                    new updres(rname);
                    dispose();
                }
            }
        );

        wtable.setAutoResizeMode(0);
        wtable.setPreferredSize(new Dimension(500,300));
        scrollPane.setBounds(50,87,500,200);
        mywork.setBounds(50, 57, 100, 25);
        update.setBounds(470,57,80,25);

        add(scrollPane);
        add(mywork);
        add(update);
        if(advisorApproved.equals("YES"))
        {
            //adding columns to rtable
            try 
            {
                PreparedStatement stm1=con.prepareStatement("select * from works where rname!=? order by year");
                stm1.setString(1,rname);
                ResultSet res1=stm1.executeQuery();
                model1.addColumn("File Name");
                model1.addColumn("Researcher");
                model1.addColumn("Year of submission");
                while(res1.next())
                {
                    model1.addRow(new String[] {res1.getString(2),res1.getString(3),res1.getString(4)});
                }
            } 
            catch (Exception e) 
            {
                System.out.println(e);
            }
            
            //filter searching for the text entered in seach textfield
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

            view.addActionListener
            (
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent ae)
                    {
                        if(rtable.getSelectedRow()!=-1)
                        {
                            row=rtable.getSelectedRow();
                            fname=(String)rtable.getValueAt(row,0);
                            try 
                            {
                                PreparedStatement stm2=con.prepareStatement("select fpath from works where wname=?");
                                stm2.setString(1,fname);
                                ResultSet rs2=stm2.executeQuery();
                                rs2.next();
                                fpath=rs2.getString(1);
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

            rtable.setAutoResizeMode(0);
            rtable.setPreferredSize(new Dimension(500,300));
            scrollPane1.setBounds(600,87,500,200);
            add(scrollPane1);
    
            view.setBounds(1010, 299, 90, 25);
            panel.add(l);
            panel.add(search);

            panel.setBounds(600,57,500,25);

            add(view);
            add(panel);
            
            setSize(1250,400);//Frame Size
            
            topPanel.setBounds(0,0,1250,45);
            logout.setBounds(1140,10,90,25);
        }
        else
        {
            setSize(600,400);   
            topPanel.setBounds(0,0,600,45);
            logout.setBounds(490,10,90,25);
        }
        //Logout listener
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
        
        topPanel.setBackground(Color.DARK_GRAY);
        add(topPanel);
        add(logout);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
