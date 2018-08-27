package usecases;

import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.reactivex.Observable;

public class SimpleUseCases {

	/**
	 * I want to combine time triggered events with custom code
	 */
	@Test
	public void funWithIntervals() throws InterruptedException {
		
		//Create a timer observable
		Observable<Long> timer = Observable.interval(3, TimeUnit.SECONDS);
		
		//Create a new observable which returns the current time on the interval observable
		Observable<Date> now = timer.map( x -> {
			return new Date();
		});
		//Et voila! Your timer!
		now.subscribe(System.out::println);
		
		//Now something different... let us check if a file exists... as long as your application is running!!! 
		//Use the same timer (immutability) and 4 lines of additional code
		timer.map(x -> {
			return Paths.get("src", "test","resources","file_checker").toFile().exists();
		}).subscribe( result -> {
			System.out.println("Directory exists? " + result);
		});
		
		Thread.sleep(60000);
	}

}
