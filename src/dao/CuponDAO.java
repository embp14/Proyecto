package dao;

import dto.CuponDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackCupon;

public class CuponDAO {
    FeedbackCupon fu = new FeedbackCupon();

    public ObservableList<CuponDTO> ListarCupones(){
        ObservableList<CuponDTO> cupones = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, codigo, descuento_pct, fecha_expiracion, uso_maximo FROM cupones ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CuponDTO dto = new CuponDTO();
                dto.setId(rs.getInt("id"));
                dto.setCodigo(rs.getString("codigo"));
                dto.setDescuentoPct(rs.getInt("descuento_pct"));
                dto.setFechaExpiracion(rs.getTimestamp("fecha_expiracion"));
                dto.setUsoMaximo(rs.getInt("uso_maximo"));
                cupones.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar cupones");
        }
        return cupones;
    }

    public ObservableList<CuponDTO> BuscarCupones(String criterio){
        ObservableList<CuponDTO> cupones = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, codigo, descuento_pct, fecha_expiracion, uso_maximo FROM cupones " +
                     "WHERE CONCAT_WS(' ', id, codigo, descuento_pct, fecha_expiracion, uso_maximo) " +
                     "LIKE '%"+criterio+"%' ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CuponDTO dto = new CuponDTO();
                dto.setId(rs.getInt("id"));
                dto.setCodigo(rs.getString("codigo"));
                dto.setDescuentoPct(rs.getInt("descuento_pct"));
                dto.setFechaExpiracion(rs.getTimestamp("fecha_expiracion"));
                dto.setUsoMaximo(rs.getInt("uso_maximo"));
                cupones.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar cupones");
        }
        return cupones;
    }

    public int InsertarCupon(CuponDTO dto){
        try{
            String sql = "INSERT INTO cupones(codigo, descuento_pct, fecha_expiracion, uso_maximo) VALUES(?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getCodigo());
            ps.setInt(2, dto.getDescuentoPct());
            ps.setTimestamp(3, dto.getFechaExpiracion());
            ps.setInt(4, dto.getUsoMaximo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()){
                codigo = rs.getInt(1);
            }
            fu.MostrarAlertas("Información del sistema", "Cupón registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar el cupón");
            return 0;
        }
    }

    public int ActualizarCupon(CuponDTO dto){
        try{
            String sql = "UPDATE cupones SET codigo=?, descuento_pct=?, fecha_expiracion=?, uso_maximo=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setString(1, dto.getCodigo());
            ps.setInt(2, dto.getDescuentoPct());
            ps.setTimestamp(3, dto.getFechaExpiracion());
            ps.setInt(4, dto.getUsoMaximo());
            ps.setInt(5, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Cupón no encontrado");
            }else{
                fu.MostrarAlertas("Información del sistema", "Cupón actualizado. Registros modificados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar el cupón");
            return 0;
        }
    }

    public int EliminarCupon(int id){
        try{
            String sql = "DELETE FROM cupones WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Cupón no encontrado");
            }else{
                fu.MostrarAlertas("Información del sistema", "Cupón eliminado. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar el cupón");
            return 0;
        }
    }
}
