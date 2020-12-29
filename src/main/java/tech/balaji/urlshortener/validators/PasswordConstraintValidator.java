package tech.balaji.urlshortener.validators;

import org.passay.*;
import tech.balaji.urlshortener.annotations.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static java.util.Arrays.asList;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {


    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator validator = new PasswordValidator(asList(
                // at least 8 characters
                new LengthRule(8, 16),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule()

        ));
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);

        String messageTemplate = String.join(",", messages);
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
