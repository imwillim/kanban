package com.project.kanban.board;

import com.project.kanban.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Override
    public List<Board> getBoards(Workspace workspace) {
        return boardRepository.getBoardsByWorkspace(workspace);
    }

    @Override
    public Optional<Board> getBoard(long workspaceId, long boardId) {
        return boardRepository.getBoardByWorkspaceIdAndId(workspaceId, boardId);
    }

    @Override
    public Board createBoard(BoardDTO requestBody, Workspace workspace) {
        return new Board(requestBody.getTitle(), requestBody.getDescription(), workspace);
    }

    @Override
    public Board updateBoard(Board updatedBoard, BoardDTO boardDTO) {
        if (updatedBoard != null){
            updatedBoard.setTitle(boardDTO.getTitle());
            updatedBoard.setDescription(boardDTO.getDescription());
            updatedBoard.setUpdatedAt(Timestamp.from(Instant.now()).getTime());
            return updatedBoard;
        }
        return null;
    }

    @Override
    public void deleteBoard(long boardId) {
        boardRepository.deleteById(boardId);
    }

    @Override
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }
}
