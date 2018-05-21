package core;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class ServiceImpl implements Service, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1113520630779691041L;

	public void salvar(Cliente cliente) throws RemoteException {
		Base.getInstance().insert(cliente);
	}

	public List<Cliente> listar() throws RemoteException {
		return Base.getInstance().listar();
	}

	public int remover(Long id) throws RemoteException {
		return Base.getInstance().delete(id);
	}
	
	public int editar(Cliente cliente) throws RemoteException {
		return Base.getInstance().update(cliente);
	}
}
