package Choi.clean_lottery.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.Set;

@Component
public class SetStringNotEmptyValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(target instanceof Set){
            HashSet<String> set = (HashSet<String>) target;
            for (String s : set) {
                if (s.isEmpty()) errors.reject("NotEmpty");
            }
        } else {
            errors.reject("UnexpectedType");
        }
    }
}
