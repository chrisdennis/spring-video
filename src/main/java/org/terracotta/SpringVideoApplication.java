package org.terracotta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringVideoApplication implements ApplicationRunner {

  public static void main(String[] args) {
      SpringApplication.run(SpringVideoApplication.class, args).close();
  }

  @Autowired VideoRepository repository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    repository.save(new Video(69, "My Embarassing Viral Video"));

    for (int i = 0; i < 11; i++) {
      Video video = repository.read(69);
      Video watched = video.watch();
      repository.save(watched);
    }

    Thread.sleep(6000);
    
    repository.delete(69);
  }
}
