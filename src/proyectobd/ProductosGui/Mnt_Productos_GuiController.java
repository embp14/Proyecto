package proyectobd.ProductosGui;

import dao.ProductoDAO;
import dto.ProductoDTO;
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
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class Mnt_Productos_GuiController implements Initializable {

    FeedbackProducto fu = new FeedbackProducto();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_vendedor;
    @FXML private TextField txt_categoria;
    @FXML private TextField txt_titulo;
    @FXML private TextField txt_descripcion;
    @FXML private TextField txt_activo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        ProductoDAO dao = new ProductoDAO();
        if(!actualizar){
            try{
                ProductoDTO dto = new ProductoDTO();
                dto.setVendedorId(Integer.parseInt(txt_vendedor.getText()));
                dto.setCategoriaId(Integer.parseInt(txt_categoria.getText()));
                dto.setTitulo(txt_titulo.getText());
                dto.setDescripcion(txt_descripcion.getText());
                dto.setCreadoEn(new Timestamp(System.currentTimeMillis()));
                dto.setActivo(Boolean.parseBoolean(txt_activo.getText()));
                int id = dao.InsertarProducto(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            ProductoDTO dto = (ProductoDTO) stage.getUserData();
            dto.setVendedorId(Integer.parseInt(txt_vendedor.getText()));
            dto.setCategoriaId(Integer.parseInt(txt_categoria.getText()));
            dto.setTitulo(txt_titulo.getText());
            dto.setDescripcion(txt_descripcion.getText());
            dto.setActivo(Boolean.parseBoolean(txt_activo.getText()));
            try{
                dao.ActualizarProducto(dto);
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
        ProductoDTO dto = (ProductoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_vendedor.setText(Integer.toString(dto.getVendedorId()));
            txt_categoria.setText(Integer.toString(dto.getCategoriaId()));
            txt_titulo.setText(dto.getTitulo());
            txt_descripcion.setText(dto.getDescripcion());
            txt_activo.setText(Boolean.toString(dto.isActivo()));
        }
    }
}
