package dao;

import dto.ImagenProductoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class ImagenProductoDAO {
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
            fu.MostrarAlertas("Error", ex.toString());
            return false;
        }
    }

    public ObservableList<ImagenProductoDTO> ListarImagenes(int productoId){
        ObservableList<ImagenProductoDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, producto_id, url, es_principal FROM imagenes_producto";
        if(productoId > 0){
            sql += " WHERE producto_id=" + productoId;
        }
        sql += " ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ImagenProductoDTO dto = new ImagenProductoDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setUrl(rs.getString("url"));
                dto.setEsPrincipal(rs.getBoolean("es_principal"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return lista;
    }

    public int InsertarImagen(ImagenProductoDTO dto){
        try{
            if(!existeProducto(dto.getProductoId())){
                fu.MostrarAlertas("Datos invalidos", "Producto inexistente");
                return 0;
            }
            String sql = "INSERT INTO imagenes_producto(producto_id, url, es_principal) VALUES(?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getProductoId());
            ps.setString(2, dto.getUrl());
            ps.setBoolean(3, dto.isEsPrincipal());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Informacion", "Imagen registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }

    public int ActualizarImagen(ImagenProductoDTO dto){
        try{
            if(!existeProducto(dto.getProductoId())){
                fu.MostrarAlertas("Datos invalidos", "Producto inexistente");
                return 0;
            }
            String sql = "UPDATE imagenes_producto SET producto_id=?, url=?, es_principal=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getProductoId());
            ps.setString(2, dto.getUrl());
            ps.setBoolean(3, dto.isEsPrincipal());
            ps.setInt(4, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Informacion", "Imagen actualizada. Registros: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }
}
