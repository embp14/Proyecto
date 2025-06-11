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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackCategoria;

public class Mnt_Categorias_GuiController implements Initializable {

    FeedbackCategoria fu = new FeedbackCategoria();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_parent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        CategoriaDAO dao = new CategoriaDAO();
        if(!actualizar){
            try{
                CategoriaDTO dto = new CategoriaDTO();
                dto.setNombre(txt_nombre.getText());
                if(!txt_parent.getText().trim().isEmpty())
                    dto.setParentId(Integer.parseInt(txt_parent.getText()));
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
            if(!txt_parent.getText().trim().isEmpty())
                dto.setParentId(Integer.parseInt(txt_parent.getText()));
            else
                dto.setParentId(null);
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
            if(dto.getParentId()!=null) txt_parent.setText(Integer.toString(dto.getParentId()));
        }
    }
}
