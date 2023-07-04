package com.project.kanban.listing;

import com.project.kanban.board.Board;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ListingServiceImpl implements ListingService {
    private final ListingRepository listingRepository;

    public ListingServiceImpl(ListingRepository listingRepository){
        this.listingRepository = listingRepository;
    }

    @Override
    public Optional<Listing> getListing(long boardId, long listingId){
        return listingRepository.getListingByBoardIdAndId(boardId, listingId);
    }

    @Override
    public List<Listing> getListings(Board board) {
        return listingRepository.getListingsByBoard(board);
    }

    @Override
    public Listing createListing(ListingDTO requestBody, Board board) {
        Listing listing = new Listing(requestBody.getTitle(), board.getListings().size(), board);
        return listingRepository.save(listing);
    }

    @Override
    public Listing updateListing(Listing updatedListing, ListingDTO requestBody) {
        if (updatedListing != null){
            updatedListing.setTitle(requestBody.getTitle());
            updatedListing.setUpdatedAt(Timestamp.from(Instant.now()).getTime());
            return listingRepository.save(updatedListing);
        }
        return null;
    }

    @Override
    public void deleteListing(long listingId) {
        listingRepository.deleteById(listingId);
    }

    @Override
    public Listing modifyArchivedListing(Listing updatedListing, ListingDTO listingDTO) {
        if (updatedListing != null){
            updatedListing.setArchived(listingDTO.isArchived());
            return listingRepository.save(updatedListing);
        }
        return null;
    }

    @Override
    public Listing modifyOrderListing(Listing updatedListing, int columnOrder) {
        if (updatedListing != null) {
            updatedListing.setColumnOrder(columnOrder);
        }
        return null;
    }

    @Override
    public Listing getListingByBoardIdAndColumnOrder(long boardId, int columnOrder) {
        return listingRepository.getListingByBoardIdAndColumnOrder(boardId, columnOrder);
    }

}
