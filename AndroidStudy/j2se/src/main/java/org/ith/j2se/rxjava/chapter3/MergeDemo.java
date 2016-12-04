package org.ith.j2se.rxjava.chapter3;

import org.ith.j2se.rxjava.chapter3.entities.CarPhoto;
import org.ith.j2se.rxjava.chapter3.entities.LicensePlate;
import rx.Observable;

/**
 * Created by tanghao on 12/5/16 07:30
 *
 * More than one Observable
 *
 * Treating Several Observables as One Using merge()
 *
 * Note:
 *
 * 1.the order passed to merge is rather arbitrary
 *
 * 2.errors appearing in any of the underlying Observables will be eagerly propaged to Observers.
 *
 * you can use the mergeDelayError()variant of merge() to postpone any errors until all of the other
 * finished().
 *
 * mergeDelayError()will collect all exceptions in rx.exceptions.CompositeException.
 */
public class MergeDemo {
  public static void main(String[] args) {
    //what we wourld like to do is run these three algorithms side by side

    CarPhoto photo1 = new CarPhoto();
    CarPhoto photo2 = new CarPhoto();
    CarPhoto photo3 = new CarPhoto();

    Observable<LicensePlate> all = Observable.merge(fastAlgo(photo1), preciseAlgo(photo2), experimentalAlgo(photo3));
    all.subscribe(System.out::println);

    //but how can I get the errors?
    Observable.mergeDelayError(fastAlgo(photo1), preciseAlgo(photo2), experimentalAlgo(photo3));
  }

  static Observable<LicensePlate> fastAlgo(CarPhoto photo) {
    //Fast but poor quality
    //just for example
    return Observable.empty();
  }

  static Observable<LicensePlate> preciseAlgo(CarPhoto photo) {
    //Precise but can be expensive
    return Observable.empty();
  }

  static Observable<LicensePlate> experimentalAlgo(CarPhoto photo) {
//    Unpredicatable,running anyway
    return Observable.empty();
  }

}
