import static spark.Spark.init;
import static spark.Spark.webSocket;

import websocket.GroupCreateSocketHandler;
import static spark.Spark.port;

public class MainInfoPortal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		port(10010);
		webSocket("/groups", GroupCreateSocketHandler.class);
		init();
	}

}
