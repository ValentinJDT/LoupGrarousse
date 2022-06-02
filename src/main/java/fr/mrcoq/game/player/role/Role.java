package fr.mrcoq.game.player.role;

public enum Role {
    VILLAGER("Villageois", 0, 0, "§7Yu gi oh, à toi de jouer "),
    WEREWOLF("Loup Garou", 1, 0, "§cC'est l'heure de manger !");

    private final String displayName;
    private final int order;
    private final int timePlay;
    private final String messageOnPlay;

    Role(String displayName, int order, int timePlay, String messageOnPlay) {
        this.displayName = displayName;
        this.order = order;
        this.timePlay = timePlay;
        this.messageOnPlay = messageOnPlay;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getOrder() {
        return this.order;
    }

    public int getTimePlay() {
        return this.timePlay;
    }

    public String getMessageOnPlay() {
        return this.messageOnPlay;
    }

}
