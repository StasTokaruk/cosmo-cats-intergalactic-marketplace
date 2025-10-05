package org.example.cosmocatsintergalacticmarketplace.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;
}
