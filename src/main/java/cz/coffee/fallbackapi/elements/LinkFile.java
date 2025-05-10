package cz.coffee.fallbackapi.elements;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.AsyncEffect;
import ch.njol.util.Kleenean;
import cz.coffeerequired.api.Api;
import cz.coffeerequired.api.FileHandler;
import cz.coffeerequired.api.cache.CacheStorageWatcher;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class LinkFile extends AsyncEffect {
    private Expression<String> exprFileString, expressionID;
    private boolean asAlive;


    @Override
    protected void execute(@NotNull Event e) {
        String fileString = exprFileString.getSingle(e);
        String id = expressionID.getSingle(e);
        if (id == null || fileString == null) return;
        var cache = Api.getCache();
        File file = new File(fileString);
        if (cache.containsKey(id)) return;
        FileHandler.get(file).whenComplete((json, error) -> {
            cache.addValue(id, json, file);
            if (asAlive) if (!CacheStorageWatcher.Extern.hasRegistered(file)) {
                try {
                    CacheStorageWatcher.Extern.register(id, file);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        assert e != null;
        return "link json file " + exprFileString.toString(e, debug) + " as " + expressionID.toString(e, debug) + (asAlive ? " and make json watcher listen" : "");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        asAlive = parseResult.hasTag("and make");
        getParser().setHasDelayBefore(Kleenean.TRUE);
        exprFileString = (Expression<String>) exprs[0];
        expressionID = (Expression<String>) exprs[1];
        return true;
    }
}