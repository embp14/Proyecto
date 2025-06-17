package dao;

import dto.ListaDeseoItemDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackListaDeseoItem;

public class ListaDeseoItemDAO {
    FeedbackListaDeseoItem fu = new FeedbackListaDeseoItem();

    private boolean existeLista(int id){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM listas_deseos WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "procesar los datos");
            return false;
        }
    }

    private boolean existeVariante(int id){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM variantes_producto WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "procesar los datos");
            return false;
        }
    }

    public ObservableList<ListaDeseoItemDTO> ListarItems(int listaId){
        ObservableList<ListaDeseoItemDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, lista_deseos_id, variante_id, cantidad FROM lista_deseos_items WHERE lista_deseos_id="+listaId+" ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ListaDeseoItemDTO dto = new ListaDeseoItemDTO();
                dto.setId(rs.getInt("id"));
                dto.setListaDeseosId(rs.getInt("lista_deseos_id"));
                dto.setVarianteId(rs.getInt("variante_id"));
                dto.setCantidad(rs.getInt("cantidad"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "procesar los datos");
        }
        return lista;
    }

    public int InsertarItem(ListaDeseoItemDTO dto){
        try{
            if(!existeLista(dto.getListaDeseosId()) || !existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Lista o variante inexistente");
                return 0;
            }
            String sql = "INSERT INTO lista_deseos_items(lista_deseos_id, variante_id, cantidad) VALUES(?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getListaDeseosId());
            ps.setInt(2, dto.getVarianteId());
            ps.setInt(3, dto.getCantidad());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Informacion", "Item registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "procesar los datos");
            return 0;
        }
    }

    public int ActualizarItem(ListaDeseoItemDTO dto){
        try{
            if(!existeLista(dto.getListaDeseosId()) || !existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Lista o variante inexistente");
                return 0;
            }
            String sql = "UPDATE lista_deseos_items SET lista_deseos_id=?, variante_id=?, cantidad=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getListaDeseosId());
            ps.setInt(2, dto.getVarianteId());
            ps.setInt(3, dto.getCantidad());
            ps.setInt(4, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Informacion", "Item actualizado. Registros: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "procesar los datos");
            return 0;
        }
    }

    public int EliminarItem(int id){
        try{
            String sql = "DELETE FROM lista_deseos_items WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Informacion", "Item eliminado. Registros afectados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "procesar los datos");
            return 0;
        }
    }
}
