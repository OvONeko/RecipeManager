package cx.rain.mc.recipemanager.logic;

public class MathEx {
    public static double Mapping(double x, double a1, double b1, double a2, double b2) {
        if (x < a1)
            throw new IllegalArgumentException("x must ≥ a₁");
        return ((x - a1) * (b2 - a2)) / (b1 - a1) + a2;
    }

    public static int IntegerMapping(int x, int a1, int b1, int a2, int b2) throws IllegalArgumentException {
        return (int) Mapping(x, a1, b1, a2, b2);
    }
}
