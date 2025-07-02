package proyectobd.ImagenesProductoGui;

import dao.ImagenProductoDAO;
import dao.ProductoDAO;
import dto.ImagenProductoDTO;
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
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackImagenProducto;

public class Mnt_ImagenesProducto_GuiController implements Initializable {

    FeedbackImagenProducto fu = new FeedbackImagenProducto();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<ProductoDTO> cmb_producto;
    @FXML private TextField txt_url;
    @FXML private TextField txt_principal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Grabar(){
        ImagenProductoDAO dao = new ImagenProductoDAO();
        if(!actualizar){
            try{
                ImagenProductoDTO dto = new ImagenProductoDTO();
                dto.setProductoId(cmb_producto.getValue().getId());
                dto.setUrl(txt_url.getText());
                dto.setEsPrincipal(Boolean.parseBoolean(txt_principal.getText()));
                int id = dao.InsertarImagen(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            ImagenProductoDTO dto = (ImagenProductoDTO) stage.getUserData();
            dto.setProductoId(cmb_producto.getValue().getId());
            dto.setUrl(txt_url.getText());
            dto.setEsPrincipal(Boolean.parseBoolean(txt_principal.getText()));
            try{
                dao.ActualizarImagen(dto);
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
        ImagenProductoDTO dto = (ImagenProductoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(ProductoDTO p : cmb_producto.getItems()){
                if(p.getId() == dto.getProductoId()){ cmb_producto.setValue(p); break; }
            }
            txt_url.setText(dto.getUrl());
            txt_principal.setText(Boolean.toString(dto.isEsPrincipal()));
        }
    }
}
