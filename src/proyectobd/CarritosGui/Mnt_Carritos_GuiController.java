package proyectobd.CarritosGui;

import dao.CarritoDAO;
import dao.UsuarioDAO;
import dto.CarritoDTO;
import dto.UsuarioDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackCarrito;

public class Mnt_Carritos_GuiController implements Initializable {

    FeedbackCarrito fu = new FeedbackCarrito();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private Button btn_Cerrar;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private DatePicker dp_creado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Guardar(){
        CarritoDAO dao = new CarritoDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                CarritoDTO dto = new CarritoDTO();
                dto.setUsuarioId(cmb_usuario.getValue().getId());
                dto.setCreadoEn(Timestamp.valueOf(dp_creado.getValue().atStartOfDay()));
                int id = dao.InsertarCarrito(dto);
                if(id>0){
                    btn_Guardar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            CarritoDTO dto = (CarritoDTO) stage.getUserData();
            dto.setUsuarioId(cmb_usuario.getValue().getId());
            dto.setCreadoEn(Timestamp.valueOf(dp_creado.getValue().atStartOfDay()));
            try{
                dao.ActualizarCarrito(dto);
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
        if(dp_creado.getValue() == null){
            fu.datosInvalidos("Fecha Creado: seleccione una fecha.");
            dp_creado.requestFocus();
            return false;
        }
        return true;
    }


    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        CarritoDTO dto = (CarritoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            for(UsuarioDTO u : cmb_usuario.getItems()){
                if(u.getId() == dto.getUsuarioId()){ cmb_usuario.setValue(u); break; }
            }
            dp_creado.setValue(dto.getCreadoEn().toLocalDateTime().toLocalDate());
        }else{
            dp_creado.setValue(java.time.LocalDate.now());
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

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }
}
