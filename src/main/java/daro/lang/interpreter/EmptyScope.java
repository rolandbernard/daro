package daro.lang.interpreter;

import java.util.Map;

/**
 * This class implements an empty scope that does not contain any variables, and
 * can not be written to in any way.
 * 
 * @author Roland Bernard
 */
public class EmptyScope extends ConstantScope {

    public EmptyScope() {
        super(Map.of());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EmptyScope) {
            return true;
        } else {
            return false;
        }
    }
}
