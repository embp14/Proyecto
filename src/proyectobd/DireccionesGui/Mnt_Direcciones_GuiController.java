package proyectobd.DireccionesGui;

import dao.DireccionDAO;
import dao.UsuarioDAO;
import dto.DireccionDTO;
import dto.UsuarioDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackDireccion;
import proyectobd.ParametrosGenerales.TextFilter;
import java.util.HashMap;
import java.util.List;

public class Mnt_Direcciones_GuiController implements Initializable {

    FeedbackDireccion fu = new FeedbackDireccion();
    private boolean actualizar = false;
    private final java.util.Map<String, java.util.Map<String, List<String>>> locationData = new HashMap<>();

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private TextField txt_alias;
    @FXML private TextField txt_direccion;
    @FXML private ComboBox<String> cmb_ciudad;
    @FXML private ComboBox<String> cmb_canton;
    @FXML private ComboBox<String> cmb_provincia;
    @FXML private TextField txt_postal;
    @FXML private TextField txt_telefono;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Guardar(){
        DireccionDAO dao = new DireccionDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                DireccionDTO dto = new DireccionDTO();
                dto.setUsuarioId(cmb_usuario.getValue().getId());
                dto.setAlias(txt_alias.getText());
                dto.setDireccion(txt_direccion.getText());
                dto.setCiudad(cmb_ciudad.getValue());
                dto.setCanton(cmb_canton.getValue());
                dto.setProvincia(cmb_provincia.getValue());
                dto.setCodigoPostal(txt_postal.getText());
                dto.setTelefonoContacto(txt_telefono.getText());
                int id = dao.InsertarDireccion(dto);
                if(id>0){
                    btn_Guardar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            DireccionDTO dto = (DireccionDTO) stage.getUserData();
            dto.setUsuarioId(cmb_usuario.getValue().getId());
            dto.setAlias(txt_alias.getText());
            dto.setDireccion(txt_direccion.getText());
            dto.setCiudad(cmb_ciudad.getValue());
            dto.setCanton(cmb_canton.getValue());
            dto.setProvincia(cmb_provincia.getValue());
            dto.setCodigoPostal(txt_postal.getText());
            dto.setTelefonoContacto(txt_telefono.getText());
            try{
                dao.ActualizarDireccion(dto);
                btn_Guardar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    private boolean validarDatos(){
        if(cmb_usuario.getValue() == null){
            fu.datosInvalidos("Usuario: seleccione un valor v\u00e1lido.");
            cmb_usuario.requestFocus();
            return false;
        }
        if(txt_alias.getText().trim().isEmpty()){
            fu.datosInvalidos("Alias: ingrese un texto.");
            txt_alias.requestFocus();
            return false;
        }
        if(TextFilter.contieneOfensas(txt_alias.getText())){
            fu.datosInvalidos("Alias: contiene palabras ofensivas.");
            txt_alias.requestFocus();
            return false;
        }
        if(txt_direccion.getText().trim().isEmpty()){
            fu.datosInvalidos("Direcci\u00f3n: ingrese un texto.");
            txt_direccion.requestFocus();
            return false;
        }
        if(cmb_ciudad.getValue() == null){
            fu.datosInvalidos("Ciudad: seleccione un valor.");
            cmb_ciudad.requestFocus();
            return false;
        }
        if(cmb_canton.getValue() == null){
            fu.datosInvalidos("Cant\u00f3n: seleccione un valor.");
            cmb_canton.requestFocus();
            return false;
        }
        if(cmb_provincia.getValue() == null){
            fu.datosInvalidos("Provincia: seleccione un valor.");
            cmb_provincia.requestFocus();
            return false;
        }
        if(txt_postal.getText().trim().isEmpty()){
            fu.datosInvalidos("C\u00f3digo Postal: ingrese un texto.");
            txt_postal.requestFocus();
            return false;
        }
        return true;
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        DireccionDTO dto = (DireccionDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            for(UsuarioDTO u : cmb_usuario.getItems()){
                if(u.getId() == dto.getUsuarioId()){ cmb_usuario.setValue(u); break; }
            }
            txt_alias.setText(dto.getAlias());
            txt_direccion.setText(dto.getDireccion());
            cmb_provincia.setValue(dto.getProvincia());
            cmb_ciudad.setValue(dto.getCiudad());
            cmb_canton.setValue(dto.getCanton());
            txt_postal.setText(dto.getCodigoPostal());
            txt_telefono.setText(dto.getTelefonoContacto());
        }
    }

    private void cargarCombos(){
        try {
            ObservableList<UsuarioDTO> usuarios = FXCollections.observableArrayList();
            UsuarioDAO udao = new UsuarioDAO();
            usuarios.addAll(udao.ListarUsuarios());
            cmb_usuario.setItems(usuarios);

            initUbicaciones();
            ObservableList<String> provincias = FXCollections.observableArrayList(locationData.keySet());
            cmb_provincia.setItems(provincias);

            cmb_provincia.valueProperty().addListener((obs, oldV, newV) -> {
                if(newV != null){
                    java.util.Map<String, List<String>> ciudades = locationData.get(newV);
                    cmb_ciudad.setItems(FXCollections.observableArrayList(ciudades.keySet()));
                    cmb_ciudad.getSelectionModel().clearSelection();
                    cmb_canton.getItems().clear();
                }
            });

            cmb_ciudad.valueProperty().addListener((obs, oldV, newV) -> {
                String provincia = cmb_provincia.getValue();
                if(provincia != null && newV != null){
                    List<String> cantones = locationData.get(provincia).get(newV);
                    cmb_canton.setItems(FXCollections.observableArrayList(cantones));
                    cmb_canton.getSelectionModel().clearSelection();
                }
            });
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void initUbicaciones(){
        if(!locationData.isEmpty()) return;
        java.util.Map<String, List<String>> pichincha = new HashMap<>();
        pichincha.put("Quito", java.util.Arrays.asList("Cayambe", "Mejía"));
        pichincha.put("Rumiñahui", java.util.Arrays.asList("Sangolquí", "San Rafael"));

        java.util.Map<String, List<String>> guayas = new HashMap<>();
        guayas.put("Guayaquil", java.util.Arrays.asList("Guayaquil", "Samborondón"));
        guayas.put("Daule", java.util.Arrays.asList("Daule", "Nobol"));

        java.util.Map<String, List<String>> azuay = new HashMap<>();
        azuay.put("Cuenca", java.util.Arrays.asList("Cuenca", "Giron"));
        azuay.put("Gualaceo", java.util.Arrays.asList("Gualaceo", "Chordeleg"));

        java.util.Map<String, List<String>> manabi = new HashMap<>();
        manabi.put("Portoviejo", java.util.Arrays.asList("Portoviejo", "Manta"));
        manabi.put("Chone", java.util.Arrays.asList("Chone", "Pedernales"));

        java.util.Map<String, List<String>> tungurahua = new HashMap<>();
        tungurahua.put("Ambato", java.util.Arrays.asList("Ambato", "Baños"));
        tungurahua.put("Pelileo", java.util.Arrays.asList("Pelileo", "Patate"));

        java.util.Map<String, List<String>> chimborazo = new HashMap<>();
        chimborazo.put("Riobamba", java.util.Arrays.asList("Riobamba", "Guano"));
        chimborazo.put("Colta", java.util.Arrays.asList("Colta", "Chambo"));

        java.util.Map<String, List<String>> loja = new HashMap<>();
        loja.put("Loja", java.util.Arrays.asList("Loja", "Vilcabamba"));
        loja.put("Catamayo", java.util.Arrays.asList("Catamayo", "Cariamanga"));

        locationData.put("Pichincha", pichincha);
        locationData.put("Guayas", guayas);
        locationData.put("Azuay", azuay);
        locationData.put("Manabí", manabi);
        locationData.put("Tungurahua", tungurahua);
        locationData.put("Chimborazo", chimborazo);
        locationData.put("Loja", loja);
    }
}
