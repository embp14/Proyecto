/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectobd.ParametrosGenerales;

import javafx.scene.control.Alert;

/**
 *
 * @author admin
 */
public class FeedbackUsuario {
        //Generación de los mensajes de feedback al usuario final.
    public void MostrarAlertas(String titulo, String mensaje){
        Alert alerta =new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.setWidth(640);
        alerta.setHeight(250);
        alerta.showAndWait();
        
    }
    
}
