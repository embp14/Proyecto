package proyectobd.UsuariosGui;

import dao.UsuarioDAO;
import dto.UsuarioDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackUsuario;

public class Mnt_Usuarios_GuiController implements Initializable {

    FeedbackUsuario fu = new FeedbackUsuario();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_rol;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_email;
    @FXML private TextField txt_contrasena;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        UsuarioDAO dao = new UsuarioDAO();
        if(!actualizar){
            try{
                UsuarioDTO dto = new UsuarioDTO();
                dto.setRolId(Integer.parseInt(txt_rol.getText()));
                dto.setNombre(txt_nombre.getText());
                dto.setEmail(txt_email.getText());
                dto.setContrasena(txt_contrasena.getText());
                int id = dao.InsertarUsuario(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            UsuarioDTO dto = (UsuarioDTO) stage.getUserData();
            dto.setRolId(Integer.parseInt(txt_rol.getText()));
            dto.setNombre(txt_nombre.getText());
            dto.setEmail(txt_email.getText());
            dto.setContrasena(txt_contrasena.getText());
            try{
                dao.ActualizarUsuario(dto);
                btn_Grabar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        UsuarioDTO dto = (UsuarioDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_rol.setText(Integer.toString(dto.getRolId()));
            txt_nombre.setText(dto.getNombre());
            txt_email.setText(dto.getEmail());
            txt_contrasena.setText(dto.getContrasena());
        }
    }
}
