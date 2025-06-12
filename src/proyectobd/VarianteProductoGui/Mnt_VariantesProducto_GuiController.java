package proyectobd.VarianteProductoGui;

import dao.VarianteProductoDAO;
import dto.VarianteProductoDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class Mnt_VariantesProducto_GuiController implements Initializable {

    FeedbackProducto fu = new FeedbackProducto();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_producto;
    @FXML private TextField txt_sku;
    @FXML private TextField txt_precio;
    @FXML private TextField txt_stock;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
        });
    }

    public void call_Grabar(){
        VarianteProductoDAO dao = new VarianteProductoDAO();
        if(!actualizar){
            try{
                VarianteProductoDTO dto = new VarianteProductoDTO();
                dto.setProductoId(Integer.parseInt(txt_producto.getText()));
                dto.setSku(txt_sku.getText());
                dto.setPrecio(Double.parseDouble(txt_precio.getText()));
                dto.setStock(Integer.parseInt(txt_stock.getText()));
                int id = dao.InsertarVariante(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            VarianteProductoDTO dto = (VarianteProductoDTO) stage.getUserData();
            dto.setProductoId(Integer.parseInt(txt_producto.getText()));
            dto.setSku(txt_sku.getText());
            dto.setPrecio(Double.parseDouble(txt_precio.getText()));
            dto.setStock(Integer.parseInt(txt_stock.getText()));
            try{
                dao.ActualizarVariante(dto);
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
        VarianteProductoDTO dto = (VarianteProductoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_producto.setText(Integer.toString(dto.getProductoId()));
            txt_sku.setText(dto.getSku());
            txt_precio.setText(Double.toString(dto.getPrecio()));
            txt_stock.setText(Integer.toString(dto.getStock()));
        }
    }
}
