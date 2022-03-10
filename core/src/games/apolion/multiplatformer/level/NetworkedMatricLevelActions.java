package games.apolion.multiplatformer.level;

public enum NetworkedMatricLevelActions {
    chat("chat");

    private NetworkedMatricLevelActions(String s) {
        this.commandStr= s;
    }
    public String commandStr;

}
