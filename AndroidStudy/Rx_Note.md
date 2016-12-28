2016-12-28 10:25
Ways of Combining Streams:concat(),merge(),and switchOnNext()

concat(): concat allow joining together two Observables:
when the first one completes,concat() subscribes to the second one.
Importantly,concat() will subscribe to the second Observable
if and only if the first one is completed.

'''
//precondition:
//fromCache maybe empty or emit sth
//but loadFromDb always emits one Car

Observable<Car> fromCache = loadFromCache();
Observable<Car> fromDb = loadFromDb();

Observable<Car> found = Observable
        .concat(fromCache, fromDb)
        .first();
'''

concat() followed by first() will initially subscribe to fromCache
and if that emits one item, concat() will not subscribe to fromDb.
However, if fromCache is empty, concat() will continue with fromDb,
subscribe to it, and load data from database.










































2016-12-24 14:00
    delay() and timer()
    delay() is more comprehensive than timer().
    because it shifts every every single event further by a given amount of time.
    whereas timer() simpley "sleep" and emits a special event after given time.

    Order of Events After flatMap()
    what flatMap() essentially does is take a master sequence(Observable) of
    values appearing over time(events) and replaces each of the events with an
    independent subsequence. so it does not preserve the original order of events.

    Preserving Order Using cancatMap()
    concatMap() does not introduce any concurrency whatsoever 
    but it preserves the order of upstream events avoid overlapping.
    
    Note:
    flatMap() uses the merger() operator internally that subscribes to all
    sub-Observables at the same time and does not make any distinction between
    them.That is why downstream events interleave with one another.
    ConcapMap().on the other hand,could technically use the concat() operator.
    concat() subscribes only to the first underlying Observable and continues with
    the second one when the first one completes.


2016-12-25 14:00
    When Steams Are not Synchronized with One Another:
        combineLastest(), withLastestFrom() and amb()

    zip two synchonized Observables are very useful
    but how about the two Observables are not synchronized ?
    one emits event fast and the other slow?
    this is where combineLatest() becomes useful.

    '''
    Observable.combineLatest(
        interval(17, MILLISECONDS).map(x -> "S" + x),
            interval(10, MILLISECONDS).map(x -> "F" + x),
                (s, f) -> f + ":" + s
                ).forEach(System.out::println);
    '''

    Output:
    F0:SO
    F1:S0
    F2:S0
    F3:S1
    F4:S1
    ...
    F1000:S587

    But as you can see . neither stream is distinguished.
    combineLatest is synmmetric,
    which means that it does not distinguish between the substream it combines.
    in another words.stream triggers each other.


    but How about you wanna first trigger second but vice versa.
    in other words,events from the second stream do not triger a downstream.
    this is what withLatestFrom() operator comes
    '''
    Observable<String> fast = interval(10, MILLISECONDS).map(x -> "F" + x);
    Observable<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);
    slow
        .withLatestFrom(fast, (s, f) -> s + ":" + f)
            .forEach(System.out::println);
    '''

    the output like
    S0:F1
    S1:F2
    S2:F4
    S3:F5
    S4:F7
    S5:F9
    S6:F11
    ...

    now as the output you can see. 
    the slow stream is primary.and all slow events appear exactly once.
    and the fast stream is just a helper used only when slow emits something.
    so some fast events are dropped.


    2016-12-26 07:05

    when two Observable has different space. one slow and one fast.
    but slow occurs first and then fast occors.
    when 
    slow.withLastestFrom(fast,(s,f)-> sth)
    at the beginning slow emit 1,2,3,4
    but the fast doesn't emit anything
    this will cause some slow event dropped.
    so the startWith() operator introduced.
'''
  Observable<String> fast = interval(10, MILLISECONDS)
  .map(x -> "F" + x)
  .delay(100, MILLISECONDS)
  .startWith("FX");
  
 Observable<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);
 slow
 .withLatestFrom(fast, (s, f) -> s + ":" + f)
 .forEach(System.out::println);
'''
the output looks like:
S0:FX
S1:FX
S2:FX
S3:FX
S4:FX
S5:FX
S6:F1
S7:F3
S9:F6
...

now noone from the primary slow event dropped.



amb() and ambWith()
amb() is instance method and ambWith() is static method but works same.

which amb() does is first subscribe all Observable.
but when a Observable emit first.
amb() suddenly ununsubscribe() b Observable.

'''
public static Observable<String> stream(int initialDelay, int interval, String name) {
  return Observable
      .interval(initialDelay, interval, MILLISECONDS)
      .map(x -> name + x)
      .doOnSubscribe(() -> System.out.println("Subscribe to " + name))
      .doOnUnsubscribe(() -> System.out.println("UUUUUUnsubscribe from " + name));
}

 public static void justDropSlower() {
    Observable.amb(
        stream(100, 17, "S"),
        stream(200, 10, "F")
    ).subscribe(System.out::println);

    TUtils.sleep();
  }

'''
the output looks like

Subscribe to S
Subscribe to F
UUUUUUnsubscribe from F
S0
S1
S2
S3
...



Scanning Through the Sequence with Scan and Reduce
consider an Observable<Long> that monitros progress of data transfer.
Every time a chunk of data is send,a single Long value appears telling.
indicating the size of that chunk.
but we realy want to know is how many bytes were transferred in total.

a very bad idea is to use global state modified inside an operator.like

'''
Observable<long> progress = transferFile();

LongAdder total = new LongAdder();
progress.subscribe(total::add);
'''
the preceding code can lead to very unpleasant concurrency buts.
the Lamba expressions within operators can be executed from 
arbitrary threads so global state must be thread safe.

you can implement this relatively complex workflow easily by using
the scan() operator:
'''
Observable<long> progress = transferFile();

Observable<long> totals = progress.scan((total,chunk)->total+chunk);
'''

also an overloaded version of scan() can provide an initial value
if it is different than simply the first element.
'''
Observable<BigInteger> fact = Observable
.range(2,100)
.scan(BigInteger.ONE,(big,cur)->
big.multiply(BigInteger.valueof(cur)));
'''
fact will generate 1,2,6,24,120,720... and so forth.
Notice that the upstream Observable starts from 2
but the downstream starts from 1.which was our initial value.(BigInteger.ONE)


what about we realy wanna the total?
'''
public <R> Observable<R> reduce(
R initialValue,
Func2<R,T,R> accumulator){
return can(initialValue,accumulator).takeLast(1);
'''

Next day
    Reduction with Mutable Accumulator: collect()

    the single() operator:
    Asserting an Observable has exactly one value or onError invoked
    notice one value.which means only one value emitted.
    Event you emit the same event everytime.onError occured.
    must be only just one value.
    '''
      //onError will be invoked
     Observable.just(1,1,1,1)
             .single()
             .subscribe(System.out::println);
    '''







'''
  Observable<List<Integer>> all = Observable
        .range(10, 20)
        .reduce(new ArrayList<>(), (list, item) -> {
          list.add(item);
          return list;
        });

    //a handy way
    Observable<List<Integer>> handyAll = Observable
        .range(10, 20)
        .collect(ArrayList::new, List::add);

    all.forEach(System.out::print);

    System.out.println("\n\n=========handyAll\n");

    handyAll.forEach(System.out::print);

    System.out.println("\n\n=========toList\n");

    Observable.range(10, 20).toList().forEach(System.out::print);

    System.out.println("\n\n=========collect used in StringBuffer\n");

    Observable
        .range(1, 10)
        .collect(StringBuilder::new,
            (sb, x) -> sb.append(x).append(", "))
        .map(StringBuilder::toString)
        .subscribe(d -> System.out.println(d));
'''

2016-12-22 11:09
introduce rx.subjects.Subject
a Subject is an Observable and also an Observer

public Subject<T,R> extends Observable<R> implements Observe<T>

you can use Subject to avoid manually managing the Subscribers in Twitter Demo.
use PublishSubject like the book writed.

but every subscriber subscribe() to PublishSubject
we also establish a http connection?

how can we avoid wasting connection and let all Subscribers share a connection?
    this is what the publish().refCount() realy does.
    and the share() == publish().refCount()
    
    The publish().refCount() tandem wrapped the underlying Observable and intercepted all subscriptions.

Another useful use case of the publish() operator is forcing subscription in the absence of any Subscriber.

what if you wanna store each event before expose it to your client?

a naive approach
    Observable<Status> tweets = //
    return tweets.doOnNext(this::saveStatus);

but Observables are lazy by desing.which means if no one 
subscribe to the tweets Observable. the saveStatus() never called.

what we really want is a fake Observer that does not really listen to events
but forces upstream Observables to produce events.

the solution is publish() connect()
ConnectableObservable<Status> published = Observable.publish();
published.connect()

every subscriber who subscribe to the ConnectableObservable
will receive the same sequence of events.
but the origin observable's create() method only triggered once


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









------------------------------------------------------------

------------------------------------------------------------





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




