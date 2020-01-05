package loja;
import java.util.Scanner; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Main {
	Scanner entrar = new Scanner(System.in);
	
	private static void Sair(JPanel janela) {
		JButton Sair = new JButton("Sair");
		ActionListener sair = new ActionListener() {public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}};
		Sair.addActionListener(sair);
		janela.add(Sair);
		}

	
	
	public static void criar_add() {
		JFrame janela = new JFrame("Cadastrar produtos");
		JPanel caixa = new JPanel();
		JButton adicionar = new JButton("Adicionar");
		caixa.add(adicionar);
		Sair(caixa);
		janela.add(caixa);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.pack();
		janela.setSize(500, 300);
		janela.setVisible(true);
		
	}
	
	public static void main(String[]args) {
		criar_add();
		Principal loja = new Principal("Casa mata", 100000);
		loja.adicionar(new Produto("banana",1.50f,100));
		loja.adicionar(new Produto("laranja",2.50f,200));
		loja.adicionar(new Produto("abacaxi",1.50f,50));
		loja.adicionar(new Produto("leite",1.71f,75));
		loja.adicionar(new Produto("queijo",3.00f,50));
		loja.calcula_valor();
		loja.venda("banana", 20);
		loja.venda("leite", 20);
		loja.check_stock();
	}
}
