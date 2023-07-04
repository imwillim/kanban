package com.project.kanban.board;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface BoardFacadeService {
    List<BoardDTO> getBoardsProcess(Authentication authentication, long workspaceId);
    Optional<BoardDTO> getBoardProcess(Authentication authentication, long workspaceId, long boardId);

    Optional<BoardDTO> createBoardProcess(Authentication authentication,
                                          long workspaceId, BoardDTO requestBody);

    Optional<BoardDTO> updateBoardProcess(Authentication authentication, long workspaceId,
                                          long boardId, BoardDTO boardDTO);
    void deleteBoardProcess(Authentication authentication, long workspaceId, long boardId);

    void assignBoard(Authentication authentication, long workspaceId,
                     long boardId, BoardAssignRequest boardAssignRequest);

}
