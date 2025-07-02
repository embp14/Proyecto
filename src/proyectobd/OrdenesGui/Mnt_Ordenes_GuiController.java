package proyectobd.OrdenesGui;

import dao.OrdenDAO;
import dao.UsuarioDAO;
import dto.OrdenDTO;
import dto.UsuarioDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackOrden;

public class Mnt_Ordenes_GuiController implements Initializable {

    FeedbackOrden fu = new FeedbackOrden();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private TextField txt_estado;
    @FXML private TextField txt_totalbruto;
    @FXML private TextField txt_totaldescuento;
    @FXML private DatePicker dp_fecha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        OrdenDAO dao = new OrdenDAO();
        if(!actualizar){
            try{
                OrdenDTO dto = new OrdenDTO();
                dto.setUsuarioId(cmb_usuario.getValue().getId());
                dto.setEstado(txt_estado.getText());
                dto.setTotalBruto(new java.math.BigDecimal(txt_totalbruto.getText()));
                dto.setTotalDescuento(new java.math.BigDecimal(txt_totaldescuento.getText()));
                dto.setFechaCreacion(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
                int id = dao.InsertarOrden(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            OrdenDTO dto = (OrdenDTO) stage.getUserData();
            dto.setUsuarioId(cmb_usuario.getValue().getId());
            dto.setEstado(txt_estado.getText());
            dto.setTotalBruto(new java.math.BigDecimal(txt_totalbruto.getText()));
            dto.setTotalDescuento(new java.math.BigDecimal(txt_totaldescuento.getText()));
            dto.setFechaCreacion(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
            try{
                dao.ActualizarOrden(dto);
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

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        OrdenDTO dto = (OrdenDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(UsuarioDTO u : cmb_usuario.getItems()){
                if(u.getId() == dto.getUsuarioId()){ cmb_usuario.setValue(u); break; }
            }
            txt_estado.setText(dto.getEstado());
            txt_totalbruto.setText(dto.getTotalBruto().toString());
            txt_totaldescuento.setText(dto.getTotalDescuento().toString());
            dp_fecha.setValue(dto.getFechaCreacion().toLocalDateTime().toLocalDate());
        }else{
            dp_fecha.setValue(java.time.LocalDate.now());
        }
    }
}
