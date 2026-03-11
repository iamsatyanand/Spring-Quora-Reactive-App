package com.satyanand.quoraapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursorPaginationDTO {

    private String nextCursor;
    private String prevCursor;
    private boolean hasNext;
    private boolean hasPrev;
    private int size;

}
