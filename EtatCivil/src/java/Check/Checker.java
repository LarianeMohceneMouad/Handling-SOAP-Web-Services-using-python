/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Check;

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
@WebService(serviceName = "civil_state")
public class Checker {
String etat="";
  
    @WebMethod(operationName = "checker")
    public String checker(@WebParam(name = "NIN") int NIN,@WebParam(name = "name") String name,@WebParam(name = "lastname") String lastname) {
       try {
                 try {
                 Class.forName("com.mysql.jdbc.Driver");
                 } catch (ClassNotFoundException e) {}
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/etatcivil","root","");
            String Req = "select * from information where NIN='"+NIN+"';";
            Statement stmt=con.createStatement();
            ResultSet resultSet=stmt.executeQuery(Req);
            if(resultSet.next()){
               String name_val = resultSet.getString("Name");
               String lastname_val = resultSet.getString("Lastname");
               
                    if((name.equals(name_val)) && (lastname.equals(lastname_val))){               
                        etat="Correct";    
                    }else{
                        etat="Wrong";}
        
            }else{
                etat="Unregistered";
            }
      }catch (SQLException ex) {
          
             Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null,ex);
      }  
        
        
        return etat;
    }
}
