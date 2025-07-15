package dao;

import dto.DireccionDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackVendedor; // use generic feedback

public class DireccionDAO {
    FeedbackVendedor fu = new FeedbackVendedor();

    private boolean tieneColumna(Connection conn, String tabla, String columna){
        try(ResultSet rs = conn.getMetaData().getColumns(null, null, tabla, columna)){
            return rs.next();
        }catch(Exception ex){
            return false;
        }
    }

    private boolean existeUsuario(int usuarioId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM usuarios WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar el usuario");
            return false;
        }
    }

    public ObservableList<DireccionDTO> ListarDirecciones(){
        ObservableList<DireccionDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT d.*, u.nombre AS usuario_nombre " +
                     "FROM direcciones d JOIN usuarios u ON d.usuario_id=u.id " +
                     "ORDER BY d.id";
        try{
            Connection cn = ConnBD.AbrirConexionBD();
            boolean hasCanton = tieneColumna(cn, "direcciones", "canton");
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                DireccionDTO dto = new DireccionDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setAlias(rs.getString("alias"));
                dto.setDireccion(rs.getString("direccion"));
                dto.setCiudad(rs.getString("ciudad"));
                if(hasCanton) dto.setCanton(rs.getString("canton"));
                dto.setProvincia(rs.getString("provincia"));
                dto.setCodigoPostal(rs.getString("codigo_postal"));
                dto.setTelefonoContacto(rs.getString("telefono_contacto"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar direcciones");
        }
        return lista;
    }

    public ObservableList<DireccionDTO> BuscarDirecciones(String criterio){
        ObservableList<DireccionDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT d.*, u.nombre AS usuario_nombre " +
                     "FROM direcciones d JOIN usuarios u ON d.usuario_id=u.id " +
                     "WHERE CONCAT_WS(' ', d.id, d.usuario_id, u.nombre, d.alias, d.direccion, d.ciudad, d.canton, d.provincia, d.codigo_postal, d.telefono_contacto) " +
                     "LIKE '%"+criterio+"%' ORDER BY d.id";
        try{
            Connection cn = ConnBD.AbrirConexionBD();
            boolean hasCanton = tieneColumna(cn, "direcciones", "canton");
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                DireccionDTO dto = new DireccionDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setAlias(rs.getString("alias"));
                dto.setDireccion(rs.getString("direccion"));
                dto.setCiudad(rs.getString("ciudad"));
                if(hasCanton) dto.setCanton(rs.getString("canton"));
                dto.setProvincia(rs.getString("provincia"));
                dto.setCodigoPostal(rs.getString("codigo_postal"));
                dto.setTelefonoContacto(rs.getString("telefono_contacto"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar direcciones");
        }
        return lista;
    }

    public int InsertarDireccion(DireccionDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("El usuario no existe");
                return 0;
            }
            ConectorBD ConnBD = new ConectorBD();
            Connection cn = ConnBD.AbrirConexionBD();
            boolean hasCanton = tieneColumna(cn, "direcciones", "canton");
            String sql;
            if(hasCanton){
                sql = "INSERT INTO direcciones(usuario_id, alias, direccion, ciudad, canton, provincia, codigo_postal, telefono_contacto) VALUES(?,?,?,?,?,?,?,?)";
            }else{
                sql = "INSERT INTO direcciones(usuario_id, alias, direccion, ciudad, provincia, codigo_postal, telefono_contacto) VALUES(?,?,?,?,?,?,?)";
            }
            PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getAlias());
            ps.setString(3, dto.getDireccion());
            ps.setString(4, dto.getCiudad());
            int idx = 5;
            if(hasCanton){
                ps.setString(idx++, dto.getCanton());
            }
            ps.setString(idx++, dto.getProvincia());
            ps.setString(idx++, dto.getCodigoPostal());
            ps.setString(idx++, dto.getTelefonoContacto());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Información", "Dirección registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar la dirección");
            return 0;
        }
    }

    public int ActualizarDireccion(DireccionDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("El usuario no existe");
                return 0;
            }
            ConectorBD ConnBD = new ConectorBD();
            Connection cn = ConnBD.AbrirConexionBD();
            boolean hasCanton = tieneColumna(cn, "direcciones", "canton");
            String sql;
            if(hasCanton){
                sql = "UPDATE direcciones SET usuario_id=?, alias=?, direccion=?, ciudad=?, canton=?, provincia=?, codigo_postal=?, telefono_contacto=? WHERE id=?";
            }else{
                sql = "UPDATE direcciones SET usuario_id=?, alias=?, direccion=?, ciudad=?, provincia=?, codigo_postal=?, telefono_contacto=? WHERE id=?";
            }
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getAlias());
            ps.setString(3, dto.getDireccion());
            ps.setString(4, dto.getCiudad());
            int idx = 5;
            if(hasCanton){
                ps.setString(idx++, dto.getCanton());
            }
            ps.setString(idx++, dto.getProvincia());
            ps.setString(idx++, dto.getCodigoPostal());
            ps.setString(idx++, dto.getTelefonoContacto());
            ps.setInt(idx, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Dirección no encontrada");
            }else{
                fu.MostrarAlertas("Información", "Dirección actualizada. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar la dirección");
            return 0;
        }
    }

    public int EliminarDireccion(int id){
        try{
            String sql = "DELETE FROM direcciones WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Dirección no encontrada");
            }else{
                fu.MostrarAlertas("Información", "Dirección eliminada. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar la dirección");
            return 0;
        }
    }
}
