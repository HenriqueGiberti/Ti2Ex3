package demo.service;

import java.util.Scanner;
import java.io.File;
import java.util.List;
import demo.dao.UsuarioDAO;
import demo.model.Usuario;
import spark.Request;
import spark.Response;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_LOGIN = 2;
    private final int FORM_ORDERBY_IDADE = 3;

    public UsuarioService() {
        makeForm();
    }

    public void makeForm() {
        makeForm(FORM_INSERT, new Usuario(), FORM_ORDERBY_LOGIN);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Usuario(), orderBy);
    }

    public void makeForm(int tipo, Usuario usuario, int orderBy) {
        String nomeArquivo = "form.html";
        form = "";
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String umUsuario = "";
        if (tipo != FORM_INSERT) {
            umUsuario += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/usuario/list/1\">Novo Usuário</a></b></font></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t</table>";
            umUsuario += "\t<br>";
        }

        if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/usuario/";
            String name, buttonLabel;
            if (tipo == FORM_INSERT) {
                action += "insert";
                name = "Inserir Usuário";
                buttonLabel = "Inserir";
            } else {
                action += "update/" + usuario.getCodigo();
                name = "Atualizar Usuário (ID " + usuario.getCodigo() + ")";
                buttonLabel = "Atualizar";
            }
            umUsuario += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umUsuario += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td colspan=\"2\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td>&nbsp;Login: <input class=\"input--register\" type=\"text\" name=\"login\" value=\"" + usuario.getLogin() + "\"></td>";
            umUsuario += "\t\t\t<td>Idade: <input class=\"input--register\" type=\"text\" name=\"idade\" value=\"" + usuario.getIdade() + "\"></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td>&nbsp;Senha: <input class=\"input--register\" type=\"password\" name=\"senha\" value=\"\"></td>";
            umUsuario += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\"" + buttonLabel + "\" class=\"input--main__style input--button\"></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t</table>";
            umUsuario += "\t</form>";
        } else if (tipo == FORM_DETAIL) {
            umUsuario += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td colspan=\"2\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Usuário (ID " + usuario.getCodigo() + ")</b></font></td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t\t<tr>";
            umUsuario += "\t\t\t<td>&nbsp;Login: " + usuario.getLogin() + "</td>";
            umUsuario += "\t\t\t<td>Idade: " + usuario.getIdade() + "</td>";
            umUsuario += "\t\t</tr>";
            umUsuario += "\t</table>";
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-USUARIO>", umUsuario);

        String list = "<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">";
        list += "\n<tr><td colspan=\"4\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Usuários</b></font></td></tr>\n" +
                "\n<tr><td colspan=\"4\">&nbsp;</td></tr>\n" +
                "\n<tr>\n" +
                "\t<td><a href=\"/usuario/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
                "\t<td><a href=\"/usuario/list/" + FORM_ORDERBY_LOGIN + "\"><b>Login</b></a></td>\n" +
                "\t<td><a href=\"/usuario/list/" + FORM_ORDERBY_IDADE + "\"><b>Idade</b></a></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
                "</tr>\n";

        List<Usuario> usuarios;
        if (orderBy == FORM_ORDERBY_ID) {
            usuarios = usuarioDAO.getOrderByCodigo();
        } else if (orderBy == FORM_ORDERBY_LOGIN) {
            usuarios = usuarioDAO.getOrderByLogin();
        } else if (orderBy == FORM_ORDERBY_IDADE) {
            usuarios = usuarioDAO.getOrderByIdade();
        } else {
            usuarios = usuarioDAO.get();
        }

        int i = 0;
        String bgcolor = "";
        for (Usuario u : usuarios) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\"" + bgcolor + "\">\n" +
                    "\t<td>" + u.getCodigo() + "</td>\n" +
                    "\t<td>" + u.getLogin() + "</td>\n" +
                    "\t<td>" + u.getIdade() + "</td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/usuario/" + u.getCodigo() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/usuario/update/" + u.getCodigo() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteUsuario('" + u.getCodigo() + "', '" + u.getLogin() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "</tr>\n";
        }
        list += "</table>";
        form = form.replaceFirst("<LISTAR-USUARIO>", list);
    }

    public Object insert(Request request, Response response) {
        String login = request.queryParams("login");
        String senha = request.queryParams("senha");
        int idade = Integer.parseInt(request.queryParams("idade"));

        String resp = "";
        Usuario usuario = new Usuario(-1, login, senha, idade);

        if (usuarioDAO.insert(usuario)) {
            resp = "Usuário (" + login + ") inserido!";
            response.status(201); // 201 Created
        } else {
            resp = "Usuário (" + login + ") não inserido!";
            response.status(404); // 404 Not found
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Usuario usuario = usuarioDAO.get(id);

        if (usuario != null) {
            response.status(200); // success
            makeForm(FORM_DETAIL, usuario, FORM_ORDERBY_LOGIN);
        } else {
            response.status(404); // 404 Not found
            String resp = "Usuário " + id + " não encontrado.";
            makeForm();
            return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }
        return form;
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        String login = request.queryParams("login");
        String senha = request.queryParams("senha");
        int idade = Integer.parseInt(request.queryParams("idade"));

        Usuario usuario = new Usuario(id, login, senha, idade);
        String resp = "";

        if (usuarioDAO.update(usuario)) {
            resp = "Usuário (" + login + ") atualizado!";
            response.status(200); // success
        } else {
            resp = "Usuário (" + login + ") não atualizado!";
            response.status(404); // 404 Not found
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        String resp = "";

        if (usuarioDAO.delete(id)) {
            resp = "Usuário (" + id + ") excluído!";
            response.status(200); // success
        } else {
            resp = "Usuário (" + id + ") não excluído!";
            response.status(404); // 404 Not found
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object list(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderBy"));
        makeForm(orderBy);
        response.status(200); // success
        return form;
    }
}
