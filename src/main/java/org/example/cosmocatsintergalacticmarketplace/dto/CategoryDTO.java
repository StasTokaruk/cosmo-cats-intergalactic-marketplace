package org.example.cosmocatsintergalacticmarketplace.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;
}
