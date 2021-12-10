/*
 * This file is part of Log4jPatcher and is Licensed under the MIT License.
 *
 * Copyright (c) 2021 CreeperHost <https://github.com/CreeperHost>
 */
package net.creeperhost.log4jpatcher;

import java.lang.instrument.Instrumentation;

/**
 * Created by covers1624 on 10/12/21.
 */
public class Main {

    public static boolean DEBUG = false;

    public static void premain(String agentArgs, Instrumentation inst) {
        if (agentArgs != null) {
            if (agentArgs.contains("debug")) {
                DEBUG = true;
            }
        }
        inst.addTransformer(new MessagePatternTransformer());
        inst.addTransformer(new JndiLookupTransformer());
    }
}
