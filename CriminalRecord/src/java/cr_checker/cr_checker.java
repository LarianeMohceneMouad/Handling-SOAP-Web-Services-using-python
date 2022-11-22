/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CR_Checker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Admin
 */
@WebService(serviceName = "criminal_record")
public class cr_checker {
boolean etat=false;
String state = "" ;
  
    @WebMethod(operationName = "cr_checker")
    public String checker(@WebParam(name = "NIN") int NIN) {
       try {
                 try {
                 Class.forName("com.mysql.jdbc.Driver");
                 } catch (ClassNotFoundException e) {}
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/criminal_record","root","");
            String Req = "select * from information where NIN='"+NIN+"';";
            Statement stmt=con.createStatement();
            ResultSet resultSet=stmt.executeQuery(Req);
             if(resultSet.next()){
               state = resultSet.getString("State");                                    
             }else{
               state = "Unknown";
             }
      }catch (SQLException ex) {
          
             Logger.getLogger(cr_checker.class.getName()).log(Level.SEVERE, null,ex);
      }  
        
        
        return state;
    }
}
