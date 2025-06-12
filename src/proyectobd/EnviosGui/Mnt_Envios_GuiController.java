package proyectobd.CarritoItemsGui;

import dao.CarritoItemDAO;
import dto.CarritoItemDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackEnvio;

public class Mnt_CarritoItems_GuiController implements Initializable {

    FeedbackEnvio fu = new FeedbackEnvio();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_carrito;
    @FXML private TextField txt_variante;
    @FXML private TextField txt_cantidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> { cargarDatos(); });
    }

    public void call_Grabar(){
        CarritoItemDAO dao = new CarritoItemDAO();
        if(!actualizar){
            try{
                CarritoItemDTO dto = new CarritoItemDTO();
                dto.setCarritoId(Integer.parseInt(txt_carrito.getText()));
                dto.setVarianteId(Integer.parseInt(txt_variante.getText()));
                dto.setCantidad(Integer.parseInt(txt_cantidad.getText()));
                int id = dao.InsertarItem(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            CarritoItemDTO dto = (CarritoItemDTO) stage.getUserData();
            dto.setCarritoId(Integer.parseInt(txt_carrito.getText()));
            dto.setVarianteId(Integer.parseInt(txt_variante.getText()));
            dto.setCantidad(Integer.parseInt(txt_cantidad.getText()));
            try{
                dao.ActualizarItem(dto);
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
        CarritoItemDTO dto = (CarritoItemDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_carrito.setText(Integer.toString(dto.getCarritoId()));
            txt_variante.setText(Integer.toString(dto.getVarianteId()));
            txt_cantidad.setText(Integer.toString(dto.getCantidad()));
        }
    }
}
