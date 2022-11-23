package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

public final class InvalidCase {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private InvalidCase() {
    }

    /**
     *
     * @param output
     * @param handIdx
     */
    public static void placeCardEnvironmentCard(final ArrayNode output, final int handIdx) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "placeCard");
        outputNode.put("error", "Cannot place environment card on table.");
        outputNode.put("handIdx", handIdx);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param handIdx
     */
    public  static void placeCardNotEnoughMana(final ArrayNode output, final int handIdx) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "placeCard");
        outputNode.put("error", "Not enough mana to place card on table.");
        outputNode.put("handIdx", handIdx);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param handIdx
     */
    public static void placeCardNotEnoughSpace(final ArrayNode output, final int handIdx) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "placeCard");
        outputNode.put("error", "Cannot place card on table since row is full.");
        outputNode.put("handIdx", handIdx);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAttackNotEnemyCard(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAttack");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacked card does not belong to the enemy.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAttackAlreadyAttacked(final ArrayNode output,
                                                final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAttack");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacker card has already attacked this turn.");
        output.addPOJO(outputNode);

    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAttackIsFrozen(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAttack");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacker card is frozen.");
        output.addPOJO(outputNode);

    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAttackTank(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAttack");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacked card is not of type 'Tank'.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAbilityIsFrozen(final ArrayNode output, final ActionsInput command) {
        ObjectNode  outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAbility");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacker card is frozen.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAbilityAlreadyAttacked(final ArrayNode output,
                                                 final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAbility");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacker card has already attacked this turn.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAbilityNotMyCard(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAbility");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacked card does not belong to the current player.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAbilityNotEnemyCard(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAbility");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacked card does not belong to the enemy.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void useAbilityTank(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "cardUsesAbility");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.putPOJO("cardAttacked", command.getCardAttacked());
        outputNode.put("error", "Attacked card is not of type 'Tank'.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void attackHeroIsFrozen(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useAttackHero");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.put("error", "Attacker card is frozen.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void attackHeroAlreadyAttacked(final ArrayNode output,
                                                 final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useAttackHero");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.put("error", "Attacker card has already attacked this turn.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param command
     */
    public static void attackHeroTank(final ArrayNode output, final ActionsInput command) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useAttackHero");
        outputNode.putPOJO("cardAttacker", command.getCardAttacker());
        outputNode.put("error", "Attacked card is not of type 'Tank'.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     */
    public static void heroAbilityNotEnoughMana(final ArrayNode output, final int affectedRow) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useHeroAbility");
        outputNode.put("affectedRow", affectedRow);
        outputNode.put("error", "Not enough mana to use hero's ability.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     */
    public static void heroAbilityAlreadyAttacked(final ArrayNode output, final int affectedRow) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useHeroAbility");
        outputNode.put("affectedRow", affectedRow);
        outputNode.put("error", "Hero has already attacked this turn.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     */
    public static void heroAbilityNotEnemyRow(final ArrayNode output, final int affectedRow) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useHeroAbility");
        outputNode.put("affectedRow", affectedRow);
        outputNode.put("error", "Selected row does not belong to the enemy.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     */
    public static void heroAbilityNotMyRow(final ArrayNode output, final int affectedRow) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useHeroAbility");
        outputNode.put("affectedRow", affectedRow);
        outputNode.put("error", "Selected row does not belong to the current player.");
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     * @param handIdx
     */
    public static void notEnvironmentType(final ArrayNode output, final int affectedRow,
                                          final int handIdx) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useEnvironmentCard");
        outputNode.put("error", "Chosen card is not of type environment.");
        outputNode.put("handIdx", handIdx);
        outputNode.put("affectedRow", affectedRow);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     * @param handIdx
     */
    public static void environmentCardNotEnoughMana(final ArrayNode output, final int affectedRow,
                                                    final int handIdx) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useEnvironmentCard");
        outputNode.put("error", "Not enough mana to use environment card.");
        outputNode.put("handIdx", handIdx);
        outputNode.put("affectedRow", affectedRow);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     * @param handIdx
     */
    public static void environmentCardNotEnemyRow(final ArrayNode output, final int affectedRow,
                                                  final int handIdx) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useEnvironmentCard");
        outputNode.put("error", "Chosen row does not belong to the enemy.");
        outputNode.put("handIdx", handIdx);
        outputNode.put("affectedRow", affectedRow);
        output.addPOJO(outputNode);
    }

    /**
     *
     * @param output
     * @param affectedRow
     * @param handIdx
     */
    public static void environmentCardNotEnoughSpace(final ArrayNode output, final int affectedRow,
                                                     final int handIdx) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("command", "useEnvironmentCard");
        outputNode.put("error", "Cannot steal enemy card since the player's row is full.");
        outputNode.put("handIdx", handIdx);
        outputNode.put("affectedRow", affectedRow);
        output.addPOJO(outputNode);
    }

}
