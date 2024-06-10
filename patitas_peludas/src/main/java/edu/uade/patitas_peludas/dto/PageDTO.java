package edu.uade.patitas_peludas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
