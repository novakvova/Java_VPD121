package org.example.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryItemDTO {
    private int id;
    private String name;
    private String description;
    private String image;
}
