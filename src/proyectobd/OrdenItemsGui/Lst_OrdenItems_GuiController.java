package proyectobd.OrdenItemsGui;

import dao.OrdenItemDAO;
import dto.OrdenItemDTO;
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
import proyectobd.ParametrosGenerales.FeedbackOrdenItem;

public class Lst_OrdenItems_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<OrdenItemDTO> tbl_Lista;
    @FXML private TableColumn<OrdenItemDTO, Integer> col_id;
    @FXML private TableColumn<OrdenItemDTO, Integer> col_orden;
    @FXML private TableColumn<OrdenItemDTO, Integer> col_variante;
    @FXML private TableColumn<OrdenItemDTO, Integer> col_cantidad;

    FeedbackOrdenItem fu = new FeedbackOrdenItem();

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
            OrdenItemDAO dao = new OrdenItemDAO();
            ObservableList<OrdenItemDTO> lista = dao.ListarItems(idCarrito);
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_orden.setCellValueFactory(new PropertyValueFactory<>("ordenId"));
            col_variante.setCellValueFactory(new PropertyValueFactory<>("varianteId"));
            col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        int id = 0;
        try{ id = Integer.parseInt(txt_Buscar.getText()); }catch(Exception e){}
        call_CargarDatos(id);
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/OrdenItemsGui/Mnt_OrdenItems_Gui.fxml"));
            stage.setTitle("Mantenimiento Items Orden");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            OrdenItemDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/OrdenItemsGui/Mnt_OrdenItems_Gui.fxml"));
            stage.setTitle("Mantenimiento Items Orden");
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
            OrdenItemDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            OrdenItemDAO dao = new OrdenItemDAO();
            dao.EliminarItem(dto.getId());
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
