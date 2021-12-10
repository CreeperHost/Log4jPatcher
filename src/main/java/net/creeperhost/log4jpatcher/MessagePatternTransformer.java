/*
 * This file is part of Log4jPatcher and is Licensed under the MIT License.
 *
 * Copyright (c) 2021 CreeperHost <https://github.com/CreeperHost>
 */
package net.creeperhost.log4jpatcher;

import org.objectweb.asm.tree.*;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.PUTFIELD;

/**
 * Created by covers1624 on 10/12/21.
 */
public class MessagePatternTransformer extends Transformer {

    private static final String CLASS_NAME = "org/apache/logging/log4j/core/pattern/MessagePatternConverter";

    @Override
    public byte[] transform(String cName, byte[] bytes) {
        if (!cName.equals(CLASS_NAME)) return bytes;
        logger.println("[Log4jPatcher] [INFO] Transforming " + cName);

        ClassNode cNode = parseClassNode(bytes);

        MethodNode ctor = findMethod(cNode, "<init>", "*");
        FieldNode noLookupsField = findField(cNode, "noLookups", "Z");
        if (ctor == null || noLookupsField == null) {
            if (ctor == null) {
                logger.println("[Log4jPatcher] [WARN]  Unable to find <init> method in " + cName);
            } else {
                logger.println("[Log4jPatcher] [WARN]  Unable to find noLookups:Z field in " + cName);
            }
            return bytes;
        }

        // Insert the following before the last instruction in the function (RETURN).
        // ALOAD 0
        // LDC true
        // PUTFIELD org/apache/logging/log4j/core/pattern/MessagePatternConverter noLookups Z
        InsnList inject = new InsnList();
        inject.add(new VarInsnNode(ALOAD, 0));
        inject.add(new LdcInsnNode(true));
        inject.add(new FieldInsnNode(PUTFIELD, "org/apache/logging/log4j/core/pattern/MessagePatternConverter", "noLookups", "Z"));
        ctor.instructions.insertBefore(ctor.instructions.getLast(), inject);
        ctor.visitMaxs(-1, -1);

        return writeClass(cNode, COMPUTE_MAXS | COMPUTE_FRAMES);
    }

}
