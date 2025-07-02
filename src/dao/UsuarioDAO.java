/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import dto.UsuarioDTO;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackRol;




/**
 *
 * @author admin
 */
public class UsuarioDAO {

    FeedbackRol fu = new FeedbackRol();
    
    // Método para obtener la lista de usuarios de la BD
    // Devolverá un array de UsuarioDTO
  
    public ObservableList<UsuarioDTO> ListarUsuarios(){
        ObservableList<UsuarioDTO> usuarios=FXCollections.observableArrayList();
        ConectorBD ConnBD=new ConectorBD();
        ResultSet rs=null;
        String sql="SELECT u.id, u.rol_id, r.nombre AS rol_nombre, u.nombre, u.email, u.contraseña, u.imagen_perfil, u.creado_en "
                + "FROM usuarios u JOIN roles r ON u.rol_id=r.id ORDER BY u.nombre";
        try{
            Statement st=ConnBD.AbrirConexionBD().createStatement();
            rs=st.executeQuery(sql);
            while(rs.next()){
                UsuarioDTO usrdto=new UsuarioDTO();
                usrdto.setId(rs.getInt("id"));
                usrdto.setRolId(rs.getInt("rol_id"));
                usrdto.setRolNombre(rs.getString("rol_nombre"));
                usrdto.setNombre(rs.getString("nombre"));
                usrdto.setEmail(rs.getString("email"));
                usrdto.setContrasena(rs.getString("contraseña"));
                usrdto.setImagenPerfil(rs.getString("imagen_perfil"));
                usrdto.setCreadoEn(rs.getTimestamp("creado_en"));
                usuarios.add(usrdto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar usuarios");
        }
        return usuarios;
    }

 

    public ObservableList<UsuarioDTO> BuscarUsuarios(String CriterioBusqueda){
        ObservableList<UsuarioDTO> usuarios=FXCollections.observableArrayList();
        ConectorBD ConnBD=new ConectorBD();
        ResultSet rs=null;
        String sql="SELECT u.id, u.rol_id, r.nombre AS rol_nombre, u.nombre, u.email, u.contraseña, u.imagen_perfil, u.creado_en "
                + "FROM usuarios u JOIN roles r ON u.rol_id=r.id "
                + "WHERE u.nombre LIKE '%"+CriterioBusqueda+"%' OR u.email LIKE '%"+CriterioBusqueda+"%' "
                + "ORDER BY u.nombre";
        try{
            Statement st=ConnBD.AbrirConexionBD().createStatement();
            rs=st.executeQuery(sql);
            while(rs.next()){
                UsuarioDTO usrdto=new UsuarioDTO();
                usrdto.setId(rs.getInt("id"));
                usrdto.setRolId(rs.getInt("rol_id"));
                usrdto.setRolNombre(rs.getString("rol_nombre"));
                usrdto.setNombre(rs.getString("nombre"));
                usrdto.setEmail(rs.getString("email"));
                usrdto.setContrasena(rs.getString("contraseña"));
                usrdto.setImagenPerfil(rs.getString("imagen_perfil"));
                usrdto.setCreadoEn(rs.getTimestamp("creado_en"));
                usuarios.add(usrdto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar usuarios");
        }
        return usuarios;
    }

    public int InsertarUsuario(UsuarioDTO usrdto){
        try{
            String sql="INSERT INTO usuarios(rol_id, nombre, email, contraseña, imagen_perfil) VALUES(?,?,?,?,?)";
            ConectorBD ConnBD=new ConectorBD();
            PreparedStatement ps=ConnBD.AbrirConexionBD().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, usrdto.getRolId());
            ps.setString(2, usrdto.getNombre());
            ps.setString(3, usrdto.getEmail());
            ps.setString(4, usrdto.getContrasena());
            ps.setString(5, usrdto.getImagenPerfil());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            int codigoInsertado=0;
            if(rs.next()){
                codigoInsertado=rs.getInt(1);
            }
            fu.MostrarAlertas("Información", "Usuario registrado con ID: "+codigoInsertado);
            ConnBD.CerrarConexionBD();
            return codigoInsertado;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar el usuario");
            return 0;
        }
    }

    public int ActualizarUsuario(UsuarioDTO usrdto){
        try{
            String sql="UPDATE usuarios SET rol_id=?, nombre=?, email=?, contraseña=?, imagen_perfil=? WHERE id=?";
            ConectorBD ConnBD=new ConectorBD();
            PreparedStatement ps=ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, usrdto.getRolId());
            ps.setString(2, usrdto.getNombre());
            ps.setString(3, usrdto.getEmail());
            ps.setString(4, usrdto.getContrasena());
            ps.setString(5, usrdto.getImagenPerfil());
            ps.setInt(6, usrdto.getId());
            int registrosActualizados=ps.executeUpdate();
            fu.MostrarAlertas("Información", "Usuario actualizado. Registros modificados: "+registrosActualizados);
            ConnBD.CerrarConexionBD();
            return registrosActualizados;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar el usuario");
            return 0;
        }
    }

    public int EliminarUsuario(int id){
        try{
            String sql = "DELETE FROM usuarios WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información", "Usuario eliminado. Registros afectados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar el usuario");
            return 0;
        }
    }
}
