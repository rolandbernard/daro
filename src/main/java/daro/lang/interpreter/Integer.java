package daro.lang.interpreter;

/**
 * This class represents an integer in the interpreter.
 *
 * @author Roland Bernard
 */
public class Integer extends Value {
    private final long value;

    public Integer(IntegerType type, long value) {
        super(type);
        if (value > 0 && value <= 64) {
            this.value = (0xffff_ffff_ffff_ffffL >>> (64 - type.getWidth())) & value;
        } else {
            // TODO: review this exception
            throw new IllegalArgumentException("Integer sizes above 64 are not supported");
        }
    }

    public long getValue() {
        return value;
    }

    private static boolean areBinaryIntegerParameters(Value[] parameters) {
        return parameters.length == 1
            && parameters[0] instanceof Integer;
    }

    /**
     * This is a utility method that returns the integer type with the bigger 
     * @param first
     * @param second
     * @return
     */
    private static IntegerType getBiggerIntegerType(Type first, Type second) {
        IntegerType intFirst = (IntegerType)first;
        IntegerType intSecond = (IntegerType)second;
        if (intFirst.getWidth() > intSecond.getWidth()) {
            return intFirst;
        } else {
            return intSecond;
        }
    }

    @Override
    public Value executeMethod(Interpreter interpreter, String method, Value[] parameters) {
        switch (method) {
            case "+":
                if (parameters.length == 0) {
                    return this; // We can return this object, because integers are immutable
                } else if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() + second.getValue());
                }
            case "-":
                if (parameters.length == 0) {
                    return new Integer((IntegerType)getType(), -getValue());
                } else if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() - second.getValue());
                }
            case "*":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() * second.getValue());
                }
            case "/":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() / second.getValue());
                }
            case "%":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() % second.getValue());
                }
            case "<<":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() << second.getValue());
                }
            case ">>":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() >> second.getValue());
                }
            case "&":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() & second.getValue());
                }
            case "|":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() | second.getValue());
                }
            case "^":
                if (areBinaryIntegerParameters(parameters)) {
                    Integer second = (Integer)parameters[0];
                    return new Integer(getBiggerIntegerType(getType(), second.getType()), getValue() ^ second.getValue());
                }
            case "~":
                if (parameters.length == 0) {
                    return new Integer((IntegerType)getType(), ~getValue());
                }
        }
        throw new UnknownMethodException(interpreter, this, method, parameters);
    }
}

