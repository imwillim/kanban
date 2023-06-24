package com.project.kanban.listing;

import com.project.kanban.util.RequestBodyError;
import com.project.kanban.util.RequestBodyException;
import com.project.kanban.util.SuccessfulResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class ListingController {
    private final ListingFacadeService listingFacadeService;

    @Autowired
    public ListingController(ListingFacadeService listingFacadeService){
        this.listingFacadeService = listingFacadeService;
    }

    private static ResponseEntity<Object> notFoundListing(){
        log.error("Listing not found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings")
    public ResponseEntity<Object> getListings(Authentication authentication,
                                              @PathVariable("workspaceId") long workspaceId,
                                              @PathVariable("boardId") long boardId){
        List<ListingDTO> listings = listingFacadeService.getListingsProcess(workspaceId, boardId);
        log.info("Listings are returned.");
        return ResponseEntity.ok().body(new SuccessfulResponse(
                HttpStatus.OK.value(),
                "Listings are returned",
                listings));
    }


    @PostMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings")
    public ResponseEntity<Object> createListing(Authentication authentication,
                                                @PathVariable("workspaceId") long workspaceId,
                                                @PathVariable("boardId") long boardId,
                                                @Valid @RequestBody ListingDTO requestBody,
                                                BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<ListingDTO> createdListing = listingFacadeService
                .createListingProcess(authentication, workspaceId, boardId, requestBody);

        if (createdListing.isPresent()){
            URI createdListingURI = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdListing.get().getId()).toUri();
            log.info("Listing is created.");
            return ResponseEntity.created(createdListingURI).body(
                    new SuccessfulResponse(HttpStatus.CREATED.value(),
                    "New listing is created",
                    createdListing));
        }
        return notFoundListing();
    }


    @PutMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}")
    public ResponseEntity<Object> updateListing(Authentication authentication,
                                                @PathVariable("workspaceId") long workspaceId,
                                                @PathVariable("boardId") long boardId,
                                                @PathVariable("listingId") long listingId,
                                                @Valid @RequestBody ListingDTO requestBody,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<ListingDTO> listingDTO = listingFacadeService
                .updateListingProcess(authentication, workspaceId, boardId, listingId, requestBody);
        log.info("Listing is updated.");
        return ResponseEntity.accepted().body(
                new SuccessfulResponse(HttpStatus.ACCEPTED.value(),
                        "Listing is updated",
                        listingDTO));
    }


    @DeleteMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}")
    public ResponseEntity<Object> deleteListing(Authentication authentication,
                                                @PathVariable("workspaceId") long workspaceId,
                                                @PathVariable("boardId") long boardId,
                                                @PathVariable("listingId") long listingId){
        listingFacadeService.deleteListingProcess(authentication, workspaceId, boardId, listingId);
        log.info("Listing is deleted.");
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.NO_CONTENT.value(),
                        "Delete a listing",
                        null), HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/boards/{boardId}/listings/{listingId}/join")
    public ResponseEntity<Object> assignListing(Authentication authentication,
                                              @PathVariable("boardId") long boardId,
                                              @PathVariable("listingId") long listingId) {
        listingFacadeService.assignListing(authentication, boardId, listingId);
        return  ResponseEntity.ok().body(new SuccessfulResponse(
                HttpStatus.OK.value(),
                "Join a board",
                null));

    }
}
