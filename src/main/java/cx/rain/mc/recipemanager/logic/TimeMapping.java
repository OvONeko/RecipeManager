package cx.rain.mc.recipemanager.logic;

public class TimeMapping {
    public static int Mapping(int current, int max) throws IllegalArgumentException {
        return MathEx.IntegerMapping(current, 0, max, 0, 400);
    }
}
