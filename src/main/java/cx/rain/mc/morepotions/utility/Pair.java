package cx.rain.mc.morepotions.utility;

public record Pair<L, R>(L left, R right) {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?,?> pair) {
            return left.equals(pair.left) && right.equals(pair.right);
        }
        return false;
    }
}
