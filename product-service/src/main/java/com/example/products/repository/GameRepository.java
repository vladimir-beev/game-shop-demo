package com.example.products.repository;

import com.example.products.entity.games.Game;
import com.example.products.entity.games.Genre;
import com.example.products.entity.games.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, String>, JpaSpecificationExecutor<Game> {
    Optional<Game> findByTitle(String title);

    List<Game> findByGenre(Genre genre);
    List<Game> findByGenre(Genre genre, Pageable pageable);

    List<Game> findByPlatform(Platform platform);
    List<Game> findByPlatform(Platform platform, Pageable pageable);
}
