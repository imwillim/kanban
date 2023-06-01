package com.project.kanban.board;


import java.util.List;
import java.util.Optional;


public interface BoardFacadeService {
    List<BoardDTO> getBoardsProcess(long workspaceId);
    Optional<BoardDTO> getBoardProcess(long workspaceId, long boardId);
    Optional<BoardDTO> createBoardProcess(long workspaceId, BoardDTO boardDTO);
    Optional<BoardDTO> updateBoardProcess(long workspaceId, long boardId, BoardDTO boardDTO);
    void deleteBoardProcess(long workspaceId, long boardId);
}
