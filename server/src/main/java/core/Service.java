package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Service extends Remote{
	
	public void salvar(Cliente cliente) throws RemoteException;
	
	public List<Cliente> listar() throws RemoteException;
	
	public int remover(Long id) throws RemoteException;
	
	public int editar(Cliente cliente) throws RemoteException;

}
