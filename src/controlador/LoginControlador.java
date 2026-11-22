package controlador;

import modelo.estructuras.AVL;
import modelo.estructuras.Cola;
import bd.Permiso;
import bd.Rol;
import bd.Usuario;
import bd.UsuariosBD;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Scanner;

public class LoginControlador {
    private AVL<Usuario> arbolUsuarios;
    private Cola<Long> intentosFallidos;
    private UsuariosBD usuariosBD;
    private Usuario usuarioActual;


    public void iniciar(){
        //Inicialización de atributos
        this.usuariosBD = new UsuariosBD();
        this.intentosFallidos = new Cola<>();

        //Scanner para las lecturas
        Scanner entrada = new Scanner(System.in);
        String escritura;

        System.out.println("Bienvenido al sistema de login :)");
        arbolUsuarios = usuariosBD.cargarUsuariosEnAVL();

        if(arbolUsuarios.estaVacio()){ //no hay usuarios guardados
            System.out.println("Detectamos que es su primera vez usando este sistema, agregaremos al primer administrador.");
            int id =0;
            String usuario="";
            String password="";


            boolean verificacionID = true;
            while(verificacionID){
                System.out.println("Escriba su ID (numérico):");
                escritura = entrada.nextLine();
                try {
                    id = Integer.parseInt(escritura);
                } catch (NumberFormatException e) {
                    System.out.println("Error, ingrese un ID numérico válido.");
                    continue;
                }
                System.out.println("Es correcto su ID "+id+" ?");
                System.out.println("1.Sí");
                System.out.println("2.No");
                escritura = entrada.nextLine();
                try {
                    if(Integer.parseInt(escritura)==1){
                        verificacionID =false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("No ingresó un número válido.");
                }
                
            }

            boolean verificacionContrasegna = true;
            while(verificacionContrasegna){
                System.out.println("Digite su contraseña: ");
                escritura = entrada.nextLine();
                password = escritura;
                System.out.println("Vuelva a digitar su contraseña: ");
                escritura = entrada.nextLine();
                if(password.equals(escritura)){
                    System.out.println("Contraseña ingresada con éxito!!! ");
                    verificacionContrasegna =false;
                }else{
                    System.out.println("Las contraseñas no coinciden, vuelva a ingresar su contraseña.");
                }
            }

            boolean verificacionUsuario = true;
            while(verificacionUsuario){
                System.out.println("Digite su nombre de usuario:");
                escritura = entrada.nextLine();
                usuario = escritura;
                System.out.println("Es correcto su usuario: "+usuario+" ?");
                System.out.println("1.Sí");
                System.out.println("2.No");
                escritura = entrada.nextLine();
                try {
                    if(Integer.parseInt(escritura)==1){
                        verificacionUsuario =false;
                        
                    }
                } catch (NumberFormatException e) {
                    System.out.println("No ingresó un número válido.");
                }
            }

            int idPermiso = usuariosBD.guardarPermiso("consola");
            int idRol = usuariosBD.guardarRol("Administrador");
            usuariosBD.asignarPermisoARol(idRol, idPermiso);

            Rol rolAdmin = new Rol(idRol, "Administrador");
            rolAdmin.agregarPermiso(new Permiso(idPermiso, "consola"));

            usuarioActual = new Usuario(id, usuario, generarHash(password), rolAdmin);
            if (usuariosBD.guardarUsuario(usuarioActual)) {
                arbolUsuarios.insertar(usuarioActual);
            } else {
                System.out.println("Error: no se pudo registrar el administrador en la base de datos.");
            }
            
        }else{ //Ya hay usuarios guardados!
            Usuario usuarioSupuesto = null;

            boolean verificacionID = true;
            while(verificacionID){
                System.out.println("Ingrese su ID (numérico): "); //ID podría ser el numero de documento
                escritura = entrada.nextLine();
                try {
                    int idBuscado = Integer.parseInt(escritura);
                    usuarioSupuesto = arbolUsuarios.buscar(new Usuario(idBuscado, null, null, null));

                    if (usuarioSupuesto == null) {
                        System.out.println("ID no encontrado");
                    } else {
                        verificacionID = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error, ingrese un ID numérico válido.");
                }
            }

            boolean verificacionPassword=true;
            while(verificacionPassword==true){
                System.out.println("Ingrese su contraseña: ");
                escritura = entrada.nextLine();
                if(usuarioSupuesto.getPasswordHash().equals(generarHash(escritura))){
                    verificacionPassword=false;
                }else{
                    System.out.println("Eror, contraseña incorrecta");
                }
            }
            
            usuarioActual = usuarioSupuesto;
        }

    
    }

    public boolean autenticar(String username, String password){
        return false;
    }

    public boolean estaBloqueado(String username){
        return false;
    }

    public Usuario getUsuarioActual(){
        return usuarioActual;
    }

    public static String generarHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash SHA-256", e);
        }
    }

}
