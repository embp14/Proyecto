package proyectobd.UsuariosGui;

import dao.UsuarioDAO;
import dao.RolDAO;
import dto.RolDTO;
import dto.UsuarioDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackUsuario;

public class Mnt_Usuarios_GuiController implements Initializable {
    FeedbackUsuario fu = new FeedbackUsuario();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_BuscarImagen;
    @FXML private ImageView img_perfil;
    @FXML private TextField txt_id;
    @FXML private ComboBox<RolDTO> cmb_rol;
    @FXML private TextField txt_imagen;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_email;
    @FXML private PasswordField txt_contrasena;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            CargarDatos();
            txt_id.setDisable(true);
        });
        txt_nombre.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("[\\p{L} ]*") ? change : null;
        }));
    }

    public void call_Grabar(){
        UsuarioDAO dao = new UsuarioDAO();
        if(!nombreValido(txt_nombre.getText())){
            fu.datosInvalidos("El campo 'Nombre' solo debe contener letras.");
            txt_nombre.requestFocus();
            return;
        }
        if(!emailValido(txt_email.getText())){
            fu.datosInvalidos("El campo 'Email' no es v\u00e1lido.");
            txt_email.requestFocus();
            return;
        }
        if(!actualizar){
            try{
                UsuarioDTO dto = new UsuarioDTO();
                dto.setRolId(cmb_rol.getValue().getId());
                dto.setNombre(txt_nombre.getText());
                dto.setEmail(txt_email.getText());
                dto.setContrasena(txt_contrasena.getText());
                dto.setImagenPerfil(txt_imagen.getText());
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
            dto.setRolId(cmb_rol.getValue().getId());
            dto.setNombre(txt_nombre.getText());
            dto.setEmail(txt_email.getText());
            dto.setContrasena(txt_contrasena.getText());
            dto.setImagenPerfil(txt_imagen.getText());
            try{
                int reg = dao.ActualizarUsuario(dto);
                if(reg>0){
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void CargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        UsuarioDTO dto = (UsuarioDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for (RolDTO r : cmb_rol.getItems()) {
                if(r.getId() == dto.getRolId()) {
                    cmb_rol.setValue(r);
                    break;
                }
            }
            txt_nombre.setText(dto.getNombre());
            txt_email.setText(dto.getEmail());
            txt_contrasena.setText(dto.getContrasena());
            txt_imagen.setText(dto.getImagenPerfil());
            if(dto.getImagenPerfil() != null){
                File f = new File(dto.getImagenPerfil());
                if(f.exists()){
                    img_perfil.setImage(new Image(f.toURI().toString()));
                }
            }
        }
    }

    private void cargarCombos(){
        try {
            ObservableList<RolDTO> roles = FXCollections.observableArrayList();
            RolDAO rdao = new RolDAO();
            roles.addAll(rdao.ListarRoles());
            cmb_rol.setItems(roles);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_SeleccionarImagen(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Im\u00e1genes", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File f = fc.showOpenDialog(Ap_Main.getScene().getWindow());
        if(f != null){
            txt_imagen.setText(f.getAbsolutePath());
            img_perfil.setImage(new Image(f.toURI().toString()));
        }
    }

    private boolean emailValido(String email){
        if(email == null) return false;
        return email.matches("^[\\w.+\\-]+@([\\w-]+\\.)+[\\w-]{2,}$");
    }

    private boolean nombreValido(String nombre){
        if(nombre == null) return false;
        return nombre.matches("[\\p{L} ]+");
    }
}
