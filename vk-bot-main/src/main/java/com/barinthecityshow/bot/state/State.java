package com.barinthecityshow.bot.state;

public interface State<K, V> {

    void put(K key, V value);

    V get(K key);

    boolean containsKey(K key);

}
