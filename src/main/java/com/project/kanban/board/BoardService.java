package com.project.kanban.board;

import com.project.kanban.workspace.Workspace;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    List<Board> getBoards(Workspace workspace);
    Optional<Board> getBoard(long workspaceId, long boardId);
    Board createBoard(BoardDTO requestBody, Workspace workspace);
    Board updateBoard(Board updatedBoard, BoardDTO requestBody);
    void deleteBoard(long boardId);
    Board saveBoard(Board board);

}
