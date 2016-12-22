package org.ith.j2se.rxjava.chapter2;

import rx.Observable;
import rx.subjects.PublishSubject;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * Created by tanghao on 12/22/16. solve the problem which LazyTwitterObservable caused
 */
public class TwitterSubject {

  private final PublishSubject<Status> subject = PublishSubject.create();

  public TwitterSubject() {
    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
    twitterStream.addListener(new SimpleStatusListener() {
      @Override
      public void onStatus(Status status) {
        subject.onNext(status);
      }

      @Override
      public void onException(Exception e) {
        subject.onError(e);
      }
    });

    twitterStream.sample();
  }

  public Observable<Status> singleUnderlying()
  {
    //let all subscribers share a single upstream Observable
    // and Observable.share() == pushlish().refCount()
    return observe().publish().refCount();
  }

  public static void main(String[] args) {
    TwitterSubject tsub = new TwitterSubject();

    tsub.observe().subscribe(System.out::println);

    tsub.observe().subscribe(System.out::println);
  }

  //Subject extends Observable
  //PublishSubject extends Subject
  public Observable<Status> observe() {
    return subject;
  }

}
