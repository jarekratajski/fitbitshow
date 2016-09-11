package ch.css.ebusiness.fitbit.impl.tocken;

import ch.css.ebusiness.fitbit.api.FitbitData;
import ch.css.ebusiness.fitbit.api.OauthTokenPair;

import javaslang.control.Either;
import javaslang.control.Option;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GetFitbitDataCommandHandler implements
   BiFunction<FitbitTokenCommand.GetData,
      FitbitTokenEntity.CommandContext<Option<FitbitData>>,
      FitbitTokenEntity
   .Persist<? extends
      FitbitTokenEvent>> {

   private final Supplier<FitbitTokenState> stateSupplier;

   private final FitbitConnector fitbit = new FitbitConnector();

   public GetFitbitDataCommandHandler(Supplier<FitbitTokenState> stateSupplier) {
      this.stateSupplier = stateSupplier;
   }

   @Override
   public FitbitTokenEntity.Persist<? extends FitbitTokenEvent> apply(FitbitTokenCommand.GetData cmd,
                                                                      FitbitTokenEntity
                                                                         .CommandContext<Option<FitbitData>>
                                                                         ctx) {
      final FitbitTokenState currentState = stateSupplier.get();
      System.out.println("before getting steps : " +currentState.getToken());

      final Option<FitbitCallResult> result= currentState.getToken().map( tokenPair-> getFitbiData(tokenPair));

      System.out.println("after getting steps : " +result);

      /*return ctx.thenPersistAll(  new ArrayList<>(), () -> {
         System.out.println("Replying....");
         ctx.reply(result.get().fitbitData);});*/

      System.out.println("after getting steps @: " +result.get().fitbitData);
      ctx.reply(result.flatMap(res->res.fitbitData));
      return ctx.done();

   }

   private FitbitCallResult getFitbiData(final OauthTokenPair tokenPair)  {
      System.out.println("get steps");
      final Option<Integer> heartRate = fitbit.getSteps(tokenPair.token);

      final Option<FitbitData> fd = heartRate.map( hr -> new FitbitData(hr,7,7,7));

      return new FitbitCallResult(fd, Option.none());
   }

   private static final class FitbitCallResult {
      final Option<FitbitData> fitbitData;
      final Option<OauthTokenPair> newToken;

      FitbitCallResult() {
         this( Option.none(), Option.none());
      }

      public FitbitCallResult(Option< FitbitData> fitbitData, Option<OauthTokenPair> newToken) {
         this.fitbitData = fitbitData;
         this.newToken = newToken;
      }
   }


}
