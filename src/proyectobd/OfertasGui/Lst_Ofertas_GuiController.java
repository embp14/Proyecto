package proyectobd.OfertasGui;

import dao.OfertaDAO;
import dto.OfertaDTO;
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
import proyectobd.ParametrosGenerales.FeedbackOferta;

public class Lst_Ofertas_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<OfertaDTO> tbl_Lista;
    @FXML private TableColumn<OfertaDTO, Integer> col_id;
    @FXML private TableColumn<OfertaDTO, Integer> col_variante;
    @FXML private TableColumn<OfertaDTO, Double> col_precio;
    @FXML private TableColumn<OfertaDTO, String> col_inicio;
    @FXML private TableColumn<OfertaDTO, String> col_fin;

    FeedbackOferta fu = new FeedbackOferta();

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
            OfertaDAO dao = new OfertaDAO();
            ObservableList<OfertaDTO> lista = dao.ListarOfertas();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_variante.setCellValueFactory(new PropertyValueFactory<>("varianteId"));
            col_precio.setCellValueFactory(new PropertyValueFactory<>("precioDescuento"));
            col_inicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
            col_fin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            OfertaDAO dao = new OfertaDAO();
            ObservableList<OfertaDTO> lista = dao.ListarOfertas();
            if(!txt_Buscar.getText().isEmpty()){
                int id = Integer.parseInt(txt_Buscar.getText());
                lista.removeIf(o -> o.getId() != id);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/OfertasGui/Mnt_Ofertas_Gui.fxml"));
            stage.setTitle("Mantenimiento de Ofertas");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            OfertaDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/OfertasGui/Mnt_Ofertas_Gui.fxml"));
            stage.setTitle("Mantenimiento de Ofertas");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
