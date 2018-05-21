package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Base {

	private long sequence = 0;
	private Map<Long,Cliente> clientes = new HashMap<Long,Cliente>();
	private static Base instance = new Base();
	
	private Base(){};
	
	public static Base getInstance(){
		return instance;
	}
	
	/**
	 * Cadastra um cliente
	 * @param cliente
	 */
	public void insert(Cliente cliente){
		cliente.setId(++sequence);
		clientes.put(cliente.getId(), cliente);
	}
	
	/**
	 * Deleta um cliente e retorna a quantidade de registros alterados
	 * @param id
	 * @return
	 */
	public int delete(long id){
		if(clientes.containsKey(id)){
			clientes.remove(id);
			return 1;
		}else{
			return 0;
		}	
	}
	
	/**
	 * Atualiza um cliente
	 * @param cliente
	 * @return
	 */
	public int update(Cliente cliente){
		if(clientes.containsKey(cliente.getId())){
			clientes.put(cliente.getId(), cliente);
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * Retorna a lista de clientes
	 * @return
	 */
	public List<Cliente> listar(){
		return clientes!=null ? new ArrayList<Cliente>(clientes.values()) : null;
	}
}
