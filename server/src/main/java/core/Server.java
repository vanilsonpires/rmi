package core;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Enumeration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Server implements Runnable{

	private boolean running;
	public static final String IP = getIP();
	public static final int PORTA = 1099;

	public static void main(String[] args) {
		try {
			new Server().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Subindo o serviço
	@SuppressWarnings("deprecation")
	public void run() {
		
		running = true;
		Thread thread = new Thread(){
			@Override
			public void run() {
				try {
					Service s = new ServiceImpl();
					LocateRegistry.createRegistry(PORTA);
					System.setProperty("java.rmi.server.hostname", IP);
					Naming.rebind("Service", s);
					System.out.println("Service RMI Running in " + IP + ":" + PORTA);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
		
		//While para manter o serviço ativo
		while(running);
		thread.destroy();
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
	
	public static <T> String objectToJson(T obj) {
		JSONSerializer serializer = new JSONSerializer();
		return serializer.serialize(obj);
	}

	public static <T> T jsonToObject(String json, Class<T> clazz) {
		JSONDeserializer<T> der = new JSONDeserializer<T>();
		return der.deserialize(json);
	}
}
