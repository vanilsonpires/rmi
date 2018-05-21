package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import core.Cliente;
import core.Service;

public class Gui extends JFrame {

	private JTable tableClientes;
	private TableClient modelClient;
	private Service service;
	private List<Cliente> clientes;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7886147914342084854L;

	public Gui() {
		super("RMI Cliente");
		setMinimumSize(new Dimension(1280, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		initComponents();
		this.pack();
		this.service = connect();
		try {
			clientes = service.listar();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		createPanelCenter();
	}

	private void createPanelCenter() {
		JPanel jPanel = new JPanel();
		jPanel.setBorder(BorderFactory.createTitledBorder("Cliente RMI - FLF"));
		Border border = jPanel.getBorder();
		Border margin = new EmptyBorder(5, 5, 5, 5);
		jPanel.setBorder(new CompoundBorder(border, margin));

		jPanel.setLayout(new BorderLayout());

		JPanel panelBotoes = new JPanel();
		panelBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton cadastrar = new JButton("Cadastrar");
		Gui gui = this;
		cadastrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ClienteDlg(new Cliente(), gui).setVisible(true);
			}
		});
		JButton editar = new JButton("Editar");
		editar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int indexCliente = tableClientes.getSelectedRow();
				if (indexCliente == -1 || indexCliente > clientes.size() - 1) {
					JOptionPane.showMessageDialog(null, "Necessário selecionar um cliente para realizar esta operação!",
							"ERRO", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new ClienteDlg(clientes.get(indexCliente), gui).setVisible(true);
				;
			}
		});
		JButton remover = new JButton("Remover");
		remover.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int indexCliente = tableClientes.getSelectedRow();
				if (indexCliente == -1 || indexCliente > clientes.size() - 1) {
					JOptionPane.showMessageDialog(null, "Necessário selecionar um cliente para realizar esta operação!",
							"ERRO", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					int qtd = service.remover(clientes.get(indexCliente).getId());
					refreshStructure();
					if (qtd > 0)
						JOptionPane.showMessageDialog(null, "Cliente removido com sucesso", "Sucesso",
								JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(null, "Nenhum cliente foi removido. Id inválido", "ERRO",
								JOptionPane.ERROR_MESSAGE);
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, e2.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		panelBotoes.add(cadastrar);
		panelBotoes.add(editar);
		panelBotoes.add(remover);

		jPanel.add(panelBotoes, BorderLayout.NORTH);
		tableClientes = new JTable(modelClient = new TableClient());
		jPanel.add(new JScrollPane(tableClientes), BorderLayout.CENTER);
		this.add(jPanel, BorderLayout.CENTER);
	}

	/**
	 * Conecta ao serviço RMI
	 * 
	 * @return
	 */
	public static Service connect() {
		try {
			return (Service) Naming.lookup("//" + getIP() + ":" + 1099 + "/Service");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Model de tabela dinâmico
	 * 
	 * @author vanilson
	 *
	 */
	@SuppressWarnings("serial")
	private class TableClient extends AbstractTableModel {

		public void addTableModelListener(TableModelListener l) {

		}

		public Class<?> getColumnClass(int columnIndex) {
			return Cliente.class.getDeclaredFields()[columnIndex].getType();
		}

		public int getColumnCount() {
			return Cliente.class.getDeclaredFields().length;
		}

		public String getColumnName(int columnIndex) {
			return Cliente.class.getDeclaredFields()[columnIndex].getName();
		}

		public int getRowCount() {
			return clientes != null ? clientes.size() : 0;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			try {
				return clientes.get(rowIndex).getClass().getDeclaredFields()[columnIndex].get(clientes.get(rowIndex));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			return null;
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public void removeTableModelListener(TableModelListener l) {

		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			try {
				clientes.get(rowIndex).getClass().getDeclaredFields()[columnIndex].set(clientes.get(rowIndex), aValue);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}

		public void refresh() {
			fireTableDataChanged();
		}

	}

	/**
	 * Atualiza a estrutura
	 */
	public void refreshStructure() {
		try {
			this.clientes = service.listar();
			modelClient.refresh();
			tableClientes.updateUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna o IP atual da máquina
	 * 
	 * @autor Vanilson Pires
	 * @date 3 de mai de 2018
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String getIP() {
		try {
			boolean preferIpv4 = true;
			boolean preferIPv6 = false;
			Enumeration en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface i = (NetworkInterface) en.nextElement();
				for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) {
					InetAddress addr = (InetAddress) en2.nextElement();
					if (!addr.isLoopbackAddress()) {
						if (addr instanceof Inet4Address) {
							if (preferIPv6) {
								continue;
							}
							return addr.getHostAddress();
						}
						if (addr instanceof Inet6Address) {
							if (preferIpv4) {
								continue;
							}
							return addr.getHostAddress();
						}
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public Service getService() {
		return service;
	}

}
