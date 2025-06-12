package proyectobd.ListaDeseoItemsGui;

import dao.ListaDeseoItemDAO;
import dto.ListaDeseoItemDTO;
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
import proyectobd.ParametrosGenerales.FeedbackListaDeseoItem;

public class Lst_ListaDeseoItems_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<ListaDeseoItemDTO> tbl_Lista;
    @FXML private TableColumn<ListaDeseoItemDTO, Integer> col_id;
    @FXML private TableColumn<ListaDeseoItemDTO, Integer> col_lista;
    @FXML private TableColumn<ListaDeseoItemDTO, Integer> col_variante;
    @FXML private TableColumn<ListaDeseoItemDTO, Integer> col_cantidad;

    FeedbackListaDeseoItem fu = new FeedbackListaDeseoItem();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(int idLista){
        try{
            ListaDeseoItemDAO dao = new ListaDeseoItemDAO();
            ObservableList<ListaDeseoItemDTO> lista = dao.ListarItems(idLista);
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_lista.setCellValueFactory(new PropertyValueFactory<>("listaDeseosId"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ListaDeseoItemsGui/Mnt_ListaDeseoItems_Gui.fxml"));
            stage.setTitle("Mantenimiento Items Lista");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            ListaDeseoItemDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ListaDeseoItemsGui/Mnt_ListaDeseoItems_Gui.fxml"));
            stage.setTitle("Mantenimiento Items Lista");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
