package cz.coffee.fallbackapi.elements;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import com.google.gson.JsonElement;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprChangerValue extends SimpleExpression<Object> {

    private boolean isSingle;
    private Expression<JsonElement> jsonElementExpression;
    private Expression<String> pathExpression;

    @Override
    protected Object @Nullable [] get(Event event) {
        return new Object[0];
    }

    @Override
    public boolean isSingle() {
        return isSingle;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "json %s %s of %s".formatted(isSingle ? "value" : "values", pathExpression.toString(event, debug), jsonElementExpression.toString(event, debug) );
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        isSingle = parseResult.hasTag("value");
        jsonElementExpression = LiteralUtils.defendExpression(expressions[0]);
        pathExpression = LiteralUtils.defendExpression(expressions[1]);
        return LiteralUtils.canInitSafely(jsonElementExpression, pathExpression);
    }
}
