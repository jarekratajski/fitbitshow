package ch.css.ebusiness.fitbit.impl.tocken;

import ch.css.ebusiness.fitbit.api.FitbitData;
import ch.css.ebusiness.fitbit.api.OauthTokenPair;
import javaslang.collection.List;
import javaslang.control.Either;
import javaslang.control.Option;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GetFitbitDataCommandHandler implements
        BiFunction<FitbitTokenCommand.GetData,
                FitbitTokenEntity.CommandContext<Either<String, FitbitData>>,
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
                                                                               .CommandContext<Either<String, FitbitData>>
                                                                               ctx) {
        final FitbitTokenState currentState = stateSupplier.get();
        System.out.println("before getting steps : " + currentState.getToken());

        final Either<String, FitbitCallResult> result =
                currentState.getToken().map(tokenPair -> Either.<String, FitbitCallResult>right(getFitbiData(tokenPair)))
                        .getOrElse(Either.left("no fitbit token - please log in"));

      /*return ctx.thenPersistAll(  new ArrayList<>(), () -> {
         System.out.println("Replying....");
         ctx.reply(result.get().fitbitData);});*/

        ctx.reply(result.flatMap(fr -> fr.fitbitData));
        return ctx.done();

    }

    private FitbitCallResult getFitbiData(final OauthTokenPair tokenPair) {
        System.out.println("get steps?");
        final Either<String, List<Integer>> heartRate = fitbit.getHr(tokenPair.token);
        final Either<String, List<Integer>> steps = fitbit.getSteps(tokenPair.token);
        final Either<String, FitbitData> fd =  heartRate.flatMap( hr ->  steps.map(st -> new FitbitData(hr, st, List.empty(), List.empty())));


        return new FitbitCallResult(fd, Option.none());
    }

    private static final class FitbitCallResult {
        final Either<String, FitbitData> fitbitData;
        final Option<OauthTokenPair> newToken;



        public FitbitCallResult(Either<String, FitbitData> fitbitData, Option<OauthTokenPair> newToken) {
            this.fitbitData = fitbitData;
            this.newToken = newToken;
        }
    }


}
