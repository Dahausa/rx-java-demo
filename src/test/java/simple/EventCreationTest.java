package simple;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;

public class EventCreationTest {
	
	/** How to create simple events? */
	@Test
	public void simple() {
		//Create an event from an array
		Observable<Integer> fromArrayObservable = Observable.fromArray(new Integer[]{100,200,300,400});
		fromArrayObservable.subscribe(System.out::println);
		
		//Create an event from a range
		Observable<Integer> rangeObservable = Observable.range(1, 10);
		rangeObservable.subscribe(System.out::println);
	}

	/** How to create an Observable with events from custom code? */
	@Test
	public void create() {
		
		//Create an observable via custom code
		Observable<Integer> customObservable = Observable.create(emitter-> {
			for(int i=0;i < 10; i++) {
				//emit every entry as event
				emitter.onNext(i);
			}
			//Notify that you're finished
			emitter.onComplete();
		});
		
		customObservable.subscribe(System.out::println);
	}
	
	/** How to create time triggered events? */
	@Test
	public void interval() throws InterruptedException {
		
		Observable<Long> intervalObservable = Observable.interval(3, TimeUnit.SECONDS);
		
		intervalObservable.subscribe((Long x) -> System.out.println("Event " + x.toString()));
		
		Thread.sleep(10000);
	}
}
