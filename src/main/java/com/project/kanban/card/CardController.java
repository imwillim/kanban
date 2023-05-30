package com.project.kanban.card;

import com.project.kanban.util.RequestBodyError;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class CardController {
    private final CardFacadeService cardFacadeService;

    public CardController(CardFacadeService cardFacadeService){
        this.cardFacadeService = cardFacadeService;
    }

    private static ResponseEntity<Object> notFoundCard(){
        log.error("Card not found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingIds}/cards")
    public ResponseEntity<Object> getCards(@PathVariable("workspaceId") long workspaceId,
                                           @PathVariable("boardId") long boardId,
                                           @PathVariable("listingId") long listingId){
        return ResponseEntity.ok().body(cardFacadeService.getCardsProcess(workspaceId, boardId, listingId));
    }

    @PostMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingIds}/cards")
    public ResponseEntity<Object> createCard(@PathVariable("workspaceId") long workspaceId,
                                             @PathVariable("boardId") long boardId,
                                             @PathVariable("listingId") long listingId,
                                             @Valid @RequestBody CardDTO requestBody,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<CardDTO> createdCard = cardFacadeService.createCardProcess(workspaceId, boardId,
                listingId, requestBody);

        if (createdCard.isPresent()){
            URI createdListingURI = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdCard.get().getId()).toUri();
            return ResponseEntity.created(createdListingURI).build();
        }
        return notFoundCard();
    }


    @PutMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingIds}/cards/{cardId}")
    public ResponseEntity<Object> updateCard(@PathVariable("workspaceId") long workspaceId,
                                             @PathVariable("boardId") long boardId,
                                             @PathVariable("listingId") long listingId,
                                             @PathVariable("cardId") long cardId,
                                             @Valid @RequestBody CardDTO requestBody,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<CardDTO> listingDTO = cardFacadeService.updateCardProcess(workspaceId, boardId,
                listingId, cardId, requestBody);
        return ResponseEntity.accepted().body(listingDTO);
    }


    @DeleteMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingIds}/cards/{cardId}")
    public ResponseEntity<Object> deleteCard(@PathVariable("workspaceId") long workspaceId,
                                             @PathVariable("boardId") long boardId,
                                             @PathVariable("listingId") long listingId,
                                             @PathVariable("listingId") long cardId){
        cardFacadeService.deleteCardProcess(workspaceId, boardId, listingId, cardId);
        return ResponseEntity.noContent().build();
    }

}
