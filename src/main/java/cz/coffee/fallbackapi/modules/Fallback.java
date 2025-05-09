package cz.coffee.fallbackapi.modules;

import ch.njol.skript.lang.ExpressionType;
import cz.coffee.fallbackapi.elements.ExprChangerArray;
import cz.coffee.fallbackapi.elements.ExprChangerObject;
import cz.coffee.fallbackapi.elements.ExprChangerValue;
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
    }
}
