package ren.ashin.hq.examrobot.util;

import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Mutable;

@Sources({"file:conf/config.properties"})
public interface MainConfig extends Mutable {

    @Key("cronTask")
    String cronTask();

    @Key("cronUser")
    String cronUser();
}
