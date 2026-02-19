package sn.xoslu.tech.ebank.utils;

import sn.xoslu.tech.ebank.exceptions.BadRequestException;
import sn.xoslu.tech.ebank.exceptions.ConflictException;
import sn.xoslu.tech.ebank.exceptions.InternalServerException;

import java.util.regex.Pattern;

public class Tools {
    private static final String REGEX_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String ERROR_EMPTY_EMAIL = "Le champ email est obligatoire";
    private static final String ERROR_INVALID_FORMAT = "Le format de l'email est invalide : %s";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(REGEX_EMAIL);

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException(ERROR_EMPTY_EMAIL);
        }
        String trimmedEmail = email.trim();
        try {
            boolean isValid = EMAIL_PATTERN.matcher(trimmedEmail).matches();
            if (!isValid) {
                throw new ConflictException(String.format(ERROR_INVALID_FORMAT, trimmedEmail));
            }
        } catch (RuntimeException e) {
            throw new ConflictException(String.format(ERROR_INVALID_FORMAT, trimmedEmail));
        }
    }
}
