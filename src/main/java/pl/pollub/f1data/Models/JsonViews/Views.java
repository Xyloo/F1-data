package pl.pollub.f1data.Models.JsonViews;

/**
 * Json views for filtering data in json responses.
 * Currently only the password field is filtered.
 * It is used to prevent sending password in json responses, but still allowing to send it in requests.
 */
public class Views {
    /**
     * Public view for filtering data in json responses.
     */
    public static class Public {}

    /**
     * Internal view for filtering data in json responses.
     */
    public static class Internal extends Public {}

}
