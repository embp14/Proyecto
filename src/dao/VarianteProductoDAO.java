package dao;

import dto.VarianteProductoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class VarianteProductoDAO {
    FeedbackProducto fu = new FeedbackProducto();

    private boolean existeProducto(int id){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM productos WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar el producto");
            return false;
        }
    }

    public ObservableList<VarianteProductoDTO> ListarVariantes(){
        ObservableList<VarianteProductoDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, producto_id, sku, precio, stock FROM variantes_producto ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                VarianteProductoDTO dto = new VarianteProductoDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setSku(rs.getString("sku"));
                dto.setPrecio(rs.getDouble("precio"));
                dto.setStock(rs.getInt("stock"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar variantes");
        }
        return lista;
    }

    public int InsertarVariante(VarianteProductoDTO dto){
        try{
            if(!existeProducto(dto.getProductoId())){
                fu.datosInvalidos("Producto inexistente");
                return 0;
            }
            String sql = "INSERT INTO variantes_producto(producto_id, sku, precio, stock) VALUES(?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getProductoId());
            ps.setString(2, dto.getSku());
            ps.setDouble(3, dto.getPrecio());
            ps.setInt(4, dto.getStock());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Informacion", "Variante registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar la variante");
            return 0;
        }
    }

    public int ActualizarVariante(VarianteProductoDTO dto){
        try{
            if(!existeProducto(dto.getProductoId())){
                fu.datosInvalidos("Producto inexistente");
                return 0;
            }
            String sql = "UPDATE variantes_producto SET producto_id=?, sku=?, precio=?, stock=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getProductoId());
            ps.setString(2, dto.getSku());
            ps.setDouble(3, dto.getPrecio());
            ps.setInt(4, dto.getStock());
            ps.setInt(5, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Variante no encontrada");
            }else{
                fu.MostrarAlertas("Informacion", "Variante actualizada. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar la variante");
            return 0;
        }
    }

    public int EliminarVariante(int id){
        try{
            String sql = "DELETE FROM variantes_producto WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Variante no encontrada");
            }else{
                fu.MostrarAlertas("Informacion", "Variante eliminada. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar la variante");
            return 0;
        }
    }
}
