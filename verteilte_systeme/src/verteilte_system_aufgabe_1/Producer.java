package verteilte_system_aufgabe_1;
import java.util.Vector;

public class Producer extends Thread{
	

		// Warteschlange der Länge 7 
		static final int MAX = 7;
		private Vector messages = new Vector();

		@Override
		public void run()
		{
			try {
				while (true) {

					//mit dem Aufruf dieser Methode wird eine Nachricht erstellt
					putMessage();
					
					//Producer schläft für 1s wenn die Warteschlange voll ist
					sleep(1000);
				}
			}
			catch (InterruptedException e) {
			}
		}

		private synchronized void putMessage()
			throws InterruptedException
		{

			//Abfrage ob die Warteschlange voll ist 
			while (messages.size() == MAX)

				//wartet bis die Warteschlange leer ist 
				wait();

			//fügt neue Nachricht hinzu 
			//in diesem Fall wird eine Nachricht mit dem Datum und der aktuellen Uhrzeit versendet 
			messages.addElement(new java.util.Date().toString());
			notify();
		}

		public synchronized String getMessage()
			throws InterruptedException
		{
			notify();
			while (messages.size() == 0)
				wait();
			String message = (String)messages.firstElement();

			//holt sich die Nachrichten aus der Warteschlangen 
			messages.removeElement(message);
			return message;
		}
	}

class Consumer extends Thread {
	Producer producer;

	Consumer(Producer p)
	{
		producer = p;
	}

	@Override
	public void run()
	{
		try {
			while (true) {
				String message = producer.getMessage();

				//sendet eine Erhaltsbestätigung 
				System.out.println("Got message: " + message);
				sleep(2000);
			}
		}
		catch (InterruptedException e) {
		}
	}

	public static void main(String args[])
	{
		//startet den Producer und weist ihm den Consumer an 
		Producer producer = new Producer();
		producer.start();
		new Consumer(producer).start();
	}
}
	
