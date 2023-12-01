package site.bleem.boot.web.cache;

public interface ILocalCache {
    void put(String key, Integer value);

    Integer get(String key);
}