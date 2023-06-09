package com.project.kanban.board;

import com.project.kanban.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> getBoardsByWorkspace(Workspace workspace);
    Optional<Board> getBoardByWorkspaceIdAndId(long workspaceId, long id);
    List<Board> getBoardsByWorkspaceId(long workspaceId);
}
