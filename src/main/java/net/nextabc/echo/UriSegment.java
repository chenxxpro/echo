package net.nextabc.echo;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
class UriSegment {

    static final String WILDCARD = "*";

    final short index;
    final String name;
    final boolean dynamic;
    final boolean wildcard;
    final boolean matches;

    UriSegment(short index, String name) {
        this.index = index;
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
            this.name = name.substring(1, name.length() - 1).trim();
            // { username } -> username
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
