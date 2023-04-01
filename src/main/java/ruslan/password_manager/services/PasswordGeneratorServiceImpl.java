package ruslan.password_manager.services;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService{

    private static final String ERROR_CODE = "400";
    private static final int AMOUNT_OF_CHARACTER_RULES = 4;

    @Override
    public String generatePassayPassword(int length) {
        PasswordGenerator gen = new PasswordGenerator();
        List<CharacterRule> rules = getAllRules();
        setMinimumChars(rules, length);
        return gen.generatePassword(length, getSpecialCharsRule(), getLowerCaseEnglishCharsRule(),
                getUpperCaseEnglishCharsRule(), getDigitsRule());
    }

    public List<CharacterRule> getAllRules() {
        return List.of(getDigitsRule(), getSpecialCharsRule(), getLowerCaseEnglishCharsRule(),
                getUpperCaseEnglishCharsRule());
    }

    public CharacterRule getLowerCaseEnglishCharsRule() {
        return new CharacterRule(EnglishCharacterData.LowerCase);
    }

    public CharacterRule getUpperCaseEnglishCharsRule() {
        return new CharacterRule(EnglishCharacterData.UpperCase);
    }

    public CharacterRule getDigitsRule() {
        return new CharacterRule(EnglishCharacterData.Digit);
    }

    public CharacterRule getSpecialCharsRule() {
        return new CharacterRule(getSpecialCharsData());
    }

    public CharacterData getSpecialCharsData() {
        return new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
    }

    public void setMinimumChars(List<CharacterRule> characterRules, int passwordLength) {
        characterRules.forEach(characterRule ->
                characterRule.setNumberOfCharacters(passwordLength / AMOUNT_OF_CHARACTER_RULES + 1));
    }
}
