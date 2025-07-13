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
            fu.errorSQL(ex, "verificar el carrito");
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
            fu.errorSQL(ex, "verificar la variante");
            return false;
        }
    }

    public ObservableList<CarritoItemDTO> ListarItems(int carritoId){
        ObservableList<CarritoItemDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT ci.id, ci.carrito_id, u.nombre AS usuario_nombre, ci.variante_id, " +
                     "v.sku AS variante_sku, p.titulo AS producto_nombre, ci.cantidad " +
                     "FROM carrito_items ci " +
                     "JOIN carritos c ON ci.carrito_id=c.id " +
                     "JOIN usuarios u ON c.usuario_id=u.id " +
                     "JOIN variantes_producto v ON ci.variante_id=v.id " +
                     "JOIN productos p ON v.producto_id=p.id ";
        if(carritoId>0){
            sql += "WHERE ci.carrito_id="+carritoId+" ";
        }
        sql += "ORDER BY ci.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CarritoItemDTO dto = new CarritoItemDTO();
                dto.setId(rs.getInt("id"));
                dto.setCarritoId(rs.getInt("carrito_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setVarianteId(rs.getInt("variante_id"));
                dto.setVarianteSku(rs.getString("variante_sku"));
                dto.setProductoNombre(rs.getString("producto_nombre"));
                dto.setCantidad(rs.getInt("cantidad"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar items de carrito");
        }
        return lista;
    }

    public int InsertarItem(CarritoItemDTO dto){
        try{
            if(!existeCarrito(dto.getCarritoId()) || !existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Carrito o variante inexistente");
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
            fu.errorSQL(ex, "agregar el item");
            return 0;
        }
    }

    public int ActualizarItem(CarritoItemDTO dto){
        try{
            if(!existeCarrito(dto.getCarritoId()) || !existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Carrito o variante inexistente");
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
            if(registros==0){
                fu.datosInvalidos("Item no encontrado");
            }else{
                fu.MostrarAlertas("Información", "Item actualizado. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar el item");
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
            if(registros==0){
                fu.datosInvalidos("Item no encontrado");
            }else{
                fu.MostrarAlertas("Información", "Item eliminado. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar el item");
            return 0;
        }
    }
}
