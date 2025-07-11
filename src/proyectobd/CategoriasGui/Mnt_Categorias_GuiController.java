package proyectobd.CategoriasGui;

import dao.CategoriaDAO;
import dto.CategoriaDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackCategoria;

public class Mnt_Categorias_GuiController implements Initializable {

    FeedbackCategoria fu = new FeedbackCategoria();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_nombre;
    @FXML private ComboBox<CategoriaDTO> cmb_parent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
            txt_id.setDisable(true);
        });
        txt_nombre.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("[\\p{L} ]*") ? change : null;
        }));
    }

    public void call_Grabar(){
        CategoriaDAO dao = new CategoriaDAO();
        if(!nombreValido(txt_nombre.getText())){
            fu.datosInvalidos("El campo 'Nombre' solo debe contener letras.");
            txt_nombre.requestFocus();
            return;
        }
        if(!actualizar){
            try{
                CategoriaDTO dto = new CategoriaDTO();
                dto.setNombre(txt_nombre.getText());
                CategoriaDTO sel = cmb_parent.getValue();
                if(sel != null) dto.setParentId(sel.getId());
                int id = dao.InsertarCategoria(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            CategoriaDTO dto = (CategoriaDTO) stage.getUserData();
            dto.setNombre(txt_nombre.getText());
            CategoriaDTO sel = cmb_parent.getValue();
            if(sel != null) dto.setParentId(sel.getId());
            else dto.setParentId(null);
            try{
                dao.ActualizarCategoria(dto);
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
        CategoriaDTO dto = (CategoriaDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_nombre.setText(dto.getNombre());
            if(dto.getParentId()!=null){
                for(CategoriaDTO c : cmb_parent.getItems()){
                    if(c.getId() == dto.getParentId()){ cmb_parent.setValue(c); break; }
                }
            }
        }
    }

    private void cargarCombos(){
        try{
            CategoriaDAO dao = new CategoriaDAO();
            ObservableList<CategoriaDTO> categorias = FXCollections.observableArrayList();
            categorias.addAll(dao.ListarCategorias());
            cmb_parent.setItems(categorias);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private boolean nombreValido(String nombre){
        if(nombre == null) return false;
        return nombre.matches("[\\p{L} ]+");
    }
}
