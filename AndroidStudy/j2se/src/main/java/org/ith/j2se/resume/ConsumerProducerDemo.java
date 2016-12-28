package org.ith.j2se.resume;

/**
 * Created by tanghao on 12/26/16. demonstrate the producer consumer question
 */
public class ConsumerProducerDemo {
  public static void main(String[] args) {

    Resource resource = new Resource();

    Producer producer = new Producer(resource);
    Consumer consumer = new Consumer(resource);

    Thread tProduct = new Thread(producer);
    Thread tConsume = new Thread(consumer);

    tProduct.start();
    tConsume.start();
  }
}

class Resource {
  String name;
  int age;
  boolean hasData;
}

class Consumer implements Runnable {
  Resource res;

  public Consumer(Resource res) {
    this.res = res;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (res) {
        if (res.hasData) {
          System.out.println("Consume--->" + res.name + ":" + res.age + "\n");
          res.hasData = false;
          //notify the producer to product data
          res.notify();
        } else {
          try {
            //wait for the producer to product data
            res.wait();
          } catch (Exception e) {

          }
        }
      }
    }
  }
}

class Producer implements Runnable {
  Resource res;

  public Producer(Resource resource) {
    this.res = resource;
  }

  @Override
  public void run() {
    int swap = 0;

    while (true) {
      synchronized (res) {
        if (res.hasData) {
          try {
            res.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {

          if (swap == 0) {
            res.age = 10;
            res.name = "China";
          } else {
            res.age = 250;
            res.name = "Ameria";
          }
          swap = (++swap) % 2;
          res.hasData = true;
          res.notify();
          System.out.println("Produce---------------------------------->" + res.name + ":" + res.age);
        }
      }
    }
  }
}
