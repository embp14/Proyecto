package proyectobd.ParametrosGenerales;

import javafx.scene.control.Alert;

public class BaseFeedback {
    protected void mostrar(Alert.AlertType tipo, String titulo, String mensaje){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.setWidth(640);
        alerta.setHeight(250);
        alerta.showAndWait();
    }

    // Compatibilidad con llamadas existentes
    public void MostrarAlertas(String titulo, String mensaje){
        if(titulo.toLowerCase().startsWith("error")){
            if(mensaje == null || mensaje.isEmpty()){
                mostrar(Alert.AlertType.ERROR, "Error", "Ocurrió un problema. Por favor intente nuevamente.");
            }else{
                mostrar(Alert.AlertType.ERROR, "Error", mensaje);
            }
        }else{
            mostrar(Alert.AlertType.INFORMATION, titulo, mensaje);
        }
    }

    public void info(String mensaje){
        mostrar(Alert.AlertType.INFORMATION, "Información", mensaje);
    }

    public void error(String operacion){
        mostrar(Alert.AlertType.ERROR, "Error", "No se pudo " + operacion + ". Por favor intente nuevamente.");
    }

    public void datosInvalidos(String detalle){
        mostrar(Alert.AlertType.WARNING, "Datos inválidos", detalle);
    }

    public void errorSQL(Exception ex, String operacion){
        String msg = "No se pudo " + operacion + ". ";
        String lower = ex.getMessage().toLowerCase();
        if(ex instanceof java.sql.SQLIntegrityConstraintViolationException){
            if(lower.contains("duplicate")){
                msg += "El registro ya existe.";
            }else if(lower.contains("foreign key")){
                msg += "Datos relacionados no válidos.";
            }else if(lower.contains("check")){
                msg += "Valores fuera de rango.";
            }else{
                msg += "Violación de integridad de datos.";
            }
        }else{
            msg += "Por favor revise los datos.";
        }
        mostrar(Alert.AlertType.ERROR, "Error", msg);
    }
}
