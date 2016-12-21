package org.ith.j2se.rxjava.chapter2;

import rx.Observable;
import rx.subscriptions.Subscriptions;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.util.function.Consumer;

/**
 * Created by tanghao on 12/19/16.
 */

public class Chapter2 {
  public static void main(String[] args) {
    cacheDemo();
  }

  public static void normalTwitterStream() {
    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
    twitterStream.addListener(
        new twitter4j.StatusListener() {
          public void onStatus(Status status) {
            System.out.println("Status: {}" + status);
          }

          @Override
          public void onException(Exception ex) {
            System.out.println("Error callback" + ex);
          }

          @Override
          public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

          }

          @Override
          public void onTrackLimitationNotice(int i) {

          }

          @Override
          public void onScrubGeo(long l, long l1) {

          }

          @Override
          public void onStallWarning(StallWarning stallWarning) {
          }
        }
    );

    // twitterStream.sample();
    // TimeUnit.SECONDS.sleep(10);
    // twitterStream.shutdown();
  }


  /***
   * n real life, you would probably process each Status message (tweet) somehow. For example, save
   * it to a database or feed a machine-learning algorithm. You can technically put that logic
   * inside the callback, but this couples the infrastructural call with the business logic. Simple
   * delegation to a separate class is better, but unfortunately not reusable. What we really want
   * is clean separation between the technical domain (consuming data from an HTTP connection) and
   * the business domain (interpreting input data). So we build a second layer of callbacks:
   *
   *
   * What if we want to count the number of tweets per second?
   *
   * Or consume just the first five?
   *
   * And what if we would like to have multiple listeners?
   */
  public static void consume(Consumer<Status> onStatus, Consumer<Exception> onException) {

    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
    twitterStream.addListener(new StatusListener() {
      @Override
      public void onStatus(Status status) {
        onStatus.accept(status);
      }

      @Override
      public void onException(Exception e) {
        onException.accept(e);
      }

      @Override
      public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

      }

      @Override
      public void onTrackLimitationNotice(int i) {

      }

      @Override
      public void onScrubGeo(long l, long l1) {

      }

      @Override
      public void onStallWarning(StallWarning stallWarning) {

      }

    });
  }

  public static Observable<Status> observe() {
    return Observable.create(subscriber -> {
      TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
      twitterStream.addListener(new StatusListener() {
        @Override
        public void onStatus(Status status) {
          if (!subscriber.isUnsubscribed())
            subscriber.onNext(status);
          else {
            twitterStream.shutdown();
          }
        }

        @Override
        public void onException(Exception e) {
          if (!subscriber.isUnsubscribed())
            subscriber.onError(e);
          else
            twitterStream.shutdown();
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

        }

        @Override
        public void onTrackLimitationNotice(int i) {

        }

        @Override
        public void onScrubGeo(long l, long l1) {

        }

        @Override
        public void onStallWarning(StallWarning stallWarning) {

        }
      });

      subscriber.add(Subscriptions.create(twitterStream::shutdown));

    });
  }

  public static void useObserve() {
    observe().subscribe(onNext -> System.out.println("Status " + onNext),
        onError -> System.out.println("Error " + onError)
    );
  }


  public static void propagateError(int id) {
    Observable.create(subscriber -> {
      try {
        subscriber.onNext(id);
        subscriber.onCompleted();
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    System.out.println("Rx Build-in way");

    Observable.fromCallable(() -> id);
  }

  /***
   * demonstrage cache()
   */
  public static void cacheDemo() {
    Observable<Integer> into = Observable.create(subscriber -> {
      System.out.println("Create");
      subscriber.onNext(42);
      subscriber.onCompleted();
    });

    System.out.println("start--------------");

    into.subscribe(ma -> System.out.println("MA " + ma));

    into.subscribe(mb -> System.out.println("MB " + mb));

    System.out.println("finish--------------");


    System.out.println("\ncache\n");

    into = into.cache();

    into.subscribe(ca -> System.out.println("Cache A " + ca));
    into.subscribe(cb -> System.out.println("Cache B  " + cb));
  }

  public void callbackHell() {

    //  button.setOnClickListener(view -> {
    //    MyApi.asyncRequest(response -> {
    //      Thread thread = new Thread(() -> {
    //        int year = datePicker.getYear();
    //        runOnUiThread(() -> {
    //          button.setEnabled(false);
    //          button.setText("" + year);
    //        });
    //      });
    //      thread.setDaemon(true);
    //      thread.start();
    //    });
    //  });

  }
}
