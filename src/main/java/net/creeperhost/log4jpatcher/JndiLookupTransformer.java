/*
 * This file is part of Log4jPatcher and is Licensed under the MIT License.
 *
 * Copyright (c) 2021 CreeperHost <https://github.com/CreeperHost>
 */
package net.creeperhost.log4jpatcher;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ARETURN;

/**
 * Created by covers1624 on 10/12/21.
 */
public class JndiLookupTransformer extends Transformer {

    private static final String CLASS_NAME = "org/apache/logging/log4j/core/lookup/JndiLookup";
    private static final String LOOKUP_DESC = "(Lorg/apache/logging/log4j/core/LogEvent;Ljava/lang/String;)Ljava/lang/String;";

    @Override
    public byte[] transform(String cName, byte[] bytes) {
        if (!CLASS_NAME.equals(cName)) return bytes;
        logger.println("[Log4jPatcher] [INFO] Transforming " + cName);

        ClassNode cNode = parseClassNode(bytes);

        MethodNode lookupMethod = findMethod(cNode, "lookup", LOOKUP_DESC);
        if (lookupMethod == null) {
            logger.println("[Log4jPatcher] [WARN]  Unable to find 'lookup" + LOOKUP_DESC + "' method in " + cName);
            return bytes;
        }

        // Replace method content with:
        // ACONST_NULL
        // ARETURN
        lookupMethod.instructions.clear();
        lookupMethod.tryCatchBlocks.clear();
        lookupMethod.visitInsn(ACONST_NULL);
        lookupMethod.visitInsn(ARETURN);
        lookupMethod.visitMaxs(-1, -1);

        return writeClass(cNode, ClassWriter.COMPUTE_MAXS);
    }
}
