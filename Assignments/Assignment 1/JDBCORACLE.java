import java.sql.*;
// We import java.io to be able to read from the command line

import java.io.*;

public class JDBCORACLE {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args)
	  	throws SQLException, IOException
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver"); //Load the Oracle JDBC driver
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Could not load the driver");
		}
		try //connect to oracle and validate the user and passwd
		{
			String Oracleuser,Oraclepass;
			Oracleuser = readEntry("Oracle username:");
			Oraclepass = readEntry("Oracle Password:");
			String url="jdbc:oracle:thin:@10.251.216.48:1521:xe";
			Connection conn = DriverManager.getConnection(url,Oracleuser,Oraclepass);//connect oracle
			
                        Statement st=conn.createStatement();
                        st.executeUpdate("delete from login");
                        st.executeUpdate("insert into login values('user1','123456')");
                        st.executeUpdate("insert into login values('user2','123456')");

                        ResultSet rs=st.executeQuery("select * from login");
                        String user="c##testl";
                        String password="t123456";
                        while (rs.next())
                        {
                           user=rs.getString(1);
                           password=rs.getString(2);
                           System.out.println("ID: " + user + "   password: " +password);
                        }
                        rs.close();
                        st.close();
                        conn.close();		
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());			
		}
	}
	
	 // Utility function to read a line from standard input
   static String readEntry(String prompt)
   {
      try
      {
         StringBuffer buffer = new StringBuffer();
         System.out.print(prompt);
         System.out.flush();
         int c = System.in.read();
         while (c != '\n' && c != -1)
         {
            buffer.append((char)c);
            c = System.in.read();
         }
         return buffer.toString().trim();
      }
      catch(IOException e)
      {
         return "";
      }
   }

    
    
}