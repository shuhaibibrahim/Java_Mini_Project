package connectpack;

import java.sql.*;

public class connect 
{
    public Connection gConnection()
    {
        try
        {
            return DriverManager.getConnection("jdbc:mysql://localhost:3308/library","root","");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

}