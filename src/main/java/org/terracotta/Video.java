package org.terracotta;

public class Video {

  private final String title;
  private final int id;
  private final int views;
  
  public Video(int id, String title) {
    this(id, title, 0);
  }
  
  private Video(int id, String title, int views) {
    this.id = id;
    this.title = title;
    this.views = views;
  }

  public int getId() {
    return id;
  }
  
  Video watch() {
    return new Video(id, title, views + 1);
  }
  
  public String toString() {
    return "Video: " + title + " views-count=" + views;
  }
}
