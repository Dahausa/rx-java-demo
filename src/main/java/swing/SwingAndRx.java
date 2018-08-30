package swing;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SwingAndRx implements Runnable {

	private JFrame mainFrame;
	private Container contentPane;
	private JTextArea textArea;
	private JButton loadDataBtn;
	private JButton trippleKlickButton;

	public static void main(String[] args) {

		Runnable guiCreator = new SwingAndRx();
		SwingUtilities.invokeLater(guiCreator);
	}

	private void doGuiStuff() {
		mainFrame = new JFrame("Fill TextArea over time with RX demo");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = mainFrame.getContentPane();
		contentPane.setLayout(new java.awt.FlowLayout());

		textArea = new JTextArea();
		contentPane.add(textArea);
		
		trippleKlickButton = new JButton("Tripple");
		contentPane.add(trippleKlickButton);

		loadDataBtn = new JButton("Load data");
		contentPane.add(loadDataBtn);

		mainFrame.setSize(300, 200);
		mainFrame.setVisible(true);
	}

	@Override
	public void run() {

		this.doGuiStuff();
		
		// Demo one -------------------------------------------------------------------------
		// If you click 3 times on buttonClickObservable a message box appears
		
		//Create observable from swing button click event
		Observable<ActionEvent> buttonClickObservable 
		= Observable.create(subscriber -> {trippleKlickButton.addActionListener(event -> subscriber.onNext(event));});
		
		//Use this observable
		buttonClickObservable
		//Ignore the first two events
		.skip(2)
		//Activate the observable
		.subscribe(event -> {
			JOptionPane.showMessageDialog(null, "3 times clicked on button");
		});
		
		
		//Demo two --------------------------------------------------------------------------
		//Fill textarea dynamically with data streamed by a backend
		
		//On press button
		loadDataBtn.addActionListener(event -> {
			//Load data as "stream" (Observable) from backend
			Database.loadData()
			//Subscribe via scheduler on a separate thread pool
			.subscribeOn(Schedulers.computation())
			//Define the action when data arrives
			.subscribe(dataSet -> {
				textArea.append(dataSet);
			})
			;
		});
	}
}

/**
 * Database Fake
 * @author joern
 *
 */
class Database {
	
	/** Simulates a long running database query which gets streamed via an observable*/
	public static Observable<String> loadData() {
		return Observable.create(emitter-> {
			for(int i=0;i < 10; i++) {
				emitter.onNext("DataSet " + Integer.toString(i));
				Thread.sleep(3000);
			}
			emitter.onComplete();
		});
	}
}
