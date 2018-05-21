package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.Cliente;

@SuppressWarnings("serial")
public class ClienteDlg extends JFrame{

	private Cliente cliente;
	private JTextField nome;
	private JTextField telefone;
	private JTextField endereco;
	private Gui gui;

	public ClienteDlg(Cliente cliente, Gui gui) {
		this.cliente = cliente;
		this.gui = gui;
		this.setPreferredSize(new Dimension(400, 200));
		this.setResizable(false);
		init();
		carregarInputs();
		pack();
		this.setLocationRelativeTo(null);
	}

	private void carregarInputs(){
		nome.setText(cliente.getNome());
		telefone.setText(cliente.getTelefone());
		endereco.setText(cliente.getEndereco());
		nome.repaint();
		telefone.repaint();
		endereco.repaint();
	}
	
	private JTextField createFild(){
		JTextField field = new JTextField();
		field.setPreferredSize(new Dimension(300, 20));
		return field;
	}
	
	private void init() {
		
		this.setLayout(new BorderLayout());
		
		JPanel panelBase = new JPanel();
		panelBase.setLayout(new GridLayout(3, 2));
		
		panelBase.add(new JLabel("Nome: "));
		panelBase.add(nome = createFild());
		
		panelBase.add(new JLabel("Telefone: "));
		panelBase.add(telefone = createFild());
		
		panelBase.add(new JLabel("Endereço: "));
		panelBase.add(endereco = createFild());
		this.add(panelBase,BorderLayout.CENTER);
		
		JPanel panelButon = new JPanel();
		panelBase.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton save = new JButton("Salvar");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cliente.setNome(nome.getText());
				cliente.setTelefone(telefone.getText());
				cliente.setEndereco(endereco.getText());
				
				try {
					if(cliente.getId()==null || cliente.getId()==0L){
						gui.getService().salvar(cliente);
					}else{
						gui.getService().editar(cliente);
					}
					gui.refreshStructure();
					JOptionPane.showMessageDialog(null, "Cliente salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, e2.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panelButon.add(save);
		this.add(panelButon, BorderLayout.SOUTH);
	}

}
