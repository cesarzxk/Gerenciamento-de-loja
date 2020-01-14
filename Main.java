package loja;
import java.util.Scanner; 
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;


public class Main {
	Scanner entrar = new Scanner(System.in);
	
	private static void Sair(JPanel janela) {
		JButton Sair = new JButton("Sair");
		ActionListener sair = new ActionListener(){public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}};
		Sair.addActionListener(sair);
		janela.add(Sair);
		}
	
	private static void Adicionar(JPanel janela, DefaultListModel lista, Principal loja, JTextField pdt, JTextField qtd, JTextField val) {
		JButton adicionar = new JButton("Adicionar");
		ActionListener Add = new ActionListener(){public void actionPerformed(ActionEvent e) {
			loja.adicionar(new Produto(pdt.getText(),Float.parseFloat(val.getText()),Integer.parseInt(qtd.getText())));
			lista.addElement("Produto: "+pdt.getText() + "     Quantidade: " + qtd.getText()+ "     Valor: R$"+ val.getText());
			
		}};
		adicionar.addActionListener(Add);
		janela.add(adicionar);
		}

	private static void tb_adicionar(JTabbedPane painel, Principal loja) {
		ImageIcon carrinho = new ImageIcon("C:\\Users\\César Vargas\\eclipse-workspace\\loja\\src\\loja\\carrinho.png");
		
		
        JPanel main = new JPanel();
		JPanel caixa = new JPanel();
		JPanel caixa2 = new JPanel();
		JPanel caixa3 = new JPanel();
		JPanel caixa4 = new JPanel();
		
		main.setLayout(new BorderLayout());
		DefaultListModel model = new DefaultListModel();
		JList lista = new JList(model);
		lista.setOpaque(false);
		
		caixa2.setBackground(new Color(250,250,250));
		caixa.setBackground(new Color(150,150,150));
		caixa3.setBackground(new Color(200,200,200));
		
		
		JTextField produto = new JTextField("Nome do produto");
		JTextField quantidade = new JTextField("Quantidade");
		JTextField valor = new JTextField("Valor");
		
		produto.setPreferredSize(new Dimension(500,20));
		valor.setPreferredSize(new Dimension(70,20));
		quantidade.setPreferredSize(new Dimension(70,20));
		
	
		
		caixa2.setLayout(new BorderLayout());
		caixa2.add(lista,BorderLayout.CENTER);
		
		main.add(caixa2,BorderLayout.CENTER);
		
		caixa3.add(produto);
		caixa3.add(quantidade);
		caixa3.add(valor);
		
		
		caixa.setLayout(new BorderLayout());
		caixa.add(caixa3,BorderLayout.NORTH);
		Sair(caixa4);
		Adicionar(caixa4, model, loja, produto, quantidade, valor);
		caixa.add(caixa4);
		
		main.add(caixa,BorderLayout.SOUTH);
		
		painel.add("Adicionar produto",main);
		
		
	}
	private static void inicio(JTabbedPane main) {
		JLabel txt = new JLabel("Bem-vindo ao sistema de gerenciamento da sua loja virtual! ");
		JPanel bvd = new JPanel();
		bvd.add(txt);
		main.add(" Inicio ",bvd);
		
	}
	
	
	private static void criar_loja(Principal loja) {
		JTabbedPane painel = new JTabbedPane();
		JFrame janela = new JFrame("Cadastrar produtos");
		
		inicio(painel);
		tb_adicionar(painel,loja);
		
		janela.add(painel);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.pack();
		janela.setSize(800, 400);
		janela.setVisible(true);
		
	}
	
	public static void main(String[]args) {
		Principal loja = new Principal("Casa mata", 100000);
		criar_loja(loja);
		loja.adicionar(new Produto("banana",1.50f,100));
		loja.adicionar(new Produto("laranja",2.50f,200));
		loja.adicionar(new Produto("abacaxi",1.50f,50));
		loja.adicionar(new Produto("leite",1.71f,75));
		loja.adicionar(new Produto("queijo",3.00f,50));
		loja.calcula_valor();
		loja.venda("banana", 20);
		loja.venda("leite", 20);
		System.out.println(loja.check_stock());
	}
}
