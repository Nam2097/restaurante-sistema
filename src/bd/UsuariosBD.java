package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelo.estructuras.AVL;

public class UsuariosBD {

    public AVL<Usuario> cargarUsuariosEnAVL() {
        AVL<Usuario> usuarios = new AVL<>();
        String sql = """
            SELECT u.id, u.username, u.password_hash, r.id AS rol_id, r.nombre_rol
            FROM usuarios u
            LEFT JOIN roles r ON u.rol_id = r.id
            """;

        try {
            Connection conn = ConexionBD.obtenerConexion();
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Rol rol = new Rol(rs.getInt("rol_id"), rs.getString("nombre_rol"));
                    Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rol
                    );
                    usuarios.insertar(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar usuarios en el AVL: " + e.getMessage());
        }

        return usuarios;
    }

    public boolean guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password_hash, rol_id) VALUES (?, ?, ?)";

        try {
            Connection conn = ConexionBD.obtenerConexion();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, usuario.getUsername());
                pstmt.setString(2, usuario.getPasswordHash());
                pstmt.setInt(3, usuario.getRol().getId());

                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Rol> obtenerRoles() {
        ArrayList<Rol> roles = new ArrayList<>();
        String sql = """
            SELECT r.id AS rol_id, r.nombre_rol, p.id AS permiso_id, p.nombre AS nombre_permiso
            FROM roles r
            LEFT JOIN rol_permisos rp ON r.id = rp.rol_id
            LEFT JOIN permisos p ON rp.permiso_id = p.id
            ORDER BY r.id
            """;

        try {
            Connection conn = ConexionBD.obtenerConexion();
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int rolId = rs.getInt("rol_id");
                    Rol rolEncontrado = null;

                    // Buscamos si el rol ya fue agregado a la lista
                    for (Rol r : roles) {
                        if (r.getId() == rolId) {
                            rolEncontrado = r;
                            break;
                        }
                    }

                    // Si no existe en la lista, lo creamos y lo agregamos
                    if (rolEncontrado == null) {
                        rolEncontrado = new Rol(rolId, rs.getString("nombre_rol"));
                        roles.add(rolEncontrado);
                    }

                    // Si la fila trae un permiso asociado, lo añadimos a ese rol
                    int permisoId = rs.getInt("permiso_id");
                    if (!rs.wasNull()) {
                        rolEncontrado.agregarPermiso(new Permiso(permisoId, rs.getString("nombre_permiso")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener roles y permisos: " + e.getMessage());
        }

        return roles;
    }
}