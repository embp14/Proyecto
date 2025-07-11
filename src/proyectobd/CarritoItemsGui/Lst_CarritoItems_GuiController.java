package proyectobd.CarritoItemsGui;

import dao.CarritoItemDAO;
import dto.CarritoItemDTO;
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
import proyectobd.ParametrosGenerales.FeedbackCarritoItem;

public class Lst_CarritoItems_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<CarritoItemDTO> tbl_Lista;
    @FXML private TableColumn<CarritoItemDTO, Integer> col_id;
    @FXML private TableColumn<CarritoItemDTO, Integer> col_carrito;
    @FXML private TableColumn<CarritoItemDTO, String> col_variante;
    @FXML private TableColumn<CarritoItemDTO, Integer> col_cantidad;

    FeedbackCarritoItem fu = new FeedbackCarritoItem();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(int idCarrito){
        try{
            CarritoItemDAO dao = new CarritoItemDAO();
            ObservableList<CarritoItemDTO> lista = dao.ListarItems(idCarrito);
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_carrito.setCellValueFactory(new PropertyValueFactory<>("carritoId"));
            col_variante.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getVarianteSku() + " - " + data.getValue().getProductoNombre() +
                        " (" + data.getValue().getVarianteId() + ")"));
            col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        String filtro = txt_Buscar.getText().trim();
        if(filtro.isEmpty()){
            call_CargarDatos(0);
            return;
        }
        if(filtro.matches("\\d+")){
            int id = Integer.parseInt(filtro);
            call_CargarDatos(id);
        }else{
            fu.datosInvalidos("Ingrese un ID num\u00e9rico");
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CarritoItemsGui/Mnt_CarritoItems_Gui.fxml"));
            stage.setTitle("Mantenimiento Items");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            CarritoItemDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CarritoItemsGui/Mnt_CarritoItems_Gui.fxml"));
            stage.setTitle("Mantenimiento Items");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Borrar(){
        try{
            CarritoItemDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            CarritoItemDAO dao = new CarritoItemDAO();
            dao.EliminarItem(dto.getId());
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
