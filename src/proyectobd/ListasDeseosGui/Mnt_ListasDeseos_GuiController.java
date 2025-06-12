package proyectobd.ListasDeseosGui;

import dao.ListaDeseoDAO;
import dto.ListaDeseoDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackVendedor;

public class Mnt_ListasDeseos_GuiController implements Initializable {

    FeedbackVendedor fu = new FeedbackVendedor();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private TextField txt_usuario;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_creado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
        });
    }

    public void call_Guardar(){
        ListaDeseoDAO dao = new ListaDeseoDAO();
        if(!actualizar){
            try{
                ListaDeseoDTO dto = new ListaDeseoDTO();
                dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
                dto.setNombre(txt_nombre.getText());
                dto.setCreadoEn(Timestamp.valueOf(txt_creado.getText()));
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
            dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
            dto.setNombre(txt_nombre.getText());
            dto.setCreadoEn(Timestamp.valueOf(txt_creado.getText()));
            try{
                dao.ActualizarLista(dto);
                btn_Guardar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        ListaDeseoDTO dto = (ListaDeseoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_usuario.setText(Integer.toString(dto.getUsuarioId()));
            txt_nombre.setText(dto.getNombre());
            txt_creado.setText(dto.getCreadoEn().toString());
        }else{
            txt_nombre.clear();
            txt_creado.setText(new Timestamp(System.currentTimeMillis()).toString());
        }
    }
}
