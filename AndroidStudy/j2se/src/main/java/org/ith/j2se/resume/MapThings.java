package org.ith.j2se.resume;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tanghao on 12/12/16.
 */

public class MapThings {
  public static void main(String[] args) {
    Map<String, String> hm = new HashMap<>();
    fillData(hm);
    hm.put(null,null);
    popData(hm);
    hm = new TreeMap<>();
//    hm.put(null,null);
    fillData(hm);
    popData(hm);
    hm = new LinkedHashMap<>();
    hm.put(null,null);
    fillData(hm);
    popData(hm);

    hm = new Hashtable<>();
    hm.put("a","av");
    hm.put("b","bv");
    hm.put("c","cv");
    System.out.println(hm.toString());
  }

  /**

   HashMap和LinkedHashMap都允许null作key和value
   但是TreeMap不允许null作key或者value.

   HashMap 不能保证元素顺序
   TreeMap和LinkedHashMap可以

   HashMap底层通过一个Node数组来实现.
   Node就是一个实现了Map.Entry<K,V>接口的单链表
   内部保存其自己的k,v,和下一个Node
   Map.Entry就是一个规定了put,get等方法的基本接口
   第一次put东西的时候才开始扩容.
   计算key的hash值.然后生成新的newNode来决定其在数组中的位置
   tab[i] = newNode(hash, key, value, null);
   get方法内部调用getNode(int hash,Object key)来实现
   根据hash值取出对应底层数组对应索引的值
   first = tab[(n-1) & hash]
   如果first为空直接return null
   否则如果first的key和当前的key相等.则return first
   否则说明发生hash冲突了.
   遍历直到当key相等并且hash值相等时返回

   TreeMap底层是一颗红黑树

   LinkedHashMap extends HashMap implements Map接口
   对这个Map接口就是HashMap底层数组元素Map.Entry所实现的Map接口
   并且多了两个变量

   The head (eldest) of the doubly linked list.
   transient LinkedHashMap.Entry<K,V> head;
   The tail (youngest) of the doubly linked list.
   transient LinkedHashMap.Entry<K,V> tail;

   LinkedHashMap的构造方法多了一个accessOrder的boolean变量
   如果accessOrder为true了.Lru(least rencently used)的基础就是他.
   该LinkedHashMap在get元素的时候.
   会进行一些操作来重新赋值head tail元素的值
   这么一来.在底层数组顶端的元素.就是lru的那个了.


   *
   */

  private static void popData(Map<String, String> hm) {
    System.out.println(hm.getClass().getSimpleName() + " --> " + hm.toString());
  }

  public static void fillData(Map<String, String> hm) {
    hm.put("k_a", "v_a");
    hm.put("k_b", "v_b");
    hm.put("k_c", "v_c");
  }
}
