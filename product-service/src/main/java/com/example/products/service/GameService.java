package com.example.products.service;

import com.example.products.dto.GameDto;
import com.example.products.entity.games.Genre;
import com.example.products.entity.games.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface GameService {

    List<GameDto> getAllGames();
    GameDto getGameById(String id);

    Page<GameDto> getGamesFiltered(
            String title,
            Platform platform,
            Genre genre,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}
