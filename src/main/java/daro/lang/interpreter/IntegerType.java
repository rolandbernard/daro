package daro.lang.interpreter;

/**
 * This class represents an integer type. Integers have a certain width, i.e. number of bits they are
 * represented with. e.g. i8 i16 i32 i64
 *
 * @author Roland Bernard
 */
public class IntegerType extends Type {
    private final int width;

    /**
     * Creates a object representing an integer type of the given width.
     * @param width The with of the type
     */
    public IntegerType(int width) {
        this.width = width;
    }

    /**
     * Returns the width of the integer type.
     * @return The integer type width
     */
    public int getWidth() {
        return width;
    }

    @Override
    public Value instantiate() {
        return new Integer(this, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerType) {
            IntegerType intType = (IntegerType)obj;
            return width == intType.getWidth();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "i" + width;
    }
}
