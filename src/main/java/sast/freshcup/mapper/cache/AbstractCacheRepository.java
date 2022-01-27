package sast.freshcup.mapper.cache;

import java.util.function.Consumer;

/**
 * @author: 風楪fy
 * @create: 2022-01-25 14:21
 **/
public abstract class AbstractCacheRepository<K, V> {
    /**
     * 取代null放入缓存，用于防止缓存击穿
     * 必须是单例
     */
    protected abstract V nullThing();

    /**
     * 从缓存中取值
     *
     * @param key
     * @return
     */
    protected abstract V getCache(K key);

    /**
     * 如果缓存中不存在值，获取值的方法（通常是从数据库获取）
     *
     * @param key
     * @return
     */
    protected abstract V getIfAbsent(K key);

    /**
     * 更新缓存
     *
     * @param key
     */
    protected abstract void updateCache(K key, V value);

    /**
     * 清除缓存
     *
     * @param key
     */
    protected abstract void removeCache(K key);

    /**
     * 对缓存进行 consumer 处理
     *
     * @param key
     * @param consumer
     */
    protected abstract void processCache(K key, Consumer<V> consumer);

}
