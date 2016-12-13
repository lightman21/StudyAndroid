package org.ith.j2se.rxjava.chapter3;

import java.util.concurrent.TimeUnit;
import org.ith.j2se.util.TUtils;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by tanghao on 12/12/16.
 */
@SuppressWarnings("unused")
public class RxJavaEssentials_ch3 {

  public static int count = 0;


  public static void main(String[] args) throws Exception {
    debounce();
  }

  public static void debounce() {
    System.out.println("start debounce: " + TUtils.now());
    generData()
        .subscribeOn(Schedulers.immediate())
        .debounce(1, TimeUnit.SECONDS)
        .subscribe(item ->{
          System.out.println(item);
        },err->{
          System.out.println("err = " + err);
        },()->{
          System.out.println("Now onCompleted()");
        });

    TUtils.sleep();

    System.out.println("end debounce: " + TUtils.now());
  }

  public static void timeout() throws InterruptedException {

    generData().timeout(5, TimeUnit.MICROSECONDS)
        .subscribe(System.out::println);

    System.out.println("with error handler");

    generData().timeout(5, TimeUnit.MICROSECONDS)
        .subscribe(System.out::println, err -> System.out.println("now timeout trigger error " + err));

    Thread.sleep(10 * 1000);
  }

  public static void sampleAndThrottle() {
    generData().forEach(System.out::println);
    System.out.println("after ");
    generData().sample(0, TimeUnit.MILLISECONDS)
        .forEach(next -> System.out.println(next));

    System.out.println("throttle ");
    generData().throttleFirst(1, TimeUnit.MILLISECONDS)
        .forEach(next -> System.out.println(next));
  }

  public static void elementOrDefault() {
    generData().elementAt(3).subscribe(System.out::println);

    System.out.println("-----elementDefault-----------");
    //this will trigger a exception
//    generData().elementAt(90).subscribe(System.out::println);

    //what if filtered and no data will pass?
    generData().filter(item -> item > 30)
        .elementAtOrDefault(3, 100).subscribe(System.out::println);

  }

  public static void firstAndLast() {
    generData().first().subscribe(System.out::println);
    System.out.println("----------------");
    generData().last().subscribe(System.out::println);

    System.out.println("first with Default");

    Observable.just(1).filter(it -> it > 2).firstOrDefault(100).subscribe(System.out::println);
    System.out.println("last with Default");
    Observable.just(2).filter(it -> it > 2).lastOrDefault(333).subscribe(System.out::println);
  }


  public static void onceAndOnlyOnce() {
    generData().repeat(3).subscribe(System.out::print);

    System.out.println("\n-----distinct------\n");

    generData().repeat(3).distinct().forEach(System.out::print);

    System.out.println("\n-----distinctUntilChanged------\n");

    generData().repeat(2).distinctUntilChanged().subscribe(it -> {
      System.out.println("until changed" + count);
      count++;
    });

  }

  public static void takeOrTakeLast() {
    generData().take(3).forEach(System.out::println);

    System.out.println("------------------");

    generData().takeLast(3).forEach(System.out::println);

  }

  public static Observable<Integer> generData() {
    return Observable.range(0, 9);
  }
}
