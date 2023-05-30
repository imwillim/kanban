package com.project.kanban.board;

import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class BoardDTOMapper implements Function<Board, BoardDTO> {
    @Override
    public BoardDTO apply(Board board) {
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getDescription(),
                board.isRemoved(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                board.getWorkspace().getId(),
                board.getListings().stream().toList()
        );
    }
}
