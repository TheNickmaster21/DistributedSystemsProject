package part2;

public class SenderThread implements Runnable {
private String serverAddress;
private int port;
	public SenderThread(String a, int p) {
		this.serverAddress = a;
		this.port = p;
	}
	@Override
	public void run() {
	TCPCommunicator comm = new TCPCommunicator(serverAddress, (port+1));// TODO Auto-generated method stub
	comm.startOrExit();

	
	DataTransferMethods.sendFile(	comm.getSocket(), "testText.txt");
	}

}
