package proyectobd.Utilidades;

/**
 * Utilidades para generar valores de Login y Email a partir del nombre y apellido de un usuario.
 */
public class GeneradorCredenciales {
    /** Dominio por defecto usado al generar direcciones de correo */
    private static final String DOMINIO_EMAIL = "@mail.com";

    /**
     * Genera un login utilizando la primera letra del nombre y el apellido completo.
     * Se remueven los espacios y se pasa a minúsculas.
     *
     * @param nombre   Nombre del usuario
     * @param apellido Apellido del usuario
     * @return login sugerido o cadena vacía si no es posible generarlo
     */
    public static String generarLogin(String nombre, String apellido) {
        if (nombre == null || apellido == null) {
            return "";
        }
        nombre = nombre.trim();
        apellido = apellido.trim();
        if (nombre.isEmpty() || apellido.isEmpty()) {
            return "";
        }
        String login = (nombre.substring(0, 1) + apellido).toLowerCase();
        login = login.replaceAll("\\s+", " ").replace(" ", "");
        return login;
    }

    /**
     * Genera un email a partir del nombre y apellido utilizando {@link #generarLogin}.
     *
     * @param nombre   Nombre del usuario
     * @param apellido Apellido del usuario
     * @return email sugerido o cadena vacía si no es posible generarlo
     */
    public static String generarEmail(String nombre, String apellido) {
        String login = generarLogin(nombre, apellido);
        if (login.isEmpty()) {
            return "";
        }
        return login + DOMINIO_EMAIL;
    }
}
