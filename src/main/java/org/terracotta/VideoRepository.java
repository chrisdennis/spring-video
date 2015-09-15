/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terracotta;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author cdennis
 */
@Component
public class VideoRepository {

  private final Map<Integer, Video> videos = new HashMap<>();

  public void save(Video video) {
    System.err.println("SAVING VIDEO " + video);
    work();
    videos.put(video.getId(), video);
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
}
