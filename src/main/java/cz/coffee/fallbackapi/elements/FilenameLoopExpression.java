package cz.coffee.fallbackapi.elements;

import ch.njol.skript.doc.NoDoc;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.sections.SecLoop;
import ch.njol.util.Kleenean;
import cz.coffeerequired.SkJson;
import cz.coffeerequired.api.FileHandler;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cz.coffeerequired.api.Api.Records.PROJECT_DEBUG;
import static cz.coffeerequired.api.json.SerializedJsonUtils.isNumeric;

@NoDoc
public class FilenameLoopExpression extends SimpleExpression<Object> {

    private String name;
    private SecLoop loop;
    private boolean isCanceled = false;
    private int mark;
    private boolean withoutExtension;


    @Override
    protected @Nullable Object @NotNull [] get(@NotNull Event e) {
        if (isCanceled) return new Object[0];
        try {
            File current = (File) loop.getCurrent(e);
            assert current != null;
            return switch (this.mark) {
                case 1 -> new Object[]{withoutExtension ? current.getName().replaceAll("\\.(.*)", "") : current.getName()};
                case 2 -> new Object[]{current.getPath()};
                case 3 -> new Object[]{current.length()};
                case 4 -> new Object[]{FileHandler.get(current).join()};
                default -> new Object[]{current};
            };
        } catch (ClassCastException exception) {
            if (PROJECT_DEBUG) SkJson.debug(exception);
            exception.getStackTrace();
            return new Object[0];
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        if (loop == null) return Object.class;
        return loop.getLoopedExpression().getReturnType();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        if (e == null) return name;
        return Classes.getDebugMessage(loop.getCurrent(e));
    }

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        MatchResult numberOfLoop = !parseResult.regexes.isEmpty() ? parseResult.regexes.get(0) : null;
        Object group = 0;
        this.mark = parseResult.mark;
        this.withoutExtension = parseResult.hasTag("without file type");
        if (numberOfLoop != null) group = numberOfLoop.group(0);
        int i = 0;
        String firstField = parseResult.expr, s = "";
        Pattern pattern = Pattern.compile("loop-(.+)(.)");
        Matcher matchingPattern = pattern.matcher(firstField);

        if (matchingPattern.matches()) {
            String[] split = firstField.split("-");
            s = split[1];
            i = isNumeric(group).intValue();
        }

        Class<?> inputClass = Classes.getClassFromUserInput(s);


        name = s;
        int j = 1;
        boolean wrongFormat = false;


        SecLoop loop = null;
        for (SecLoop l : getParser().getCurrentSections(SecLoop.class)) {
            if ((inputClass != null
                    && inputClass.isAssignableFrom(l.getLoopedExpression().getReturnType()))
                    || l.getLoopedExpression().isLoopOf("file")
            ) {
                if (j < i) {
                    j++;
                    continue;
                }
                if (loop != null) {
                    isCanceled = true;
                    break;
                }
                loop = l;
                wrongFormat = !(loop.toString().endsWith("as files"));
                if (j == i) break;
            }
        }
        if (loop == null) {
            SkJson.severe(getParser().getNode(), "There's no sloop that matches loop-" + s + "'");
            return false;
        }

        if (isCanceled) {
            SkJson.severe(getParser().getNode(), "There are multiple loops that match loop-" + s + ". Use loop-" + s + "-1/2/3/etc. to specify witch loop's value you want.");
            return false;
        }
        if (wrongFormat) {
            SkJson.severe(getParser().getNode(), "There are files loop without return as File, if you want get filename you may use '" + loop+" as files:'");
            return false;
        }
        this.loop = loop;
        return true;
    }

    public boolean isLoopOf(@NotNull String s) {
        return false;
    }
}