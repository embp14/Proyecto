package proyectobd.VarianteProductoGui;

import dao.VarianteProductoDAO;
import dto.VarianteProductoDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class Lst_VariantesProducto_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<VarianteProductoDTO> tbl_Lista;
    @FXML private TableColumn<VarianteProductoDTO, Integer> col_id;
    @FXML private TableColumn<VarianteProductoDTO, Integer> col_producto;
    @FXML private TableColumn<VarianteProductoDTO, String> col_sku;
    @FXML private TableColumn<VarianteProductoDTO, Double> col_precio;
    @FXML private TableColumn<VarianteProductoDTO, Integer> col_stock;

    FeedbackProducto fu = new FeedbackProducto();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
        call_CargarDatos();
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(){
        try{
            VarianteProductoDAO dao = new VarianteProductoDAO();
            ObservableList<VarianteProductoDTO> lista = dao.ListarVariantes();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_producto.setCellValueFactory(new PropertyValueFactory<>("productoId"));
            col_sku.setCellValueFactory(new PropertyValueFactory<>("sku"));
            col_precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            VarianteProductoDAO dao = new VarianteProductoDAO();
            ObservableList<VarianteProductoDTO> lista;
            if(txt_Buscar.getText().isEmpty()){
                lista = dao.ListarVariantes();
            }else{
                int id = Integer.parseInt(txt_Buscar.getText());
                lista = dao.ListarVariantes();
                lista.removeIf(v -> v.getProductoId() != id);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/VarianteProductoGui/Mnt_VariantesProducto_Gui.fxml"));
            stage.setTitle("Mantenimiento de Variantes");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            VarianteProductoDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/VarianteProductoGui/Mnt_VariantesProducto_Gui.fxml"));
            stage.setTitle("Mantenimiento de Variantes");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
