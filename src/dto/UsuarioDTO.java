/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author admin
 */

import java.sql.Timestamp;


public class UsuarioDTO {
    // Se define un atributo privado por cada campo de la tabla
    // Debe escoger un tipo de dato equivalente al tipo de dato de la tabla
    private int idUsuario;
    private String Nombre;
    private String Apellido;
    private String Login;
    private String Password;
    private String Email;
    private boolean Inactivo;
    private String Crea;
    private Timestamp FechaCrea;
    private String Modifica;
    private Timestamp FechaModifica;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public boolean isInactivo() {
        return Inactivo;
    }

    public void setInactivo(boolean Inactivo) {
        this.Inactivo = Inactivo;
    }

    public String getCrea() {
        return Crea;
    }

    public void setCrea(String Crea) {
        this.Crea = Crea;
    }

    public Timestamp getFechaCrea() {
        return FechaCrea;
    }

    public void setFechaCrea(Timestamp FechaCrea) {
        this.FechaCrea = FechaCrea;
    }

    public String getModifica() {
        return Modifica;
    }

    public void setModifica(String Modifica) {
        this.Modifica = Modifica;
    }

    public Timestamp getFechaModifica() {
        return FechaModifica;
    }

    public void setFechaModifica(Timestamp FechaModifica) {
        this.FechaModifica = FechaModifica;
    }

}
