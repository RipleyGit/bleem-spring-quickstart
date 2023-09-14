package site.bleem.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class RedisService<T> {
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    private RedisTemplate redisTemplate;

    public RedisService() {
    }

    public boolean setExpireTime(String key, long time) {
        try {
            int randNum = (new Random()).nextInt(91) + 10;
            time += (long)randNum;
            return this.setExpireTime(key, time, TimeUnit.SECONDS);
        } catch (Exception var5) {
            log.error("redis setExpireTime exception:", var5);
            return false;
        }
    }

    public boolean setExpireTime(String key, long time, TimeUnit unit) {
        try {
            if (time > 0L) {
                this.redisTemplate.expire(key, time, unit);
            }

            return true;
        } catch (Exception var6) {
            log.error("redis setExpireTime exception:", var6);
            return false;
        }
    }

    public long getExpireTime(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean setExpireAt(String key, Date date) {
        return this.redisTemplate.expireAt(key, date);
    }

    public void removePattern(String pattern) {
        Set<Serializable> keys = this.redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            this.redisTemplate.delete(keys);
        }

    }

    public void remove(String key) {
        if (this.hasKey(key)) {
            this.redisTemplate.delete(key);
        }

    }

    public void remove(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.redisTemplate.delete(key[0]);
            } else {
                this.redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }

    }

    public boolean hasKey(String key) {
        try {
            return this.redisTemplate.hasKey(key);
        } catch (Exception var3) {
            log.error("redis hasKey exception:", var3);
            return false;
        }
    }

    public Set<String> keys(String pattern) {
        Set<String> keys = this.redisTemplate.keys(pattern);
        return keys.size() > 0 ? keys : null;
    }

    public Object get(String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public boolean set(String key, Object value) {
        boolean result = false;

        try {
            ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception var5) {
            log.error("redis set exception:", var5);
        }

        return result;
    }

    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;

        try {
            ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
            this.redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception var6) {
            log.error("redis set exception:", var6);
        }

        return result;
    }

    public boolean set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;

        try {
            ValueOperations<Serializable, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
            this.redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception var7) {
            log.error("redis set exception:", var7);
        }

        return result;
    }

    public long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, delta);
        }
    }

    public long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, -delta);
        }
    }

    public Object hGet(String key, String item) {
        try {
            return this.redisTemplate.opsForHash().get(key, item);
        } catch (Exception var4) {
            log.error("redis hGet exception:", var4);
            return null;
        }
    }

    public List<T> hGetList(String key) {
        return this.redisTemplate.opsForHash().values(key);
    }

    public Map<Object, Object> hmGet(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var4) {
            log.error("redis hmSet exception:", var4);
            return false;
        }
    }

    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                this.setExpireTime(key, time);
            }

            return true;
        } catch (Exception var6) {
            log.error("redis hmSet exception:", var6);
            return false;
        }
    }

    public boolean hSet(String key, String item, Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var5) {
            log.error("redis hSet exception:", var5);
            return false;
        }
    }

    public boolean hSet(String key, String item, Object value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                this.setExpireTime(key, time);
            }

            return true;
        } catch (Exception var7) {
            log.error("redis hSet exception:", var7);
            return false;
        }
    }

    public void hDel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item);
    }

    public double hincr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }

    public double hdecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }

    public Set<Object> sGet(String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (Exception var3) {
            log.error("redis sGet exception:", var3);
            return null;
        }
    }

    public boolean sHasKey(String key, Object value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var4) {
            log.error("redis sHasKey exception:", var4);
            return false;
        }
    }

    public long sSet(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (Exception var4) {
            log.error("redis sSet exception:", var4);
            return 0L;
        }
    }

    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                this.setExpireTime(key, time);
            }

            return count;
        } catch (Exception var6) {
            log.error("redis sSetAndTime exception:", var6);
            return 0L;
        }
    }

    public long sGetSetSize(String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (Exception var3) {
            log.error("redis sGetSetSize exception:", var3);
            return 0L;
        }
    }

    public long setRemove(String key, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception var4) {
            log.error("redis setRemove exception:", var4);
            return 0L;
        }
    }

    public List<Object> lGet(String key, long start, long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var7) {
            log.error("redis lGet exception:", var7);
            return null;
        }
    }

    public List<Object> lGet(String key) {
        return this.lGet(key, 0L, -1L);
    }

    public long lGetListSize(String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (Exception var3) {
            log.error("redis lGetListSize exception:", var3);
            return 0L;
        }
    }

    public Object lGetIndex(String key, long index) {
        try {
            return this.redisTemplate.opsForList().index(key, index);
        } catch (Exception var5) {
            log.error("redis lGetIndex exception:", var5);
            return null;
        }
    }

    public boolean lSet(String key, Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var4) {
            log.error("redis lSet exception:", var4);
            return false;
        }
    }

    public boolean lLeftPush(String key, Object value) {
        try {
            this.redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception var4) {
            log.error("redis lLeftPush exception:", var4);
            return false;
        }
    }

    public boolean lRightPush(String key, Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var4) {
            log.error("redis lRightPush exception:", var4);
            return false;
        }
    }

    public boolean lSet(String key, Object value, long time) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                this.setExpireTime(key, time);
            }

            return true;
        } catch (Exception var6) {
            log.error("redis lSet exception:", var6);
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception var4) {
            log.error("redis lSet exception:", var4);
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value, long time) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                this.setExpireTime(key, time);
            }

            return true;
        } catch (Exception var6) {
            log.error("redis lSet exception:", var6);
            return false;
        }
    }

    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var6) {
            log.error("redis lUpdateIndex exception:", var6);
            return false;
        }
    }

    public Object lPop(String key) {
        try {
            return this.redisTemplate.opsForList().leftPop(key);
        } catch (Exception var3) {
            log.error("redis lPop exception:", var3);
            return null;
        }
    }

    public Object rPop(String key) {
        try {
            return this.redisTemplate.opsForList().rightPop(key);
        } catch (Exception var3) {
            log.error("redis rPop exception:", var3);
            return null;
        }
    }

    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = this.redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception var6) {
            log.error("redis lRemove exception:", var6);
            return 0L;
        }
    }

    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    public void setArray(String key, Object value) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        set.add(key, new Object[]{value});
    }

    public Set<Object> getArray(String key) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        return set.members(key);
    }

    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = this.redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = this.redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    public Set<Object> zTopNum(String key, Long topNum) {
        ZSetOperations<String, Object> zset = this.redisTemplate.opsForZSet();
        return zset.reverseRange(key, 0L, topNum - 1L);
    }

    public Double zIncrementScore(String key, Object value, double increment) {
        ZSetOperations<String, Object> zset = this.redisTemplate.opsForZSet();
        return zset.incrementScore(key, value, increment);
    }

    public Long zRemove(String key, Object value) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.remove(key, new Object[]{value});
    }

    public Long zSize(String key) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.size(key);
    }

    public Set<Object> zRange(String key, Long start, Long end) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.range(key, start, end);
    }

    public Set<Object> zReverseRange(String key, Long start, Long end) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.reverseRange(key, start, end);
    }

    public Long zRank(String key, Object value) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.rank(key, value);
    }

    public Long zReverseRank(String key, Object value) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.reverseRank(key, value);
    }

    public Double zScore(String key, Object value) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.score(key, value);
    }

    public boolean lock(String key, String value) {
        if (this.redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        } else {
            String currentValue = (String)this.redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
                String oldValues = (String)this.redisTemplate.opsForValue().getAndSet(key, value);
                if (!StringUtils.isEmpty(oldValues) && oldValues.equals(currentValue)) {
                    return true;
                }
            }

            return false;
        }
    }

    public void unlock(String key, String value) {
        try {
            String currentValue = (String)this.redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                this.redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception var4) {
            log.error("『redis分布式锁』解锁异常，{}", var4);
        }

    }

    public Set<T> zRangeByScore(String key, double min, double max) {
        ZSetOperations zSet = this.redisTemplate.opsForZSet();
        return zSet.rangeByScore(key, min, max);
    }

    public Set<T> zReverseRangeByScore(String key, double min, double max) {
        ZSetOperations zSet = this.redisTemplate.opsForZSet();
        return zSet.reverseRangeByScore(key, min, max);
    }

    public Long zRemoveRangeByScore(String key, double min, double max) {
        ZSetOperations zSet = this.redisTemplate.opsForZSet();
        return zSet.removeRangeByScore(key, min, max);
    }

    public Long zRemoveRange(String key, long min, long max) {
        ZSetOperations zSet = this.redisTemplate.opsForZSet();
        return zSet.removeRange(key, min, max);
    }
}
