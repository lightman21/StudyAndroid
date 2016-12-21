


2016-12-21 10:55
there is a useful method in Subscriptions.java
which allow you to create a Subscription
and set what to do when unsubscribed.

```
/***
Creates and returns a Subscription that invokes the given
Action0 when unsubscribed.
*/
public static Subscription create(final Action0 unsubscribe) {
      return BooleanSubscription.create(unsubscribe);
}

```
and there is a add(Subscription s) method in Subscriber.java

```
    // method in Subscriber.java
    public final void add(Subscription s) {
        subscriptions.add(s);
    }

```
so when you have a Subscriber and you wanna do sth during it unsubscribe
you can do like this

```
subscriber.add(Subscriptions.create(
  ()->{
    //here do what you wanna when unsubscribe
  }
));
```
























Take and TakeLast
  When we don't need the whole sequence but only a few elements at beginning or end.
  we can use take() or takeLast()

Skip and SkipLast
  The skip() and skipLast()functions are the counterpart of take()
  and takeLast(),They get an integer N as a parameter

Once and Only once
  An emitting Observable sequence counld emit duplicates
  by error or by design.
  The distinct and distinctUntilChanged() function allow
  us to deal smoothly with duplicates.

DistinctUntilChanged
  what if we want to get notified when an Obervable sequence
  emits a new value that is different from the previous one?
  The distinctUntilChanged() filtering function will do the trick


First and last
  The first() and list() methods are quite easy to figure out.
  They emit only the first or last element emitted by Observable.
  Both of them can even get Func1 as a parameter: a predicate
  that can be used to decide the actual first or last element
  we are interested in.

ElementAt
  What if we want only the fifth element emitted by an Observable?
  elementAt()function emits only the n element from a sequence.
  and then it completes.

Sampling
  Let's go back to our Observable temperature sensor.
  it's emitting the current room temperature every second.
  Honestly,we don't think the temperature will change so repidly.
  appending sample() to the Observable source,we will create a new
  bservable sequence that will emit the most reent item emitted
  by the Observable source in a decided time interval

ThrottleFirst
  if we want the first item emitted in the time interval instead of the last item
  use throttleFirst()instead.which means throttleFirst is the opposites of sample()

Timeout
  Let's suppose we are working in a very time-sensitive environment.
  Our temperature sensor is emitting a value every second.
  We want to be absolutely sure we get at least one value every two seconds.
  We can use the timeout()function to mirror the source Observable sequence
  and emit an error if we don't get a value.
  It mirrors the original Observable and fires OnError() if the Observable
  doesn't emit values in the decided time interval.

Debounce
    The debounce() function filters out items emitted by the Observable that are rapidly
    followed by another emitted item;it only emits an item from Observable if a particular
    timespan has passed without it emitting another item


FlatMap(1 to many)
    RxJava's flatMap() function provides a way to flatten the sequence.merging all the
    emitted value into one final Observable.
When working with a potentially large number of Observables,it is important to keep in mind
that in case of error in any of the Observables,flatmap itself will trigger its onError()
function and abort the whole chain.

An important note is about the merging part: it allows interleaving.
This means that flatMap() is not able to maintain the exact emitting order of the source
Observables in the final Observable.

ConcatMap(1 to many,and maintain the order)
    RxJava's concatMap() function solves flatMap() interleaving issue.
    providing a flattening function that is able to concatenate the emitted values.
    instead of mergeing them.

FlatMapIterable(1 to many,not Observable.is Iterable)
    flatMapIterable() works similarily to flatMap().The only concrete difference is that
    it pairs up source items and generated Iterables.
    ranther than source items and generated Observables.

SwitchMap
    switchMap() acts similarly to flatMap().but it unsubscribes from and stops mirroring
    the Observable that was generated from the previously emitted item and begins mirroring
    only the current one whenever a new item is emitted by the source Observable.

Scan
    RxJava's scan() function can be considered as an accumulator function.
    The scan() function applies a function to every item emitted by the Observable.
    computes the function result, and injects the result back into the Observable sequnce.
    waiting to use it with the next emitted value.

GroupBy
    RxJava provides a useful function to group elements from a list according to a specific
    criteria:groupBy().
    this function transforms the source Observable into a new Observable.
    which emits Observables.Each one of these new Observable emits the items of s specific group.

Buffer
    RxJava's buffer() function transforms the source Observable into a new Observable.
    which emit values as a list instead of a single item.

Window
    RxJava's window() function is similar to buffer() but it emits Observables instead of lists.

Cast
    RxJava's cast() function is a specialized version of the map() operator.
    it transforms each item from the source Observable into new type.
    casting it to a different Class.

Chapter 6 Combing Observables

Merge
    Living in an asynchronizes world often creates scenarios in which we have multiple sources
    but we want to provide only one fruition: multiple input,single output.

Zip
    Dealing with multiple sources brings in a new possible scenario:
    receive data from multiple Observables,processing them,and making them available 
    as a new Observable sequence.
    zip() combines the value emitted by two or more Observables.
    transforms them according to a specified function Func*.and emits a new value.

Join
    join allows you to pair together items from two sequences.We've already

CombineLatest
    zip() works on the latest  unzipped unzipped unzipped item of the two Observables.
    Instead.combineLastest() works on the latest emitted  emitted emitted items.
    Observable.combineLastest(first,second,Func2)
    all second with lastest first

And,Then,and When
    shit I can't practise it. where is the fucking JoinObservable


Switch
    There could be complex scenarios wherein we should be able to automatically unsubscribe from
    an Obervable to subscribe to a new one in a countinuous subscribe-unsubscribe sequence.

    RxJava's switch(),as per the definition.transforms an Obervable emitting Observables into
    an Observable emitting the most recent emitted Observable.
    Given a source Observable that emits a sequence of Observables.switch() subscribes to the
    source Observable and starts emitting the same items emitted by the first emitted Observable.
    the source emits a new Observable.switch() immediately unsubscribes from the old Observable.
    thus interrupting the item flow from it.and subscribes to the new one.starting to emit its items.
    
    
StartWith
    RxJava's startWith() is the counterpart of concat().As concat() appends items to the ones emitted by
    an Observable,startWith() emits a sequence of items. passed as a parameter.before the Observable 
    starts emitting their own items.

Concat
    The merge operator combines the emissions of two or more Observables. but may interleave them.
    whereas concat() never interleaves the emissions from multiple Observables.

Chapter 7 Defeating the Android MainThead

Schedulers
    Schedulers.io()
    This Scheduler is meant for I/O operations.It's based on a thread pool that adjust its
    size when neccessry.growing and shrinking.

    Scheduler.computation()
    This is the default scheduler for every computational work that is not related to I/O.
    It's the default for a lot of RxJava operator:
    buffer(),debounce(),delay(),interval(),sample() and skip()

    Scheduler.immediate()
    This scheduler allows you to immediately start the specified work on the current thread.
    it's the default Scheduler for
    timeout(),timeInterval() and timestamp()

    Scheduler.newThread()
    This scheduler is what it looks like: it starts a new thread for the specified work.

    How to use Scheduler run a runable?
    Schedulers.io().createWorker().schedule(Runnable)




