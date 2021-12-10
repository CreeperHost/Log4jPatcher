/*
 * This file is part of Log4jPatcher and is Licensed under the MIT License.
 *
 * Copyright (c) 2021 CreeperHost <https://github.com/CreeperHost>
 */
package net.creeperhost.log4jpatcher;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

/**
 * Created by covers1624 on 10/12/21.
 */
public abstract class Transformer implements ClassFileTransformer {

    protected static PrintStream logger = System.out;

    @Override
    public final byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        try {
            return transform(className, bytes);
        } catch (Throwable ex) {
            return bytes;
        }
    }

    public abstract byte[] transform(String cName, byte[] bytes);

    protected ClassNode parseClassNode(byte[] bytes) {
        return parseClassNode(bytes, 0);
    }

    protected ClassNode parseClassNode(byte[] bytes, int flags) {
        ClassReader reader = new ClassReader(bytes);
        ClassNode cNode = new ClassNode();
        reader.accept(cNode, flags);
        return cNode;
    }

    protected byte[] writeClass(ClassNode cNode) {
        return writeClass(cNode, 0);
    }

    protected byte[] writeClass(ClassNode cNode, int flags) {
        ClassWriter writer = new ClassWriter(flags);
        cNode.accept(writer);
        byte[] bytes = writer.toByteArray();

        if (Main.DEBUG) {
            try {
                Path path = Paths.get("asm/log4jpatcher/" + cNode.name + ".class");
                if (!Files.exists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }
                Files.write(path, bytes);
            } catch (IOException e) {
                logger.println("[Log4jPatcher] [WARN] Failed to write file.");
                e.printStackTrace(logger);
            }
        }

        return bytes;
    }

    protected MethodNode findMethod(ClassNode cNode, String name, String desc) {
        for (MethodNode method : cNode.methods) {
            if (method.name.equals(name) && (method.desc.equals(desc) || desc.equals("*"))) {
                return method;
            }
        }
        return null;
    }

    protected FieldNode findField(ClassNode cNode, String name, String desc) {
        for (FieldNode field : cNode.fields) {
            if (field.name.equals(name) && (field.desc.equals(desc) || desc.equals("*"))) {
                return field;
            }
        }
        return null;
    }
}
