package ch.css.ebusiness.fitbit.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javaslang.collection.List;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class FitbitData {
    public final List<Integer> pulse;
    public final List<Integer> steps;
    public final List<Integer> km;
    public final List<Integer> stairs;

    @JsonCreator
    public FitbitData(List<Integer> pulse, List<Integer> steps, List<Integer> km, List<Integer> stairs) {
        this.pulse = pulse;
        this.steps = steps;
        this.km = km;
        this.stairs = stairs;
    }
}
