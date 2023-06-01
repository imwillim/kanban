package com.project.kanban.listing;

import com.project.kanban.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> getListingsByBoard(Board board);
    Optional<Listing> getListingsById(long listingId);

}
