package com.example.products.service;

import com.example.products.dto.GameDto;
import com.example.products.entity.games.Game;
import com.example.products.entity.games.Genre;
import com.example.products.entity.games.Platform;
import com.example.products.repository.GameRepository;
import com.example.products.specification.GameSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public List<GameDto> getAllGames() {
        List<Game> games = gameRepository.findAll();

        if (!games.isEmpty()) {
            return getGameDtoList(games);
        }

        return null;
    }

    @Override
    public GameDto getGameById(String id) {
        return toDto(gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + id)));
    }

    @Override
    public Page<GameDto> getGamesFiltered(
            String title,
            Platform platform,
            Genre genre,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        Specification<Game> spec = Specification
                .where(GameSpecifications.hasPlatform(platform))
                .and(GameSpecifications.hasGenre(genre))
                .and(GameSpecifications.priceBetween(minPrice, maxPrice))
                .and(GameSpecifications.titleContains(title));

        return gameRepository.findAll(spec, pageable)
                .map(this::toDto);
    }

    private GameDto toDto(Game game) {
        return new GameDto(
                game.getId(),
                game.getTitle(),
                game.getPrice(),
                game.getCoverImageUrl(),
                game.getDescription(),
                game.getSku(),
                "Game",
                game.getPlatform(),
                game.getGenre(),
                game.getReleaseDate(),
                game.getPublisher(),
                game.getDeveloper()
        );
    }

    private @NonNull List<GameDto> getGameDtoList(List<Game> games) {
        return games.stream()
                .map(this::toDto)
                .toList();
    }
}
