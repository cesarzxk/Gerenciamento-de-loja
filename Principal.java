package loja;

public class Principal {
	float valor;
	String nome;
	Produto [] lista = new Produto[1000];
	int contador; 
	
	public void calcula_valor() {
		for (int i = 0; i < contador; i++) {
			valor = valor + lista[i].get_qtd()*lista[i].get_valor();
		}
		System.out.println("Valor de venda da loja: R$"+ valor);
	}
	
	public Principal(String n, float v) {
		valor = v;
		nome = n;
		contador = 0;
	}
	public void adicionar(Produto p) {
		lista[contador] = p;
		contador++;
	}
	
	public void check_stock() {
		System.out.println(" Nome "+ " Quantidade "+ " Valor ");
		for (int i = 0; i < contador; i++) {
			System.out.println(lista[i].get_nome() +"     "+ lista[i].get_qtd() +"     "+ lista[i].get_valor());
		}
	}
	
	public void venda(String item, int qtd) {
		boolean achou = false;
		for (int i = 0; i < contador; i++) {
			if (lista[i].get_nome() == item) {
				lista[i].set_qtd(lista[i].get_qtd() - qtd);
				System.out.println("Vendido "+ qtd +" unidades de " + item + ", restando "+lista[i].get_qtd()+".");
				achou = true;
				break;
			}
		}
		if (achou = false){
		       System.out.println("Produto não encontrado!");
		}
	}
	

	
}
