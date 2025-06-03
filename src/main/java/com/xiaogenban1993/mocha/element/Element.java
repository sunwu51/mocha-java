package com.xiaogenban1993.mocha.element;

import java.util.*;

/**
 * @author Frank
 * @date 2025/2/4 19:45
 */
public class Element {

    public Map<String, Element> map = new HashMap<>();




    public Element get(String key) {
        Element res = null;

        // 自身属性
        if ((res = map.get(key)) != null) {
            return res;
        }

        // 原型链
        Element prototype = map.get("$$pro$$");
        if (prototype != null) {
            return prototype.get(key);
        }
        return Constants.NULL;
    }


    public void set(String key, Element value) {
        if (Objects.equals(key, "$$pro$$")) {
            throw new IllegalStateException("Protected property");
        }
        map.put(key, value);
    }

    public ProtoElement getPrototype() {
        Element res = map.get("$$pro$$");
        if (res == null) {
            return null;
        }
        return (ProtoElement) res;
    }

    public Element get(Element index) {
        if (index instanceof NumberElement && this instanceof ArrayElement) {
            return ((ArrayElement)this).array.get((int) (((NumberElement) index).value) );
        }
        if (index instanceof StringElement) {
            return get(((StringElement) index).value);
        }
        throw new RuntimeException("not support");
    }

    public void set(Element index, Element value) {
        if (index instanceof NumberElement && this instanceof ArrayElement) {
            ((ArrayElement)this).array.set((int) (((NumberElement) index).value), value);
        } else if  (index instanceof StringElement) {
            set(((StringElement) index).value, value);
        } else {
            throw new RuntimeException("not support");
        }

    }

    public void setPrototype(ProtoElement proto) {
        map.put("$$pro$$", proto);
    }

    public String toString() {
        return map.toString();
    }

    public BooleanElement toBooleanElement() {
        if (this instanceof BooleanElement) {
            return (BooleanElement)this;
        }
        if (this instanceof NullElement) {
            return (BooleanElement)Constants.FALSE;
        }
        if (this instanceof NumberElement) {
            if (((NumberElement)this).value == 0.0) {
                return (BooleanElement)Constants.FALSE;
            }
        }
        return (BooleanElement)Constants.TRUE;
    }



    public static Element option(Element e1, Element e2, String opcode) {
        if (e1 != null && e2 != null) {
            switch (opcode) {
                case "+":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return new NumberElement(((NumberElement) e1).value + ((NumberElement) e2).value);
                    }
                    return new StringElement(e1.toString() + e2.toString());
                case "-":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return new NumberElement(((NumberElement) e1).value - ((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                case "*":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return new NumberElement(((NumberElement) e1).value * ((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                case "/":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return new NumberElement(((NumberElement) e1).value / ((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                case "%":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return new NumberElement(((NumberElement) e1).value % ((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                case "==":
                    return e1.equals(e2) ? Constants.TRUE : Constants.FALSE;
                case "!=":
                    return e1.equals(e2) ? Constants.FALSE : Constants.TRUE;
                case ">":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return ((NumberElement) e1).value > ((NumberElement) e2).value ? Constants.TRUE : Constants.FALSE;
                    }
                    throw new RuntimeException("not support");
                case ">=":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return ((NumberElement) e1).value >= ((NumberElement) e2).value ? Constants.TRUE : Constants.FALSE;
                    }
                    throw new RuntimeException("not support");
                case "<":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return ((NumberElement) e1).value < ((NumberElement) e2).value ? Constants.TRUE : Constants.FALSE;
                    }
                    throw new RuntimeException("not support");
                case "<=":
                    if (e1 instanceof NumberElement && e2 instanceof NumberElement) {
                        return ((NumberElement) e1).value <= ((NumberElement) e2).value ? Constants.TRUE : Constants.FALSE;
                    }
                    throw new RuntimeException("not support");
                case "&&":
                    return e1.toBooleanElement().value && e2.toBooleanElement().value ? Constants.TRUE : Constants.FALSE;
                case "||":
                    return e1.toBooleanElement().value || e2.toBooleanElement().value ? Constants.TRUE : Constants.FALSE;
                default:
                    return Constants.NULL;
            }
        } else if (e1 == null) {
            switch (opcode) {
                case "+":
                    if (e2 instanceof NumberElement) {
                        return new NumberElement(+((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                case "-":
                    if (e2 instanceof NumberElement) {
                        return new NumberElement(-((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                case "!":
                    return e2.toBooleanElement().value ? Constants.FALSE : Constants.TRUE;
                case "++":
                    if (e2 instanceof NumberElement) {
                        return new NumberElement(++((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                case "--":
                    if (e2 instanceof NumberElement) {
                        return new NumberElement(--((NumberElement) e2).value);
                    }
                    throw new RuntimeException("not support");
                default:
                    throw new RuntimeException("not support");
            }
        } else {
            switch (opcode) {
                case "++":
                    if (e1 instanceof NumberElement) {
                        return new NumberElement(((NumberElement) e1).value++);
                    }
                    throw new RuntimeException("not support");
                case "--":
                    if (e1 instanceof NumberElement) {
                        return new NumberElement(((NumberElement) e1).value--);
                    }
                    throw new RuntimeException("not support");
                default:
                    throw new RuntimeException("not support");
            }
        }
    }

}