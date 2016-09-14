package ch.css.ebusiness.fitbit.impl.tocken;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;
import java.util.function.BiFunction;

public class FitbitTokenEntity extends PersistentEntity<FitbitTokenCommand,
        FitbitTokenEvent, FitbitTokenState> {
    @Override
    public Behavior initialBehavior(Optional<FitbitTokenState> snapshot) {
        BehaviorBuilder b = newBehaviorBuilder(
                snapshot.orElse(new FitbitEmptyTokenState()));

        b.setCommandHandler(FitbitTokenCommand.TokenChanged.class,
                getTokenChangedCommandContextPersistBiFunction()
        );

        b.setCommandHandler(FitbitTokenCommand.GetData.class,
                new GetFitbitDataCommandHandler(() -> state()));


        b.setReadOnlyCommandHandler(FitbitTokenCommand.GetToken.class,
                (cmd, ctx) ->
                        ctx.reply(state().getToken())

        );


        b.setEventHandler(FitbitTokenEvent.FitbitTokenChanged.class, changed ->
                new FitbitValidTokenState(changed.token));

        return b.build();
    }

    private BiFunction<FitbitTokenCommand.TokenChanged, CommandContext<Done>, Persist<? extends FitbitTokenEvent>> getTokenChangedCommandContextPersistBiFunction() {
        return (cmd, ctx) -> {
            System.out.println("token changed: " + cmd.token);
            return ctx.thenPersist(new FitbitTokenEvent.FitbitTokenChanged(cmd.token),
                    evt -> ctx.reply(Done.getInstance()));
        };
    }
}
