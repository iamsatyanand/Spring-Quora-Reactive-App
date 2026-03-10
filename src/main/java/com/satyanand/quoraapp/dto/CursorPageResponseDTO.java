package com.satyanand.quoraapp.dto;

import java.util.List;

public class CursorPageResponseDTO<T> {

    private List<T> data;
    private CursorPaginationDTO pagination;
}
