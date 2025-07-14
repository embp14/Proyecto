package proyectobd.RolesGui;

import dao.RolDAO;
import dto.RolDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackRol;
import proyectobd.ParametrosGenerales.TextFilter;

public class Mnt_Roles_GuiController implements Initializable {

    FeedbackRol fu = new FeedbackRol();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Borrar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_nombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        RolDAO dao = new RolDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                RolDTO dto = new RolDTO();
                dto.setNombre(txt_nombre.getText());
                int id = dao.InsertarRol(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            RolDTO dto = (RolDTO) stage.getUserData();
            dto.setNombre(txt_nombre.getText());
            try{
                dao.ActualizarRol(dto);
                btn_Grabar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    private boolean validarDatos(){
        if(txt_nombre.getText().trim().isEmpty()){
            fu.datosInvalidos("Nombre: ingrese un texto.");
            txt_nombre.requestFocus();
            return false;
        }
        if(TextFilter.contieneLenguajeInapropiado(txt_nombre.getText())){
            fu.datosInvalidos("Nombre: contiene lenguaje inapropiado.");
            txt_nombre.requestFocus();
            return false;
        }
        return true;
    }

    public void call_Borrar(){
        try{
            if(txt_id.getText().isEmpty()){
                fu.MostrarAlertas("Información", "No hay registro para borrar");
                return;
            }
            RolDAO dao = new RolDAO();
            dao.EliminarRol(Integer.parseInt(txt_id.getText()));
            btn_Grabar.setDisable(true);
            btn_Borrar.setDisable(true);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        RolDTO dto = (RolDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_nombre.setText(dto.getNombre());
        }
    }
}
