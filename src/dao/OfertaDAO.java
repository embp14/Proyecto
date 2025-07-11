package dao;

import dto.OfertaDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class OfertaDAO {
    FeedbackProducto fu = new FeedbackProducto();

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
            fu.errorSQL(ex, "verificar la variante");
            return false;
        }
    }

    public ObservableList<OfertaDTO> ListarOfertas(){
        ObservableList<OfertaDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, variante_id, precio_descuento, fecha_inicio, fecha_fin FROM ofertas ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                OfertaDTO dto = new OfertaDTO();
                dto.setId(rs.getInt("id"));
                dto.setVarianteId(rs.getInt("variante_id"));
                dto.setPrecioDescuento(rs.getDouble("precio_descuento"));
                dto.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                dto.setFechaFin(rs.getTimestamp("fecha_fin"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar ofertas");
        }
        return lista;
    }

    public int InsertarOferta(OfertaDTO dto){
        try{
            if(!existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Variante inexistente");
                return 0;
            }
            String sql = "INSERT INTO ofertas(variante_id, precio_descuento, fecha_inicio, fecha_fin) VALUES(?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getVarianteId());
            ps.setDouble(2, dto.getPrecioDescuento());
            ps.setTimestamp(3, dto.getFechaInicio());
            ps.setTimestamp(4, dto.getFechaFin());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Informacion", "Oferta registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar la oferta");
            return 0;
        }
    }

    public int ActualizarOferta(OfertaDTO dto){
        try{
            if(!existeVariante(dto.getVarianteId())){
                fu.datosInvalidos("Variante inexistente");
                return 0;
            }
            String sql = "UPDATE ofertas SET variante_id=?, precio_descuento=?, fecha_inicio=?, fecha_fin=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getVarianteId());
            ps.setDouble(2, dto.getPrecioDescuento());
            ps.setTimestamp(3, dto.getFechaInicio());
            ps.setTimestamp(4, dto.getFechaFin());
            ps.setInt(5, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Oferta no encontrada");
            }else{
                fu.MostrarAlertas("Informacion", "Oferta actualizada. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar la oferta");
            return 0;
        }
    }

    public int EliminarOferta(int id){
        try{
            String sql = "DELETE FROM ofertas WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Oferta no encontrada");
            }else{
                fu.MostrarAlertas("Informacion", "Oferta eliminada. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar la oferta");
            return 0;
        }
    }
}
