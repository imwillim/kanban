package com.project.kanban.card;

import com.project.kanban.util.RequestBodyError;
import com.project.kanban.util.RequestBodyException;
import com.project.kanban.util.SuccessfulResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
public class CardController {
    private final CardFacadeService cardFacadeService;

    public CardController(CardFacadeService cardFacadeService){
        this.cardFacadeService = cardFacadeService;
    }

    private static ResponseEntity<Object> notFoundCard(){
        log.error("Card not found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}/cards")
    public ResponseEntity<Object> getCards(Authentication authentication,
                                           @PathVariable("workspaceId") long workspaceId,
                                           @PathVariable("boardId") long boardId,
                                           @PathVariable("listingId") long listingId){
        List<CardDTO> cards = cardFacadeService.getCardsProcess(workspaceId, boardId, listingId);
        log.info("List of cards is returned.");
        return ResponseEntity.ok().body(new SuccessfulResponse(HttpStatus.OK.value(),
                "List of cards returned",
                cards)
        );
    }

    @PostMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}/cards")
    public ResponseEntity<Object> createCard(Authentication authentication,
            @PathVariable("workspaceId") long workspaceId,
                                             @PathVariable("boardId") long boardId,
                                             @PathVariable("listingId") long listingId,
                                             @Valid @RequestBody CardDTO requestBody,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<CardDTO> createdCard = cardFacadeService.createCardProcess(authentication, workspaceId, boardId,
                listingId, requestBody);

        if (createdCard.isPresent()){
            URI createdListingURI = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdCard.get().getId()).toUri();
            log.info("New card is created.");
            return ResponseEntity.created(createdListingURI).body(
                    new SuccessfulResponse(HttpStatus.CREATED.value(),
                            "New card is created",
                            createdCard));
        }
        return notFoundCard();
    }


    @PutMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}/cards/{cardId}")
    public ResponseEntity<Object> updateCard(Authentication authentication,
            @PathVariable("workspaceId") long workspaceId,
                                             @PathVariable("boardId") long boardId,
                                             @PathVariable("listingId") long listingId,
                                             @PathVariable("cardId") long cardId,
                                             @Valid @RequestBody CardDTO requestBody,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<CardDTO> card = cardFacadeService.updateCardProcess(authentication, workspaceId, boardId,
                listingId, cardId, requestBody);
        log.info("Card is updated.");
        return ResponseEntity.accepted().body(
                new SuccessfulResponse(
                        HttpStatus.ACCEPTED.value(),
                        "Card is updated",
                        card));
    }


    @DeleteMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/listings/{listingId}/cards/{cardId}")
    public ResponseEntity<Object> deleteCard(Authentication authentication,
                                             @PathVariable("workspaceId") long workspaceId,
                                             @PathVariable("boardId") long boardId,
                                             @PathVariable("listingId") long listingId,
                                             @PathVariable("cardId") long cardId){
        cardFacadeService.deleteCardProcess(authentication, workspaceId, boardId, listingId, cardId);
        log.info("Card is deleted.");
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.NO_CONTENT.value(),
                        "Delete a card",
                        null), HttpStatus.NO_CONTENT);
    }


    @PostMapping(path = "/listings/{listingId}/cards/{cardId}/join")
    public ResponseEntity<Object> assignCard(Authentication authentication,
                                              @PathVariable("listingId") long listingId,
                                              @PathVariable("cardId") long cardId) {
        cardFacadeService.assignCard(authentication, listingId, cardId);
        return  ResponseEntity.ok().body(new SuccessfulResponse(
                HttpStatus.OK.value(),
                "Join a card",
                null));

    }
}
