package proyectobd.ListasDeseosGui;

import dao.ListaDeseoDAO;
import dto.ListaDeseoDTO;
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
import proyectobd.ParametrosGenerales.FeedbackListaDeseo;

public class Lst_ListasDeseos_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<ListaDeseoDTO> tbl_Lista;
    @FXML private TableColumn<ListaDeseoDTO, Integer> col_id;
    @FXML private TableColumn<ListaDeseoDTO, Integer> col_usuario;
    @FXML private TableColumn<ListaDeseoDTO, String> col_nombre;
    @FXML private TableColumn<ListaDeseoDTO, String> col_creado;

    FeedbackListaDeseo fu = new FeedbackListaDeseo();

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
            ListaDeseoDAO dao = new ListaDeseoDAO();
            ObservableList<ListaDeseoDTO> lista = dao.ListarListas();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_usuario.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
            col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            col_creado.setCellValueFactory(new PropertyValueFactory<>("creadoEn"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            ListaDeseoDAO dao = new ListaDeseoDAO();
            ObservableList<ListaDeseoDTO> lista = dao.ListarListas();
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ListasDeseosGui/Mnt_ListasDeseos_Gui.fxml"));
            stage.setTitle("Mantenimiento Lista Deseos");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            ListaDeseoDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ListasDeseosGui/Mnt_ListasDeseos_Gui.fxml"));
            stage.setTitle("Mantenimiento Lista Deseos");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
