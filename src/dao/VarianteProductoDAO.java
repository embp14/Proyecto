package dao;

import dto.VarianteProductoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class VarianteProductoDAO {
    FeedbackProducto fu = new FeedbackProducto();

    private boolean tieneColumna(Connection conn, String tabla, String columna){
        try(ResultSet rs = conn.getMetaData().getColumns(null, null, tabla, columna)){
            return rs.next();
        }catch(Exception ex){
            return false;
        }
    }

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
        try{
            Connection cn = ConnBD.AbrirConexionBD();
            boolean hasNombre = tieneColumna(cn, "variantes_producto", "nombre");
            String sql = "SELECT v.id, v.producto_id, v.sku, p.titulo AS producto_nombre, ";
            if(hasNombre) sql += "v.nombre, ";
            sql += "v.precio, v.stock FROM variantes_producto v JOIN productos p ON v.producto_id=p.id ORDER BY v.id";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                VarianteProductoDTO dto = new VarianteProductoDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setSku(rs.getString("sku"));
                dto.setProductoNombre(rs.getString("producto_nombre"));
                if(hasNombre) dto.setNombre(rs.getString("nombre"));
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
            ConectorBD ConnBD = new ConectorBD();
            Connection cn = ConnBD.AbrirConexionBD();
            boolean hasNombre = tieneColumna(cn, "variantes_producto", "nombre");
            String sql;
            if(hasNombre){
                sql = "INSERT INTO variantes_producto(producto_id, sku, nombre, precio, stock) VALUES(?,?,?,?,?)";
            }else{
                sql = "INSERT INTO variantes_producto(producto_id, sku, precio, stock) VALUES(?,?,?,?)";
            }
            PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getProductoId());
            ps.setString(2, dto.getSku());
            int idx = 3;
            if(hasNombre){
                ps.setString(idx++, dto.getNombre());
            }
            ps.setDouble(idx++, dto.getPrecio());
            ps.setInt(idx++, dto.getStock());
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
            ConectorBD ConnBD = new ConectorBD();
            Connection cn = ConnBD.AbrirConexionBD();
            boolean hasNombre = tieneColumna(cn, "variantes_producto", "nombre");
            String sql;
            if(hasNombre){
                sql = "UPDATE variantes_producto SET producto_id=?, sku=?, nombre=?, precio=?, stock=? WHERE id=?";
            }else{
                sql = "UPDATE variantes_producto SET producto_id=?, sku=?, precio=?, stock=? WHERE id=?";
            }
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, dto.getProductoId());
            ps.setString(2, dto.getSku());
            int idx = 3;
            if(hasNombre){
                ps.setString(idx++, dto.getNombre());
            }
            ps.setDouble(idx++, dto.getPrecio());
            ps.setInt(idx++, dto.getStock());
            ps.setInt(idx, dto.getId());
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
