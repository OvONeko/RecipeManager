package cx.rain.mc.recipemanager.logic;

import java.util.Random;

public class Perlin {
    private final double seed;
    private long defaultSize;
    private int[] p;
    private int[] permutation;

    public Perlin(double seed) {
        this.seed = seed;
        Init();
    }

    public Perlin() {
        this.seed = new Random().nextGaussian() * 255;
        Init();
    }

    private void Init() {
        this.p = new int[512];
        this.permutation = new int[]{
                151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99,
                37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32,
                57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27,
                166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
                102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130,
                116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147,
                118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213,
                119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108,
                110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12,
                191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84,
                204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141,
                128, 195, 78, 66, 215, 61, 156, 180
        };
        this.defaultSize = 50;
        for (int i = 0; i < permutation.length; ++i) {
            p[256 + i] = permutation[i];
            p[i] = permutation[i];
        }
    }

    public double getSeed() {
        return this.seed;
    }

    private double Fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double Lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double Grad(int hash, double x, double y, double z) {
        int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
        double u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
                v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public double SmoothNoise3D(double x, double y, double z) {
        x += this.seed;
        y += this.seed;
        x += this.seed;
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        int Z = (int) Math.floor(z) & 255;
        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);
        double u = Fade(x);
        double v = Fade(y);
        double w = Fade(z);
        int A = p[X] + Y;
        int AA = p[A] + Z;
        int AB = p[A + 1] + Z;
        int B = p[X + 1] + Y;
        int BA = p[B] + Z;
        int BB = p[B + 1] + Z;
        return Lerp(w, Lerp(v, Lerp(u, Grad(p[AA], x, y, z), Grad(p[BA], x - 1, y, z)), Lerp(u, Grad(p[AB], x, y - 1, z), Grad(p[BB], x - 1, y - 1, z))), Lerp(v, Lerp(u, Grad(p[AA + 1], x, y, z - 1), Grad(p[BA + 1], x - 1, y, z - 1)), Lerp(u, Grad(p[AB + 1], x, y - 1, z - 1), Grad(p[BB + 1], x - 1, y - 1, z - 1))));
    }

    public double Noise3DS(double x, double y, double z, long size) {
        double value = 0.0f;
        double initSize = size;
        while ((size /= 2.0) >= 1)
            value += SmoothNoise3D((x / size), (y / size), (z / size)) * size;
        return value / initSize;
    }

    public double Noise3D(double x, double y, double z) {
        return Noise3DS(x, y, z, this.defaultSize);
    }

    public double Noise2DS(double x, double y, long size) {
        return Noise3DS(x, y, 0.0f, size);
    }

    public double Noise2D(double x, double y) {
        return Noise2DS(x, y, this.defaultSize);
    }

    public double NoiseDS(double x, long size) {
        return Noise2DS(x, 0.0f, size);
    }

    public double NoiseD(double x) {
        return NoiseDS(x, this.defaultSize);
    }
}
