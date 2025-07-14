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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackDireccion;
import proyectobd.ParametrosGenerales.TextFilter;
import java.util.HashMap;
import java.util.List;

public class Mnt_Direcciones_GuiController implements Initializable {

    FeedbackDireccion fu = new FeedbackDireccion();
    private boolean actualizar = false;
    private final java.util.Map<String, List<String>> locationData = new HashMap<>();

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private Button btn_Cerrar;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private TextField txt_alias;
    @FXML private TextField txt_direccion;
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
                dto.setCiudad(cmb_canton.getValue());
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
            dto.setCiudad(cmb_canton.getValue());
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
        if(TextFilter.contieneLenguajeInapropiado(txt_alias.getText())){
            fu.datosInvalidos("Alias: contiene lenguaje inapropiado.");
            txt_alias.requestFocus();
            return false;
        }
        if(txt_direccion.getText().trim().isEmpty()){
            fu.datosInvalidos("Direcci\u00f3n: ingrese un texto.");
            txt_direccion.requestFocus();
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
        if(!codigoPostalValido(txt_postal.getText())){
            fu.datosInvalidos("C\u00f3digo Postal: formato inv\u00e1lido. Use solo d\u00edgitos.");
            txt_postal.requestFocus();
            return false;
        }
        if(txt_telefono.getText().trim().isEmpty()){
            fu.datosInvalidos("Tel\u00e9fono: ingrese un valor.");
            txt_telefono.requestFocus();
            return false;
        }
        if(!telefonoEcuadorValido(txt_telefono.getText())){
            fu.datosInvalidos("Tel\u00e9fono: formato ecuatoriano inv\u00e1lido.");
            txt_telefono.requestFocus();
            return false;
        }
        return true;
    }

    private boolean codigoPostalValido(String cp){
        if(cp == null) return false;
        String trimmed = cp.trim();
        if(trimmed.isEmpty()) return false;
        return trimmed.matches("\\d{5,6}");
    }

    private boolean telefonoEcuadorValido(String num){
        if(num == null) return false;
        String t = num.trim();
        if(t.isEmpty()) return false;
        // Landlines: 0[2-7]XXXXXXX (9 digits), Mobiles: 09XXXXXXXX (10 digits)
        return t.matches("09\\d{8}") || t.matches("0[2-7]\\d{7}");
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
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
                    List<String> cantones = locationData.get(newV);
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

        // Provinces and cantons based on the official listing. Parishes are
        // omitted as requested.

        locationData.put("Azuay", java.util.Arrays.asList(
                "Cuenca", "Girón", "Gualaceo", "Nabón", "Paute", "Pucará",
                "San Fernando", "Santa Isabel", "Sigsig", "Oña", "Chordeleg",
                "El Pan", "Sevilla de Oro", "Guachapala",
                "Camilo Ponce Enríquez"));

        locationData.put("Bolívar", java.util.Arrays.asList(
                "Guaranda", "Chillanes", "Chimbo", "Echeandía", "San Miguel",
                "Caluma", "Las Naves"));

        locationData.put("Cañar", java.util.Arrays.asList(
                "Azogues", "Biblián", "Cañar", "La Troncal", "El Tambo",
                "Déleg", "Suscal"));

        locationData.put("Carchi", java.util.Arrays.asList(
                "Tulcán", "Bolívar", "Espejo", "Mira", "Montúfar",
                "San Pedro de Huaca"));

        locationData.put("Cotopaxi", java.util.Arrays.asList(
                "Latacunga", "La Maná", "Pangua", "Pujilí", "Salcedo",
                "Saquisilí", "Sigchos"));

        locationData.put("Chimborazo", java.util.Arrays.asList(
                "Riobamba", "Alausí", "Colta", "Chambo", "Chunchi",
                "Guamote", "Guano", "Pallatanga", "Penipe", "Cumandá"));

        locationData.put("El Oro", java.util.Arrays.asList(
                "Machala", "Arenillas", "Atahualpa", "Balsas", "Chilla",
                "El Guabo", "Huaquillas", "Marcabelí", "Pasaje", "Piñas",
                "Portovelo", "Santa Rosa", "Zaruma", "Las Lajas"));

        locationData.put("Esmeraldas", java.util.Arrays.asList(
                "Esmeraldas", "Eloy Alfaro", "Muisne", "Quinindé",
                "San Lorenzo", "Atacames", "Rioverde", "La Concordia"));

        locationData.put("Guayas", java.util.Arrays.asList(
                "Guayaquil", "Alfredo Baquerizo Moreno (Juján)", "Balao",
                "Balzar", "Colimes", "Daule", "Durán", "El Empalme",
                "El Triunfo", "Milagro", "Naranjal", "Naranjito",
                "Palestina", "Pedro Carbo", "Samborondón", "Santa Lucía",
                "Salitre (Urbina Jado)", "San Jacinto de Yaguachi", "Playas",
                "Simón Bolívar", "Coronel Marcelino Maridueña",
                "Lomas de Sargentillo", "Nobol", "General Antonio Elizalde",
                "Isidro Ayora"));

        locationData.put("Imbabura", java.util.Arrays.asList(
                "Ibarra", "Antonio Ante", "Cotacachi", "Otavalo",
                "Pimampiro", "San Miguel de Urcuquí"));

        locationData.put("Loja", java.util.Arrays.asList(
                "Loja", "Calvas", "Catamayo", "Celica", "Chaguarpamba",
                "Espíndola"));
    }
}
