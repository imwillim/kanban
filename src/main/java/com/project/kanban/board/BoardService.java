package com.project.kanban.board;

import com.project.kanban.workspace.Workspace;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BoardService {
    List<Board> getBoards(Workspace workspace);

    Optional<Board> getBoard(long boardId);

    Board createBoard(BoardDTO requestBody, Workspace workspace);

    Board updateBoard(long boardId, BoardDTO requestBody);
    void deleteBoard(long boardId);

    Board saveBoard(Board board);
}
