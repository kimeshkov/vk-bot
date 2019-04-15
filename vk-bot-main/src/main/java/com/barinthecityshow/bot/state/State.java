package com.barinthecityshow.bot.state;

public interface State<K, V> {

    void put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

}
