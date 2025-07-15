package dao;

import dto.ProductoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class ProductoDAO {
    FeedbackProducto fu = new FeedbackProducto();

    private boolean existeVendedor(int vendedorId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM vendedores WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, vendedorId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar el vendedor");
            return false;
        }
    }

    private boolean existeCategoria(int categoriaId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM categorias WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, categoriaId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar la categoría");
            return false;
        }
    }

    public ObservableList<ProductoDTO> ListarProductos(){
        ObservableList<ProductoDTO> productos = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT p.id, p.vendedor_id, v.nombre_tienda AS vendedor_nombre, " +
                     "p.categoria_id, c.nombre AS categoria_nombre, p.titulo, p.descripcion, p.creado_en, p.activo " +
                     "FROM productos p JOIN vendedores v ON p.vendedor_id=v.id " +
                     "JOIN categorias c ON p.categoria_id=c.id ORDER BY p.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ProductoDTO dto = new ProductoDTO();
                dto.setId(rs.getInt("id"));
                dto.setVendedorId(rs.getInt("vendedor_id"));
                dto.setVendedorNombre(rs.getString("vendedor_nombre"));
                dto.setCategoriaId(rs.getInt("categoria_id"));
                dto.setCategoriaNombre(rs.getString("categoria_nombre"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setCreadoEn(rs.getTimestamp("creado_en"));
                dto.setActivo(rs.getBoolean("activo"));
                productos.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar productos");
        }
        return productos;
    }

    public ObservableList<ProductoDTO> BuscarProductos(String criterio){
        ObservableList<ProductoDTO> productos = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT p.id, p.vendedor_id, v.nombre_tienda AS vendedor_nombre, " +
                     "p.categoria_id, c.nombre AS categoria_nombre, p.titulo, p.descripcion, p.creado_en, p.activo " +
                     "FROM productos p JOIN vendedores v ON p.vendedor_id=v.id " +
                     "JOIN categorias c ON p.categoria_id=c.id WHERE CONCAT_WS(' ', p.id, p.vendedor_id, v.nombre_tienda, p.categoria_id, c.nombre, p.titulo, p.descripcion, p.creado_en, p.activo) " +
                     "LIKE '%"+criterio+"%' ORDER BY p.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ProductoDTO dto = new ProductoDTO();
                dto.setId(rs.getInt("id"));
                dto.setVendedorId(rs.getInt("vendedor_id"));
                dto.setVendedorNombre(rs.getString("vendedor_nombre"));
                dto.setCategoriaId(rs.getInt("categoria_id"));
                dto.setCategoriaNombre(rs.getString("categoria_nombre"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setCreadoEn(rs.getTimestamp("creado_en"));
                dto.setActivo(rs.getBoolean("activo"));
                productos.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar productos");
        }
        return productos;
    }

    public int InsertarProducto(ProductoDTO dto){
        try{
            if(!existeVendedor(dto.getVendedorId()) || !existeCategoria(dto.getCategoriaId())){
                fu.datosInvalidos("Verifique vendedor y categoría");
                return 0;
            }
            String sql = "INSERT INTO productos(vendedor_id, categoria_id, titulo, descripcion, creado_en, activo) VALUES(?,?,?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getVendedorId());
            ps.setInt(2, dto.getCategoriaId());
            ps.setString(3, dto.getTitulo());
            ps.setString(4, dto.getDescripcion());
            ps.setTimestamp(5, dto.getCreadoEn());
            ps.setBoolean(6, dto.isActivo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()){
                codigo = rs.getInt(1);
            }
            fu.MostrarAlertas("Información del sistema", "Producto registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar el producto");
            return 0;
        }
    }

    public int ActualizarProducto(ProductoDTO dto){
        try{
            if(!existeVendedor(dto.getVendedorId()) || !existeCategoria(dto.getCategoriaId())){
                fu.datosInvalidos("Verifique vendedor y categoría");
                return 0;
            }
            String sql = "UPDATE productos SET vendedor_id=?, categoria_id=?, titulo=?, descripcion=?, creado_en=?, activo=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getVendedorId());
            ps.setInt(2, dto.getCategoriaId());
            ps.setString(3, dto.getTitulo());
            ps.setString(4, dto.getDescripcion());
            ps.setTimestamp(5, dto.getCreadoEn());
            ps.setBoolean(6, dto.isActivo());
            ps.setInt(7, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Producto no encontrado");
            }else{
                fu.MostrarAlertas("Información del sistema", "Producto actualizado. Registros modificados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar el producto");
            return 0;
        }
    }

    public int EliminarProducto(int id){
        try{
            String sql = "DELETE FROM productos WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Producto no encontrado");
            }else{
                fu.MostrarAlertas("Información del sistema", "Producto eliminado. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar el producto");
            return 0;
        }
    }
}
