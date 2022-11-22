Copyright 2022-2023 Preda Diana 324CA

# GwentStone

**Student: Preda Diana**

**Group: 324CA**

## Description

This project represents the implementation of a simpler version of the famous Gwent-Stone game,
a strategy turn-base card game. There are 3 types of cards:

Environment Card: *Enironment Cards can one be used once. These cards effect an entire row.*


* Firestorm - this card lowers the 'health' points of all minions from the targeted row by 1 point
* Winterfell - this card freezes all minions on the targeted row
* Heart Hound - this card steals the minion with the highest 'health' on the targeted row and place it on the
  other player's side

Minion Card: *Minion Card can be placed on the playing table, these cards have 3 social statuses:*

* Basic Minions (Sentinel and Berserker) - these cards can only attack;
* Tank Minions (Warden and Goliath) - these cards have to be attacked first;
* Special Minions (The Cursed One, Disciple, The Ripper and Miraj) - these cards have special abilities

* The Cursed One - this card swaps the 'attack damage' and the 'health' points of a targeted minion card
  belonging to the enemy
* Disciple - this card increases 'health' points of a targeted minion card by 2 points
* The Ripper - this card reduces the 'attack damage' of an enemy minion card by 2 points
* Miraj - this card swaps its 'health' points with a targeted enemy card 'health' points


Hero Card: *At the beginning of the match, each player is offered a Hero card, in order to win, players have to
kill opponent's Hero. Each hero starts with 30 'health' point. There are 4 types of Heroes:*

* Lord Royce - this Hero freezes the card with the highest 'attack damage' from the targeted row
* King Mudface - this Hero increases 'health' points of all cards from the targeted row by 1 point
* General Kocioraw - this Hero increases 'attack damage' points of all cards from the targeted row by 1 point
* Empress Thorina - this Hero destroys the card with the highest health from the targeted row

## Commands

**Debug**
- getPlayerDeck: displays a specified player cards
- getCardsInHand: displays a specified player cards in hand
- getPlayerHero: displays a specified player hero
- getPlayerTurn: displays
- getPlayerMana: displays a specified player mana
- getCardsOnTable: displays cards on table
- getFrozenCardsOnTable: displays frozen cards on table
- getEnvironmentCardsInHand: displays environment cards of a specified player
- getCardAtPosition: displays the card placed at a given position

**Gameplay**
- placeCard: places card at a given index on the table; consumes player's mana
- useEnvironmentCard: uses environment card from player's hand, affecting targeted row on the table;
  consumes player's mana
- cardUsesAttack: uses a minion card placed on the table to attack another minion
- cardUsesAbility: uses the ability of a special minion card placed on another minion
- useAttackHero: uses a minion card placed on the table to attack the enemy hero; if hero dies, game ends
- useHeroAbility: uses hero's ability on a specified row
- endPlayerTurn: ends current player's turn

**Statistics**
- getTotalGamesPlayer: displays the number of games played
- getPlayerOneWins: displays the number of games won by player One
- getPlayerTwoWins: displays the number of games won by player Two

## Implementation

My game implementations follows a few simple steps: from the beginning I get from the
input necessary information for my game, such as player's Decks, Cards and Heroes. Then I iterate through
games command list, displaying required information and attacking specified cards and rows. Every player
gets mana (equaling the number of rounds played) and one new card (from the chosen deck)
after every round. Game ends when one player Hero is down.

I made use of 5 auxiliary classes: Cards (consists of generic card type and extends with Minion,
Hero and Environment); Debug (for debugging commends); Gameplay (for gameplay commends such as card
uses attack or ability); Statistics (just numbers, doesn't influence game course); Utils (helps
verifying if there is any 'Tank' type card).

## Possible improvements:
- better code structure
- classes for every card subtype
- shorter methods
- more suggestive name for Utils class instance 'a'
- I also used 'a' instance and turn in the same method, just for simplicity,
even though is not very oop like

## Final comments:
This project helped me in order to better understand Java code writing. It was a long journey,
but after a few days of hard work it was very satisfying to see the results.



