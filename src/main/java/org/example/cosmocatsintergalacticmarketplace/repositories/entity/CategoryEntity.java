package org.example.cosmocatsintergalacticmarketplace.repositories.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_category")
    @SequenceGenerator(name = "seq_category", sequenceName = "seq_category", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;
}