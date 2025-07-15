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
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<ListaDeseoItemDTO> tbl_Lista;
    @FXML private TableColumn<ListaDeseoItemDTO, Integer> col_id;
    @FXML private TableColumn<ListaDeseoItemDTO, String> col_lista;
    @FXML private TableColumn<ListaDeseoItemDTO, String> col_variante;
    @FXML private TableColumn<ListaDeseoItemDTO, Integer> col_cantidad;

    FeedbackListaDeseoItem fu = new FeedbackListaDeseoItem();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configure table columns up front so search results display correctly
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_lista.setCellValueFactory(data ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getListaNombre() + " - ID " +
                        data.getValue().getListaDeseosId()));
        col_variante.setCellValueFactory(data ->
                new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getVarianteSku() + " - " +
                        data.getValue().getProductoNombre() +
                        " - ID " + data.getValue().getVarianteId()));
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
        call_Buscar();
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
            col_lista.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getListaNombre() + " - ID " + data.getValue().getListaDeseosId()));
            col_variante.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getVarianteSku() + " - " + data.getValue().getProductoNombre() +
                        " - ID " + data.getValue().getVarianteId()));
            col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            ListaDeseoItemDAO dao = new ListaDeseoItemDAO();
            String filtro = txt_Buscar.getText().trim();
            ObservableList<ListaDeseoItemDTO> lista;
            if(filtro.isEmpty()){
                lista = dao.ListarItems(0);
            }else if(filtro.matches("\\d+")){
                int id = Integer.parseInt(filtro);
                lista = dao.ListarItems(id);
            }else{
                lista = dao.BuscarItems(filtro);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
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

    public void call_Borrar(){
        try{
            ListaDeseoItemDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            ListaDeseoItemDAO dao = new ListaDeseoItemDAO();
            dao.EliminarItem(dto.getId());
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
