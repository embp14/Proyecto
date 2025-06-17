package dao;

import dto.OrdenItemDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackOrden;

public class OrdenItemDAO {
    FeedbackOrden fu = new FeedbackOrden();

    private boolean existeOrden(int id){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM ordenes WHERE id=?";
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

    public ObservableList<OrdenItemDTO> ListarItems(int ordenId){
        ObservableList<OrdenItemDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, orden_id, variante_id, cantidad, precio_unitario, precio_descuento FROM orden_items WHERE orden_id="+ordenId+" ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                OrdenItemDTO dto = new OrdenItemDTO();
                dto.setId(rs.getInt("id"));
                dto.setOrdenId(rs.getInt("orden_id"));
                dto.setVarianteId(rs.getInt("variante_id"));
                dto.setCantidad(rs.getInt("cantidad"));
                dto.setPrecioUnitario(rs.getDouble("precio_unitario"));
                dto.setPrecioDescuento(rs.getDouble("precio_descuento"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "procesar los datos");
        }
        return lista;
    }

    public int InsertarItem(OrdenItemDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId()) || !existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Orden o variante inexistente");
                return 0;
            }
            String sql = "INSERT INTO orden_items(orden_id, variante_id, cantidad, precio_unitario, precio_descuento) VALUES(?,?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getOrdenId());
            ps.setInt(2, dto.getVarianteId());
            ps.setInt(3, dto.getCantidad());
            ps.setDouble(4, dto.getPrecioUnitario());
            ps.setDouble(5, dto.getPrecioDescuento());
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

    public int ActualizarItem(OrdenItemDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId()) || !existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Orden o variante inexistente");
                return 0;
            }
            String sql = "UPDATE orden_items SET orden_id=?, variante_id=?, cantidad=?, precio_unitario=?, precio_descuento=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getOrdenId());
            ps.setInt(2, dto.getVarianteId());
            ps.setInt(3, dto.getCantidad());
            ps.setDouble(4, dto.getPrecioUnitario());
            ps.setDouble(5, dto.getPrecioDescuento());
            ps.setInt(6, dto.getId());
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
            String sql = "DELETE FROM orden_items WHERE id=?";
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
