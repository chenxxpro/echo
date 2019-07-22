package net.nextabc.echo;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author 陈哈哈 yoojiachen@gmail.com
 **/
public class TextsTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void testRequireEmpty() {
        Texts.requireNonEmpty("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequireNull() {
        Texts.requireNonEmpty(null);
    }

    @Test
    public void split(){
        String[] array = new String[]{"wx", "users", "profile"};
        Assert.assertArrayEquals(array, Texts.split("/wx/users/profile", "/"));
        Assert.assertArrayEquals(array, Texts.split("/wx/users/profile", "/"));
    }

    @Test
    public void splitNoSplitter(){
        String[] array = new String[]{"wx|users|profile"};
        Assert.assertArrayEquals(array, Texts.split("wx|users|profile", "/"));
    }
}
