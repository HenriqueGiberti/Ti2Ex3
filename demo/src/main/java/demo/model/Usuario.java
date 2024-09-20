package demo.model;

public class Usuario {
	private int id;
	private String login;
	private String senha;
	private int idade;
	
	public Usuario() {
		this.id = -1;
		this.login = "";
		this.senha = "";
		this.idade = 0;
	}
	
	public Usuario(int id, String login, String senha, int idade) {
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.idade = idade;
	}

	public int getCodigo() {
		return id;
	}

	public void setCodigo(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", senha=" + senha + ", idade=" + idade + "]";
	}	
}
