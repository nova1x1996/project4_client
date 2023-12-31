/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package group2.client.repository;

import group2.client.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author hokim
 */
public interface NewsRepository extends JpaRepository<News, Integer> {
    public News findByTitleAndContent(String title, String content);
    public News findByTitle(String title);
}
