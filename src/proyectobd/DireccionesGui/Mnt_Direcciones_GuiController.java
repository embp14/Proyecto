package proyectobd.DireccionesGui;

import dao.DireccionDAO;
import dto.DireccionDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackDireccion;

public class Mnt_Direcciones_GuiController implements Initializable {

    FeedbackDireccion fu = new FeedbackDireccion();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private TextField txt_usuario;
    @FXML private TextField txt_alias;
    @FXML private TextField txt_direccion;
    @FXML private TextField txt_ciudad;
    @FXML private TextField txt_provincia;
    @FXML private TextField txt_postal;
    @FXML private TextField txt_telefono;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
        });
    }

    public void call_Guardar(){
        DireccionDAO dao = new DireccionDAO();
        if(!actualizar){
            try{
                DireccionDTO dto = new DireccionDTO();
                dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
                dto.setAlias(txt_alias.getText());
                dto.setDireccion(txt_direccion.getText());
                dto.setCiudad(txt_ciudad.getText());
                dto.setProvincia(txt_provincia.getText());
                dto.setCodigoPostal(txt_postal.getText());
                dto.setTelefonoContacto(txt_telefono.getText());
                int id = dao.InsertarDireccion(dto);
                if(id>0){
                    btn_Guardar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            DireccionDTO dto = (DireccionDTO) stage.getUserData();
            dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
            dto.setAlias(txt_alias.getText());
            dto.setDireccion(txt_direccion.getText());
            dto.setCiudad(txt_ciudad.getText());
            dto.setProvincia(txt_provincia.getText());
            dto.setCodigoPostal(txt_postal.getText());
            dto.setTelefonoContacto(txt_telefono.getText());
            try{
                dao.ActualizarDireccion(dto);
                btn_Guardar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        DireccionDTO dto = (DireccionDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_usuario.setText(Integer.toString(dto.getUsuarioId()));
            txt_alias.setText(dto.getAlias());
            txt_direccion.setText(dto.getDireccion());
            txt_ciudad.setText(dto.getCiudad());
            txt_provincia.setText(dto.getProvincia());
            txt_postal.setText(dto.getCodigoPostal());
            txt_telefono.setText(dto.getTelefonoContacto());
        }
    }
}
