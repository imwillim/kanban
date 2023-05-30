package com.project.kanban.listing;

import com.project.kanban.util.RequestBodyError;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<Object> getListings(@PathVariable("workspaceId") long workspaceId,
                                              @PathVariable("boardId") long boardId){
        return ResponseEntity.ok().body(listingFacadeService.getListingsProcess(workspaceId, boardId));
    }


    @PostMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings")
    public ResponseEntity<Object> createListing(@PathVariable("workspaceId") long workspaceId,
                                                @PathVariable("boardId") long boardId,
                                                @Valid @RequestBody ListingDTO requestBody,
                                                BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<ListingDTO> createdListing = listingFacadeService
                .createListingProcess(workspaceId, boardId, requestBody);

        if (createdListing.isPresent()){
            URI createdListingURI = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdListing.get().getId()).toUri();
            return ResponseEntity.created(createdListingURI).build();
        }
        return notFoundListing();
    }


    @PutMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}")
    public ResponseEntity<Object> updateListing(@PathVariable("workspaceId") long workspaceId,
                                                @PathVariable("boardId") long boardId,
                                                @PathVariable("listingId") long listingId,
                                                @Valid @RequestBody ListingDTO requestBody,
                                                BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<ListingDTO> listingDTO = listingFacadeService
                .updateListingProcess(workspaceId, boardId, listingId, requestBody);
        return ResponseEntity.accepted().body(listingDTO);
    }


    @DeleteMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}")
    public ResponseEntity<Object> deleteListing(@PathVariable("workspaceId") long workspaceId,
                                                @PathVariable("boardId") long boardId,
                                                @PathVariable("listingId") long listingId){
        listingFacadeService.deleteListingProcess(workspaceId, boardId, listingId);
        return ResponseEntity.noContent().build();
    }
}
