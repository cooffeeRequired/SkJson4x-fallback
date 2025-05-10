package cz.coffee.fallbackapi.elements;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import cz.coffeerequired.api.FileHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@NoDoc
public class AllJsonInFolder extends SimpleExpression<Object> {

    private Expression<String> directoryInputString;
    boolean asFiles;

    @Override
    protected Object @NotNull [] get(@NotNull Event event) {
        var inputDirectory = directoryInputString.getSingle(event);
        assert inputDirectory != null;
        return Arrays.stream((FileHandler.walk(inputDirectory)).join()).toArray();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        assert event != null;
        return "All json files from directory " + directoryInputString.toString(event, b) + " " + (asFiles ? "as files" : "");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int i, @NotNull Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
        directoryInputString = (Expression<String>) expressions[0];
        asFiles = parseResult.hasTag("as files");
        return true;
    }

    @Override
    public boolean isLoopOf(@NotNull String s) {
        return s.equals("file");
    }
}
