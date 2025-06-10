/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectobd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class ProyectoBD extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
        
    }

    @Override
    public void start(Stage stageMenuPrincipal) throws Exception {
        //Cargamos la interface principal de nuestra aplicación:
        Parent root=FXMLLoader.load(getClass().getResource("ParametrosGenerales/MenuPrincipal.fxml"));
        // Establecemos el título de la ventana
        stageMenuPrincipal.setTitle("Proyecto Programación con Bases de Datos");
        //Establecemos la escena que se mostrará en el escenario
        stageMenuPrincipal.setScene(new Scene(root));
        //Mostramos el escenario
        stageMenuPrincipal.show();
       
    }
}
