package proyectobd.ParametrosGenerales;

import javafx.scene.control.Alert;

public class FeedbackDireccion {
    public void MostrarAlertas(String titulo, String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.setWidth(640);
        alerta.setHeight(250);
        alerta.showAndWait();
    }
}
