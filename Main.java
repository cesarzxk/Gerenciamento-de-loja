package loja;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class Main {
	private static Connection conexao = null;
	private static Statement status = null;
	private static ResultSet resultado = null;
	private static String [] dados;
	private static boolean carregado =false;
	
	private static String driver = "com.mysql.cj.jdbc.Driver";
	
	private static DefaultListModel model = new DefaultListModel();
	private static JList lista = new JList(model);
	
    private static String[] leitura() throws IOException {
    	String[] dados = new String [3];
    	
    	FileInputStream stream = new FileInputStream("C:\\Users\\César Vargas\\eclipse-workspace\\Gerenciador\\src\\loja\\dados.txt");
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
			carregado = true;
			}else {
				Class.forName(driver);
				conexao = DriverManager.getConnection(serv,user,pass);
				status = conexao.createStatement();
			}
		
		} catch(Exception e) {
			System.out.println("Erro de conexão!" + e.getMessage());
			status = null;
			}
	}
	
	private static void inserir_BD(String nome, int qtd, float valor) throws SQLException {
			try{
				String sql = "INSERT INTO produto (nome,quantidade,valor) VALUES ('"+nome.trim().toLowerCase()+"','"+qtd+"','"+valor+"')";
			    PreparedStatement ps = conexao.prepareStatement(sql);
			    ps.execute();
			
			}catch(SQLException e) {
				
				String sql = "UPDATE produto SET quantidade = ?, 'valor' = ? WHERE 'nome' ='"+nome.trim().toLowerCase()+"'";
				PreparedStatement ps = conexao.prepareStatement(sql);
				ps.setInt(1,qtd);
				ps.setFloat(2,valor);
				ps.executeUpdate();
				
			}
	}
	
	private static void remover_BD(String nome) throws SQLException {
		try{
			String sql = "DELETE FROM produto WHERE nome ='"+nome+"'" ;
		    PreparedStatement ps = conexao.prepareStatement(sql);
		    ps.executeUpdate();
		    System.out.println("Removido com sucesso!");
		
		}catch(SQLException e) {
			
			System.out.println("Objeto não encontrado!");
			
		}
}
	
	private static String[] Procurar_BD(String nome) {
		try {
		String sql = "SELECT nome, quantidade, valor FROM produto WHERE nome = '"+nome+"'";
		PreparedStatement ps = conexao.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		String Nome = rs.getString("nome");
		int Qtd = rs.getInt("quantidade");
		float Valor = rs.getFloat("valor");
		   
		return (Nome+" "+Qtd+" "+Valor).split(" ");
		
		}catch (SQLException e) {
			System.out.println(e);
		}
		return null;
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
	
	private static void atualizar_BD(String nome, int qtd, float valor) throws SQLException {
		try{
			String sql = "UPDATE produto SET quantidade = ?, valor = ? WHERE nome ='"+nome.trim().toLowerCase()+"'";
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setInt(1,qtd);
			ps.setFloat(2,valor);
			ps.executeUpdate();
		
		}catch(SQLException e) {
			System.out.println("Não foi encontrado");
			
			
		}
}

//-------------------------------------------------------------------------------Janela__Venda----------------------------------------------------------------------------//
	static String x = "";
	
	private static void adicionar_vd(JLabel lista, JPanel janela,JTextField nome){
		JButton adicionar = new JButton("Adicionar");
		ActionListener add = new ActionListener(){public void actionPerformed(ActionEvent e) {
			System.out.println(nome.getText());
			String[] item = Procurar_BD(nome.getText());
			x = x +"<p> Produto: "+item[0]+" Quantidade: "+item[1]+" Valor: R$"+item[2]+" </p>";
			lista.setText("<HTML>"+ x +"</HTML>");
		}};
		adicionar.addActionListener(add);
		janela.add(adicionar);
	}
	
	private static void venda_tb(JTabbedPane main) {
		JPanel venda = new JPanel(new BorderLayout());
		JLabel texto = new JLabel();
		JTextField codigo = new JTextField();
		codigo.setPreferredSize(new Dimension(100,20));
		
		venda.add(texto,BorderLayout.NORTH);
		JPanel buttons = new JPanel();
		buttons.add(codigo);
		adicionar_vd(texto, buttons,codigo);
		venda.add(buttons,BorderLayout.SOUTH);
		main.add(" Venda ",venda);
	}
	

//-------------------------------------------------------------------------------Janela__Inicio----------------------------------------------------------------------------//
	private static void conect_button (JPanel janela, JLabel info, JTextField usuario, JTextField senha, JTextField servidor) {
		JButton conectar_bt = new JButton("Conectar");
		ActionListener conectar_al = new ActionListener(){public void actionPerformed(ActionEvent e) {
			try {
				conectar_BD(usuario.getText(), senha.getText(), servidor.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			info.setText("   Status: "+teste_server());;
			if (status != null && carregado == false) {
				carregar_BD(model);
				carregado = true;
			}else {System.out.print("A lista já foi carregada!");}
			
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
			    try {inserir_BD(pdt.getText(),Integer.parseInt(qtd.getText()),Float.parseFloat( val.getText()));}catch (SQLException x) {}
			
			} else {
				ImageIcon png = new ImageIcon("C://Users//César Vargas//eclipse-workspace//Gerenciador//src//loja//batman.png");
				int input = JOptionPane.showConfirmDialog(null, "Gostaria de chamar o Batman?","Be honest...",
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
				    try {
						atualizar_BD(pdt.getText(),Integer.parseInt(qtd.getText()),Float.parseFloat( val.getText()));
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	
				}
			    
			}
		}};
		Atualizar.addActionListener(sair);
		janela.add(Atualizar);
	}
	
	private static void remover_button(JPanel janela,DefaultListModel list, JTextField pdt) {
		JButton remover = new JButton("Remover");
		ActionListener remove = new ActionListener(){public void actionPerformed(ActionEvent e) {
			int index = 0;
			boolean achado = false;
			for (int i=0;i<list.getSize(); i++) {
				String array[] = list.get(i).toString().split(" ");
				if (array[1].intern() == pdt.getText().intern()) {
					index = i;
					achado = true;
					System.out.println("Removido");
				    try {
						remover_BD(pdt.getText());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			if (achado == true) { lista.clearSelection();list.remove(index);System.out.print(index); }
		}};
		remover.addActionListener(remove);
		janela.add(remover);
		}
	
	private static void tb_adicionar(JTabbedPane painel, Principal loja) {
        JPanel main = new JPanel();
		JPanel caixa = new JPanel();
		JPanel caixa2 = new JPanel();
		JPanel caixa3 = new JPanel();
		JPanel caixa4 = new JPanel();
		
		main.setLayout(new BorderLayout());
		
		caixa2.setBackground(new Color(250,250,250));
		caixa.setBackground(new Color(150,150,150));
		caixa3.setBackground(new Color(200,200,200));
		
		
		JTextField produto = new JTextField("Nome do produto");
		JTextField quantidade = new JTextField("Quantidade");
		JTextField valor = new JTextField("Valor");
		
		produto.setPreferredSize(new Dimension(500,20));
		valor.setPreferredSize(new Dimension(70,20));
		quantidade.setPreferredSize(new Dimension(70,20));
		
		on_select(produto, quantidade, valor);
		
		caixa2.setLayout(new BorderLayout());
		caixa2.add(lista,BorderLayout.CENTER);
		
		main.add(caixa2,BorderLayout.CENTER);
		
		caixa3.add(produto);
		caixa3.add(quantidade);
		caixa3.add(valor);
		
		caixa.setLayout(new BorderLayout());
		caixa.add(caixa3,BorderLayout.NORTH);
		Adicionar_button(caixa4, model, loja, produto, quantidade, valor);
		Atualizar_button(caixa4, model, loja, produto, quantidade, valor);
		remover_button(caixa4,model,produto);
		try {
		carregar_BD(model);
		}catch (Exception e) {
			System.out.println("");
		}
		
		caixa.add(caixa4,BorderLayout.SOUTH);
		
		 
		main.add(caixa,BorderLayout.SOUTH);
		
		painel.add("Adicionar produto",main);
		
		
	}

	private static void on_select(JTextField pdt, JTextField qtd, JTextField val) {
		MouseListener mouseListener = new MouseAdapter() {
		      public void mouseClicked(MouseEvent mouseEvent) {
		        JList theList = (JList) mouseEvent.getSource();
		        if (mouseEvent.getClickCount() == 1) {
		          int index = theList.locationToIndex(mouseEvent.getPoint());
		          if (index >= 0) {
		            Object o = theList.getModel().getElementAt(index);
		            String [] texto = o.toString().split(" ");
		            pdt.setText(texto[1]);
	                qtd.setText(texto[7]);
	                val.setText(texto[14]);
		          }
		        }
		      }
		    };
		    lista.addMouseListener(mouseListener);
	    }
	
//------------------------------------------------------------------------------------Main---------------------------------------------------------------------------------//
	private static void criar_loja(Principal loja) throws SQLException {
		JTabbedPane painel = new JTabbedPane();
		JFrame janela = new JFrame("Cadastrar produtos");
		
		inicio(painel);
		tb_adicionar(painel,loja);
		venda_tb(painel);
		
		janela.add(painel);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.pack();
		janela.setSize(800, 400);
		janela.setVisible(true);
		
	}
	
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