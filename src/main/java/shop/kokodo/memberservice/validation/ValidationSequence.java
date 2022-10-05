package shop.kokodo.memberservice.validation;


import javax.validation.GroupSequence;
import javax.validation.groups.Default;
import shop.kokodo.memberservice.validation.ValidationGroups.NotEmptyGroup;
import shop.kokodo.memberservice.validation.ValidationGroups.PatternCheckGroup;

@GroupSequence({Default.class, NotEmptyGroup.class, PatternCheckGroup.class })
public interface ValidationSequence {
}