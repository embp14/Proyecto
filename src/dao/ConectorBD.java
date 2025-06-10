/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author admin
 */

import java.sql.Connection;
import java.sql.DriverManager;
import javafx.scene.control.Alert;


public class ConectorBD {
    public Connection connBD=null;
    String UsuarioBD="root";
    String PassBD="Yto146o2004";
    String NombreBD="ekuamarket";
    String HostBD="localhost";
    String PuertoBD="3306";
    String CadenaConexion="jdbc:mysql://"+HostBD+":"+PuertoBD+"/"+NombreBD;

public Connection AbrirConexionBD(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connBD=DriverManager.getConnection(CadenaConexion,UsuarioBD,PassBD);
            //MostrarAlertas("Información de la conexión","Conexión con la base de datos realizada correctamente\n La cadena de conexión es: "+CadenaConexion);
            /*System.out.println("La cadena de conexión es:\n"+CadenaConexion);
            if(connBD!=null){
                System.out.println("Conexión Exitosa\n"+CadenaConexion);
            }*/
        }catch(Exception ex){
            MostrarAlertas("Información de la conexión","Conexión con la base NO se pudo realizar\n"+CadenaConexion+"\n Error: "+ex.toString());
        }
        //Retornamos el objeto de la conexión con la BD
        return connBD;
    }
    
    //Método para cerrar las conexiones con la BD
    public void CerrarConexionBD(){
        try{
            if (connBD!=null && !connBD.isClosed()){
                connBD.close();
                //MostrarAlertas("Información del sistema","Conexión cerrada correctamente");
            }            
        }catch(Exception ex){
            MostrarAlertas("Información del sistema", "No hay una conexión abierta"+ex.toString());
        }
    }
    
    //Generación de los mensajes de feedback al usuario final.
    private void MostrarAlertas(String titulo, String mensaje){
        Alert alerta =new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
        
    }

    
}
