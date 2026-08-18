package com.example.products.entity.games;

import com.example.products.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "games")
@DiscriminatorValue("GAME")
public class Game extends Product {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    private LocalDate releaseDate;

    private String publisher;

    private String developer;

    @Enumerated(EnumType.STRING)
    private Genre genre;
}
