package com.example.jwt.service;

import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public interface IRedisService {

    /**
     * 往redis里面添加一个key
     * @param key key值
     */
    void set(String key);

    /**
     * 往redis里面添加一个key
     * @param key key值
     * @param timeout 存活时间（分钟)
     */
    void set(String key, Long timeout);

    /**
     * 往redis里面添加一个key
     * @param key key值
     * @param timeout 剩余时间
     */
    void set(String key, Duration timeout);

    /**
     * 检查redis里是否有key
     * @param key 待检查的key
     * @return 存在返回true，不存在返回false
     */
    boolean check(String key);

    /**
     * 从redis里面删除一个key
     * @param key 待删除的key
     * @return 删除成功返回true，不成功返回false
     */
    boolean delete(String key);
}
