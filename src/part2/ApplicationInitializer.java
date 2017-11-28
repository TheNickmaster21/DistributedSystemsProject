package part2;

import java.io.IOException;

public class ApplicationInitializer {
	public static void main(String[] args) throws IOException, InterruptedException {

		// // Start two routers
		// Router router1 = new Router();
		// router1.setPort("5555");
		// router1.setCompanionRouterIP("127.0.0.1");
		// router1.setCompanionRouterPort("5556");
		// new Thread(router1).start();
		//
		// Router router2 = new Router();
		// router2.setPort("5556");
		// router2.setCompanionRouterIP("127.0.0.1");
		// router2.setCompanionRouterPort("5555");
		// new Thread(router2).start();
		//
		ServerClient server = new ServerClient();
		server.setName("Test Server");
		server.setPort("5655");
		server.setRouterIP("10.100.97.43");
		server.setRouterPort("5555");
		server.register();
		server.request();
		server.service();

		Thread.sleep(4000);
		server = new ServerClient();
		server.setName("Test Server");
		server.setPort("5658");
		server.setRouterIP("10.100.97.43");
		server.setRouterPort("5555");
		server.register();
		server.request();
		server.service();

		Thread.sleep(4000);
		server = new ServerClient();
		server.setName("Test Server");
		server.setPort("5666");
		server.setRouterIP("10.100.97.43");
		server.setRouterPort("5555");
		server.register();
		server.request();
		server.service();

		Thread.sleep(4000);
		server = new ServerClient();
		server.setName("Test Server");
		server.setPort("5679");
		server.setRouterIP("10.100.97.43");
		server.setRouterPort("5555");
		server.register();
		server.request();
		server.service();

		Thread.sleep(4000);
		server = new ServerClient();
		server.setName("Test Server");
		server.setPort("5635");
		server.setRouterIP("10.100.97.43");
		server.setRouterPort("5555");
		server.register();
		server.request();
		server.service();

		// new Thread(server).start(); ServerClient server2 = new ServerClient();
		// server2.setName("Test Server");
		// server2.setPort("5656");
		// server2.setRouterIP("127.0.0.1");
		// server2.setRouterPort("5556");
		// new Thread(server2).start();
		//
		// ServerClient server3 = new ServerClient();
		// server3.setName("Test Server");
		// server3.setPort("5657");
		// server3.setRouterIP("127.0.0.1");
		// server3.setRouterPort("5555");
		// new Thread(server3).start();
		//
		// ServerClient server4 = new ServerClient();
		// server4.setName("Test Server");
		// server4.setPort("5658");
		// server4.setRouterIP("127.0.0.1");
		// server4.setRouterPort("5556");
		// new Thread(server4).start();
		//
		// ServerClient server5 = new ServerClient();
		// server5.setName("Test Server");
		// server5.setPort("5659");
		// server5.setRouterIP("127.0.0.1");
		// server5.setRouterPort("5555");
		// new Thread(server5).start();
		//
		// ServerClient server6 = new ServerClient();
		// server6.setName("Test Server");
		// server6.setPort("5660");
		// server6.setRouterIP("127.0.0.1");
		// server6.setRouterPort("5556");
		// new Thread(server6).start();

		// ServerClient server2 = new ServerClient();
		// server2.setName("Test Server");
		// server2.setPort("5656");
		// server2.setRouterIP("127.0.0.1");
		// server2.setRouterPort("5556");
		// new Thread(server2).start();
		//
		// ServerClient server3 = new ServerClient();
		// server3.setName("Test Server");
		// server3.setPort("5657");
		// server3.setRouterIP("127.0.0.1");
		// server3.setRouterPort("5555");
		// new Thread(server3).start();
		//
		// ServerClient server4 = new ServerClient();
		// server4.setName("Test Server");
		// server4.setPort("5658");
		// server4.setRouterIP("127.0.0.1");
		// server4.setRouterPort("5556");
		// new Thread(server4).start();
		//
		// ServerClient server5 = new ServerClient();
		// server5.setName("Test Server");
		// server5.setPort("5659");
		// server5.setRouterIP("127.0.0.1");
		// server5.setRouterPort("5555");
		// new Thread(server5).start();
		//
		// ServerClient server6 = new ServerClient();
		// server6.setName("Test Server");
		// server6.setPort("5660");
		// server6.setRouterIP("127.0.0.1");
		// server6.setRouterPort("5556");
		// new Thread(server6).start();

	}
}
