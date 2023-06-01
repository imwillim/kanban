package com.project.kanban.board;

import com.project.kanban.workspace.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Optional<Board> getBoard(long boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public Board createBoard(BoardDTO requestBody, Workspace workspace) {
        return new Board(requestBody.getTitle(), requestBody.getDescription(), workspace);
    }

    @Override
    public Board updateBoard(long boardId, BoardDTO boardDTO) {
        Board updatedBoard = getBoard(boardId).orElse(null);
        if (updatedBoard != null){
            updatedBoard.setTitle(boardDTO.getTitle());
            updatedBoard.setDescription(boardDTO.getDescription());
            updatedBoard.setUpdatedAt(LocalDateTime.now());
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
