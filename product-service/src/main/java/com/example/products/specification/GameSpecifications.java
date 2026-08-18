package com.example.products.specification;

import com.example.products.entity.games.Game;
import com.example.products.entity.games.Genre;
import com.example.products.entity.games.Platform;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class GameSpecifications {

    public static Specification<Game> titleContains(String title) {
        return (
                (root, query, cb) -> {
                    if (title == null || title.isBlank()) {
                        return null;
                    }

                    return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
                }
        );
    }

    public static Specification<Game> hasPlatform(Platform platform) {
        return (
                (root, query, cb) ->
                        platform == null ? null : cb.equal(root.get("platform"), platform)
        );
    }

    public static Specification<Game> hasGenre(Genre genre) {
        return (
                (root, query, cb) ->
                        genre == null ? null : cb.equal(root.get("genre"), genre)
        );
    }

    public static Specification<Game> priceBetween(BigDecimal min, BigDecimal max) {
        return (
                (root, query, cb) -> {
                    if (min == null && max == null) {
                        return null;
                    }

                    if (min != null && max != null) {
                        return cb.between(root.get("price"), min, max);
                    }

                    if (min != null) {
                        return cb.greaterThanOrEqualTo(root.get("price"), min);
                    }

                    return cb.lessThanOrEqualTo(root.get("price"), max);
                }
        );
    }
}
