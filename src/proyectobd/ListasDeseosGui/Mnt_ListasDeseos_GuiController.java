package proyectobd.ListasDeseosGui;

import dao.ListaDeseoDAO;
import dao.UsuarioDAO;
import dto.ListaDeseoDTO;
import dto.UsuarioDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackListaDeseo;
import proyectobd.ParametrosGenerales.TextFilter;

public class Mnt_ListasDeseos_GuiController implements Initializable {

    FeedbackListaDeseo fu = new FeedbackListaDeseo();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private TextField txt_nombre;
    @FXML private DatePicker dp_creado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Guardar(){
        ListaDeseoDAO dao = new ListaDeseoDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                ListaDeseoDTO dto = new ListaDeseoDTO();
                dto.setUsuarioId(cmb_usuario.getValue().getId());
                dto.setNombre(txt_nombre.getText());
                dto.setCreadoEn(Timestamp.valueOf(dp_creado.getValue().atStartOfDay()));
                int id = dao.InsertarLista(dto);
                if(id>0){
                    btn_Guardar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            ListaDeseoDTO dto = (ListaDeseoDTO) stage.getUserData();
            dto.setUsuarioId(cmb_usuario.getValue().getId());
            dto.setNombre(txt_nombre.getText());
            dto.setCreadoEn(Timestamp.valueOf(dp_creado.getValue().atStartOfDay()));
            try{
                dao.ActualizarLista(dto);
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
        if(dp_creado.getValue() == null){
            fu.datosInvalidos("Fecha: seleccione una fecha.");
            dp_creado.requestFocus();
            return false;
        }
        return true;
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        ListaDeseoDTO dto = (ListaDeseoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            for(UsuarioDTO u : cmb_usuario.getItems()){
                if(u.getId() == dto.getUsuarioId()){ cmb_usuario.setValue(u); break; }
            }
            txt_nombre.setText(dto.getNombre());
            dp_creado.setValue(dto.getCreadoEn().toLocalDateTime().toLocalDate());
        }else{
            txt_nombre.clear();
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
}
