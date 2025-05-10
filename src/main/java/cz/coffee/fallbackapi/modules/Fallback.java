package cz.coffee.fallbackapi.modules;

import ch.njol.skript.lang.ExpressionType;
import cz.coffee.fallbackapi.elements.AllJsonInFolder;
import cz.coffee.fallbackapi.elements.FilenameLoopExpression;
import cz.coffee.fallbackapi.elements.LinkFile;
import cz.coffee.fallbackapi.elements.SaveCache;
import cz.coffeerequired.api.Extensible;
import cz.coffeerequired.api.Register;
import cz.coffeerequired.api.annotators.Module;


@Module(module = "fallback")
public class Fallback extends Extensible {

    public Fallback() {
        this.sign = this.getClass().getSimpleName();
        this.skriptElementPath = "cz.coffee.fallbackapi.elements";
    }

    @Override
    public void registerElements(Register.SkriptRegister skriptRegister) {
        skriptRegister.apply(this);
        skriptRegister.registerExpression(FilenameLoopExpression.class, Object.class, ExpressionType.SIMPLE,
                "[the] loop-file's (1:name|2:path|3:size|4:content)[-<(\\d+)>] [:without file type]"
        );
        skriptRegister.registerExpression(AllJsonInFolder.class, Object.class, ExpressionType.SIMPLE,
                "all json [files] (from|in) (dir[ectory]|folder) %string% [:as files]"
        );
        skriptRegister.registerEffect(LinkFile.class, "link [json] file %string% as %string% [(:and make) [[json] watcher] listen]");
        skriptRegister.registerEffect(SaveCache.class,
                "save cached json %string%",
                "save all cached jsons"
        );
    }
}
