package io.jenkins.plugins.sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibraryFunctions {

    /**
     * Used as regular expression check for name field on the configure job screen
     * @param value = represents a name of a user that the user has typed in
     * @return if the name matches the format, it is a valid name (only letters and hyphens)
     */
    public static boolean nameFormatMatcher(String value){
        Pattern pattern = Pattern.compile("^[A-Za-z\\s-]+$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

}
