package demo.app;

import java.util.*;
import static spark.Spark.*;

import demo.dao.UsuarioDAO;
import demo.model.Usuario;

public class Aplicacao {
    
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static Random random = new Random();

    public static void main(String[] args) {
        port(6789);

        post("/usuario/insert", (request, response) -> {
            String login = request.queryParams("login");
            String senha = request.queryParams("senha");
            int idade = Integer.parseInt(request.queryParams("idade"));
            int id = random.nextInt(100)+1;
            Usuario usuario = new Usuario(id, login, senha, idade); // ID is auto-generated
            if (usuarioDAO.insert(usuario)) {
                response.status(201); // Created
                return "Usuário inserido com sucesso!";
            } else {
                response.status(500); // Internal Server Error
                return "Erro ao inserir usuário.";
            }
        });

        get("/usuario/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Usuario usuario = usuarioDAO.get(id);
            if (usuario != null) {
                response.status(200); // OK
                return usuario; // Return as JSON or any suitable format
            } else {
                response.status(404); // Not Found
                return "Usuário não encontrado.";
            }
        });

        get("/usuario/delete/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            if (usuarioDAO.delete(id)) {
                response.status(200); // OK
                return "Usuário removido com sucesso!";
            } else {
                response.status(404); // Not Found
                return "Usuário não encontrado.";
            }
        });

        get("/usuario", (request, response) -> {
            List<Usuario> usuarios = usuarioDAO.get();
            response.status(200); // OK
            return usuarios; // Return as JSON or any suitable format
        });
        
        get("/usuario/list/:orderBy", (request, response) -> {
            int orderBy = Integer.parseInt(request.params(":orderBy"));
            List<Usuario> usuarios;

            if (orderBy == 1) {
                usuarios = usuarioDAO.getOrderByCodigo();
            } else if (orderBy == 2) {
                usuarios = usuarioDAO.getOrderByLogin();
            } else if (orderBy == 3) {
                usuarios = usuarioDAO.getOrderByIdade();
            } else {
                usuarios = usuarioDAO.get();
            }

            response.status(200);
            return usuarios; // Retorna como JSON ou qualquer formato adequado
        });

    }
}
