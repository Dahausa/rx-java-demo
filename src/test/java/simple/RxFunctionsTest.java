package simple;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.reactivex.Observable;

public class RxFunctionsTest {
	
	
	/**
	 *  Regular observable emits events first although subscribed as second
	 */
	@Test
	public void delaySubscription() throws InterruptedException {
		
		//Observable that emits values with delay of 2 seconds
		Observable<Integer> delayedEmit = Observable.range(1, 10)
													.delaySubscription(2, TimeUnit.SECONDS)
													.doFinally(() -> System.out.println("Delayed observable finished!"));
		
		//Observable that emits value directly at subscription
		Observable<Integer> normalEmit = Observable.range(11, 20)
													.doFinally(() -> System.out.println("Regular observable finished!"));
		
		//Subscribe delayed observable first
		delayedEmit.subscribe((Integer x) -> System.out.println("Delayed: " + x.toString()));
		//Second subscribe normal observable
		normalEmit.subscribe((Integer x) -> System.out.println("Regluar: " + x.toString()));
		
		//Just to delay program termination
		Thread.sleep(5000);
	}
	
	@Test
	public void ofType() {
		//Create objects of different classes
		Observable<Object> allObjects = Observable.just("ARD", "ZDF", "C&A",1,"BRD",2, "DRK", new EventCreationTest());
		
		//Create a new observable that filters Strings
		Observable<String> filteredStrings = allObjects.ofType(String.class);
		
		//Activate the String observable
		filteredStrings.subscribe(s -> System.out.println("Found String '" + s + "'"));
		
		//Create a new observable that filters Integer
		Observable<Integer> filteredIntegers = allObjects.ofType(Integer.class);
		
		//Activate the Integer observable
		filteredIntegers.subscribe(s -> System.out.println("Found Integer '" + s.toString() + "'"));
	}

}
