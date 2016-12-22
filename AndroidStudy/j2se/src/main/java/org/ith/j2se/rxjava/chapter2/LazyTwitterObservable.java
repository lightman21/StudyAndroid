package org.ith.j2se.rxjava.chapter2;

import com.annimon.stream.Stream;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 * Created by tanghao on 12/21/16.
 *
 * If Manually managing Subscribers
 */
public class LazyTwitterObservable {

  private final Set<Subscriber<? super Status>> subscribers = new CopyOnWriteArraySet<>();

  private final TwitterStream twitterStream;

  private final Observable<Status> observalbe = Observable.create(
      subscriber -> {

        register(subscriber);

        subscriber.add(Subscriptions.create(() ->
            this.deregister(subscriber)
        ));

        //Subscriptons.create(Action0 unsubscribe)

        //public static Subscription create(final Action0 unsubscribe) {
        //  return BooleanSubscription.create(unsubscribe);
        //}
      }
  );

  public LazyTwitterObservable() {
    this.twitterStream = new TwitterStreamFactory().getInstance();
    this.twitterStream.addListener(new SimpleStatusListener() {
      @Override
      public void onStatus(Status status) {
        Stream.of(subscribers).forEach(s -> s.onNext(status));
      }

      @Override
      public void onException(Exception e) {
        Stream.of(subscribers).forEach(s -> s.onError(e));
      }
    });
  }

  Observable<Status> observe() {
    return observalbe;
  }

  private synchronized void register(Subscriber<? super Status> subscriber) {
    if (subscribers.isEmpty()) {
      subscribers.add(subscriber);
      twitterStream.sample();
    } else {
      subscribers.add(subscriber);
    }
  }

  private synchronized void deregister(Subscriber<? super Status> subscriber) {
    subscribers.remove(subscriber);

    if (subscribers.isEmpty()) {
      twitterStream.shutdown();
    }
  }
}
