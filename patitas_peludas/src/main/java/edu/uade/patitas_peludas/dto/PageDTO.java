package edu.uade.patitas_peludas.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<DTO> {
    List<DTO> content;
    Integer totalPages;
    Long totalElements;
    Integer currentPage;
    Integer pageSize;
}
