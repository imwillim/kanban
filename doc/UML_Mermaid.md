``` mermaid
classDiagram
    User "1"--o"n" MemberWorkspace
    MemberWorkspace "n"o--"1" Workspace
    Workspace "1"o--"0..n" Board
    Board "1"*--"1..n" Listing
    Listing "1"o--"0..n" Card

    Board "1"--o"n" BoardAssignee
    User "1"--o"n" BoardAssignee
    
    Card "1"--o"n" CardAssignee
    User "1"--o"n" CardAssignee


    class User {
        -long id
        -String username
        -String email
        -String password
        -Date createdAt
        -Date updatedAt
    }

    class MemberWorkspace {
        -long id
        -long user_id
        -long workspace_id
        -String role
    }


    class Workspace {
        -long id
        -long user_id
        -long workspaceId
        -String role
        -String description
        -Date createdAt
        -Date updatedAt
    }

    class Board {
        -long id
        -String title
        -String description
        -boolean isArchived
        -Date createdAt
        -Date updatedAt
        -long workspace_id
    }

    class Listing {
        -long id
        -String title
        -boolean isArchived
        -Date createdAt
        -Date updatedAt
        -long board_id
    }

    class Card {
        -long id
        -String title
        -boolean isArchived
        -Date createdAt
        -Date updatedAt
        -long listing_id
    }

    class BoardAssignee {
        -long id
        -long user_id
        -long board_id
        -String role
    }

    class CardAssignee {
        -long id
        -long user_id
        -long card_id
        -String role
    }
    
```