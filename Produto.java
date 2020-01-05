package loja;

public class Produto {
	String nome;
	float valor;
	int quantidade;
	
	public Produto(String n, float v, int qtd) {
		nome = n;
		valor = v;
		quantidade = qtd;
	}
	
	public float get_valor() {
		return valor;
	}
	
	public int get_qtd() {
		return quantidade;
	}
	
	public String get_nome() {
		return	nome;
	}
	
	public void set_valor(int v) {
		valor = v;
	}
	
	public void set_qtd(int qtd) {
		quantidade = qtd;
	}
}
