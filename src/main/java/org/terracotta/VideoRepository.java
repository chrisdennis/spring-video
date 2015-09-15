/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terracotta;

import java.util.HashMap;
import java.util.Map;
import org.ehcache.config.loaderwriter.DefaultCacheLoaderWriterConfiguration;
import org.ehcache.exceptions.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static org.ehcache.config.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.writebehind.WriteBehindConfigurationBuilder.newWriteBehindConfiguration;
import static org.ehcache.jsr107.Eh107Configuration.fromEhcacheCacheConfiguration;

/**
 *
 * @author cdennis
 */
@Component
@CacheConfig(cacheNames = "video-cache")
public class VideoRepository {

  @Component
  public static class CacheCustomizer implements JCacheManagerCustomizer {

    @Override
    public void customize(javax.cache.CacheManager manager) {
      manager.createCache("video-cache", fromEhcacheCacheConfiguration(
              newCacheConfigurationBuilder()
                      .add(new DefaultCacheLoaderWriterConfiguration(RealVideoRepository.class))
                      .add(newWriteBehindConfiguration()
                              .batchSize(10)
                              .delay(2, 5)
                              .enableCoalescing())
              .buildConfig(Object.class, Object.class)));
    }
  }
  
  @CachePut(key = "#video.getId()")
  public Video save(Video video) {
    return video;
  }

  @Cacheable
  public Video read(int id) {
    throw new AssertionError();
  }

  @CacheEvict
  public void delete(int id) {
    //no-op
  }

  public static class RealVideoRepository implements CacheLoaderWriter<Integer, Video> {

    private final Map<Integer, Video> videos = new HashMap<>();

    public Video save(Video video) {
      System.err.println("SAVING VIDEO " + video);
      work();
      videos.put(video.getId(), video);
      return video;
    }

    public Video read(int id) {
      System.err.println("READING VIDEO " + id);
      work();
      return videos.get(id);
    }

    public void delete(int id) {
      System.err.println("DELETING VIDEO " + id);
      work();
      videos.remove(id);
    }

    private static final void work() {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    @Override
    public Video load(Integer key) throws Exception {
      return read(key);
    }

    @Override
    public Map<Integer, Video> loadAll(Iterable<? extends Integer> keys) throws Exception {
      throw new UnsupportedOperationException();
    }

    @Override
    public void write(Integer key, Video value) throws Exception {
      save(value);
    }

    @Override
    public void writeAll(Iterable<? extends Map.Entry<? extends Integer, ? extends Video>> entries) throws BulkCacheWritingException, Exception {
      for (Map.Entry<? extends Integer, ? extends Video> e : entries) {
        write(e.getKey(), e.getValue());
      }
    }

    @Override
    public void delete(Integer key) throws Exception {
      delete(key.intValue());
    }

    @Override
    public void deleteAll(Iterable<? extends Integer> keys) throws BulkCacheWritingException, Exception {
      for (Integer k : keys) {
        delete(k);
      }
    }
  }
}
