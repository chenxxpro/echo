package net.nextabc.echo;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class UriMatcherTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void nullPattern(){
        new UriMatcher(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPattern(){
        new UriMatcher("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalWildcardPosition(){
        new UriMatcher("/wx/*/users");
    }

    @Test(expected = IllegalArgumentException.class)
    public void notStartWith(){
        new UriMatcher("wx/users");
    }

    @Test(expected = IllegalArgumentException.class)
    public void wildcardDynamicNotAllow(){
        new UriMatcher("/wx/{userId}/*");
    }

    @Test
    public void rootWildcardMatches(){
        UriMatcher m = new UriMatcher("/*");
        Assert.assertTrue(m.match("/users"));
        Assert.assertTrue(m.match("/users/"));
        Assert.assertTrue(m.match("/users/001"));

        Assert.assertFalse(m.match("/"));
    }

    @Test
    public void anyWildcardMatches(){
        UriMatcher m = new UriMatcher("*");
        Assert.assertTrue(m.match("/"));
        Assert.assertTrue(m.match("/users"));
        Assert.assertTrue(m.match("/users/"));
        Assert.assertTrue(m.match("/users/001"));
    }

    @Test
    public void wildcardMatches(){
        UriMatcher m = new UriMatcher("/wx/users/*");
        Assert.assertEquals("/wx/users/*", m.pattern());
        Assert.assertTrue(m.match("/wx/users/chen"));
        Assert.assertTrue(m.match("/wx/users/001"));

        Assert.assertFalse(m.match("/wx"));
        Assert.assertFalse(m.match("/wx/users"));
        Assert.assertFalse(m.match("/wx/friends/001"));
        Assert.assertFalse(m.match("/articles/001"));
        Assert.assertFalse(m.match("/articles"));
        Assert.assertFalse(m.match("/"));
    }

    @Test
    public void dynamicMatches(){
        UriMatcher m = new UriMatcher("/wx/users/{   userId}");
        Assert.assertTrue(m.match("/wx/users/chen"));
        Assert.assertTrue(m.match("/wx/users/0001"));

        Assert.assertFalse(m.match("/wx"));
        Assert.assertFalse(m.match("/ali/users/001"));
        Assert.assertFalse(m.match("/wx/friends/001"));
    }

    @Test
    public void staticPattern(){
        UriMatcher m = new UriMatcher("/wx/users");
        Assert.assertTrue(m.match("/wx/users"));

        Assert.assertFalse(m.match("/wx/users/"));
        Assert.assertFalse(m.match("/wx/users/001"));
        Assert.assertFalse(m.match("/ali/users"));
    }
}
