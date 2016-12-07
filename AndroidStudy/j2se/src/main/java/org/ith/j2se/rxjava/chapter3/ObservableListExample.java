package org.ith.j2se.rxjava.chapter3;

/**
 * Created by tanghao on 12/7/16.
 */

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ObservableListExample {

  public static void main(String[] args) throws Exception {
    ObservableList<Integer> olist = new ObservableList<>();

    olist.getObservable().subscribe(System.out::println);

    olist.add(1);
    Thread.sleep(1000);
    olist.add(2);
    Thread.sleep(1000);
    olist.add(3);
  }

  public static class ObservableList<T> {

    protected final List<T> list;
    protected final PublishSubject<T> onAdd;

    public ObservableList() {
      this.list = new ArrayList<T>();
      this.onAdd = PublishSubject.create();
    }

    public void add(T value) {
      list.add(value);
      onAdd.onNext(value);
    }

    public Observable<T> getObservable() {
      return onAdd;
    }
  }
}