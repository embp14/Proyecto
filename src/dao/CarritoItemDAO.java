package dao;

import dto.CarritoItemDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackVendedor; // reuse feedback

public class CarritoItemDAO {
    FeedbackVendedor fu = new FeedbackVendedor();

    private boolean existeCarrito(int carritoId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM carritos WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, carritoId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
            return false;
        }
    }

    private boolean existeVariante(int varianteId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM variantes_producto WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, varianteId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
            return false;
        }
    }

    public ObservableList<CarritoItemDTO> ListarItems(int carritoId){
        ObservableList<CarritoItemDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, carrito_id, variante_id, cantidad FROM carrito_items WHERE carrito_id="+carritoId+" ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CarritoItemDTO dto = new CarritoItemDTO();
                dto.setId(rs.getInt("id"));
                dto.setCarritoId(rs.getInt("carrito_id"));
                dto.setVarianteId(rs.getInt("variante_id"));
                dto.setCantidad(rs.getInt("cantidad"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return lista;
    }

    public int InsertarItem(CarritoItemDTO dto){
        try{
            if(!existeCarrito(dto.getCarritoId()) || !existeVariante(dto.getVarianteId())){
                fu.MostrarAlertas("Datos inválidos", "Carrito o variante inexistente");
                return 0;
            }
            String sql = "INSERT INTO carrito_items(carrito_id, variante_id, cantidad) VALUES(?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getCarritoId());
            ps.setInt(2, dto.getVarianteId());
            ps.setInt(3, dto.getCantidad());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Información", "Item agregado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }

    public int ActualizarItem(CarritoItemDTO dto){
        try{
            if(!existeCarrito(dto.getCarritoId()) || !existeVariante(dto.getVarianteId())){
                fu.MostrarAlertas("Datos inválidos", "Carrito o variante inexistente");
                return 0;
            }
            String sql = "UPDATE carrito_items SET carrito_id=?, variante_id=?, cantidad=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getCarritoId());
            ps.setInt(2, dto.getVarianteId());
            ps.setInt(3, dto.getCantidad());
            ps.setInt(4, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información", "Item actualizado. Registros: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }

    public int EliminarItem(int id){
        try{
            String sql = "DELETE FROM carrito_items WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información", "Item eliminado. Registros afectados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }
}
