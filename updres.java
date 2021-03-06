import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;


import java.awt.Dimension;
import java.awt.*;

import java.awt.image.*;
import javax.imageio.*;


import javax.swing.table.TableRowSorter;
import javax.swing.text.TableView.TableRow;

import connectpack.connect;

public class updres extends JFrame 
{
    BufferedImage img=null;

    JPanel topPanel=new JPanel();

    String fname,fpath,fname1="",fpath1="",lastModified;
    Connection con;

    DefaultTableModel model=new DefaultTableModel();
    JTable wtable=new JTable(model);
    JScrollPane scrollPane=new JScrollPane(wtable);
    TableRowSorter<DefaultTableModel> filtered=new TableRowSorter<DefaultTableModel>(model);
    
    JLabel mywork=new JLabel("My Works");
    JButton addWork=new JButton("Add new work");

    JButton view=new JButton("View/Edit File");
    Desktop desktop=Desktop.getDesktop();
    int row;

    File file1;
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    JButton logout=new JButton("Logout");
    JButton back;

    JTextField search=new JTextField(10);
    JLabel l=new JLabel("Enter the topic or year to search your research works : ");
    JPanel panel=new JPanel();
    public updres(String rname)
    {
        topPanel.setLayout(null);

        try
        {
            img= ImageIO.read(new File("back.png"));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        Image dimg = img.getScaledInstance(30, 25, Image.SCALE_SMOOTH);
        ImageIcon backicon = new ImageIcon(dimg);
        back=new JButton(backicon);

        try
        {
            //Connecting to database
            Class.forName("com.mysql.cj.jdbc.Driver");  
            connect c=new connect();
            Connection con = c.gConnection();

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
                        PreparedStatement stm=con.prepareStatement("insert into works (wname,rname,year,fpath,LastModified) values(?,?,?,?,?)");
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
                                    JOptionPane.showMessageDialog(updres.this,"File does not exist","Error",JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            else
                                JOptionPane.showMessageDialog(updres.this,"File cannot be opened","Error",JOptionPane.ERROR_MESSAGE);
                        }
                        else
                            JOptionPane.showMessageDialog(updres.this,"Select a file to view or edit","Alert",JOptionPane.INFORMATION_MESSAGE);
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
        //back button
        back.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    new homeres(rname);
                    dispose();
                }
            }
        );
        search.addKeyListener(
            new KeyAdapter()
            {
                public void keyReleased(KeyEvent e)
                {
                    wtable.setRowSorter(filtered);
                    filtered.setRowFilter(RowFilter.regexFilter("(?i)"+search.getText()));
                }
            }
        );
        panel.add(l);
        panel.add(search);
        panel.setBounds(50,82,500,25);
        add(panel);

        topPanel.setBounds(0,0,600,45);
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.add(back);
        topPanel.add(logout);
        back.setBounds(10,10,30,25);
        logout.setBounds(490,10,90,25);

        add(topPanel);

        wtable.setAutoResizeMode(0);
        wtable.setPreferredSize(new Dimension(500,300));
        scrollPane.setBounds(50,112,500,200);
        mywork.setBounds(50, 57, 100, 25);
        addWork.setBounds(50,332,150,25);
        view.setBounds(420,332,130,25);

        add(view);
        add(scrollPane);
        add(addWork);
        add(mywork);

        setTitle("Research Artifact System");
        setLayout(null);
        setSize(600,450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        revalidate();
    }
}
