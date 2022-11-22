/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package safetystatechecker;

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
@WebService(serviceName = "safety_state")
public class ss_checker {
boolean etat=false;
String state = "" ;
  
    @WebMethod(operationName = "ss_checker")
    public String checker(@WebParam(name = "NIN") int NIN) {
       try {
                 try {
                 Class.forName("com.mysql.jdbc.Driver");
                 } catch (ClassNotFoundException e) {}
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/safety_state","root","");
            String Req = "select * from information where NIN='"+NIN+"';";
            Statement stmt=con.createStatement();
            ResultSet resultSet=stmt.executeQuery(Req);
             if(resultSet.next()){
               state = resultSet.getString("State");                                    
             }else{
               state = "Unknown";
             }
      }catch (SQLException ex) {
          
             Logger.getLogger(ss_checker.class.getName()).log(Level.SEVERE, null,ex);
      }  
        
        
        return state;
    }
}
