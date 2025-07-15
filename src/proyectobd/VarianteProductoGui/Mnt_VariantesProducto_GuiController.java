package proyectobd.VarianteProductoGui;

import dao.VarianteProductoDAO;
import dao.ProductoDAO;
import dto.VarianteProductoDTO;
import dto.ProductoDTO;
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
import proyectobd.ParametrosGenerales.FeedbackVarianteProducto;

public class Mnt_VariantesProducto_GuiController implements Initializable {

    FeedbackVarianteProducto fu = new FeedbackVarianteProducto();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<ProductoDTO> cmb_producto;
    @FXML private TextField txt_sku;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_precio;
    @FXML private TextField txt_stock;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
            txt_id.setDisable(true);
            txt_sku.setDisable(true);
        });
    }

    public void call_Grabar(){
        VarianteProductoDAO dao = new VarianteProductoDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                VarianteProductoDTO dto = new VarianteProductoDTO();
                dto.setProductoId(cmb_producto.getValue().getId());
                dto.setSku(generarSku());
                dto.setNombre(txt_nombre.getText());
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
            dto.setProductoId(cmb_producto.getValue().getId());
            dto.setSku(txt_sku.getText());
            dto.setNombre(txt_nombre.getText());
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

    private void cargarCombos(){
        try {
            ObservableList<ProductoDTO> productos = FXCollections.observableArrayList();
            ProductoDAO pdao = new ProductoDAO();
            productos.addAll(pdao.ListarProductos());
            cmb_producto.setItems(productos);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        VarianteProductoDTO dto = (VarianteProductoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(ProductoDTO p : cmb_producto.getItems()){
                if(p.getId() == dto.getProductoId()){ cmb_producto.setValue(p); break; }
            }
            txt_sku.setText(dto.getSku());
            txt_nombre.setText(dto.getNombre());
            txt_precio.setText(Double.toString(dto.getPrecio()));
            txt_stock.setText(Integer.toString(dto.getStock()));
        } else {
            txt_sku.setText(generarSku());
            txt_nombre.clear();
        }
    }

    private String generarSku(){
        return "SKU" + System.currentTimeMillis();
    }

    private boolean validarDatos(){
        if(cmb_producto.getValue() == null){
            fu.datosInvalidos("Producto: seleccione un valor v\u00e1lido.");
            cmb_producto.requestFocus();
            return false;
        }
        if(txt_nombre.getText().trim().isEmpty()){
            fu.datosInvalidos("Nombre: ingrese un valor v\u00e1lido.");
            txt_nombre.requestFocus();
            return false;
        }
        try{
            double p = Double.parseDouble(txt_precio.getText());
            if(p < 0){
                fu.datosInvalidos("Precio: no puede ser negativo.");
                txt_precio.requestFocus();
                return false;
            }
        }catch(Exception ex){
            fu.datosInvalidos("Precio: ingrese un n\u00famero v\u00e1lido.");
            txt_precio.requestFocus();
            return false;
        }
        try{
            int s = Integer.parseInt(txt_stock.getText());
            if(s < 0){
                fu.datosInvalidos("Stock: no puede ser negativo.");
                txt_stock.requestFocus();
                return false;
            }
        }catch(Exception ex){
            fu.datosInvalidos("Stock: ingrese un n\u00famero v\u00e1lido.");
            txt_stock.requestFocus();
            return false;
        }
        return true;
    }
}
