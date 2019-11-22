public interface Constants {
    int SCREEN_HEIGHT = 800;
    int SCREEN_WIDTH = 1280;
    String USER_ID = "s87sun";
    String NAME = "Shuyi Sun";
    String DESCRIPTION = "Instruction:\n"
        + "   The purpose of the game is to collect fruit (in orange).\n"
        + "   - left/right arrow keys: turn left/right relative to its current path.\n"
        + "   - s: start the game\n"
        + "   - p: pause and un-pause the game\n"
        + "   - r: reset to the splash screen\n"
        + "   - 1/2/3: start corresponding level\n"
        + "   - q: quit and display the high score screen";
    int GRID_HEIGHT = 25;
    int GRID_WIDTH = 40;
    int FPS = 25;
    int OBSTACLE_PERIOD = FPS * 5;
    int OBSTACLE_COUNT = 6;
    int BORDER_WIDTH = 10;
    int BORDER_HEIGHT = 5;
}