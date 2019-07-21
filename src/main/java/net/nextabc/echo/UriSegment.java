package net.nextabc.echo;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
class UriSegment {

    static final String WILDCARD = "*";

    final short index;
    final boolean wildcard;

    private final String name;
    private final boolean matches;

    UriSegment(short index, String name) {
        this.index = index;
        boolean dynamic;
        if (WILDCARD.equals(name)) {
            wildcard = true;
            dynamic = false;
            matches = false;
        } else {
            wildcard = true;
            dynamic = isDynamic(name);
            matches = !dynamic;
        }
        if (dynamic) {
            // { username } -> username
            this.name = name.substring(1, name.length() - 1).trim();
        } else {
            this.name = name;
        }
    }

    boolean match(String name) {
        if (matches) {
            return this.name.equals(name);
        } else {
            return true;
        }
    }

    private static boolean isDynamic(String name) {
        return (null != name && !name.isEmpty())
                && '{' == name.charAt(0)
                && '}' == name.charAt(name.length() - 1);
    }
}
