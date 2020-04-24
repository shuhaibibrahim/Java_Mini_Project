import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;

import connectpack.connect;

public class update 
{
    public update()
    {
        int id;
        File file;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String lm,fpath;
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");  
            connect c=new connect();
            Connection con = c.gConnection();

            Statement stm=con.createStatement();
            ResultSet rs=stm.executeQuery("select * from works");

            PreparedStatement stm2=con.prepareStatement("update works set LastModified=? where id=?");
            while(rs.next())
            {
                id=rs.getInt(1);
                fpath=rs.getString(5);
                file=new File(fpath);
                lm=sdf.format(file.lastModified())+"";
                stm2.setString(1,lm);
                stm2.setInt(2,id);
                stm2.executeUpdate();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}