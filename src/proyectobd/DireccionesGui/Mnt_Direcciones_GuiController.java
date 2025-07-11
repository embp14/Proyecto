package proyectobd.DireccionesGui;

import dao.DireccionDAO;
import dao.UsuarioDAO;
import dto.DireccionDTO;
import dto.UsuarioDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackDireccion;

public class Mnt_Direcciones_GuiController implements Initializable {

    FeedbackDireccion fu = new FeedbackDireccion();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private TextField txt_alias;
    @FXML private TextField txt_direccion;
    @FXML private TextField txt_ciudad;
    @FXML private TextField txt_provincia;
    @FXML private TextField txt_postal;
    @FXML private TextField txt_telefono;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Guardar(){
        DireccionDAO dao = new DireccionDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                DireccionDTO dto = new DireccionDTO();
                dto.setUsuarioId(cmb_usuario.getValue().getId());
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
            dto.setUsuarioId(cmb_usuario.getValue().getId());
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

    private boolean validarDatos(){
        if(cmb_usuario.getValue() == null){
            fu.datosInvalidos("Usuario: seleccione un valor v\u00e1lido.");
            cmb_usuario.requestFocus();
            return false;
        }
        if(txt_alias.getText().trim().isEmpty()){
            fu.datosInvalidos("Alias: ingrese un texto.");
            txt_alias.requestFocus();
            return false;
        }
        if(txt_direccion.getText().trim().isEmpty()){
            fu.datosInvalidos("Direcci\u00f3n: ingrese un texto.");
            txt_direccion.requestFocus();
            return false;
        }
        if(txt_ciudad.getText().trim().isEmpty()){
            fu.datosInvalidos("Ciudad: ingrese un texto.");
            txt_ciudad.requestFocus();
            return false;
        }
        if(txt_provincia.getText().trim().isEmpty()){
            fu.datosInvalidos("Provincia: ingrese un texto.");
            txt_provincia.requestFocus();
            return false;
        }
        if(txt_postal.getText().trim().isEmpty()){
            fu.datosInvalidos("C\u00f3digo Postal: ingrese un texto.");
            txt_postal.requestFocus();
            return false;
        }
        return true;
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        DireccionDTO dto = (DireccionDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            for(UsuarioDTO u : cmb_usuario.getItems()){
                if(u.getId() == dto.getUsuarioId()){ cmb_usuario.setValue(u); break; }
            }
            txt_alias.setText(dto.getAlias());
            txt_direccion.setText(dto.getDireccion());
            txt_ciudad.setText(dto.getCiudad());
            txt_provincia.setText(dto.getProvincia());
            txt_postal.setText(dto.getCodigoPostal());
            txt_telefono.setText(dto.getTelefonoContacto());
        }
    }

    private void cargarCombos(){
        try {
            ObservableList<UsuarioDTO> usuarios = FXCollections.observableArrayList();
            UsuarioDAO udao = new UsuarioDAO();
            usuarios.addAll(udao.ListarUsuarios());
            cmb_usuario.setItems(usuarios);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
