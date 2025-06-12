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
            fu.MostrarAlertas("Error", ex.toString());
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
            fu.MostrarAlertas("Error", ex.toString());
            return false;
        }
    }

    public ObservableList<ProductoDTO> ListarProductos(){
        ObservableList<ProductoDTO> productos = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, vendedor_id, categoria_id, titulo, descripcion, creado_en, activo FROM productos ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ProductoDTO dto = new ProductoDTO();
                dto.setId(rs.getInt("id"));
                dto.setVendedorId(rs.getInt("vendedor_id"));
                dto.setCategoriaId(rs.getInt("categoria_id"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setCreadoEn(rs.getTimestamp("creado_en"));
                dto.setActivo(rs.getBoolean("activo"));
                productos.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return productos;
    }

    public ObservableList<ProductoDTO> BuscarProductos(String criterio){
        ObservableList<ProductoDTO> productos = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, vendedor_id, categoria_id, titulo, descripcion, creado_en, activo FROM productos " +
                     "WHERE titulo LIKE '%"+criterio+"%' ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ProductoDTO dto = new ProductoDTO();
                dto.setId(rs.getInt("id"));
                dto.setVendedorId(rs.getInt("vendedor_id"));
                dto.setCategoriaId(rs.getInt("categoria_id"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setCreadoEn(rs.getTimestamp("creado_en"));
                dto.setActivo(rs.getBoolean("activo"));
                productos.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return productos;
    }

    public int InsertarProducto(ProductoDTO dto){
        try{
            if(!existeVendedor(dto.getVendedorId()) || !existeCategoria(dto.getCategoriaId())){
                fu.MostrarAlertas("Datos inválidos", "Verifique vendedor y categoría");
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
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }

    public int ActualizarProducto(ProductoDTO dto){
        try{
            if(!existeVendedor(dto.getVendedorId()) || !existeCategoria(dto.getCategoriaId())){
                fu.MostrarAlertas("Datos inválidos", "Verifique vendedor y categoría");
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
            fu.MostrarAlertas("Información del sistema", "Producto actualizado. Registros modificados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }
}
