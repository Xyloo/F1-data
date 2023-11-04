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

    /**
     * This class allows fields to be null (but not blank).
     * It is used in updating user info - if a field is not null, it will be updated if it is validated.
     */
    public static class ValidateUserInfo {}

    /**
     * This class is used to validate user input when creating a new user.
     * It forces fields to not be blank along all the other validation rules.
     */
    public static class NewUserInfo extends ValidateUserInfo {}
}
