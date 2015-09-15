/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terracotta;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 *
 * @author cdennis
 */
@Component
@CacheConfig(cacheNames = "video-cache")
public class VideoRepository {

  private final Map<Integer, Video> videos = new HashMap<>();

  @CachePut(key = "#video.getId()")
  public Video save(Video video) {
    System.err.println("SAVING VIDEO " + video);
    work();
    videos.put(video.getId(), video);
    return video;
  }

  @Cacheable
  public Video read(int id) {
    System.err.println("READING VIDEO " + id);
    work();
    return videos.get(id);
  }

  @CacheEvict
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
}
