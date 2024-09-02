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
			System.out.println("Could not load the driver \nError: "+e);
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
   public static void menu(Connection conn)
   {
      int decacct=-1;
      int incacct=-1;
      float amount=-1;
      boolean keepgoing=true;
      boolean ReadyToProcess=false;
      while(keepgoing==true)
      {
         System.out.println("***************************************************");
         System.out.println("***                                             ***");
         System.out.println("***   Hello User                                ***");
         System.out.println("***     Welcome to JeanLuc's Simple             ***");
         System.out.println("***         Banking JDBC Program                ***");
         System.out.println("***                                             ***");
         System.out.println("***                                             ***");
         System.out.println("***  1. Enter Sending Account Number            ***");
         System.out.println("***  2. Enter Receiving Account Number          ***");
         System.out.println("***  3. Enter Amount to send                    ***");
         System.out.println("***                                             ***");
         if(incacct!=-1&&decacct!=-1&&amount!=-1)
         {
            System.out.println("***  4. Attempt Transaction                     ***");
            ReadyToProcess=true;
         }
         System.out.println("***                                             ***");
         System.out.println("***************************************************");
         int choice = 0;
         try
         {
            choice = Integer.parseInt(readEntry(" "));
         }
         catch (ArithmeticException e)
         {
            choice=0;
         }
         switch (choice)
         {
            case 1:
               System.out.println("");
               boolean retry=true;
               while(retry==true)
               {
                  try
                  {
                     decacct = Integer.parseInt(readEntry("Please enter sending account number: "));

                  }
                  catch (ArithmeticException e)
                  {
                     System.out.println("Invalid Input, ");
                     decacct=-1;
                  }
                  if(amount!=-1)
                  {
                     retry=false;
                  }
               }
               
            case 2:
               System.out.println("");
               retry=true;
               while(retry==true)
               {
                  try
                  {
                     incacct = Integer.parseInt(readEntry("Please enter receiving account number: "));

                  }
                  catch (ArithmeticException e)
                  {
                     System.out.println("Invalid input, ");
                     incacct=-1;
                  }
                  if(amount!=-1)
                  {
                     retry=false;
                  }
               }
               
            case 3:
               System.out.println("");
               retry=true;
               while(retry==true)
               {
                  
                  try
                  {
                     amount = Integer.parseInt(readEntry("Please enter amount to be transferred: "));
                  }
                  catch (ArithmeticException e)
                  {
                     System.out.println("Invalid input, ");
                     amount=-1;
                  }
                  if(amount!=-1)
                  {
                     retry=false;
                  }
               }
            case 4:
               if(ReadyToProcess)
               {
                  boolean successfultransfer = false;
                  try
                  {
                     successfultransfer = transfer(decacct,incacct,amount,conn);
                  }
                  catch(SQLException e)
                  {
                     System.out.println(e);
                     successfultransfer = false;
                  }
                  if(successfultransfer==true)
                  {
                     System.out.println("Transfer Successful");
                  }
                  else
                  {
                     System.out.println("Transfer Failed");
                  }
               }
               else
               {
                  System.out.println("Invalid Option, Please enter a valid option");
               }
            default:
               System.out.println("Invalid Option, Please enter a valid option");

         }
      }
   }
   public static boolean transfer(int decacct, int incacct, float quantity, Connection conn) throws SQLException
   {
      conn.setAutoCommit(false); // Set autocommit to false in order to ensure multiple updates can be performed in one transaction
      String decreaseSql = "UPDATE bankaccount SET balance = balance - ? WHERE accnum = ?";
      String increaseSql = "UPDATE bankaccount SET balance = balance + ? WHERE accnum = ?";
      try{
         PreparedStatement decstmt=null;
         PreparedStatement incstmt=null;
         decstmt=conn.prepareStatement(decreaseSql);
         incstmt=conn.prepareStatement(increaseSql);
         decstmt.setFloat(1, quantity);
         decstmt.setInt(2, decacct);
         incstmt.setFloat(1, quantity);
         incstmt.setInt(2, incacct);
         int numrowsinc=incstmt.executeUpdate();
         int numrowsdec=decstmt.executeUpdate();
         if(numrowsdec>0&&numrowsdec<1&&numrowsinc>0&&numrowsinc<1)
         {
            conn.commit();
            return true;
         }
         else
         {
            System.out.println("Failed to write the correct number of lines\nRolling back...");
            conn.rollback();
            return false;
         }
      }
      catch(SQLException e)
      {   
         conn.rollback();
         e.printStackTrace();
         return false;
      }
   }
}
