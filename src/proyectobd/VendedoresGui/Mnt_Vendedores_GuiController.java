package proyectobd.VendedoresGui;

import dao.VendedorDAO;
import dto.VendedorDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackVendedor;

public class Mnt_Vendedores_GuiController implements Initializable {

    FeedbackVendedor fu = new FeedbackVendedor();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_usuario;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_descripcion;
    @FXML private TextField txt_calificacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        VendedorDAO dao = new VendedorDAO();
        if(!actualizar){
            try{
                VendedorDTO dto = new VendedorDTO();
                dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
                dto.setNombreTienda(txt_nombre.getText());
                dto.setDescripcion(txt_descripcion.getText());
                dto.setCalificacionPromedio(new java.math.BigDecimal(txt_calificacion.getText()));
                int id = dao.InsertarVendedor(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            VendedorDTO dto = (VendedorDTO) stage.getUserData();
            dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
            dto.setNombreTienda(txt_nombre.getText());
            dto.setDescripcion(txt_descripcion.getText());
            dto.setCalificacionPromedio(new java.math.BigDecimal(txt_calificacion.getText()));
            try{
                dao.ActualizarVendedor(dto);
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
        VendedorDTO dto = (VendedorDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_usuario.setText(Integer.toString(dto.getUsuarioId()));
            txt_nombre.setText(dto.getNombreTienda());
            txt_descripcion.setText(dto.getDescripcion());
            txt_calificacion.setText(dto.getCalificacionPromedio().toString());
        }
    }
}
