package ch.css.ebusiness.fitbit.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public class FitbitData {
   public final long pulse;
   public final long steps;
   public final long km;
   public final long stairs;

   @JsonCreator
   public FitbitData(long pulse, long steps, long km, long stairs) {
      this.pulse = pulse;
      this.steps = steps;
      this.km = km;
      this.stairs = stairs;
   }
}
