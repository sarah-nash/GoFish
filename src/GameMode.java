public enum GameMode {
    PAIRS,
    BOOKS;

    private GameMode(){

    }

    public static String modeToString(GameMode mode){
        if (mode.equals(PAIRS)){
            return "Pairs";
        }
        else if(mode.equals(BOOKS)){
            return "Books";
        }
        return null;
    }

}
