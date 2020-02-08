package loja;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class Main {
	private static Connection conexao = null;
	private static Statement status = null;
	private static ResultSet resultado = null;
	private static String [] dados;
	
	private static String driver = "com.mysql.cj.jdbc.Driver";
	
	private static DefaultListModel model = new DefaultListModel();
	private static JList lista = new JList(model);
	
    public static String[] leitura() throws IOException {
    	String[] dados = new String [3];
    	
    	FileInputStream stream = new FileInputStream("dados.txt");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String linha = br.readLine();
        int i = 0;
        while(linha != null) {
            dados[i] = linha;
            i++;
            linha = br.readLine();
            
     }
         if (dados[2] == null) {dados[2]="";};
         return dados;
    }
	
//-------------------------------------------------------------------------------Banco_de_dados---------------------------------------------------------------------------//	

	private static void conectar_BD(String user, String pass, String serv) throws IOException {
		dados = leitura();
		
		try {
			if (user == "") {
			Class.forName(driver);
			conexao = DriverManager.getConnection(dados[0],dados[1],dados[2]);
			status = conexao.createStatement();
			
			System.out.println("Conectado com sucesso ao banco de dados!");
			}else {
				Class.forName(driver);
				conexao = DriverManager.getConnection(serv,user,pass);
				status = conexao.createStatement();
			}
		
		} catch(Exception e) {
			System.out.println("Erro de conexão!" + e.getMessage());
			}
	}
	
	private static void inserir_BD(String nome, int qtd, float valor) throws SQLException {
			try{
				String sql = "INSERT INTO produto (nome,quantidade,valor) VALUES ('"+nome.trim().toLowerCase()+"','"+qtd+"','"+valor+"')";
			    PreparedStatement ps = conexao.prepareStatement(sql);
			    ps.execute();
			
			}catch(SQLException e) {
				
				String sql = "UPDATE produto SET quantidade = ?, valor = ? WHERE nome ='"+nome.trim().toLowerCase()+"'";
				PreparedStatement ps = conexao.prepareStatement(sql);
				ps.setInt(1,qtd);
				ps.setFloat(2,valor);
				ps.executeUpdate();
				
			}
	}
	
	private static void carregar_BD(DefaultListModel lista) {
		try {
        
		String sql = "SELECT nome, quantidade, valor FROM produto";
		PreparedStatement ps = conexao.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			
			String Nome = rs.getString("nome");
		    int Qtd = rs.getInt("quantidade");
		    float Valor = rs.getFloat("valor");
		   
		    lista.addElement("Produto: "+Nome+ "     Quantidade: " + Qtd+ "     Valor: R$ "+ Valor);
		}
		}catch (SQLException e) {
			System.out.println("Carregamento do BD não realizado!");
		}
	}
	

//-------------------------------------------------------------------------------Janela__Inicio----------------------------------------------------------------------------//
	private static void conect_button (JPanel janela, JLabel info, JTextField usuario, JTextField senha, JTextField servidor) {
		JButton conectar_bt = new JButton("Conectar");
		ActionListener conectar_al = new ActionListener(){public void actionPerformed(ActionEvent e) {
			System.out.println(teste_server());
			try {
				conectar_BD(usuario.getText(), senha.getText(), servidor.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			info.setText("   Status: "+teste_server());;
			if (status != null) {
				carregar_BD(model);
			}
			
		}};
		conectar_bt.addActionListener(conectar_al);
		janela.add(conectar_bt);
		
	}

	private static String teste_server() {
		if (status != null){
			return "Conectado";
		} else {
			return "Desconectado";
		}
	}
	
	private static void inicio(JTabbedPane main) {
		JLabel txt = new JLabel("Bem-vindo ao sistema de gerenciamento da sua loja virtual! ");
		
		JLabel info1 = new JLabel("Usuário: ");
		JLabel info2 = new JLabel("Senha: ");
		JLabel info3 = new JLabel("   Servidor: ");
		JLabel info4 = new JLabel("   Status: "+ teste_server());
		
		JTextField usuario = new JTextField(dados[1]);
		JTextField senha = new JTextField(dados[2]);
		JTextField servidor = new JTextField(dados[0]);
		
		usuario.setPreferredSize(new Dimension(100,20));
		senha.setPreferredSize(new Dimension(100,20));
		servidor.setPreferredSize(new Dimension(100,20));
		
		
		JPanel bvd = new JPanel(new BorderLayout());
		JPanel info = new JPanel();
		JPanel msg = new JPanel();
		
	    bvd.add(msg,BorderLayout.NORTH);
		bvd.add(info,BorderLayout.SOUTH);
		
		msg.add(txt);
		info.add(info1);
		info.add(usuario);
		info.add(info2);
		info.add(senha);
		info.add(info3);
		info.add(servidor);
		info.add(info4);
		conect_button(info, info4, usuario, senha, servidor);
		
		
		main.add(" Inicio ",bvd);
		
	}

//------------------------------------------------------------------------------Janela_Adicionar---------------------------------------------------------------------------//	
	private static void Sair_button(JPanel janela) {
		JButton Sair = new JButton("Sair");
		ActionListener sair = new ActionListener(){public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}};
		Sair.addActionListener(sair);
		janela.add(Sair);
		}
	
	private static void Adicionar_button(JPanel janela, DefaultListModel lista, Principal loja, JTextField pdt, JTextField qtd, JTextField val) {
		JButton adicionar = new JButton("Adicionar");
		ActionListener Add = new ActionListener(){public void actionPerformed(ActionEvent e) {
			boolean a = false;
			for (int i=0;i<lista.getSize(); i++) {
				String array[] = lista.get(i).toString().split(" ");
			
				System.out.println(array[1].intern() == pdt.getText().intern());
				
				if (array[1].intern() == pdt.getText().intern()) {
				    a = true;
	
				}
				
				}
			if (a == false){
				loja.adicionar(new Produto(pdt.getText(),Float.parseFloat(val.getText()),Integer.parseInt(qtd.getText())));
			    lista.addElement("Produto: "+pdt.getText() + "     Quantidade: " + qtd.getText()+ "     Valor: R$ "+ val.getText());
			
			} else {
				ImageIcon png = new ImageIcon("C://Users//César Vargas//eclipse-workspace//loja//src//loja//batman.png");
				int input = JOptionPane.showConfirmDialog(null, "Gostaria de chamar o batman?","Be honest...",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,png);
				if (input == 0) {
					for (int i=0;i<lista.getSize(); i++) {
						String array[] = lista.get(i).toString().split(" ");
					
						System.out.println(array[1].intern() == pdt.getText().intern());
						
						if (array[1].intern() == pdt.getText().intern()) {
						    lista.setElementAt( "Produto: "+pdt.getText() + "     Quantidade: " + qtd.getText()+ "     Valor: R$ "+ val.getText(), i);
			
						}
					    
					}
				}
			}
		}
				
        };
		adicionar.addActionListener(Add);
		janela.add(adicionar);
		}
	
	private static void Atualizar_button(JPanel janela, DefaultListModel lista, Principal loja, JTextField pdt, JTextField qtd, JTextField val) {
		JButton Atualizar = new JButton("Atualizar");
		ActionListener sair = new ActionListener(){public void actionPerformed(ActionEvent e) {
			for (int i=0;i<lista.getSize(); i++) {
				String array[] = lista.get(i).toString().split(" ");
			
				System.out.println(array[1].intern() == pdt.getText().intern());
				
				if (array[1].intern() == pdt.getText().intern()) {
				    lista.setElementAt( "Produto: "+pdt.getText() + "     Quantidade: " + qtd.getText()+ "     Valor: R$ "+ val.getText(), i);
	
				}
			    
			}
		}};
		Atualizar.addActionListener(sair);
		janela.add(Atualizar);
	}
	
	private static void tb_adicionar(JTabbedPane painel, Principal loja) {
        JPanel main = new JPanel();
		JPanel caixa = new JPanel();
		JPanel caixa2 = new JPanel();
		JPanel caixa3 = new JPanel();
		JPanel caixa4 = new JPanel();
		
		main.setLayout(new BorderLayout());
		
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
		Sair_button(caixa4);
		Adicionar_button(caixa4, model, loja, produto, quantidade, valor);
		Atualizar_button(caixa4, model, loja, produto, quantidade, valor);
		try {
		carregar_BD(model);
		}catch (Exception e) {
			System.out.println("");
		}
		
		caixa.add(caixa4);
		
		
		main.add(caixa,BorderLayout.SOUTH);
		
		painel.add("Adicionar produto",main);
		
		
	}

	private static void criar_loja(Principal loja) throws SQLException {
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
	
//------------------------------------------------------------------------------------Main---------------------------------------------------------------------------------//
	public static void main(String[]args) throws IOException {
		Principal loja = new Principal("Casa mata", 1000000);
		try {
			conectar_BD("","","");
			criar_loja(loja);
			}catch (SQLException e) {
				System.out.println("Não conectado ao banco de dados!");
			}

		loja.adicionar(new Produto("banana",1.50f,100));
		loja.adicionar(new Produto("laranja",2.50f,200));
		loja.adicionar(new Produto("abacaxi",1.50f,50));
		loja.adicionar(new Produto("leite",1.71f,75));
		loja.adicionar(new Produto("queijo",3.00f,50));
	}


}
