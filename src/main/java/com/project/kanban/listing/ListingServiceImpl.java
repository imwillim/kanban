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
        Listing listing = new Listing(requestBody.getTitle(), requestBody.getType(), board);
        return listingRepository.save(listing);
    }

    @Override
    public Listing updateListing(Listing updatedListing, ListingDTO requestBody) {
        if (updatedListing != null){
            updatedListing.setTitle(requestBody.getTitle());
            updatedListing.setType(ListingType.valueOf(requestBody.getType()));
            updatedListing.setUpdatedAt(Timestamp.from(Instant.now()).getTime());
            return listingRepository.save(updatedListing);
        }
        return null;

    }

    @Override
    public void deleteListing(long listingId) {
        listingRepository.deleteById(listingId);
    }

}
